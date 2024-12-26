package EventBusCommunication;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

public class RequestResponseVericle {
  public static void main(String[] args) {
    var vertx=Vertx.vertx();
    vertx.deployVerticle(RequestVerticle.class.getName(),new DeploymentOptions().setInstances(4));
   // vertx.deployVerticle(new ResponseVerticle());
  }
  static class RequestVerticle extends AbstractVerticle {
    static final String ADDRESS = "my.request.address";

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      var eventbus=vertx.eventBus();
      System.out.println("Sending Hello world");
      eventbus.request(ADDRESS,"Hello world", reply->{
        System.out.println("Response :"+reply.result().body());
        System.out.println("request:"+Thread.currentThread().getName());
      });
    }
  }
  static class ResponseVerticle extends AbstractVerticle{
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().consumer(RequestVerticle.ADDRESS,message -> {
        System.out.println("Message Received....");
        message.reply("Received you message Thanks....");
        System.out.println("response1:"+Thread.currentThread().getName());
      });
      vertx.eventBus().consumer(RequestVerticle.ADDRESS,message -> {
        System.out.println("Message Received....");
        message.reply("Received you message Thanks....");
        System.out.println("response2:"+Thread.currentThread().getName());
      });
    }
  }
}
