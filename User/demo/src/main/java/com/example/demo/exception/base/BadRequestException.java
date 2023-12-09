package com.example.demo.exception.base;

import static com.example.demo.constant.Message.ExceptionStatusConstant.UNAUTHORIZED;

public class BadRequestException extends BaseException {
  public BadRequestException() {
    setCode("com.example.demo.exception.base.BadRequestException");
    setStatus(UNAUTHORIZED);
  }
}
