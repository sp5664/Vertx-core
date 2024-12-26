package VerticlePractice;

import io.vertx.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkerExample extends AbstractVerticle {
  private static final Logger log= LoggerFactory.getLogger(WorkerExample.class);

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new WorkerExample(),new DeploymentOptions().setThreadingModel(ThreadingModel.WORKER).setWorkerPoolSize(1).setWorkerPoolName("temp"));
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    startPromise.complete();
    log.info("verticle started on thread {}", Thread.currentThread().getName());
    vertx.executeBlocking(event->{
      log.info("blocking code");
      log.info("verticle started on worker thread {}", Thread.currentThread().getName());
      try {
        Thread.sleep(3000);
        event.fail("Failed");
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    },result->{
      if(result.succeeded()){
        log.info("blocking code");
        log.info("blocking code done");
        log.info("verticle result on worker thread {}", Thread.currentThread().getName());
      }
      else {
        log.error("blocking code failed",result.cause());
      }
    });
  }
}
