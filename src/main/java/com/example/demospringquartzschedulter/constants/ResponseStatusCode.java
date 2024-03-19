package com.example.demospringquartzschedulter.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseStatusCode {

  SUCCESS("DS200", "Success"),
  CREATED("DS201", "Created"),
  NO_PERMISSION("DS40000", "The client has no permission calling this API"),
  INVALID_PARAMETER("DS40001", "Invalid request parameter"),
  INTERNAL_SERVER_ERROR("DS50000", "Internal server error"),
  UPDATE_ENTITY_FAILED("DS50001", "Update failed. Please try again"),
  CONNECT_ERROR("DS50006", "Connect to third party failed."),
  SERVER_3RD_ERROR("DS50001", "Server third party failed."),
  CLIENT_REQUEST_ERROR("DS50002", "Request to third party failed."),
  DUPLICATED_KEY("DS40003", "Environment key has already exists, please select another code"),
  SCOPE_NOT_FOUND("DS40004", "Scope not found"),
  RESOURCE_NOT_FOUND("DS4040", "Resource not found"),
  INCORRECT_FILE_FORMAT("DS400", "Incorrect file format"),
  CODE_INVALID("DS40002", "Project code has already exists, please select another code"),
  UNAUTHORIZED("DS401", "Unauthorized"),
  JOB_ALREADY_EXIST("DS60001", "Job already exist"),
  JOB_CLASS_NOT_FOUND("DS60002", "Job class not found");

  private String code;
  private String message;
}