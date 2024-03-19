package com.example.demospringquartzschedulter.factory;


import com.example.demospringquartzschedulter.constants.ResponseStatusCode;
import java.util.Date;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class GetObjectResponseEntity {

  public ResponseEntity<Object> getObjectResponseEntity(ResponseStatusCode response,
      HttpStatus status, String message) {
    ResponseStatus responseStatus = new ResponseStatus(response.getCode(), response.getMessage());
    GeneralResponse<Object> responseObject = new GeneralResponse<>();
    responseObject.setData(message);
    responseObject.setStatus(responseStatus);
    responseObject.setTimestamp(new Date());
    return new ResponseEntity<>(responseObject, status);
  }
}
