package Verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.UUID;

public class MainVerticle extends AbstractVerticle {

  private static final Logger log= LoggerFactory.getLogger(MainVerticle.class);

  public static void main(String[] args) {
     final Vertx vertx=Vertx.vertx();
     vertx.deployVerticle(new MainVerticle());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    log.info("start {}",getClass().getName());
    vertx.deployVerticle(VerticleA.class.getName(),new DeploymentOptions()
    //  .setInstances(400)
      .setConfig(new JsonObject()
        .put("id", UUID.randomUUID().toString())
        .put("ClassName",VerticleA.class.getSimpleName())
      ));
    startPromise.complete();
  }
}
