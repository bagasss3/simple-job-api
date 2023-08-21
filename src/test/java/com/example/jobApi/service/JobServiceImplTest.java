package com.example.jobApi.service;

import com.example.jobApi.config.ApplicationConfig;
import com.example.jobApi.dto.Job;
import com.example.jobApi.exception.CustomException;
import com.example.jobApi.service.impl.JobServiceImpl;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JobServiceImplTest {
    @Mock
    private ApplicationConfig applicationConfig;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private JobServiceImpl jobService;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(applicationConfig, "URL_DANS_PRO_JOB_LISTS", "http://example.com/api/jobs");
        ReflectionTestUtils.setField(applicationConfig, "URL_DANS_PRO_JOB_DETAIL", "http://example.com/api/job/");
    }

    @Test
    public void testGetJobs_Successful() {
        String groupBy = "location";

        List<Job> mockJobList = createMockJobList();

        ResponseEntity<List<Job>> responseEntity = new ResponseEntity<>(mockJobList, HttpStatus.OK);
        when(restTemplate.exchange(Mockito.eq(applicationConfig.URL_DANS_PRO_JOB_LISTS), Mockito.eq(HttpMethod.GET), Mockito.isNull(), Mockito.any(ParameterizedTypeReference.class)))
                .thenReturn(responseEntity);

        List<?> jobList = jobService.getJobs(groupBy);

        Assertions.assertNotNull(jobList);
        Assertions.assertEquals(mockJobList.size(), jobList.size());

        Mockito.verify(restTemplate).exchange(Mockito.eq(applicationConfig.URL_DANS_PRO_JOB_LISTS), Mockito.eq(HttpMethod.GET), Mockito.isNull(), Mockito.any(ParameterizedTypeReference.class));
    }

    @Test
    public void testGetJobById_Successful() {
        String mockJobId = "123";
        Job mockJob = createMockJob("1", "A");

        ResponseEntity<Job> responseEntity = new ResponseEntity<>(mockJob, HttpStatus.OK);
        when(restTemplate.exchange(Mockito.eq(applicationConfig.URL_DANS_PRO_JOB_DETAIL+mockJobId), Mockito.eq(HttpMethod.GET), Mockito.isNull(), Mockito.eq(Job.class)))
                .thenReturn(responseEntity);

        Job job = jobService.getJobById(mockJobId);

        Assertions.assertNotNull(job);
        Assertions.assertEquals(mockJob.getId(), job.getId());

        Mockito.verify(restTemplate).exchange(Mockito.eq(applicationConfig.URL_DANS_PRO_JOB_DETAIL+mockJobId), Mockito.eq(HttpMethod.GET), Mockito.isNull(), Mockito.eq(Job.class));
    }

    @Test
    public void testGetJobById_JobNotFound() {
        String mockJobId = "123";
        Job mockJob = new Job();

        ResponseEntity<Job> responseEntity = new ResponseEntity<>(mockJob, HttpStatus.OK);
        when(restTemplate.exchange(Mockito.eq(applicationConfig.URL_DANS_PRO_JOB_DETAIL+mockJobId), Mockito.eq(HttpMethod.GET), Mockito.isNull(), Mockito.eq(Job.class)))
                .thenReturn(responseEntity);

        Assertions.assertThrows(CustomException.class, () -> jobService.getJobById(mockJobId));

        Mockito.verify(restTemplate).exchange(Mockito.eq(applicationConfig.URL_DANS_PRO_JOB_DETAIL+mockJobId), Mockito.eq(HttpMethod.GET), Mockito.isNull(), Mockito.eq(Job.class));
    }

    private List<Job> createMockJobList() {
        List<Job> jobs = new ArrayList<>();
        jobs.add(createMockJob("1","A"));
        jobs.add(createMockJob("2", "B"));
        jobs.add(createMockJob("3", "C"));
        return jobs;
    }

    private Job createMockJob(String id, String location) {
        Job job = new Job();
        job.setId(id);
        job.setLocation(location);
        return job;
    }
}
