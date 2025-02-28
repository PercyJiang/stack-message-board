package com.msg.api;

import com.msg.util.JwtUtil;
import com.msg.dto.Message;
import com.msg.service.MessageService;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SuppressWarnings("unused")
@RestController
@RequestMapping(path = "messages")
@CrossOrigin
public class MessageController {

  private final MessageService messageService;
  private final JwtUtil jwtUtil;

  @Autowired
  public MessageController(MessageService messageService, JwtUtil jwtUtil) {
    this.messageService = messageService;
    this.jwtUtil = jwtUtil;
  }

  @GetMapping(value = "/{username}/getAll")
  public ResponseEntity<List<Message>> getAll(
      @RequestHeader("Authorization") String authorizationHeader, @PathVariable String username) {
    jwtUtil.validateJwt(authorizationHeader, username);
    List<Message> response = messageService.getAll();
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping(
      value = "/{username}/post",
      consumes = {MediaType.TEXT_PLAIN_VALUE})
  public ResponseEntity<Integer> create(
      @RequestHeader("Authorization") String authorizationHeader,
      @PathVariable String username,
      @RequestBody String content) {
    jwtUtil.validateJwt(authorizationHeader, username);
    int result = messageService.create(username, content);
    return result == 1
        ? new ResponseEntity<>(HttpStatus.CREATED)
        : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @PutMapping(
      value = "/{username}/put/{id}",
      consumes = {MediaType.TEXT_PLAIN_VALUE})
  public ResponseEntity<Void> update(
      @RequestHeader("Authorization") String authorizationHeader,
      @PathVariable String username,
      @PathVariable UUID id,
      @RequestBody String content) {
    jwtUtil.validateJwt(authorizationHeader, username);
    int result = messageService.update(username, id, content);
    return result == 1
        ? new ResponseEntity<>(HttpStatus.ACCEPTED)
        : new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @DeleteMapping(value = "/{username}/delete/{id}")
  public ResponseEntity<Void> delete(
      @RequestHeader("Authorization") String authorizationHeader,
      @PathVariable String username,
      @PathVariable UUID id) {
    jwtUtil.validateJwt(authorizationHeader, username);
    int result = messageService.delete(username, id);
    return result == 1
        ? new ResponseEntity<>(HttpStatus.ACCEPTED)
        : new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }
}
