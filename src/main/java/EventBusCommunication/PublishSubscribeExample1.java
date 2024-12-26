package EventBusCommunication;

import java.time.Duration;

import io.vertx.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PublishSubscribeExample1 {

  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new Publish());
    vertx.deployVerticle(new Subscriber1(),new DeploymentOptions().setThreadingModel(ThreadingModel.WORKER).setWorkerPoolSize(4).setWorkerPoolName("temp"));
  //  vertx.deployVerticle(Subscriber2.class.getName(), new DeploymentOptions().setInstances(2));
  }

  public static class Publish extends AbstractVerticle {
    @Override
    public void start(final Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      System.out.println("Publish message");
      vertx.setPeriodic(Duration.ofSeconds(10).toMillis(), id ->
        vertx.eventBus().publish(Publish.class.getName(), "A message for everyone!")
      );
    }
  }

  public static class Subscriber1 extends AbstractVerticle {

    @Override
    public void start(final Promise<Void> startPromise) throws Exception {
      vertx.eventBus().<String>consumer(Publish.class.getName(), message -> {
        System.out.println("Received:"+message.body());
        System.out.println(Thread.currentThread().getName());
      });
      startPromise.complete();
    }
  }

//  public static class Subscriber2 extends AbstractVerticle {
//
//    private static final Logger LOG = LoggerFactory.getLogger(Subscriber2.class);
//
//    @Override
//    public void start(final Promise<Void> startPromise) throws Exception {
//      vertx.eventBus().<String>consumer(Publish.class.getName(), message -> {
//        LOG.debug("Received: {}", message.body());
//      });
//      startPromise.complete();
//    }
//  }
}
