package com.example.demospringquartzschedulter.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constant {
    public static final Integer NOT_CLEANED_YET = 0;
    public static final Integer CLEANED_CAMUNDA = 1;
    public static final Integer SELF_CLEANED_CAMUNDA = 10;
    public static final Integer CLEANED_MIN_IO = 2;
}
