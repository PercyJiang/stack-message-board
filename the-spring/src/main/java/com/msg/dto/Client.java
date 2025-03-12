package com.msg.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Client {
  private UUID id;
  private String username;
  private String password;
  private LocalDateTime created;
}
