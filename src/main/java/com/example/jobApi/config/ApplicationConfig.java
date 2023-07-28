package com.example.jobApi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    @Value("${jwt.secret_key}")
    public String SECRET_KEY;
    @Value("${url.dans_pro_job_lists}")
    public String URL_DANS_PRO_JOB_LISTS;
    @Value("${url.dans_pro_job_detail}")
    public String URL_DANS_PRO_JOB_DETAIL;

}
