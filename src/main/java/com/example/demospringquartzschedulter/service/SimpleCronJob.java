package com.example.demospringquartzschedulter.service;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

@DisallowConcurrentExecution
@Slf4j
@RequiredArgsConstructor
public class SimpleCronJob extends QuartzJobBean {


  private final RestTemplate restTemplate;



  @Override
  @SuppressWarnings("unchecked")
  /*
   * The method will call a callback url which is responsible for actual work
   */
  protected void executeInternal(JobExecutionContext context) {
    JobDetail jobDetail = context.getJobDetail();
    JobDataMap jobDataMap = jobDetail.getJobDataMap();
    
    log.info("{} in Group {} Started at {}", jobDataMap.get("NAME"), jobDataMap.get("GROUP"),
    		new Date());
    
    String url = jobDataMap.get("URL").toString();
    Map<String, String> requestParams = Collections.emptyMap();
    if ( jobDataMap.containsKey("REQUEST_PARAMETERS") ) {
    	log.info("The job has request params. Concatenate the params to url >>>>>>>>>>>>>>");
    	 requestParams = (Map<String, String>) jobDataMap.get("REQUEST_PARAMETERS");
    	 if ( !CollectionUtils.isEmpty(requestParams) ) {
    		 String requestParamsMapping = requestParams.entrySet()
    				 .stream()
    				 .map(e -> e.getKey() + "={" + e.getKey() + "}")
    				 .collect(Collectors.joining("&"));
    		 url = url + "?" + requestParamsMapping;
    		 log.info("The full url for this job is: {} >>>>>>>>>>>>>>>>>", url);
    	 }
    }
    
    HttpHeaders headers = new HttpHeaders();
    final JsonNode requestBody = (JsonNode) jobDataMap.get("REQUEST_BODY");
    final HttpEntity<JsonNode> httpEntity = new HttpEntity<>(requestBody, headers);
    restTemplate.exchange(url, (HttpMethod) jobDataMap.get("REQUEST_METHOD"), httpEntity, String.class, requestParams);
    log.info("{} Finished at {} ", jobDataMap.get("NAME"), new Date());
  }
}
