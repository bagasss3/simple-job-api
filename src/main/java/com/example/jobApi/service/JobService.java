package com.example.jobApi.service;

import com.example.jobApi.dto.Job;

import java.util.List;

public interface JobService {
    List<Job> getJobs();
    Job getJobById(String id);
}
