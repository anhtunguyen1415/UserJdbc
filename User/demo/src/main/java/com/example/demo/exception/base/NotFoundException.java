package com.example.demo.exception.base;

import static com.example.demo.constant.Message.ExceptionStatusConstant.NOT_FOUND;

public class NotFoundException extends BaseException {
  public NotFoundException(String objectName, String id) {
    setCode("com.example.demo.exception.base.NotFoundException");
    setStatus(NOT_FOUND);
    addParam("id", id);
    addParam("objectName", objectName);
}

  public NotFoundException() {
    setCode("com.example.demo.exception.base.NotFoundException");
    setStatus(NOT_FOUND);
  }
}
