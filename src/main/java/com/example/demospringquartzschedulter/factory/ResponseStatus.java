package com.example.demospringquartzschedulter.factory;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseStatus implements Serializable {

  private static final long serialVersionUID = 1L;

  @JsonProperty("code")
  private String code;

  @JsonProperty("message")
  private String message;
}
