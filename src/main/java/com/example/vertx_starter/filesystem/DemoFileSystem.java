package com.example.vertx_starter.filesystem;

import io.vertx.core.Vertx;
import io.vertx.core.file.FileSystem;



public class DemoFileSystem {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    FileSystem fs=vertx.fileSystem();
    fs.createFile("/home/shubham/vertx/s2.txt").onComplete(result -> {
      if(result.succeeded())
      {
        System.out.println("Success"+Thread.currentThread().getName());
      }
      else{
        System.out.println("Failure"+Thread.currentThread().getName());
      }
    });


  }
}
