package com.example.demospringquartzschedulter.factory;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GeneralResponse<T> implements Serializable {

  private static final long serialVersionUID = 1L;

  @JsonProperty
  private Date timestamp;

  @JsonProperty("status")
  private ResponseStatus status;

  @JsonProperty("data")
  private T data;

}
