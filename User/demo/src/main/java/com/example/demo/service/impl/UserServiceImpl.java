package com.example.demo.service.impl;

import com.example.demo.dao.UserDAO;
import com.example.demo.dao.impl.UserDAOImpl;
import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.request.UserRequest;
import com.example.demo.dto.response.LoginResponse;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.exception.DuplicateUsernameException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.exception.VerificationException;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
  private final UserDAO userDAO = new UserDAOImpl();
  private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

  @Override
  public UserResponse create(UserRequest userRequest) {
    logger.info("Creating a new user: {}", userRequest.getUsername());
    User existingUser = userDAO.getByUsername(userRequest.getUsername());
    if (existingUser != null) {
      throw new DuplicateUsernameException(userRequest.getUsername());
    }
    User user = new User(
          userRequest.getUsername(),
          userRequest.getPassword(),
          userRequest.getEmail());
    userDAO.create(user);
    UserResponse userResponse = new UserResponse(user.getId(), user.getUsername(), user.getEmail());
    return userResponse;
  }

  @Override
  public UserResponse update(UserRequest userRequest, int id) {
    logger.info("Updating user with ID: {}", id);
    User existingUser = userDAO.getById(id);
    if (existingUser != null) {
      existingUser.setUsername(userRequest.getUsername());
      existingUser.setPassword(userRequest.getPassword());
      existingUser.setEmail(userRequest.getEmail());
      User updatedUser = userDAO.update(existingUser, id);
      UserResponse userResponse = new UserResponse(
            updatedUser.getId(),
            updatedUser.getUsername(),
            updatedUser.getEmail());
      return userResponse;
    } else {
      logger.warn("User not found with ID: {}", id);
      throw new UserNotFoundException();
    }
  }

  @Override
  public void delete(int id) {
    logger.info("Deleting user with ID: {}", id);
    User existingUser = userDAO.getById(id);
    if (existingUser != null)
      userDAO.delete(id);
    else
      throw new UserNotFoundException();
  }

  @Override
  public List<UserResponse> list() {
    logger.info("Retrieving all users");
    List<User> users = userDAO.list();
    List<UserResponse> usersResponse = new ArrayList<>();
    for (User user : users) {
      UserResponse userResponse = new UserResponse(user.getId(), user.getUsername(), user.getEmail());
      usersResponse.add(userResponse);
    }
    return usersResponse;
  }

  @Override
  public UserResponse getByUsername(String username) {
    logger.info("Retrieving user with username: {}", username);
    User user = userDAO.getByUsername(username);
    if (user == null) {
      throw new UserNotFoundException();
    }
    UserResponse userResponse = new UserResponse(
          user.getId(),
          user.getUsername(),
          user.getEmail());
    return userResponse;
  }
  @Override
  public UserResponse getById(int id) {
    logger.info("Retrieving user with id: {}", id);
    User user = userDAO.getById(id);
    if (user == null) {
      throw new UserNotFoundException();
    }
    UserResponse userResponse = new UserResponse(
          user.getId(),
          user.getUsername(),
          user.getEmail());
    return userResponse;
  }
  @Override
  public LoginResponse login(LoginRequest loginRequest) {
    logger.info("Processing login request for user: {}", loginRequest.getUsername());
    User existingUser = userDAO.getByUsername(loginRequest.getUsername());
    if (existingUser == null) {
      logger.warn("User not found for login: {}", loginRequest.getUsername());
      throw new UserNotFoundException();
    }
    //giai ma mat khau va so sanh voi password nhap vao
    if (!BCrypt.checkpw(loginRequest.getPassword(), existingUser.getPassword())) {
      logger.warn("Authentication failed for user: {}", loginRequest.getUsername());
      throw new VerificationException();
    }
    String token = TokenUtil.generateToken();
    existingUser.setToken(token);
    return new LoginResponse(existingUser.getId(), existingUser.getUsername(), existingUser.getEmail(), token);
  }
}
