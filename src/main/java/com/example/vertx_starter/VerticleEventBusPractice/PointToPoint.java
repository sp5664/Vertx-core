package com.example.vertx_starter.VerticleEventBusPractice;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import io.vertx.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class PointToPoint {

  private static final Logger log = LoggerFactory.getLogger(PointToPoint.class);

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx(new VertxOptions()
      .setMaxEventLoopExecuteTime(500)
      .setMaxEventLoopExecuteTimeUnit(TimeUnit.MILLISECONDS)
    .setEventLoopPoolSize(1));
    vertx.deployVerticle(new SendVerticle(),deployment->{

    });
    vertx.deployVerticle(new ReceiveVerticle());
  }

  public static final String MSG_ADDR = "msg.addr";

  static class SendVerticle extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      var eventBus = vertx.eventBus();
      log.debug("Sending message...");
      vertx.setPeriodic(1000,id->{
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
        eventBus.send(SendVerticle.class.getName(),"Hello World");
      });
     // eventBus.send(SendVerticle.class.getName(),"Hello World");
      startPromise.complete();
    }

  }

  static class ReceiveVerticle extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      vertx.eventBus().consumer(SendVerticle.class.getName(), msg -> {
        log.debug(msg.body().toString());
      });
      startPromise.complete();
    }
  }
}
