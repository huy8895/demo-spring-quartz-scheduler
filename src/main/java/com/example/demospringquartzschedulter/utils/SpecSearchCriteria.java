package com.example.demospringquartzschedulter.utils;

import java.io.Serializable;
import lombok.Getter;

@Getter
public class SpecSearchCriteria implements Serializable {

  private final String key;
  private final SearchOperation operation;
  private final transient Object value;

  public SpecSearchCriteria(final String key, final SearchOperation operation, final Object value) {
    super();
    this.key = key;
    this.operation = operation;
    this.value = value;
  }
}
