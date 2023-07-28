package com.example.jobApi.service.impl;

import com.example.jobApi.config.ApplicationConfig;
import com.example.jobApi.dto.Job;
import com.example.jobApi.exception.CustomException;
import com.example.jobApi.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobServiceImpl implements JobService {
    @Autowired
    ApplicationConfig applicationConfig;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<Job> getJobs() {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(applicationConfig.URL_DANS_PRO_JOB_LISTS);
        ResponseEntity<List<Job>> result = restTemplate.
                exchange(uriBuilder.toUriString(),
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Job>>() {});
        List<Job> jobList = new ArrayList<>();
        jobList.addAll(result.getBody());
        return jobList;
    }

    @Override
    public Job getJobById(String id) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(applicationConfig.URL_DANS_PRO_JOB_DETAIL+id);
        ResponseEntity<Job> result = restTemplate.
                exchange(uriBuilder.toUriString(),
                        HttpMethod.GET, null, Job.class);
        if(result.getBody().getId() == null){
            throw new CustomException("Job not found", HttpStatus.NOT_FOUND);
        }
        return result.getBody();
    }
}
