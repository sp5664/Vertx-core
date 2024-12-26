package EventBusCommunication;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class RequestResponseVericleJson extends AbstractVerticle {
  public static void main(String[] args) {
    var vertx= Vertx.vertx();
    vertx.deployVerticle(new RequestResponseVericleJson.RequestVerticle());
    vertx.deployVerticle(new RequestResponseVericleJson.ResponseVerticle());
  }
  static class RequestVerticle extends AbstractVerticle {
    static final String ADDRESS = "my.request.address";

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      var eventbus=vertx.eventBus();
      var message =new JsonObject().put("message", "Hello World")
        .put("version",1);
      System.out.println("Sending Hello world");
      eventbus.<JsonObject>request(ADDRESS,message, reply->{
        System.out.println("Response :"+reply.result().body());
        System.out.println("request:"+Thread.currentThread().getName());
      });
    }
  }
  static class ResponseVerticle extends AbstractVerticle{
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().<JsonObject>consumer(RequestResponseVericle.RequestVerticle.ADDRESS, message -> {
        System.out.println("Message Received....");
        System.out.println("received :"+message.body());
        message.reply(new JsonArray().add("one").add("two").add("three"));
      });
    }
  }
}
