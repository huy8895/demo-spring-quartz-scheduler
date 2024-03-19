package com.example.demospringquartzschedulter.utils;

import java.io.Serializable;

public enum SearchOperation implements Serializable {
  EQUALITY, NEGATION, GREATER_THAN, LESS_THAN, LIKE, IN,
  GREATER_THAN_OR_EQUAL, LESS_THAN_OR_EQUAL, IS_NULL, IS_NOT_NULL, LIKE_IGNORE_CASE;

  public static SearchOperation getSimpleOperation(final String input) {
    switch (input) {
      case ":":
        return EQUALITY;
      case "!":
        return NEGATION;
      case ">":
        return GREATER_THAN;
      case "<":
        return LESS_THAN;
      case "~":
        return LIKE;
      case "#":
        return IN;
      case ">=":
        return GREATER_THAN_OR_EQUAL;
      case "<=":
        return LESS_THAN_OR_EQUAL;
      case "IS_NULL":
        return IS_NULL;
      case "LIKE_IGNORE_CASE":
        return LIKE_IGNORE_CASE;
      default:
        return null;
    }
  }
}