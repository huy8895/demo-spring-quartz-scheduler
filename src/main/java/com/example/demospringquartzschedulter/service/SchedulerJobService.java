package com.example.demospringquartzschedulter.service;

import com.example.demospringquartzschedulter.config.JobScheduleCreator;
import com.example.demospringquartzschedulter.constants.JobStatusCode;
import com.example.demospringquartzschedulter.constants.ResponseStatusCode;
import com.example.demospringquartzschedulter.dto.CreateJobDto;
import com.example.demospringquartzschedulter.dto.JobDto;
import com.example.demospringquartzschedulter.dto.SchedulerJobInfoDTO;
import com.example.demospringquartzschedulter.dto.SearchDto;
import com.example.demospringquartzschedulter.dto.UpdateJobDto;
import com.example.demospringquartzschedulter.entity.SchedulerJobInfo;
import com.example.demospringquartzschedulter.exceptions.ScheduleJobErrorException;
import com.example.demospringquartzschedulter.factory.BaseSpecification;
import com.example.demospringquartzschedulter.factory.ResponseFactory;
import com.example.demospringquartzschedulter.repository.SchedulerRepository;
import com.example.demospringquartzschedulter.utils.PageUtil;
import com.example.demospringquartzschedulter.utils.SearchOperation;
import com.example.demospringquartzschedulter.utils.SpecSearchCriteria;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@Slf4j
public class SchedulerJobService {

  private final SchedulerFactoryBean schedulerFactoryBean;

  private final SchedulerRepository schedulerRepository;

  private final ApplicationContext context;

  private final JobScheduleCreator scheduleCreator;

  private final ResponseFactory responseFactory;
  private final ObjectMapper objectMapper;
  public static final String JOBS = "jobs";
  public static final String CURRENT_PAGE = "currentPage";
  public static final String TOTAL_ITEMS = "totalItems";
  public static final String TOTAL_PAGES = "totalPages";
  @Autowired
  public SchedulerJobService(
      SchedulerFactoryBean schedulerFactoryBean,
      SchedulerRepository schedulerRepository,
      ApplicationContext context, JobScheduleCreator scheduleCreator,
      ResponseFactory responseFactory,
      ObjectMapper objectMapper) {
    this.schedulerFactoryBean = schedulerFactoryBean;
    this.schedulerRepository = schedulerRepository;
    this.context = context;
    this.scheduleCreator = scheduleCreator;
    this.responseFactory = responseFactory;
    this.objectMapper = objectMapper;
  }

