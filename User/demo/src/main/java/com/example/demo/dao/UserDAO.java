package com.example.demo.dao;

import com.example.demo.model.User;

import java.util.List;

public interface UserDAO {
  User create(User user);

  User update(User user, int id);

  void delete(int id);

  User getByUsername(String username);

  List<User> list();

  User getById(int id);
}
