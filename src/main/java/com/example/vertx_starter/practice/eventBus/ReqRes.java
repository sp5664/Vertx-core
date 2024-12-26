package com.example.vertx_starter.practice.eventBus;

import EventBusCommunication.PointToPointExample;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReqRes {
  private static final Logger log = LoggerFactory.getLogger(ReqRes.class);
  public static final String MSG_ADDR = "msg.addr";
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
   // vertx.deployVerticle(new ReqVerticle());
    vertx.deployVerticle(new SendVerticle());
   // vertx.deployVerticle(new ResVerticle());
    vertx.deployVerticle(new Receiver());
  }

  public static class SendVerticle extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.setPeriodic(1000,id->{
        vertx.eventBus().send(ReqRes.SendVerticle.class.getName(),"sending message....");
      });
    }
  }

  public static class PublishVerticle extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      vertx.setPeriodic(1000,id->{
        vertx.eventBus().publish(PublishVerticle.class.getName(),new JsonObject().put(MSG_ADDR,"test"));
      });
      startPromise.complete();
    }
  }

  public static class ReqVerticle extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      vertx.eventBus().request(MSG_ADDR,new JsonObject().put("Name","Hello World"),result->{
              log.debug("reply:{}",result.result().body());
      });
      startPromise.complete();
    }
  }

//  public static class ResVerticle extends AbstractVerticle {
//    @Override
//    public void start(Promise<Void> startPromise) throws Exception {
//      vertx.eventBus().consumer(PublishVerticle.class.getName(),res->{
//        log.debug("response:{}",res.body());
//      });
//      startPromise.complete();
//    }

  public static class ReceiveVerticle extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().consumer(SendVerticle.class.getName(),res->{
        log.debug("Received:{}",res.body());
      });

    }
  }


  static class Receiver extends AbstractVerticle{
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().consumer(ReqRes.SendVerticle.class.getName(), message -> {
        System.out.println("Received message:"+message.body());
      });
    }
  }
}
