package com.example.demo.exception;

import com.example.demo.exception.base.NotFoundException;

public class UserNotFoundException extends NotFoundException {
  public UserNotFoundException() {
    setCode("com.example.demo.exception.UserNotFoundException");
  }
}
