package com.example.demospringquartzschedulter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenResponse {

  @JsonProperty("access_token")
  private String accessToken;
  private List<String> roles;
  @JsonProperty("token_type")
  private String tokenType;
  @JsonProperty("expires_in")
  private int expiresIn;
  private String username;
}
