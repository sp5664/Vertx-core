package com.example.vertx_starter.VerticleEventBusPractice;

import com.example.vertx_starter.CustomCodec.LocalMessageCodec;
import com.example.vertx_starter.CustomCodec.Ping;
import com.example.vertx_starter.CustomCodec.Pong;
import io.vertx.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PingPongExample {

  private static final Logger log = LoggerFactory.getLogger(PingPongExample.class);
  public static final String MSG_ADDR = "msg.addr";
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new PingVerticle(), getAsyncResultHandler());
    vertx.deployVerticle(new PongVerticle(),getAsyncResultHandler());
  }
  //
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
      Ping ping = new Ping("Hello",true);
      vertx.eventBus().registerDefaultCodec(Ping.class,new LocalMessageCodec<>(Ping.class));
      vertx.eventBus().<Pong>request(MSG_ADDR,ping, reply->{
        log.debug("Pong Received:{}",reply.result().body().toString());
      });

      startPromise.complete();
    }
  }

  static class PongVerticle extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) throws Exception {

      vertx.eventBus().registerDefaultCodec(Pong.class,new LocalMessageCodec<>(Pong.class));

      vertx.eventBus().<Ping>consumer(MSG_ADDR,reply->{
        log.debug("Ping Message received: {}", reply.body());
        reply.reply(new Pong(0));
      }).exceptionHandler(error->{
        log.error("Pong Error:{}", error.getCause());
      });
    startPromise.complete();
    }
  }
}
