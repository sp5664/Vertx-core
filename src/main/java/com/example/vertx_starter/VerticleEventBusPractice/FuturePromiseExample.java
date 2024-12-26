package com.example.vertx_starter.VerticleEventBusPractice;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.file.FileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FuturePromiseExample extends AbstractVerticle {
  private static final Logger log = LoggerFactory.getLogger(FuturePromiseExample.class);
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new FuturePromiseExample(),reult->{
      log.debug("Verticle deployed:{}",reult.toString());
      vertx.undeploy(reult.toString());
    });
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    startPromise.complete();
    FileSystem fs = vertx.fileSystem();

    vertx.executeBlocking(event -> {
      log.debug("Blocking event");
      event.complete();
    },asyncResult -> {
      if(asyncResult.succeeded()) {
        log.debug("Async result sucessfully");
      }
      else{
        log.error("Async result failed");
      }
    });
//    Future<String> fe=getFutureObject();
//    fe.onSuccess(result->{
//      log.debug("result: {}",result);
//
//    });
  }

  private static Future<String> getFutureObject() {
    Promise<String> promise = Promise.promise();

    promise.complete("success");

    return promise.future();

  }


}
