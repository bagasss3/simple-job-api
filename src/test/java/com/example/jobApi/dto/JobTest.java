package com.example.jobApi.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class JobTest {
    @Test
    public void JobTest(){
        String id = "test";
        String type = "t";
        String url = "urltest";
        String company = "companytest";
        String company_url = "url_test";
        String location = "loctest";
        String title = "titletest";
        String desc = "descTest";
        String how_to_apply = "htatest";
        String logo = "logotest";

        Job job = new Job();
        job.setId(id);
        job.setType(type);
        job.setUrl(url);
        job.setCompany(company);
        job.setCompany_url(company_url);
        job.setLocation(location);
        job.setTitle(title);
        job.setDescription(desc);
        job.setHow_to_apply(how_to_apply);
        job.setCompany_logo(logo);

        Assertions.assertEquals(id, job.getId());
        Assertions.assertEquals(type, job.getType());
        Assertions.assertEquals(url, job.getUrl());
        Assertions.assertEquals(company, job.getCompany());
        Assertions.assertEquals(company_url, job.getCompany_url());
        Assertions.assertEquals(location, job.getLocation());
        Assertions.assertEquals(title, job.getTitle());
        Assertions.assertEquals(desc, job.getDescription());
        Assertions.assertEquals(how_to_apply, job.getHow_to_apply());
        Assertions.assertEquals(logo, job.getCompany_logo());
    }
}
