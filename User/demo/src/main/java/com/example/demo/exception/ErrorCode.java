package com.example.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor(staticName = "of")
@Data
@Getter
@Setter
public class ErrorCode {
  private String code;
  private Object message;
}
