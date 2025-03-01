package com.msg.api;

import com.msg.dto.Message;
import com.msg.service.MessageService;
import com.msg.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SuppressWarnings("unused")
@RestController
@RequestMapping(path = "messages")
@CrossOrigin
@Tag(name = "Messages")
public class MessageController {

  private final MessageService messageService;
  private final JwtUtil jwtUtil;

  @Autowired
  public MessageController(MessageService messageService, JwtUtil jwtUtil) {
    this.messageService = messageService;
    this.jwtUtil = jwtUtil;
  }

  @Operation(summary = "Get all messages", description = "Retrieves all messages")
  @ApiResponse(responseCode = "200", description = "Successfully retrieved messages")
  @GetMapping(value = "/{username}/getAll")
  public ResponseEntity<List<Message>> getAll(
      @RequestHeader("Authorization") String authorizationHeader, @PathVariable String username)
      throws BadRequestException {
    jwtUtil.validateJwt(authorizationHeader, username);
    List<Message> response = messageService.getAll();
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @Operation(summary = "Create a new message", description = "Creates a new message for a user")
  @ApiResponse(responseCode = "201", description = "Message created successfully")
  @ApiResponse(responseCode = "500", description = "Internal server error")
  @PostMapping(
      value = "/{username}/post",
      consumes = {MediaType.TEXT_PLAIN_VALUE})
  public ResponseEntity<Void> create(
      @RequestHeader("Authorization") String authorizationHeader,
      @PathVariable String username,
      @RequestBody String content)
      throws BadRequestException {
    jwtUtil.validateJwt(authorizationHeader, username);
    int result = messageService.create(username, content);
    return result == 1
        ? new ResponseEntity<>(HttpStatus.CREATED)
        : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Operation(summary = "Update a message", description = "Updates an existing message for a user")
  @ApiResponse(responseCode = "202", description = "Message updated successfully")
  @ApiResponse(responseCode = "404", description = "Message not found")
  @PutMapping(
      value = "/{username}/put/{id}",
      consumes = {MediaType.TEXT_PLAIN_VALUE})
  public ResponseEntity<Void> update(
      @RequestHeader("Authorization") String authorizationHeader,
      @PathVariable String username,
      @PathVariable UUID id,
      @RequestBody String content)
      throws BadRequestException {
    jwtUtil.validateJwt(authorizationHeader, username);
    int result = messageService.update(username, id, content);
    return result == 1
        ? new ResponseEntity<>(HttpStatus.ACCEPTED)
        : new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @Operation(summary = "Delete a message", description = "Deletes an existing message for a user")
  @ApiResponse(responseCode = "202", description = "Message deleted successfully")
  @ApiResponse(responseCode = "404", description = "Message not found")
  @DeleteMapping(value = "/{username}/delete/{id}")
  public ResponseEntity<Void> delete(
      @RequestHeader("Authorization") String authorizationHeader,
      @PathVariable String username,
      @PathVariable UUID id)
      throws BadRequestException {
    jwtUtil.validateJwt(authorizationHeader, username);
    int result = messageService.delete(username, id);
    return result == 1
        ? new ResponseEntity<>(HttpStatus.ACCEPTED)
        : new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }
}
