package EventBusCommunication;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PointToPointExample {
  private static final Logger log= LoggerFactory.getLogger(PointToPointExample.class);
  public static void main(String[] args) {
    var vertx= Vertx.vertx();
    vertx.deployVerticle(new Sender());
    vertx.deployVerticle(new Receiver(),result->{
      log.debug("Verticle deployed id:{}",result.toString());
    });
    vertx.deployVerticle(new Receiver1());
    Buffer buffer = Buffer.buffer("Hello World");

  }
  static class Sender extends AbstractVerticle{
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      //vertx.eventBus().send(Sender.class.getName(),"sending messageq2esadsadsadsadsadsa....");
      vertx.setPeriodic(1000,id->{
        vertx.eventBus().send(Sender.class.getName(),"sending message....");
      });
    }
  }

  static class Receiver extends AbstractVerticle{
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().consumer(Sender.class.getName(),message -> {
        System.out.println("Received message:"+message.body());
      });

    }
  }

  static class Receiver1 extends AbstractVerticle{
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().consumer(Sender.class.getName(),message -> {
        System.out.println("Received1 message:"+message.body());
      });
    }
  }
}
