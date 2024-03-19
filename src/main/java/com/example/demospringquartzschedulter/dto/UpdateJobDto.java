package com.example.demospringquartzschedulter.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateJobDto extends CreateJobDto {

  @NotNull(message = "Id must not be null")
//  @Schema(title = "Id của job", example = "1333")
  private Integer jobId;
}
