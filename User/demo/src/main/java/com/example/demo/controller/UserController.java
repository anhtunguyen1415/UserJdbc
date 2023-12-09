package com.example.demo.controller;

import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.request.UserRequest;
import com.example.demo.dto.response.LoginResponse;
import com.example.demo.dto.response.ResponseGeneral;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.service.MessageService;
import com.example.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.example.demo.constant.Message.LanguageConstants.DEFAULT_LANGUAGE;
import static com.example.demo.constant.Message.LanguageConstants.LANGUAGE;
import static com.example.demo.constant.Message.MessageResponse.*;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
  private static final Logger log = LoggerFactory.getLogger(UserController.class);
  private final UserService userService;
  private final MessageService messageService;

  public UserController(UserService userService, MessageService messageService) {
    this.userService = userService;
    this.messageService = messageService;
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping()
  public ResponseGeneral<UserResponse> create(
        @RequestBody @Valid UserRequest userRequest,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(create) request: {}", userRequest);
    UserResponse userResponse = userService.create(userRequest);
    return ResponseGeneral.ofSuccess(messageService.getMessage(SUCCESS, language), userResponse);
  }

  @PutMapping("{id}")
  public ResponseGeneral<UserResponse> update(
        @RequestBody @Valid UserRequest userRequest,
        @PathVariable(name = "id") int id,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(update) Request to update user with id {}: {}", id, userRequest);
    UserResponse userResponse = userService.update(userRequest, id);
    return ResponseGeneral.ofSuccess(messageService.getMessage(SUCCESS, language), userResponse);
  }

  @GetMapping()
  public ResponseGeneral<List<UserResponse>> list(
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(list) Request to get all users.");
    List<UserResponse> userResponse = userService.list();
    return ResponseGeneral.ofSuccess(messageService.getMessage(SUCCESS, language), userResponse);
  }

  @DeleteMapping("{id}")
  public ResponseGeneral<Void> delete(
        @PathVariable(name = "id") int id,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(delete) Request to delete user with id: {}", id);
    userService.delete(id);
    return ResponseGeneral.ofSuccess(messageService.getMessage(SUCCESS, language));
  }

  @PostMapping("login")
  public ResponseGeneral<LoginResponse> login(
        @RequestBody LoginRequest loginRequest,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    LoginResponse loginResponse = userService.login(loginRequest);
    log.info("(login) User logged in successfully: {}", loginResponse.getUsername());
    return ResponseGeneral.ofSuccess(messageService.getMessage(SUCCESS, language), loginResponse);
  }

  @GetMapping({"username"})
  public ResponseGeneral<UserResponse> getByUsername(
        @RequestParam(name = "username") String username,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("Request to get by username.");
    UserResponse userResponse = userService.getByUsername(username);
    return ResponseGeneral.ofSuccess(messageService.getMessage(SUCCESS, language), userResponse);
  }

  @GetMapping("{id}")
  public ResponseGeneral<UserResponse> getById(
        @PathVariable(name = "id") int id,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("Request to get by id.");
    UserResponse userResponse = userService.getById(id);
    return ResponseGeneral.ofSuccess(messageService.getMessage(SUCCESS, language), userResponse);
  }
}
