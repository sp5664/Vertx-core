package com.example.vertx_starter;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;

import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(VertxExtension.class)
public class futurePromiseExample {
    private static final Logger log = LoggerFactory.getLogger(futurePromiseExample.class);

    @Test
    void promise_success(Vertx vertx, VertxTestContext testContext) {
        final Promise<String> promise = Promise.promise();
        log.debug("start");
        vertx.setTimer(500,id->{
          promise.complete("success");
          log.debug("success");
          testContext.completeNow();
        });
        log.debug("end");
    }

    @Test
    void promise_fail(Vertx vertx, VertxTestContext testContext) {
      final Promise<String> promise = Promise.promise();
      log.debug("start");
      vertx.setTimer(500,id->{
        promise.fail(new RuntimeException("failed"));
        log.debug("failed");
        testContext.completeNow();
      });
      log.debug("end");
    }

    @Test
    void future_success(Vertx vertx, VertxTestContext testContext) {
      final Promise<String> promise = Promise.promise();
      log.debug("start");
      vertx.setTimer(500,id->{
        promise.complete("success");
        log.debug("Timer done");

      });

      final Future<String> future=promise.future();
      future.onSuccess(result->{
        log.debug("Result: {}", result);
        testContext.completeNow();
      }).onFailure(testContext::failNow);

    }

    @Test
    void future_fail(Vertx vertx, VertxTestContext testContext) {
      final Promise<String> promise = Promise.promise();
      log.debug("start");
      vertx.setTimer(500,id->{
          promise.fail(new RuntimeException("failed"));
          log.debug("failed");

      });

      final Future<String> future=promise.future();
      future.onSuccess(result->{
        log.debug("Result: {}", result);
        testContext.completeNow();
      }).onFailure(error->{
        log.debug("Error: {}", error);
        testContext.completeNow();
      });
    }

    @Test
    void future_map(Vertx vertx, VertxTestContext testContext)
    {
      final Promise<String> promise=Promise.promise();
      log.debug("start");
      vertx.setTimer(500,id->{
        promise.complete("success");
        log.debug("Timer done");
      });

      final Future<String> future=promise.future();
      future.map(asString->{
        log.debug("Map String to object");
        return new JsonObject().put("key",asString);
        })
        .map(jsonObject->{return new JsonArray().add(jsonObject);
        })
        .onSuccess(result->{
        log.debug("Result: {}", result);
        testContext.completeNow();
      });
    }
}
