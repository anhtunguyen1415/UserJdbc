package com.example.demo.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

public class DataSource {

  private static HikariConfig config = new HikariConfig();
  private static HikariDataSource ds;
  private static DataSource dataSource;

  static {
    config.setJdbcUrl("jdbc:mysql://localhost:3306/UserManage");
    config.setUsername("root");
    config.setPassword("123456");
    config.setMaximumPoolSize(10);
    config.setMinimumIdle(5);
    config.setAutoCommit(false);
    ds = new HikariDataSource(config);
  }

  private DataSource() {
  }

  public Connection getConnection() throws SQLException {
    return ds.getConnection();
  }

  public static DataSource getInstance() {
    if (Objects.isNull(dataSource)) {
      dataSource = new DataSource();
    }
    return dataSource;
  }

}