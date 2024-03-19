package com.example.demospringquartzschedulter.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "scheduler_job_info")
public class SchedulerJobInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "job_id")
  private Integer jobId;

  @Column(name = "job_name")
  private String jobName;

  @Column(name = "job_group")
  private String jobGroup;

  @Column(name = "job_status")
  private String jobStatus;

  @Column(name = "job_class")
  private String jobClass;

  @Column(name = "job_method")
  private String jobMethod;

  @Column(name = "cron_expression")
  private String cronExpression;

  @Column(name = "description")
  private String description;

  @Column(name = "interface_name")
  private String interfaceName;

  @Column(name = "repeat_time")
  private Long repeatTime;

  @Column(name = "cron_job")
  private Boolean cronJob;

  @Column(name = "request_param")
  private String requestParams;

  @Column(name = "request_body")
  private String requestBody;

  @Column(name = "request_method")
  private String requestMethod;

  @Column(name = "url")
  private String url;

}