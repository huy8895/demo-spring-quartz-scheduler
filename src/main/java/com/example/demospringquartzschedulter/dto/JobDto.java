package com.example.demospringquartzschedulter.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JobDto {

  @NotNull(message = "JobName must not be null")
  private String jobName;

  @NotNull(message = "JobGroup must not be null")
  private String jobGroup;
}
