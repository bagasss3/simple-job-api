package com.example.jobApi.controller;

import com.example.jobApi.dto.ApiResponse;
import com.example.jobApi.dto.Job;
import com.example.jobApi.exception.CustomException;
import com.example.jobApi.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class JobController {
    @Autowired
    JobService jobService;

    @GetMapping("/jobs")
    public ResponseEntity<ApiResponse<?>> listJob(@RequestParam(required = false, defaultValue = "defaultGroupBy") String groupBy){
        try{
            List<?> jobs = jobService.getJobs(groupBy);
            ApiResponse<List<?>> response = new ApiResponse<>();
            response.setSuccess(true);
            response.setMessage("Success get all jobs");
            response.setData(jobs);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>();
            response.setSuccess(false);
            response.setMessage("Error message: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/jobs/{id}")
    public ResponseEntity<ApiResponse<?>> getJobById(@PathVariable("id") String id){
        try{
            Job job = jobService.getJobById(id);
            ApiResponse<Job> response = new ApiResponse<>();
            response.setSuccess(true);
            response.setMessage("Success get job by id: "+id);
            response.setData(job);
            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            ApiResponse<Void> response = new ApiResponse<>();
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            return ResponseEntity.status(e.getHttpStatus()).body(response);
        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>();
            response.setSuccess(false);
            response.setMessage("Error message: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
