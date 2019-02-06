package com.sport;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.MalformedURLException;
import java.net.URL;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CityServiceSecurityTests {

    URL applicationUrl;
    @LocalServerPort
    int port;

    @Before
    public void setUp() throws MalformedURLException {

        applicationUrl = new URL("http://localhost:" + port + "/routes");
    }

    @Test
    public void rightUserRightsTest()
            throws IllegalStateException {
        TestRestTemplate userRightsTemplate = new TestRestTemplate("testsport", "testcity");
        ResponseEntity<String> response = userRightsTemplate.getForEntity(applicationUrl.toString(), String.class);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void wrongUserRightsTest() {

        TestRestTemplate wrongUserRightsTemplate = new TestRestTemplate("testsport", "ytictset");
        ResponseEntity<String> response = wrongUserRightsTemplate.getForEntity(applicationUrl.toString(), String.class);

        Assert.assertEquals(HttpStatus.FOUND, response.getStatusCode());
    }
}
