package com.msg.dao;

import com.msg.dto.Message;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

  @Transactional
  public int create(String source, String content) {
    String query = "insert into message (source, content) values (?, ?);";
    return jdbcTemplate.update(query, source, content);
  }

  @Transactional
  public int update(String source, UUID id, String content) {
    String query = "update message set content =? where id =? and source =?;";
    return jdbcTemplate.update(query, content, id, source);
  }

  @Transactional
  public int delete(String source, UUID id) {
    String query = "delete from message where id =? and source =?;";
    return jdbcTemplate.update(query, id, source);
  }
}
