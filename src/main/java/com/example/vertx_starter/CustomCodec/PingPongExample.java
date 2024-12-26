package com.example.vertx_starter.CustomCodec;

import io.vertx.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PingPongExample {
  private static final Logger log= LoggerFactory.getLogger(PingPongExample.class);

  public static final String MSG_ADDR = PingVerticle.class.getName();

  public static void main(String[] args) {
    Vertx vertx= Vertx.vertx();
    vertx.deployVerticle(new PingVerticle(), getAsyncResultHandler());
    vertx.deployVerticle(new PongVerticle(), getAsyncResultHandler());
  }

  private static Handler<AsyncResult<String>> getAsyncResultHandler() {
    return error -> {
      if (error.failed()) {
        log.error("Failed: ", error.cause());
      }
    };
  }

  static class PingVerticle extends AbstractVerticle {


    @Override
    public void start(Promise<Void> startPromise) throws Exception {

     // JsonObject message = new JsonObject().put("name","Hello World").put("version","1.0.0");
      Ping message=new Ping("Hello",true);
      log.debug("Sending: {}",message);
      var eventBus=vertx.eventBus();
      //Registered only once
      eventBus.registerDefaultCodec(Ping.class,new LocalMessageCodec<>(Ping.class));
      eventBus.<Pong>request(MSG_ADDR,message,reply->{
        if(reply.failed())
        {
          log.error("Failed: ",reply.cause());
          return;
        }
        log.debug("Response: {}",reply.result().body());
      });
      startPromise.complete();
    }
  }
  static class PongVerticle extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      vertx.eventBus().registerDefaultCodec(Pong.class,new LocalMessageCodec<>(Pong.class));
      vertx.eventBus().<Ping>consumer(MSG_ADDR,message -> {
        log.debug("Received your message: {}",message.body());
        message.reply(new Pong(0));
      }).exceptionHandler(error->{
        log.error("Error:",error);
      });
      startPromise.complete();
    }
  }

}
