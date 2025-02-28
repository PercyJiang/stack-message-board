package com.msg.dao;

import com.msg.dto.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ClientRepository {

  private static final BeanPropertyRowMapper<Client> USER_BEAN_MAPPER =
      BeanPropertyRowMapper.newInstance(Client.class);

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public ClientRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public int create(String username, String password) {
    try {
      String query = "insert into client (username, password) values (?, ?);";
      return jdbcTemplate.update(query, username, password);
    } catch (DuplicateKeyException e) {
      return 2;
    }
  }

  public Client getByUsername(String username) {
    String query = "select * from client where username =?;";
    return jdbcTemplate.queryForObject(query, USER_BEAN_MAPPER, username);
  }
}
