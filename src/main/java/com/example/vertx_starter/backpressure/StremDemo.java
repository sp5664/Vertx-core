package com.example.vertx_starter.backpressure;

import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;

public class StremDemo {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    NetServer server = vertx.createNetServer(
      new NetServerOptions().setPort(1234).setHost("localhost")
    );
    vertx.setPeriodic(100,id->{
      server.connectHandler(sock -> {
        sock.handler(buffer -> {
          buffer.appendString("Hello World");
          sock.write(buffer);
        });
      }).exceptionHandler(error->{

      }).listen();
    });

  }
}
