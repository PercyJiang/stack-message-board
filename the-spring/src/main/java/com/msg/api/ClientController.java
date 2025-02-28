package com.msg.api;

import com.msg.util.JwtUtil;
import com.msg.dto.Client;
import com.msg.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SuppressWarnings("unused")
@RestController
@RequestMapping(path = "clients")
@CrossOrigin
public class ClientController {

  private final ClientService clientService;
  private final JwtUtil jwtUtil;

  @Autowired
  public ClientController(ClientService clientService, JwtUtil jwtUtil) {
    this.clientService = clientService;
    this.jwtUtil = jwtUtil;
  }

  @PostMapping(
      value = "/post",
      consumes = {MediaType.APPLICATION_JSON_VALUE},
      produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<Void> create(@RequestBody Client client) {
    int result = clientService.create(client.getUsername(), client.getPassword());

    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    if (result == 1) status = HttpStatus.CREATED;
    if (result == 2) status = HttpStatus.CONFLICT;

    return new ResponseEntity<>(status);
  }

  @GetMapping(
      path = "",
      consumes = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<String> login(@RequestBody Client client) {
    boolean result = clientService.login(client.getUsername(), client.getPassword());

    if (!result) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    String token = jwtUtil.generateToken(client.getUsername());
    return new ResponseEntity<>(token, HttpStatus.OK);
  }
}
