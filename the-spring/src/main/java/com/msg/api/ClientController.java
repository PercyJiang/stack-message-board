package com.msg.api;

import com.msg.dao.ClientRepository;
import com.msg.dao.RabbitDao;
import com.msg.dto.Client;
import com.msg.util.JwtUtil;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@SuppressWarnings("unused")
@RestController
@RequestMapping(path = "clients")
@CrossOrigin
public class ClientController {

  private final ClientRepository clientRepository;
  private final JwtUtil jwtUtil;
  private final RabbitDao rabbitDao;

  @Autowired
  public ClientController(ClientRepository clientRepository, JwtUtil jwtUtil, RabbitDao rabbitDao) {
    this.clientRepository = clientRepository;
    this.jwtUtil = jwtUtil;
    this.rabbitDao = rabbitDao;
  }

  @PostMapping(
      value = "/post",
      consumes = {MediaType.APPLICATION_JSON_VALUE},
      produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<Void> create(@RequestBody Client client) {

    int result = clientRepository.create(client.getUsername(), client.getPassword());

    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    if (result == 1) status = HttpStatus.CREATED;
    if (result == 2) status = HttpStatus.CONFLICT;

    return new ResponseEntity<>(status);
  }

  @GetMapping(
      path = "",
      consumes = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<String> login(@RequestBody Client client) {

    Client clientFound = clientRepository.get(client.getUsername(), client.getPassword());
    boolean result =
        clientFound != null
            && clientFound.getUsername() != null
            && clientFound.getPassword() != null;

    if (!result) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    String token = jwtUtil.generateToken(client.getUsername());
    return new ResponseEntity<>(token, HttpStatus.OK);
  }

  @PostMapping(
      value = "/{username}/post/pic",
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
      produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<Void> postPic(
      @RequestHeader("Authorization") String authorizationHeader,
      @PathVariable String username,
      @RequestParam("file") MultipartFile file)
      throws BadRequestException {

    jwtUtil.validateJwt(authorizationHeader, username);
    if (System.getenv("USE_RABBITMQ").equals("true")) {
      rabbitDao.publish(username, file);
    }
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @PostMapping(
      value = "/{username}/post/brief",
      consumes = {MediaType.APPLICATION_JSON_VALUE},
      produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<Void> postBrief(
      @RequestHeader("Authorization") String authorizationHeader,
      @PathVariable String username,
      @RequestBody Object brief)
      throws BadRequestException {

    jwtUtil.validateJwt(authorizationHeader, username);
    if (System.getenv("USE_RABBITMQ").equals("true")) {
      rabbitDao.publish(username, brief);
    }
    return new ResponseEntity<>(HttpStatus.CREATED);
  }
}
