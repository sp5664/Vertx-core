package EventBusCommunication;

import io.vertx.core.*;

import java.time.Duration;

public class PublishSubscriberExample {
  public static void main(String[] args) {
    var vertx= Vertx.vertx();
    vertx.deployVerticle(new Publish());
    vertx.deployVerticle(new Subscriber1(),new DeploymentOptions().setInstances(4));
 //   vertx.deployVerticle(new Subscriber2());
  }
 public static class Publish extends AbstractVerticle{
   @Override
   public void start(final Promise<Void> startPromise) throws Exception {
     startPromise.complete();
     System.out.println("publishing message");
   //  vertx.eventBus().publish(Publish.class.getName(),"A message for everyone!");
     vertx.setPeriodic(Duration.ofSeconds(1).toMillis(), id ->
       vertx.eventBus().publish(PublishSubscriberExample.Publish.class.getName(), "A message for everyone!")
     );
   }
 }

 public static class Subscriber1 extends AbstractVerticle{
   @Override
   public void start(final Promise<Void> startPromise) throws Exception {

     vertx.eventBus().<String>consumer(PublishSubscriberExample.Publish.class.getName(),message -> {
       System.out.println("Received message subscriber 1:"+message.body());
       System.out.println("Thread name1:"+Thread.currentThread().getName());
     });

     vertx.eventBus().<String>consumer(PublishSubscriberExample.Publish.class.getName(),message -> {
       System.out.println("Received message subscriber 2:"+message.body());
       System.out.println("Thread name2:"+Thread.currentThread().getName());
     });

     startPromise.complete();
   }
 }
//
//
// public static class Subscriber2 extends AbstractVerticle{
//    @Override
//    public void start(Promise<Void> startPromise) throws Exception {
//      startPromise.complete();
//      vertx.eventBus().consumer(PublishSubscriberExample.Publish.class.getName(),message -> {
//        System.out.println("Received message subscriber 2:"+message.body());
//      });
//    }
//  }

}
