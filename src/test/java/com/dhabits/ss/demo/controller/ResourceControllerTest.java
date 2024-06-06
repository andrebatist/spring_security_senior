package com.dhabits.ss.demo.controller;

import com.dhabits.ss.demo.domain.entity.ResourceObjectEntity;
import com.dhabits.ss.demo.domain.model.ResourceObject;
import com.dhabits.ss.demo.repository.ResourceObjectRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ResourceControllerTest {

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private ResourceObjectRepository resourceObjectRepository;

    @Test
    public void givenAuthRequestOnCreateResourceWithRoleAdmin_shouldSucceedWith200AndAnswer() throws Exception {
        ResourceObject resourceObject = new ResourceObject(1,"val", "/home");
        ResponseEntity<String> result = template.withBasicAuth("admin", "Hx=W!q3SMm")
                .postForEntity("/resource", resourceObject, String.class);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("1", result.getBody());
    }


    @Test
    public void givenAuthRequestOnCreateResourceWithRoleUser_shouldReturn403AndAnswerUnAuthorized() throws Exception {
        ResourceObject resourceObject = new ResourceObject(1,"val", "/home");
        ResponseEntity<String> result = template.withBasicAuth("user", "2xI_b2y2")
                .postForEntity("/resource", resourceObject, String.class);
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
        assertEquals("UnAuthorized", result.getBody());
    }

    @Test
    public void givenAuthRequestOnGetResourceWithRoleAdmin_shouldSucceedWith200AndAnswer() throws Exception {
        ResourceObjectEntity resourceObjectEntity = new ResourceObjectEntity(2,"val2", "/home2");
        resourceObjectRepository.save(resourceObjectEntity);
        ResponseEntity<String> result = template.withBasicAuth("admin", "Hx=W!q3SMm")
                .getForEntity("/resource/2",String.class);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody().contains("/home2"));
    }

    @Test
    public void givenAuthRequestOnGetResourceWithRoleUser_shouldReturn403WithAnswerUnAuthorized() throws Exception {
        ResponseEntity<String> result = template.withBasicAuth("user", "2xI_b2y2")
                .getForEntity("/resource/1",String.class);
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
        assertEquals("UnAuthorized", result.getBody());
    }
}
