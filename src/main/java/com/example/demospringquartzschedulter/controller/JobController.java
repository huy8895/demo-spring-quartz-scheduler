package com.example.demospringquartzschedulter.controller;


import com.example.demospringquartzschedulter.dto.CreateJobDto;
import com.example.demospringquartzschedulter.dto.JobDto;
import com.example.demospringquartzschedulter.dto.SchedulerJobInfoDTO;
import com.example.demospringquartzschedulter.dto.SearchDto;
import com.example.demospringquartzschedulter.dto.UpdateJobDto;
import com.example.demospringquartzschedulter.factory.ResponseFactory;
import com.example.demospringquartzschedulter.service.SchedulerJobService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/job")
public class JobController {

    private final SchedulerJobService schedulerJobService;
    private final ResponseFactory responseFactory;

    @Autowired
    public JobController(SchedulerJobService schedulerJobService,
        ResponseFactory responseFactory) {
        this.schedulerJobService = schedulerJobService;
        this.responseFactory = responseFactory;
    }

    @PostMapping
    public Object create(@RequestBody CreateJobDto createJobDto) throws JsonProcessingException {
        SchedulerJobInfoDTO schedulerJobInfo = schedulerJobService.scheduleNewJob(createJobDto);
        return schedulerJobInfo;
    }

    @PutMapping
    public Object update(@RequestBody UpdateJobDto updateJobDto) {
        SchedulerJobInfoDTO schedulerJobInfo = schedulerJobService.updateScheduleJob(updateJobDto);
        return responseFactory.success(schedulerJobInfo, SchedulerJobInfoDTO.class);
    }

    @PutMapping("/resume")
    public ResponseEntity<Object> resume(@RequestBody JobDto jobDto) {
        schedulerJobService.resumeJob(jobDto);
        return responseFactory.success();

    }

    @PutMapping("/pause")
    public ResponseEntity<Object> pause(@RequestBody JobDto jobDto) {
        schedulerJobService.pauseJob(jobDto);
        return responseFactory.success();
    }

    @PutMapping("/start")
    public ResponseEntity<Object> start(@RequestBody JobDto jobDto) {
        schedulerJobService.startJobNow(jobDto);
        return responseFactory.success();
    }

    @GetMapping("/list")
    public ResponseEntity<Object> getSchedules() {
        return schedulerJobService.getSchedules();
    }

    @PostMapping("/list-job")
    public ResponseEntity<Object> getSchedules(@RequestBody SearchDto searchDto) {
        return schedulerJobService.getSchedules(searchDto);
    }
}
