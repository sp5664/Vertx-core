package com.example.vertx_starter.VerticleEventBusPractice;

import io.vertx.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Verticle {
  private static final Logger log = LoggerFactory.getLogger(Verticle.class);

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();

    vertx.deployVerticle(Verticle1.class,new DeploymentOptions().setInstances(2),stringAsyncResult -> {
      if(stringAsyncResult.failed())
      {
        log.error("Verticle deployment failed",stringAsyncResult.cause());
      }
    });
//    vertx.deployVerticle(Verticle2.class,new DeploymentOptions().setInstances(16),stringAsyncResult -> {
//      if(stringAsyncResult.failed())
//      {
//        log.error("Verticle deployment failed",stringAsyncResult.cause());
//      }
//    });
  }

  public static class Verticle1 extends AbstractVerticle{
    @Override
    public void start(Promise<Void> startPromise) throws Exception {

      Future<String> future=getFuture(vertx);
      future.onSuccess(result->{
        log.debug("Future result: {}",result);
      });
      log.debug("Verticle1 started");
      startPromise.complete();
    }
    public Future<String> getFuture(Vertx vertx) {

      Promise<String> promise = Promise.promise();
      vertx.setTimer(500,arr->{
        log.debug("Promise called");
        promise.complete("success");
      });
      return promise.future();
    }
  }
  public static class Verticle2 extends AbstractVerticle{
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      log.debug("Verticle2 started");
      startPromise.complete();
    }
  }


}
