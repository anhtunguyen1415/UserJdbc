package com.example.demo.exception;

import com.example.demo.exception.base.ConflictException;
import org.springframework.http.HttpStatus;

public class DuplicateUsernameException extends ConflictException {
  public DuplicateUsernameException(String username) {
    super(username);
    setCode("com.example.demo.exception.DuplicateUsernameException");
    setStatus(HttpStatus.CONFLICT.value());
    addParam("username", username);
  }
}