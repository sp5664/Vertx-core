package com.example.vertx_starter;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(VertxExtension.class)
public class PromiseFutureEx {

  private static final Logger log = LoggerFactory.getLogger(PromiseFutureEx.class);

  @Test
  void promiseSuccess(Vertx vertx, VertxTestContext testContext) {

    final Promise<String> promise= Promise.promise();

    vertx.setTimer(500,message->{
      promise.complete("test");
      testContext.completeNow();
    });
  }

  @Test
  void promiseFail(Vertx vertx, VertxTestContext testContext) {
    final Promise<String> promise= Promise.promise();
    vertx.setTimer(500,message->{
      promise.fail(new RuntimeException("failed"));
      testContext.completeNow();
    });
  }

  @Test
  void promiseFuture(Vertx vertx, VertxTestContext testContext) {
    final Promise<String> promise= Promise.promise();
    vertx.setTimer(500,message->{
     promise.complete("test");
      log.debug("Thread promise:{}",Thread.currentThread().getName());
      //promise.fail(new RuntimeException("failed"));
      testContext.completeNow();
    });
//
//    Future<String> future = promise.future();
//    future.onSuccess(message -> {
//      log.debug("Result: {}", message);
//      testContext.completeNow();
//    }).onFailure(testContext::failNow);
//
//
    Future<String> future=promise.future();
    future.map(asString->{
      return new JsonObject().put("foo","bar");
    }).onSuccess(result->{
      log.debug("Thread future:{}",Thread.currentThread().getName());
      log.debug("result: {}", result);
      testContext.completeNow();
    });

  }
}
