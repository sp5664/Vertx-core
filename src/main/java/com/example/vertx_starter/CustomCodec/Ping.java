package com.example.vertx_starter.CustomCodec;

public class Ping {
  private String message;
  private boolean enabled;

  public Ping(String message, boolean enabled) {
    this.message = message;
    this.enabled = enabled;
  }
  public Ping() {}

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public String toString() {
    return "Ping{" +
      "message='" + message + '\'' +
      ", enabled=" + enabled +
      '}';
  }
}
