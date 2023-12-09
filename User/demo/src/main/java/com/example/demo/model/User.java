package com.example.demo.model;

import lombok.*;

@Getter
@Setter
public class User {
  private int id;
  private String username;
  private String password;
  private String email;
  private String token;
  public User() {
  }
  public User(String username, String password, String email) {
    this.username = username;
    this.password = password;
    this.email = email;
  }
}
