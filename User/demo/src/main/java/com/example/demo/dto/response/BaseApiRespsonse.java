package com.example.demo.dto.response;

import com.example.demo.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BaseApiRespsonse<T> {
  private ErrorCode errorCode;
  private T data;

  public static BaseApiRespsonse successNoData() {
    return BaseApiRespsonse.builder().data("Successfully")
          .build();
  }

  public static <T> BaseApiRespsonse<T> successWithData(T data) {
    return BaseApiRespsonse.<T>builder()
          .data(data)
          .build();
  }

  public static BaseApiRespsonse error(ErrorCode errorCode, String message) {
    return BaseApiRespsonse.builder().data(message)
          .errorCode(errorCode)
          .build();
  }
}