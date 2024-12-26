package Verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class VerticleA extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.deployVerticle(new VerticleAA());
    System.out.println("Start :"+getClass().getName()+" on Thread:"+Thread.currentThread().getName()+" Config:"+config().toString());
  }
}
