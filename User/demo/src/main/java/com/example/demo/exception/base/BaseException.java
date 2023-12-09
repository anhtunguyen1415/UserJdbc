package com.example.demo.exception.base;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
public class BaseException extends RuntimeException {
  private String code;
  private int status;
  private Map<String, String> params;

  public BaseException() {
    super();
    this.params = new HashMap<>();
  }
  public void addParam(String key, String value) {
    if (Objects.isNull(params)) {
      params = new HashMap<>();
    }
    params.put(key, value);
  }
}
