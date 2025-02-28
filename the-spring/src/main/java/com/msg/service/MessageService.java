package com.msg.service;

import com.msg.dao.MessageRepository;
import com.msg.dto.Message;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

  private final MessageRepository messageRepository;

  @Autowired
  public MessageService(MessageRepository messageRepository) {
    this.messageRepository = messageRepository;
  }

  public List<Message> getAll() {
    return messageRepository.getAll();
  }

  public int create(String source, String content) {
    return messageRepository.create(source, content);
  }

  public int update(String username, UUID id, String content) {
    return messageRepository.update(username, id, content);
  }

  public int delete(String username, UUID id) {
    return messageRepository.delete(username, id);
  }
}
