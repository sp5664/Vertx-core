package com.example.vertx_starter.VerticleEventBusPractice;

import io.vertx.core.*;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PublisherConsumer {

private static final Logger log = LoggerFactory.getLogger(PublisherConsumer.class);

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(PublishVerticle.class.getName(),new DeploymentOptions().setInstances(4));
    vertx.deployVerticle(new ConsumerVerticle(),getAsyncResultHandler());
  }

  private static Handler<AsyncResult<String>> getAsyncResultHandler() {
    return error -> {
      if (error.failed()) {
        log.error("Failed: ", error.cause());
      }
    };
  }
  static class PublishVerticle extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      var eventBus = vertx.eventBus();
      log.debug("Sending messsage");
      vertx.setPeriodic(500,id->{
        eventBus.<JsonObject>publish(PublishVerticle.class.getName(),new JsonObject().put("name","Hello World"));
      });

      startPromise.complete();

    }
  }

  static class ConsumerVerticle extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      vertx.eventBus().<JsonObject>consumer(PublishVerticle.class.getName(),message ->
      {
          log.debug("Received Message:{}",message.body());
      });
      startPromise.complete();
    }
  }
}
