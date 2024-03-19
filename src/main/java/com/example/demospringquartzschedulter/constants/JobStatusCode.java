package com.example.demospringquartzschedulter.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JobStatusCode {

	SCHEDULED("SCHEDULED", "SCHEDULED"),
	EDITED_AND_SCHEDULED("EDITED & SCHEDULED", "EDITED_AND_SCHEDULED"),
	EDITED_AND_STARTED("SCHEDULED & STARTED", "EDITED_AND_STARTED"),
	PAUSED("PAUSED", "PAUSED"),
	RESUMED("RESUMED", "RESUMED");

	private String code;
	private String message;
}