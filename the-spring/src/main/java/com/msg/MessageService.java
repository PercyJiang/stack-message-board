package com.msg;

import com.msg.dto.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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

    public int update(UUID id, String content) {
        return messageRepository.update(id, content);
    }

    public int delete(UUID id) {
        return messageRepository.delete(id);
    }
}
