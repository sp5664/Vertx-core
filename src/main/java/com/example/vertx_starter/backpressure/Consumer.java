package com.example.vertx_starter.backpressure;

import io.vertx.core.Vertx;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;

public class Consumer {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    NetClient client = vertx.createNetClient(new NetClientOptions());

    client.connect(1234, "localhost", res -> {
      if (res.succeeded()) {
        System.out.println("Connected to server!");
//        NetSocket socket = res.result();
//        socket.handler(buffer -> {
//          System.out.println("Received: " + buffer.toString());
//        });

      } else {
        System.out.println("Failed to connect to server!");
      }
    });
  }
}
