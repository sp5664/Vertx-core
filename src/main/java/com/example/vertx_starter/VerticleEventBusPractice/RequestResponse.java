package com.example.vertx_starter.VerticleEventBusPractice;

import com.example.vertx_starter.CustomCodec.Ping;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestResponse {
  private static final Logger log = LoggerFactory.getLogger(RequestResponse.class);
  public static final String MSG_ADDR = "msg.addr";
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();

    vertx.deployVerticle(RequestResponse.RequestVerticle.class,new DeploymentOptions().setInstances(2));
    vertx.deployVerticle(new ResponseVerticle());
  }

  static class RequestVerticle extends AbstractVerticle {


    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      Ping ping = new Ping();
      log.debug("sending request message");
      vertx.eventBus().request(MSG_ADDR,"Hello world",reply->{
        log.debug("Reply: {}",reply.result().body());
      });
      startPromise.complete();
    }
  }

  static class ResponseVerticle extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      vertx.eventBus().consumer(MSG_ADDR,res->{
        res.reply("Received your message thanks!");
        log.debug("Result:{}",res.body());
      });
      startPromise.complete();
    }
  }
}
