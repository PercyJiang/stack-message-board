package com.msg.service;

import com.msg.dao.ClientRepository;
import com.msg.dto.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

  private final ClientRepository clientRepository;

  @Autowired
  public ClientService(ClientRepository clientRepository) {
    this.clientRepository = clientRepository;
  }

  public int create(String username, String password) {
    return clientRepository.create(username, password);
  }

  public boolean login(String username, String password) {
    Client client = clientRepository.getByUsername(username);
    return client != null
        && client.getUsername() != null
        && client.getPassword() != null
        && client.getUsername().equals(username)
        && client.getPassword().equals(password);
  }
}