  public SchedulerJobInfoDTO scheduleNewJob(CreateJobDto createJobDto) throws JsonProcessingException {
    SchedulerJobInfo jobInfo = new SchedulerJobInfo();
    String packageName = "com.example.demospringquartzschedulter.service.";
    jobInfo.setJobClass(packageName + createJobDto.getJobClass());
    jobInfo.setCronJob(true);
    jobInfo.setJobName(createJobDto.getJobName());
    jobInfo.setJobGroup(createJobDto.getJobGroup());
    jobInfo.setCronExpression(createJobDto.getCronExpression());
    jobInfo.setJobMethod(createJobDto.getJobMethod());
    jobInfo.setDescription(createJobDto.getDescription());
    jobInfo.setRequestBody(createJobDto.getRequestBody().toString());
    jobInfo.setRequestParams(objectMapper.writeValueAsString(createJobDto.getRequestParams()));
    jobInfo.setRequestMethod(createJobDto.getRequestMethod().name());
    jobInfo.setUrl(createJobDto.getUrl());
    try {
      Scheduler scheduler = schedulerFactoryBean.getScheduler();
      JobDataMap jobDataMap = new JobDataMap();
      jobDataMap.put("URL", createJobDto.getUrl());
      jobDataMap.put("NAME", createJobDto.getJobName());
      jobDataMap.put("GROUP", createJobDto.getJobGroup());
      jobDataMap.put("METHOD_NAME", createJobDto.getJobMethod());

      if ( !CollectionUtils.isEmpty(createJobDto.getRequestParams()) ) {
    	  jobDataMap.put("REQUEST_PARAMETERS", createJobDto.getRequestParams());
      }
      if ( Objects.nonNull(createJobDto.getRequestBody()) ) {
    	  jobDataMap.put("REQUEST_BODY", createJobDto.getRequestBody());
      }
      if ( Objects.nonNull(createJobDto.getRequestMethod()) ) {
    	  jobDataMap.put("REQUEST_METHOD", createJobDto.getRequestMethod());
      }

      JobDetail jobDetail = JobBuilder
          .newJob((Class<? extends QuartzJobBean>) Class.forName(jobInfo.getJobClass()))
          .withIdentity(jobInfo.getJobName(), jobInfo.getJobGroup())
          .build();
      if (!scheduler.checkExists(jobDetail.getKey())) {

        jobDetail = scheduleCreator.createJob(
            (Class<? extends QuartzJobBean>) Class.forName(jobInfo.getJobClass()), false, context,
            jobInfo.getJobName(), jobInfo.getJobGroup(), jobDataMap);

        Trigger trigger = scheduleCreator.createCronTrigger(
            jobInfo.getJobName(),
            new Date(),
            jobInfo.getCronExpression(),
            SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        scheduler.scheduleJob(jobDetail, trigger);
        jobInfo.setJobStatus(JobStatusCode.SCHEDULED.getCode());
        jobInfo.setInterfaceName("interface_");
        log.info(">>>>> jobName = [" + jobInfo.getJobName() + "]" + " scheduled.");
        log.info(">>>>> jobName = [" + jobInfo.getJobName() + "]" + " created.");
        return new SchedulerJobInfoDTO(schedulerRepository.save(jobInfo));
      } else {
        log.error("scheduleNewJobRequest.jobAlreadyExist");
        throw new ScheduleJobErrorException(ResponseStatusCode.JOB_ALREADY_EXIST, "Job already exist");
      }
    } catch (ClassNotFoundException e) {
      log.error("Class Not Found - {}", jobInfo.getJobClass(), e);
      throw new ScheduleJobErrorException(ResponseStatusCode.JOB_CLASS_NOT_FOUND, "Class not found");
    } catch (SchedulerException e) {
      log.error(e.getMessage(), e);
      throw new ScheduleJobErrorException("error: " + e.getMessage());
    }
  }

  public SchedulerJobInfoDTO updateScheduleJob(UpdateJobDto updateJobDto) {
    SchedulerJobInfo jobInfo = schedulerRepository.findById(updateJobDto.getJobId()).orElseThrow();
    jobInfo.setCronExpression(updateJobDto.getCronExpression());
    jobInfo.setJobName(updateJobDto.getJobName());
    jobInfo.setJobGroup(updateJobDto.getJobGroup());
    jobInfo.setJobMethod(updateJobDto.getJobMethod());
    jobInfo.setDescription(updateJobDto.getDescription());
    Trigger newTrigger;

    final JobDataMap jobDataMap = new JobDataMap();
    jobDataMap.put("URL", updateJobDto.getUrl());
    jobDataMap.put("NAME", updateJobDto.getJobName());
    jobDataMap.put("GROUP", updateJobDto.getJobGroup());
    jobDataMap.put("METHOD_NAME", updateJobDto.getJobMethod());
    if ( !CollectionUtils.isEmpty(updateJobDto.getRequestParams()) ) {
      jobDataMap.put("REQUEST_PARAMETERS", updateJobDto.getRequestParams());
    }
    if ( Objects.nonNull(updateJobDto.getRequestBody()) ) {
      jobDataMap.put("REQUEST_BODY", updateJobDto.getRequestBody());
    }
    if ( Objects.nonNull(updateJobDto.getRequestMethod()) ) {
      jobDataMap.put("REQUEST_METHOD", updateJobDto.getRequestMethod());
    }


    newTrigger = scheduleCreator.createCronTrigger(
        jobInfo.getJobName(),
        new Date(),
        jobInfo.getCronExpression(),
        SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
    try {
      //Get and update job detail
      Scheduler scheduler = schedulerFactoryBean.getScheduler();
      JobDetail jobDetail = scheduleCreator.createJob(
          (Class<? extends QuartzJobBean>) Class.forName(jobInfo.getJobClass()), true, context,
          jobInfo.getJobName(), jobInfo.getJobGroup(), jobDataMap);
      scheduler.addJob(jobDetail, true);
      scheduler.triggerJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()), jobDataMap);
      //Reschedule the job
      schedulerFactoryBean.getScheduler()
          .rescheduleJob(TriggerKey.triggerKey(jobInfo.getJobName()), newTrigger);
      jobInfo.setJobStatus(JobStatusCode.EDITED_AND_SCHEDULED.getCode());
      log.info(">>>>> jobName = [" + jobInfo.getJobName() + "]" + " updated and scheduled.");
      return new SchedulerJobInfoDTO(schedulerRepository.save(jobInfo));
    } catch (SchedulerException | ClassNotFoundException e) {
      log.error(e.getMessage(), e);
      throw new ScheduleJobErrorException("error:" + e.getMessage());
    }
  }

  public void startJobNow(JobDto jobDto) {
    try {
      SchedulerJobInfo getJobInfo = schedulerRepository.findByJobName(jobDto.getJobName());
      getJobInfo.setJobStatus(JobStatusCode.EDITED_AND_STARTED.getCode());
      schedulerRepository.save(getJobInfo);
      schedulerFactoryBean.getScheduler()
          .triggerJob(new JobKey(jobDto.getJobName(), jobDto.getJobGroup()));
      log.info(">>>>> jobName = [" + jobDto.getJobName() + "]" + " scheduled and started now.");
    } catch (SchedulerException e) {
      log.error("Failed to start new job - {}", jobDto.getJobName(), e);
      throw new ScheduleJobErrorException("error: " + e.getMessage());
    }
  }

