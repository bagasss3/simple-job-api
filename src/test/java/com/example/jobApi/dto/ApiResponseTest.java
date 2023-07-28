package com.example.jobApi.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ApiResponseTest {
    @Test
    public void ApiResponseTest(){
        Job job = new Job();
        job.setId("test1");
        String msg = "testmsg";
        Boolean success = true;

        ApiResponse<Job> apiResponse = new ApiResponse<>();
        apiResponse.setSuccess(success);
        apiResponse.setMessage(msg);
        apiResponse.setData(job);

        Assertions.assertEquals(job, apiResponse.getData());
        Assertions.assertEquals(msg, apiResponse.getMessage());
        Assertions.assertEquals(success, apiResponse.isSuccess());
    }
}
