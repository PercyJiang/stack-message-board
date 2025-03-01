package com.msg.api;

import com.msg.util.JwtUtil;
import com.msg.dto.Client;
import com.msg.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SuppressWarnings("unused")
@RestController
@RequestMapping(path = "clients")
@CrossOrigin
@Tag(name = "Clients", description = "Client management API")
public class ClientController {

  private final ClientService clientService;
  private final JwtUtil jwtUtil;

  @Autowired
  public ClientController(ClientService clientService, JwtUtil jwtUtil) {
    this.clientService = clientService;
    this.jwtUtil = jwtUtil;
  }

  @Operation(summary = "Create a new client", description = "Creates a new client with the given username and password")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Client created successfully"),
      @ApiResponse(responseCode = "409", description = "Client already exists"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
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

  @Operation(summary = "Login client", description = "Authenticates a client and returns a JWT token")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful login", 
          content = @Content(schema = @Schema(implementation = String.class))),
      @ApiResponse(responseCode = "404", description = "Client not found")
  })
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