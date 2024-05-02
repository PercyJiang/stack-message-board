package com.msg.graphql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MessageRepository {

  private static final BeanPropertyRowMapper<Message> MESSAGE_BEAN_MAPPER =
      BeanPropertyRowMapper.newInstance(Message.class);

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public MessageRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public List<Message> getAll() {
    String query = "select * from message;";
    return jdbcTemplate.query(query, MESSAGE_BEAN_MAPPER);
  }

  public int create(String source, String content) {
    String query = "insert into message (source, content) values (?, ?);";
    return jdbcTemplate.update(query, source, content);
  }
}
