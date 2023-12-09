package com.example.demo.exception;

import lombok.Getter;

@Getter
public class ApiException extends Exception {
  private ErrorCode errorCode;

  public ApiException(String message, ErrorCode errorCode) {
    super(message);
    this.errorCode = errorCode;
  }

  public ApiException(String message, Throwable cause, ErrorCode errorCode) {
    super(message, cause);
    this.errorCode = errorCode;
  }
}