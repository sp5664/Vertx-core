package com.example.vertx_starter.backpressure;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Producer {
  private static final Logger log = LoggerFactory.getLogger(Producer.class);

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    NetServer server = vertx.createNetServer(new NetServerOptions().setPort(1234).setHost("localhost"));

    server.connectHandler(sock -> {
      sock.setWriteQueueMaxSize(2); // Set a very small max size to trigger backpressure quickly

      // Send data continuously
      vertx.setPeriodic(10, id -> {
        if (sock.writeQueueFull()) {
          sock.pause();
          log.debug("Paused");
          sock.drainHandler(done -> {
            sock.resume();
            log.debug("Resumed");
          });
        }
        else {
          log.debug("Send : {}", "data");
          sock.write(Buffer.buffer(new byte[2048]));

        }
      });
    }).listen(res -> {
      if (res.succeeded()) {
        System.out.println("Server is now listening!");
      } else {
        System.out.println("Failed to bind!");
      }
    });
  }
}

//import io.vertx.core.Vertx;
//import io.vertx.core.buffer.Buffer;
//import io.vertx.core.net.NetServer;
//import io.vertx.core.net.NetServerOptions;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class Producer {
//  private static final Logger log = LoggerFactory.getLogger(Producer.class);
//  public static void main(String[] args) {
//    Vertx vertx = Vertx.vertx();
//    NetServer server = vertx.createNetServer(new NetServerOptions().setPort(1234).setHost("localhost"));
//
//    server.connectHandler(sock -> {
//      sock.setWriteQueueMaxSize(2);
//      vertx.setPeriodic(1000,id->{
//        if (sock.writeQueueFull()) {
//          sock.pause();
//          log.debug("Paused");
//          sock.drainHandler(done -> {
//            sock.resume();
//            log.debug("Resumed");
//          });
//        }
//        else {
//          log.debug("Send : {}","data");
//          sock.write(Buffer.buffer(new byte[2048]));
//        }
//      });
//    }).listen(res -> {
//      if (res.succeeded()) {
//        System.out.println("Server is now listening!");
//      } else {
//        System.out.println("Failed to bind!");
//      }
//    });
//  }
//}

