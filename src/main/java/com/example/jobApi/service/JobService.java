package com.example.jobApi.service;

import com.example.jobApi.dto.Job;

import java.util.List;
import java.util.Map;

public interface JobService {
    List<?> getJobs(String groupBy);
    Job getJobById(String id);
}
