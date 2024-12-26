package com.example.vertx_starter;

import dto.Person;
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

import java.util.HashMap;
import java.util.Map;


import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(VertxExtension.class)
public class TestMainVerticle {
  private  static final Logger log = LoggerFactory.getLogger(TestMainVerticle.class.getName());

  @Test
  void verticle_deployed() throws Throwable {
    Map<String,Object> config = new HashMap<>();
    config.put("host", "localhost");
    config.put("port", 8080);
    config.put("name", "vertx-starter");

  //  assertEquals("{\"host\":\"localhost\",\"port\":8080,\"name\":\"vertx-starter\"}",config.encode());
    JsonObject jsonObject = new JsonObject(config);
    assertEquals(config,jsonObject.getMap());
    assertEquals("vertx-starter",jsonObject.getString("name"));
    assertEquals("localhost",jsonObject.getString("host"));

    JsonArray jsonArray=new JsonArray();
    jsonArray.add(jsonObject)
      .add("test");

    assertEquals("[{\"port\":8080,\"host\":\"localhost\",\"name\":\"vertx-starter\"},\"test\"]",jsonArray.encode());
  }

  @Test
  void canMapJavaObject(){
    Person person=new Person("Alice",1);
    JsonObject jsonObject = JsonObject.mapFrom(person);

    assertEquals(person.getName(),jsonObject.getString("name"));
    assertEquals(person.getAge(),jsonObject.getInteger("age"));

    Person person2=jsonObject.mapTo(Person.class);

    assertEquals(person.getName(),person2.getName());
    assertEquals(person.getAge(),person2.getAge());



  }

  @Test
  void jsonArrayCanBeMapped() {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonObject().put("id", 1))
      .add(new JsonObject().put("id", 2))
      .add(new JsonObject().put("id", 3))
      .add("random");

    assertEquals("[{\"id\":1},{\"id\":2},{\"id\":3},\"random\"]", jsonArray.encode());
  }


  @Test
  void futurePromise(Vertx vertx,VertxTestContext vertxTestContext) throws Throwable {
    Promise<String> promise = Promise.promise();
    vertx.setTimer(500,event->{
      promise.complete("success");
      vertxTestContext.completeNow();
    });

    Future<String> future = promise.future();
    future.onSuccess(message->{
      log.debug("message:{}",message);
    });

  }
}
