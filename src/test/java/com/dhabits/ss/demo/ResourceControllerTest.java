package com.dhabits.ss.demo;

import com.dhabits.ss.demo.domain.entity.ResourceObjectEntity;
import com.dhabits.ss.demo.domain.model.ResourceObject;
import com.dhabits.ss.demo.dto.AuthRequestDTO;
import com.dhabits.ss.demo.dto.JwtResponseDTO;
import com.dhabits.ss.demo.repository.ResourceObjectRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Objects;

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
    public void givenJwtTokenRequestOnCreateResourceWithRoleAdmin_shouldSucceedWith200AndAnswer() throws Exception {
        String accessToken = getAccessToken("admin", "Hx=W!q3SMm");
        ResourceObject resourceObject = new ResourceObject(1,"val", "/home");
        HttpHeaders headers = getBearerTokenHeaders(accessToken);

        HttpEntity<ResourceObject> entity = new HttpEntity<>(resourceObject, headers);
        ResponseEntity<String> result = template.postForEntity("/resource", entity, String.class);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("1", result.getBody());

    }

    @Test
    public void givenJwtTokenRequestOnCreateResourceWithRoleUser_shouldReturn403() throws Exception {
        String accessToken = getAccessToken("user", "2xI_b2y2");
        ResourceObject resourceObject = new ResourceObject(1,"val", "/home");
        HttpHeaders headers = getBearerTokenHeaders(accessToken);

        HttpEntity<ResourceObject> entity = new HttpEntity<>(resourceObject, headers);
        ResponseEntity<String> result = template.postForEntity("/resource", entity, String.class);

        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    public void givenJwtTokenRequestOnGetResourceWithRoleAdmin_shouldSucceedWith200AndAnswer() throws Exception {
        ResourceObjectEntity resourceObjectEntity = new ResourceObjectEntity(2,"val2", "/home2");
        resourceObjectRepository.save(resourceObjectEntity);

        String accessToken = getAccessToken("admin", "Hx=W!q3SMm");

        HttpHeaders headers = getBearerTokenHeaders(accessToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<String> result = template.exchange(
                "/resource/2", HttpMethod.GET, entity, String.class);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody().contains("/home2"));
    }

    @Test
    public void givenJwtRequestOnGetResourceWithRoleUser_shouldReturn403() throws Exception {
        String accessToken = getAccessToken("user", "2xI_b2y2");
        HttpHeaders headers = getBearerTokenHeaders(accessToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<String> result = template.exchange(
                "/resource/1", HttpMethod.GET, entity, String.class);

        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    private static HttpHeaders getBearerTokenHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);
        return headers;
    }

    private String getAccessToken(String username, String password) {
        AuthRequestDTO requestDTO = new AuthRequestDTO(username, password);
        ResponseEntity<JwtResponseDTO> authResponse =
                template.postForEntity("/login", requestDTO, JwtResponseDTO.class);
        return Objects.requireNonNull(authResponse.getBody()).getAccessToken();
    }

}
