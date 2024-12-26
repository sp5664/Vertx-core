package com.example.vertx_starter.Pr1;

import com.example.vertx_starter.CustomCodec.LocalMessageCodec;
import com.example.vertx_starter.CustomCodec.Ping;
import com.example.vertx_starter.CustomCodec.PingPongExample;
import com.example.vertx_starter.CustomCodec.Pong;
import io.vertx.core.*;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;
import io.vertx.ext.dropwizard.MetricsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainVerticle {

  private static final Logger log = LoggerFactory.getLogger(MainVerticle.class);
  public static final String MSG_ADDR = "msg.addr";
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(new DropwizardMetricsOptions().setEnabled(true)));
    JsonObject config = new JsonObject().put("host", "localhost");
    vertx.deployVerticle(new PingVerticle(),new DeploymentOptions().setConfig(config) ,getAsyncResultHandler());
    vertx.deployVerticle(new PongVerticle(),getAsyncResultHandler());
    MetricsService metricsService = MetricsService.create(vertx);
    JsonObject metricsConfig = metricsService.getMetricsSnapshot("vertx");
      log.debug("Metrics data: {}", metricsConfig);

  }

  private static Handler<AsyncResult<String>> getAsyncResultHandler() {
    return error -> {
      log.debug("Deployment id: {}",error.result());
      if(error.failed()){
        log.error("Failed:{}",error.cause());
      }
    };
  }

  static class PingVerticle extends AbstractVerticle {


    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      Ping message = new Ping("Hello World",true);
      vertx.eventBus().registerDefaultCodec(Ping.class,new LocalMessageCodec<>(Ping.class));
      vertx.eventBus().<Pong>request(MSG_ADDR,message,reply->{
        log.debug("Reply message:{}",reply.result().body());
        log.debug("Config details:{}",config().getString("host"));
      });

      startPromise.complete();
    }
  }

  static class PongVerticle extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      vertx.eventBus().registerDefaultCodec(Pong.class,new LocalMessageCodec<>(Pong.class));
      vertx.eventBus().<Ping>consumer(MSG_ADDR,result->{
        log.debug("Req message:{}",result.body());
        result.reply(new Pong(0));
      }).exceptionHandler(error->{
        log.error("Error",error.getCause());
      });
      startPromise.complete();
    }
  }
}
