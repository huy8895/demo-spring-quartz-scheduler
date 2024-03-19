package com.example.demospringquartzschedulter.dto;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpMethod;

@Getter
@Setter
@NoArgsConstructor
public class CreateJobDto extends JobDto {

  @NotNull(message = "Cron Expression must not be null")
  //@Schema(title = "Cron expession", example = "0 0 1 ? * * *")
  private String cronExpression;

  //@Schema(title = "Url sẽ call", example = "http://api-bpm-dev.msb.com.vn/cleanup-service/api/v1/clean-up-ms-ctqtxxxx")
  private String url;

//  //@Schema(title = "Request params khi call url", example = "{\"jsonNode\": { " +
//          "   \"HM_EB\": { " +
//          "    \"waringStatus\": [ " +
//          "     \"HM10\", " +
//          "     \"HM12\", " +
//          "     \"HM1C\", " +
//          "     \"HM21\", " +
//          "     \"HM22\", " +
//          "     \"HM23\", " +
//          "     \"HM31\", " +
//          "     \"HM32\", " +
//          "     \"HM33\" " +
//          "    ], " +
//          "    \"nonWaringStatus\": [ " +
//          "     \"HM3E\" " +
//          "    ], " +
//          "    \"warningThreshold\": 80, " +
//          "    \"mortgageTime\": 240, " +
//          "    \"unsecuredTime\": 192, " +
//          "    \"morningStartHour\": \"08:00:00\", " +
//          "    \"morningEndHour\": \"12:00:00\", " +
//          "    \"afternoonStartHour\": \"13:00:00\", " +
//          "    \"afternoonEndHour\": \"17:00:00\" " +
//          "   }, " +
//          "   \"HM_RB\": { " +
//          "    \"waringStatus\": [ " +
//          "     \"HM10\", " +
//          "     \"HM12\", " +
//          "     \"HM1C\", " +
//          "     \"HM21\", " +
//          "     \"HM22\", " +
//          "     \"HM23\", " +
//          "     \"HM31\", " +
//          "     \"HM32\", " +
//          "     \"HM33\" " +
//          "    ], " +
//          "    \"nonWaringStatus\": [ " +
//          "     \"HM3E\" " +
//          "    ], " +
//          "    \"warningThreshold\": 80, " +
//          "    \"mortgageTime\": 240, " +
//          "    \"unsecuredTime\": 192, " +
//          "    \"morningStartHour\": \"08:00:00\", " +
//          "    \"morningEndHour\": \"12:00:00\", " +
//          "    \"afternoonStartHour\": \"13:00:00\", " +
//          "    \"afternoonEndHour\": \"17:00:00\" " +
//          "   } " +
//          "  } " +
//          "    }")
  private Map<String, String> requestParams;
  
  //Not implemented yet
  private JsonNode requestBody;
  
  //Not implemented yet
//  //@Schema(title = "Request Method", example = "GET")
  private HttpMethod requestMethod;

  @Enumerated(EnumType.STRING)
//  //@Schema(title = "Class để cron: SimpleCronJob -> chọn nếu call API, MethodCronJob -> chọn nếu gọi medthod", example = "SimpleCronJob")
  private JobClass jobClass = JobClass.SimpleCronJob;

  //@Schema(title = "Tên medthod cần gọi", example = "helloWorld")
  private String jobMethod;

  //@Schema(title = "Mô tả job", example = "Đây là job")
  private String description;

}
