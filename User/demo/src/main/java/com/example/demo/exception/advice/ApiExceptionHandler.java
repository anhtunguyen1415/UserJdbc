package com.example.demo.exception.advice;

import com.example.demo.dto.response.BaseApiRespsonse;
import com.example.demo.dto.response.ResponseGeneral;
import com.example.demo.exception.ApiException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.dto.common.Error;
import com.example.demo.exception.base.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static com.example.demo.constant.Message.LanguageConstants.DEFAULT_LANGUAGE;
import static com.example.demo.constant.Message.LanguageConstants.LANGUAGE;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ApiExceptionHandler {
  private final MessageSource messageSource;

  @ExceptionHandler(value = ApiException.class)
  public ResponseEntity<BaseApiRespsonse> handleException(ApiException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseApiRespsonse.error(e.getErrorCode(), e.getMessage()));
  }

  @ExceptionHandler(value = {BaseException.class})
  public ResponseEntity<ResponseGeneral<Error>> handleFinanceBaseException(
        BaseException ex,
        WebRequest webRequest
  ) {

    String language = webRequest.getHeader(LANGUAGE);

    return ResponseEntity
          .status(ex.getStatus())
          .body(getError(
                ex.getStatus(),
                ex.getCode(),
                Objects.isNull(language) ? new Locale("en") : new Locale(language),
                ex.getParams())
          );
  }

  private ResponseGeneral<Error> getError(int status, String code, Map<String, String> params) {
    return ResponseGeneral.of(
          status,
          HttpStatus.valueOf(status).getReasonPhrase(),
          Error.of(code, params)
    );
  }

  private ResponseGeneral<Error> getError(int status, String code, Locale locale, Map<String, String> params) {
    return ResponseGeneral.of(
          status,
          HttpStatus.valueOf(status).getReasonPhrase(),
          Error.of(code, getMessage(code, locale, params))
    );
  }

  private String getMessage(String code, Locale locale, Map<String, String> params) {
    var message = getMessage(code, locale);
    if (params != null && !params.isEmpty()) {
      for (var param : params.entrySet()) {
        message = message.replace(getMessageParamsKey(param.getKey()), param.getValue());
      }
    }
    return message;
  }

  private String getMessage(String code, Locale locale) {
    try {
      return messageSource.getMessage(code, null, locale);
    } catch (Exception ex) {
      return code;
    }
  }

  private String getMessageParamsKey(String key) {
    return "%" + key + "%";
  }

  private static HttpStatus getHttpStatus(ErrorCode errorCode) {
    String errorcode = errorCode.toString();
    if (errorcode.toString().equals("BAD_REQUEST")) return HttpStatus.BAD_REQUEST;
    else if (errorcode.toString().equals("UNAUTHORIZED")) return HttpStatus.UNAUTHORIZED;
    else if (errorcode.toString().equals("FORBIDDEN")) return HttpStatus.FORBIDDEN;
    else if (errorcode.toString().equals("NOT_FOUND")) return HttpStatus.NOT_FOUND;
    else return HttpStatus.OK;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ResponseGeneral<Error>> handleValidationExceptions(
        MethodArgumentNotValidException exception,
        WebRequest webRequest
  ) {
    log.error("(handleValidationExceptions)exception: {}", exception.getMessage());
    String language = Objects.nonNull(webRequest.getHeader(LANGUAGE)) ?
          webRequest.getHeader(LANGUAGE) : DEFAULT_LANGUAGE;
    String errorMessage = exception.getBindingResult().getFieldErrors().stream()
          .map(DefaultMessageSourceResolvable::getDefaultMessage)
          .findFirst()
          .orElse(exception.getMessage());

    log.error("(handleValidationExceptions) {}", errorMessage);
    return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body(getError(HttpStatus.BAD_REQUEST.value(), errorMessage, language));
  }

  private ResponseGeneral<Error> getError(int status, String message, String language) {
    return ResponseGeneral.of(
          status,
          HttpStatus.valueOf(status).getReasonPhrase(),
          Error.of(message, getMessage(message, new Locale(language)))
    );
  }

}