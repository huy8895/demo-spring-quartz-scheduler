package com.example.demospringquartzschedulter.repository;


import com.example.demospringquartzschedulter.entity.SchedulerJobInfo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SchedulerRepository extends JpaRepository<SchedulerJobInfo, Integer>,
    JpaSpecificationExecutor<SchedulerJobInfo> {
  SchedulerJobInfo findByJobName(String jobName);
  
  List<SchedulerJobInfo> findByJobStatus(String jobStatus);
}
