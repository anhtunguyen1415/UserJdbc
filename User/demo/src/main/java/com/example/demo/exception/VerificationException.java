package com.example.demo.exception;

import com.example.demo.exception.base.BadRequestException;
import org.springframework.http.HttpStatus;

public class VerificationException extends BadRequestException {
  public VerificationException() {
    super();
    setCode("com.example.demo.exception.AuthenticationException");
    setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
  }
}