  public void pauseJob(JobDto jobInfo) {
    try {
      SchedulerJobInfo getJobInfo = schedulerRepository.findByJobName(jobInfo.getJobName());
      getJobInfo.setJobStatus(JobStatusCode.PAUSED.getCode());
      schedulerRepository.save(getJobInfo);
      schedulerFactoryBean.getScheduler()
          .pauseJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
      log.info(">>>>> jobName = [" + jobInfo.getJobName() + "]" + " paused.");
    } catch (SchedulerException e) {
      log.error("Failed to pause job - {}", jobInfo.getJobName(), e);
      throw new ScheduleJobErrorException("error: " + e.getMessage());
    }
  }

  public void resumeJob(JobDto jobInfo) {
    try {
      SchedulerJobInfo getJobInfo = schedulerRepository.findByJobName(jobInfo.getJobName());
      getJobInfo.setJobStatus(JobStatusCode.RESUMED.getCode());
      schedulerRepository.save(getJobInfo);
      schedulerFactoryBean.getScheduler()
          .resumeJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
      log.info(">>>>> jobName = [" + jobInfo.getJobName() + "]" + " resumed.");
    } catch (SchedulerException e) {
      log.error("Failed to resume job - {}", jobInfo.getJobName(), e);
      throw new ScheduleJobErrorException("error: " + e.getMessage());
    }
  }

  public boolean deleteJob(JobDto jobInfo) {
    try {
      SchedulerJobInfo getJobInfo = schedulerRepository.findByJobName(jobInfo.getJobName());
      schedulerRepository.delete(getJobInfo);
      log.info(">>>>> jobName = [" + jobInfo.getJobName() + "]" + " deleted.");
      return schedulerFactoryBean.getScheduler()
          .deleteJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
    } catch (SchedulerException e) {
      log.error("Failed to delete job - {}", jobInfo.getJobName(), e);
      throw new ScheduleJobErrorException("error: " + e.getMessage());
    }
  }

  public ResponseEntity<Object> getSchedules() {
    List<SchedulerJobInfo> jobInfos = schedulerRepository.findAll(Sort.by("jobId"));

    return responseFactory.success(jobInfos, List.class);
  }

  public ResponseEntity<Object> getSchedules(SearchDto searchDto) {
        Specification<SchedulerJobInfo> fullQuery = null;


    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");;
    for (SearchDto.SearchKey searchKey: searchDto.getSearch()) {
      Object data = searchKey.getValue();
      if (searchKey.getKey().contains("At")) {
        try {
          data = formatter.parse(searchKey.getValue());
        } catch (ParseException ignore) {}
      }
      BaseSpecification<SchedulerJobInfo> spec = new BaseSpecification<>(
              new SpecSearchCriteria(searchKey.getKey(), SearchOperation.getSimpleOperation(searchKey.getOperator()),
                      data));
      if (fullQuery == null) {
        fullQuery = spec;
      } else {
        fullQuery = Specification.where(fullQuery).and(spec);
      }
    }
    String fullSearch = searchDto.getFullSearch();

    Specification<SchedulerJobInfo> quickSearchQuery = null;
    if (Strings.isNotEmpty(fullSearch)) {
      String[] quickSearchKey = {"jobName", "jobGroup"};
      List<SearchDto.SearchKey> searchLike = new ArrayList<>();
      String likeValue = "%" + fullSearch.toLowerCase() + "%";
      String operator = "LIKE_IGNORE_CASE";
      Arrays.stream(quickSearchKey).forEach(key -> searchLike.add(
              new SearchDto.SearchKey(true, key, operator, likeValue)));

      for (SearchDto.SearchKey searchKey: searchLike) {
        Specification<SchedulerJobInfo> spec = (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get(searchKey.getKey())), searchKey.getValue());
        if (quickSearchQuery == null) {
          quickSearchQuery = spec;
        } else {
          quickSearchQuery = Specification.where(fullQuery).or(spec);
        }
      }
      log.info("full search key '{}' -> search like '{}'", fullSearch, searchLike);
      if (fullQuery == null) {
        fullQuery = quickSearchQuery;
      } else {
        fullQuery = Specification.where(fullQuery).and(quickSearchQuery);
      }
    }
    Pageable paging = PageUtil.getPageable(searchDto.getPageable());
    Page<SchedulerJobInfoDTO> jobInfos = schedulerRepository.findAll(fullQuery, paging).map(SchedulerJobInfoDTO::new);
    Map<String, Object> response = new HashMap<>();
    response.put(JOBS, jobInfos.getContent());
    response.put(CURRENT_PAGE, jobInfos.getNumber());
    response.put(TOTAL_ITEMS, jobInfos.getTotalElements());
    response.put(TOTAL_PAGES, jobInfos.getTotalPages());
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
