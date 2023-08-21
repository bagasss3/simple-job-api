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

import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {
    @Autowired
    ApplicationConfig applicationConfig;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<?> getJobs(String groupBy) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString(applicationConfig.URL_DANS_PRO_JOB_LISTS);
        ResponseEntity<List<Job>> result = restTemplate.
                exchange(uriBuilder.toUriString(),
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Job>>() {});

        List<Job> jobs = result.getBody();
        if(groupBy.equals("location")){
            Map<String, List<Job>> jobsByLocation = jobs.stream()
                    .collect(Collectors.groupingBy(Job::getLocation));

            List<Map<String, Object>> groupedJobs = jobsByLocation.entrySet().stream()
                    .map(entry -> {
                        Map<String, Object> groupMap = new LinkedHashMap<>();
                        groupMap.put("location", entry.getKey());
                        groupMap.put("data", entry.getValue());
                        return groupMap;
                    })
                    .collect(Collectors.toList());

            return groupedJobs;
        }
        List<Job> jobList = new ArrayList<>();
        jobList.addAll(jobs);
        return jobList;
    }

//    @Override
//    public List<Map<String, Object>> getFilteredJobs() {
//        UriComponentsBuilder uriBuilder = UriComponentsBuilder
//                .fromUriString(applicationConfig.URL_DANS_PRO_JOB_LISTS);
//        ResponseEntity<List<Job>> result = restTemplate
//                .exchange(uriBuilder.toUriString(),
//                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Job>>() {});
//
//        List<Job> jobs = result.getBody();
//
//        Map<String, List<Job>> jobsByLocation = jobs.stream()
//                .collect(Collectors.groupingBy(Job::getLocation));
//
//        List<Map<String, Object>> groupedJobs = jobsByLocation.entrySet().stream()
//                .map(entry -> {
//                    Map<String, Object> groupMap = new LinkedHashMap<>();
//                    groupMap.put("location", entry.getKey());
//                    groupMap.put("data", entry.getValue());
//                    return groupMap;
//                })
//                .collect(Collectors.toList());
//
//        return groupedJobs;
//    }

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
