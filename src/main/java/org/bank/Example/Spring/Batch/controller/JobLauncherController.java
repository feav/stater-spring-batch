package org.bank.Example.Spring.Batch.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class JobLauncherController {
    @Autowired @Qualifier("importUserJob")
    Job job;
    @Autowired @Qualifier("jobRepository")
    JobRepository jobRepository;
    @RequestMapping("/load-job")
    public String handle() throws Exception{
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.afterPropertiesSet();
        JobExecution x = jobLauncher.run(job, new JobParameters());
        return "Elements Chargés";
    }
}
