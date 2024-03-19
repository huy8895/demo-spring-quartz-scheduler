package com.example.demospringquartzschedulter.factory;

import com.example.demospringquartzschedulter.constants.ResponseStatusCode;
import java.util.Date;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseFactory {

  public ResponseEntity<Object> success(Object data, Class<?> clazz) {
    GeneralResponse<Object> responseObject = new GeneralResponse<>();
    ResponseStatus responseStatus = new ResponseStatus();
    responseStatus.setCode(ResponseStatusCode.SUCCESS.getCode());
    responseStatus.setMessage(ResponseStatusCode.SUCCESS.getMessage());
    responseObject.setStatus(responseStatus);
    responseObject.setData(clazz.cast(data));
    responseObject.setTimestamp(new Date());

    return ResponseEntity.ok(responseObject);
  }

  public ResponseEntity<Object> success() {
    GeneralResponse<Object> responseObject = new GeneralResponse<>();

    ResponseStatus responseStatus = new ResponseStatus();
    responseStatus.setCode(ResponseStatusCode.SUCCESS.getCode());
    responseStatus.setMessage(ResponseStatusCode.SUCCESS.getMessage());
    responseObject.setStatus(responseStatus);
    responseObject.setTimestamp(new Date());
    return ResponseEntity.ok(responseObject);
  }

  public ResponseEntity<Object> created(Object data, Class<?> clazz) {
    GeneralResponse<Object> responseObject = new GeneralResponse<>();
    ResponseStatus responseStatus = new ResponseStatus();
    responseStatus.setCode(ResponseStatusCode.CREATED.getCode());
    responseStatus.setMessage(ResponseStatusCode.CREATED.getMessage());
    responseObject.setStatus(responseStatus);
    responseObject.setData(clazz.cast(data));
    responseObject.setTimestamp(new Date());

    return new ResponseEntity<Object>(responseObject, HttpStatus.CREATED);
  }

  public ResponseEntity<Object> nonPermission() {
    GeneralResponse<Object> responseObject = new GeneralResponse<>();
    ResponseStatus responseStatus = new ResponseStatus();
    responseStatus.setCode(ResponseStatusCode.NO_PERMISSION.getCode());
    responseStatus.setMessage(ResponseStatusCode.NO_PERMISSION.getMessage());
    responseObject.setStatus(responseStatus);
    responseObject.setTimestamp(new Date());
    return ResponseEntity.ok(responseObject);
  }

}
