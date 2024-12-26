package VerticlePractice;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.util.UUID;

public class MainVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Vertx vertx=Vertx.vertx();
    vertx.deployVerticle(MainVerticle.class.getName(),new DeploymentOptions()
      .setInstances(4).setConfig(new JsonObject().put("id", UUID.randomUUID().toString())),
      deployed->{
      System.out.println("Deployed:"+MainVerticle.class.getName());
      vertx.undeploy(deployed.result());
    });
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    System.out.println("This is the main verticle:"+Thread.currentThread().getName());
    System.out.println("MainVerticle started:"+"with config"+config().toString() );
    startPromise.complete();
  }

  @Override
  public void stop(Promise<Void> stopPromise) throws Exception {
    System.out.println("MainVerticle stopped:"+getClass().getName());
    stopPromise.complete();
  }
}
