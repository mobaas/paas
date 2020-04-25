package com.mobaas.paas.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.mobaas.paas.job.JobInfo;

@Component
@ConfigurationProperties(prefix="app.jobs")
public class JobsConfig {

    private List<JobInfo> jobs;

	public List<JobInfo> getJobs() {
		return jobs;
	}

	public void setJobs(List<JobInfo> jobs) {
		this.jobs = jobs;
	}
    
    
}
