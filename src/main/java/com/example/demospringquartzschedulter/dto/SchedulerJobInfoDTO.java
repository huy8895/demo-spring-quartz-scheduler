package com.example.demospringquartzschedulter.dto;

import com.example.demospringquartzschedulter.entity.SchedulerJobInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SchedulerJobInfoDTO {

  private Integer jobId;
  private String jobName;
  private String jobGroup;
  private String jobStatus;
  private String jobClass;
  private String jobMethod;
  private String cronExpression;
  private String description;
  private String interfaceName;
  private Long repeatTime;
  private Boolean cronJob;
  private Map<String, String> requestParams;
  private JsonNode requestBody;
  private String requestMethod;
  private String url;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss", timezone = "Asia/Bangkok")
  private Timestamp createdDate;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss", timezone = "Asia/Bangkok")
  private Timestamp updatedAt;

  public SchedulerJobInfoDTO(SchedulerJobInfo schedulerJobInfo) {
    this.jobId = schedulerJobInfo.getJobId();
    this.jobName = schedulerJobInfo.getJobName();
    this.jobGroup = schedulerJobInfo.getJobGroup();
    this.jobStatus = schedulerJobInfo.getJobStatus();
    this.jobClass = schedulerJobInfo.getJobClass();
    this.jobMethod = schedulerJobInfo.getJobMethod();
    this.cronExpression = schedulerJobInfo.getCronExpression();
    this.description = schedulerJobInfo.getDescription();
    this.interfaceName = schedulerJobInfo.getInterfaceName();
    this.repeatTime = schedulerJobInfo.getRepeatTime();
    this.cronJob = schedulerJobInfo.getCronJob();
    try {
      ObjectMapper mapper = new ObjectMapper();
      if (schedulerJobInfo.getRequestParams() != null) {
        this.requestParams = mapper.readValue(schedulerJobInfo.getRequestParams(), HashMap.class);
      }
      if (schedulerJobInfo.getRequestBody() != null) {
        this.requestBody = mapper.readTree(schedulerJobInfo.getRequestBody());
      }
    } catch (JsonProcessingException ignored) {}
    this.requestMethod = schedulerJobInfo.getRequestMethod();
    this.url = schedulerJobInfo.getUrl();
    this.createdDate = schedulerJobInfo.getCreatedDate();
    this.updatedAt = schedulerJobInfo.getUpdatedAt();
  }
}