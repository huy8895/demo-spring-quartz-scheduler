package com.example.demospringquartzschedulter.exceptions;


import com.example.demospringquartzschedulter.constants.ResponseStatusCode;
import lombok.Getter;

@Getter
public class ScheduleJobErrorException extends RuntimeException {
  private ResponseStatusCode code;
  public ScheduleJobErrorException(ResponseStatusCode code, String errorMessage) {
    super(errorMessage);
    this.code = code;
  }

  public ScheduleJobErrorException(String errorMessage) {
    super(errorMessage);
  }
}
