package com.example.demospringquartzschedulter.exceptions;


import com.example.demospringquartzschedulter.constants.ResponseStatusCode;
import com.example.demospringquartzschedulter.factory.GeneralResponse;
import com.example.demospringquartzschedulter.factory.GetObjectResponseEntity;
import com.example.demospringquartzschedulter.factory.ResponseStatus;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ServiceExceptionHandler extends ResponseEntityExceptionHandler {

  private static final Logger log =
      LoggerFactory.getLogger(ServiceExceptionHandler.class);
  private final GetObjectResponseEntity getObjectResponseEntity = new GetObjectResponseEntity();

  /**
   * Handle ConstraintViolationException. Happens when request JSON is malformed.
   *
   * @param ex ConstraintViolationException
   * @return the ResponseEntity<> object
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Object> handleConstraintViolationException(
      ConstraintViolationException ex) {
    log.error("ConstraintViolationException: {}", ex.getMessage(), ex);
    StringBuilder message = new StringBuilder();
    Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
    return this.createErrorResponse(
        ResponseStatusCode.INVALID_PARAMETER,
        HttpStatus.BAD_REQUEST,
        violations.toString());
  }




  /**
   * Handle RuntimeException.
   *
   * @param ex RuntimeException
   * @return the ResponseEntity<> object
   */
  @ExceptionHandler(RuntimeException.class)
  public final ResponseEntity<Object> handleAllRunTimeExceptions(RuntimeException ex) {
    log.error("RuntimeException: {}", ex.getMessage(), ex);
    return this.createErrorResponse(
        ResponseStatusCode.INTERNAL_SERVER_ERROR,
        HttpStatus.INTERNAL_SERVER_ERROR,
        ex.getMessage());
  }

  @ExceptionHandler(ResourceAccessException.class)
  public final ResponseEntity<Object> handleConnectException(ResourceAccessException ex) {
    log.error("ConnectException: {}", ex.getMessage(), ex);
    return this.createErrorResponse(
        ResponseStatusCode.CONNECT_ERROR,
        HttpStatus.INTERNAL_SERVER_ERROR,
        ex.getMessage());
  }

  /**
   * Handle Exception.
   *
   * @param ex Exception
   * @return the ResponseEntity<> object
   */
  @ExceptionHandler(Exception.class)
  public final ResponseEntity<Object> handleAllExceptions(Exception ex) {
    log.error("Exception: {}", ex.getMessage(), ex);
    return this.createErrorResponse(
        ResponseStatusCode.INTERNAL_SERVER_ERROR,
        HttpStatus.INTERNAL_SERVER_ERROR,
        ex.getMessage());
  }

  /**
   * Handle DataIntegrityViolationException.
   *
   * @param ex DataIntegrityViolationException
   * @return the ResponseEntity<> object
   */
  @ExceptionHandler(DataIntegrityViolationException.class)
  public final ResponseEntity<Object> handleSqlExceptions(DataIntegrityViolationException ex) {
    log.error("DataIntegrityViolationException: {} ", ex.getMessage(), ex);
    return this.createErrorResponse(
        ResponseStatusCode.INTERNAL_SERVER_ERROR,
        HttpStatus.BAD_REQUEST,
        ex.getMessage());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public final ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
    log.error("IllegalArgumentException {} ", ex.getMessage(), ex);
    return this.createErrorResponse(
        ResponseStatusCode.INTERNAL_SERVER_ERROR,
        HttpStatus.INTERNAL_SERVER_ERROR,
        ex.getMessage());
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public final ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
    log.error("EntityNotFoundException: {}", ex.getMessage(), ex);
    return this.createErrorResponse(
        ResponseStatusCode.RESOURCE_NOT_FOUND,
        HttpStatus.NOT_FOUND,
        ex.getMessage());
  }

  @ExceptionHandler(AccessDeniedException.class)
  public final ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
    log.error("AccessDeniedException: {}", ex.getMessage(), ex);
    return this.createErrorResponse(
        ResponseStatusCode.UNAUTHORIZED,
        HttpStatus.FORBIDDEN,
        ex.getMessage());
  }

  @ExceptionHandler(ScheduleJobErrorException.class)
  public final ResponseEntity<Object> handleGeneralInfoFieldErrorException(ScheduleJobErrorException ex) {
    log.error("AccessDeniedException: {}", ex.getMessage(), ex);
    return this.createErrorResponse(
            ex.getCode() == null ? ResponseStatusCode.INTERNAL_SERVER_ERROR : ex.getCode(),
        HttpStatus.INTERNAL_SERVER_ERROR,
        ex.getMessage());
  }

  private ResponseEntity<Object> createErrorResponse(
      ResponseStatusCode response, HttpStatus status, List<String> errorsDetails) {
    ResponseStatus responseStatus = new ResponseStatus(response.getCode(), response.getMessage());
    GeneralResponse<Object> responseObject = new GeneralResponse<>();
    responseObject.setData(errorsDetails);
    responseObject.setTimestamp(new Date());
    responseObject.setStatus(responseStatus);
    return new ResponseEntity<>(responseObject, status);
  }

  private ResponseEntity<Object> createErrorResponse(
          ResponseStatusCode response, HttpStatus status, String message) {
    return getObjectResponseEntity.getObjectResponseEntity(response, status, message);
  }
}
