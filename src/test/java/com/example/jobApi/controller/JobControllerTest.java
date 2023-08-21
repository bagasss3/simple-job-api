package com.example.jobApi.controller;

import com.example.jobApi.dto.ApiResponse;
import com.example.jobApi.dto.Job;
import com.example.jobApi.exception.CustomException;
import com.example.jobApi.service.JobService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JobControllerTest {
    @Mock
    private JobService jobService;
    @InjectMocks
    private JobController jobController;

    @Test
    public void testListJob_Successful() {
        String groupBy = "location";
        List<?> mockJobList = createMockJobList();

        Mockito.doReturn(mockJobList).when(jobService).getJobs(groupBy);
        ResponseEntity<ApiResponse<?>> responseEntity = jobController.listJob(groupBy);

        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        ApiResponse<?> response = responseEntity.getBody();
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals("Success get all jobs", response.getMessage());

        List<?> responseData = (List<?>) response.getData();
        Assertions.assertNotNull(responseData);
        Assertions.assertEquals(mockJobList.size(), responseData.size());

        Mockito.verify(jobService).getJobs(groupBy);
    }

    @Test
    public void testGetJobById_Successful() {
        String mockJobId = "123";
        Job mockJob = createMockJob(mockJobId);

        when(jobService.getJobById(mockJobId)).thenReturn(mockJob);

        ResponseEntity<ApiResponse<?>> responseEntity = jobController.getJobById(mockJobId);

        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        ApiResponse<?> response = responseEntity.getBody();
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals("Success get job by id: " + mockJobId, response.getMessage());

        Job responseData = (Job) response.getData();
        Assertions.assertNotNull(responseData);
        Assertions.assertEquals(mockJobId, responseData.getId());

        Mockito.verify(jobService).getJobById(mockJobId);
    }

    @Test
    public void testGetJobById_JobNotFound() {
        String mockJobId = "123";

        when(jobService.getJobById(mockJobId)).thenThrow(new CustomException("Job not found", HttpStatus.NOT_FOUND));

        ResponseEntity<ApiResponse<?>> responseEntity = jobController.getJobById(mockJobId);

        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        ApiResponse<?> response = responseEntity.getBody();
        Assertions.assertNotNull(response);
        Assertions.assertFalse(response.isSuccess());
        Assertions.assertEquals("Job not found", response.getMessage());
        Assertions.assertNull(response.getData());

        Mockito.verify(jobService).getJobById(mockJobId);
    }

    @Test
    public void testListJob_InternalServerError() {
        String groupBy = "location";
        when(jobService.getJobs(groupBy)).thenThrow(new RuntimeException("Some internal error"));

        ResponseEntity<ApiResponse<?>> responseEntity = jobController.listJob(groupBy);

        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        ApiResponse<?> response = responseEntity.getBody();
        Assertions.assertNotNull(response);
        Assertions.assertFalse(response.isSuccess());
        Assertions.assertEquals("Error message: Some internal error", response.getMessage());
        Assertions.assertNull(response.getData());

        Mockito.verify(jobService).getJobs(groupBy);
    }

    @Test
    public void testGetJobById_InternalServerError() {
        String mockJobId = "123";

        when(jobService.getJobById(mockJobId)).thenThrow(new RuntimeException("Some internal error"));

        ResponseEntity<ApiResponse<?>> responseEntity = jobController.getJobById(mockJobId);

        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        ApiResponse<?> response = responseEntity.getBody();
        Assertions.assertNotNull(response);
        Assertions.assertFalse(response.isSuccess());
        Assertions.assertEquals("Error message: Some internal error", response.getMessage());
        Assertions.assertNull(response.getData());

        Mockito.verify(jobService).getJobById(mockJobId);
    }

    private List<Job> createMockJobList() {
        List<Job> jobs = new ArrayList<>();
        jobs.add(createMockJob("1"));
        jobs.add(createMockJob("2"));
        jobs.add(createMockJob("3"));
        return jobs;
    }

    private Job createMockJob(String id) {
        Job job = new Job();
        job.setId(id);
        return job;
    }


}
