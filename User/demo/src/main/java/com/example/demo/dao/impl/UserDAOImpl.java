package com.example.demo.dao.impl;

import com.example.demo.dao.DataSource;
import com.example.demo.dao.UserDAO;
import com.example.demo.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.mindrot.jbcrypt.BCrypt;

public class UserDAOImpl implements UserDAO {
  private static final String CREATE_USER = "INSERT INTO users (username,password,email) VALUES (?,?,?)";
  private static final String UPDATE_USER = "UPDATE users SET username=?,password=?,email=? WHERE id=?";
  private static final String DELETE_USER = "DELETE FROM users WHERE id = ?";
  private static final String GET_ALL = "SELECT id, username,password, email FROM Users";
  private static final String GET_BY_USERNAME = "SELECT * FROM users WHERE username=?";
  private static final String GET_BY_ID = "SELECT * FROM users WHERE id=?";

  @Override
  public User create(User user) {
    Connection conn = null;
    try {
      conn = DataSource.getInstance().getConnection();
      PreparedStatement ps = conn.prepareStatement(CREATE_USER, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, user.getUsername());
      // ma hoa mat khau
      String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
      ps.setString(2, hashedPassword);
      ps.setString(3, user.getEmail());
      ps.executeUpdate();
      ResultSet generatedKeys = ps.getGeneratedKeys();
      if (generatedKeys.next()) {
        int generatedId = generatedKeys.getInt(1);
        user.setId(generatedId);
      }
      conn.commit();
    } catch (SQLException e) {
      if (conn != null) {
        try {
          conn.rollback();
        } catch (SQLException ex) {
          throw new RuntimeException(ex);
        }
      }
      e.printStackTrace();
    } finally {
      if (Objects.nonNull(conn)) {
        try {
          conn.close();
        } catch (SQLException e) {
        }
      }
    }
    return user;
  }

  @Override
  public User update(User user, int id) {
    Connection conn = null;
    try {
      conn = DataSource.getInstance().getConnection();
      PreparedStatement ps = conn.prepareStatement(UPDATE_USER);
      ps.setString(1, user.getUsername());
      ps.setString(2, user.getPassword());
      ps.setString(3, user.getEmail());
      ps.setInt(4, id);
      ps.executeUpdate();
      conn.commit();
    } catch (SQLException e) {
      if (conn != null) {
        try {
          conn.rollback();
        } catch (SQLException ex) {
          throw new RuntimeException(ex);
        }
      }
      e.printStackTrace();
    } finally {
      if (Objects.nonNull(conn)) {
        try {
          conn.close();
        } catch (SQLException e) {
        }
      }
    }
    return user;
  }

  @Override
  public void delete(int id) {
    Connection conn = null;
    try {
      conn = DataSource.getInstance().getConnection();
      PreparedStatement ps = conn.prepareStatement(DELETE_USER);
      ps.setInt(1, id);
      ps.executeUpdate();
      conn.commit();
    } catch (SQLException e) {
      if (conn != null) {
        try {
          conn.rollback();
        } catch (SQLException ex) {
          throw new RuntimeException(ex);
        }
      }
      e.printStackTrace();
    } finally {
      if (Objects.nonNull(conn)) {
        try {
          conn.close();
        } catch (SQLException e) {
        }
      }
    }
  }

  @Override
  public User getByUsername(String username) {
    Connection conn = null;
    try {
      conn = DataSource.getInstance().getConnection();
      PreparedStatement ps = conn.prepareStatement(GET_BY_USERNAME);
      ps.setString(1, username);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        return user;
      }
    } catch (SQLException e) {
      if (conn != null) {
        try {
          conn.rollback();
        } catch (SQLException ex) {
          throw new RuntimeException(ex);
        }
      }
      e.printStackTrace();
    } finally {
      if (Objects.nonNull(conn)) {
        try {
          conn.close();
        } catch (SQLException e) {
        }
      }
    }
    return null;
  }

  @Override
  public List<User> list() {
    List<User> list = new ArrayList<>();
    Connection conn = null;
    try {
      conn = DataSource.getInstance().getConnection();
      PreparedStatement ps = conn.prepareStatement(GET_ALL);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        list.add(user);
      }
    } catch (SQLException e) {
      if (conn != null) {
        try {
          conn.rollback();
        } catch (SQLException ex) {
          throw new RuntimeException(ex);
        }
      }
      e.printStackTrace();
    } finally {
      if (Objects.nonNull(conn)) {
        try {
          conn.close();
        } catch (SQLException e) {
        }
      }
    }
    return list;
  }

  @Override
  public User getById(int id) {
    Connection conn = null;
    try {
      conn = DataSource.getInstance().getConnection();
      PreparedStatement ps = conn.prepareStatement(GET_BY_ID);
      ps.setInt(1, id);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        return user;
      }
    } catch (SQLException e) {
      if (conn != null) {
        try {
          conn.rollback();
        } catch (SQLException ex) {
          throw new RuntimeException(ex);
        }
      }
      e.printStackTrace();
    } finally {
      if (Objects.nonNull(conn)) {
        try {
          conn.close();
        } catch (SQLException e) {
        }
      }
    }
    return null;
  }
}
