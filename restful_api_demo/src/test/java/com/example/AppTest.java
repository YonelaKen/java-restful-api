package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

public class AppTest {

    App app;
    @BeforeEach
    public void setUp(){
        app = new App();
        app.setUpServer();
        app.start();
    }

    @AfterEach
    public void tearDown() throws InterruptedException{
        app.stop();
        app = null;
        Thread.sleep(2000);
    }

    @Test
    public void sendGetAllUsersRequest() {
        HttpResponse<String> response  = Unirest.get("http://localhost:7070/users/").basicAuth("test", "invalid").asString();
        assertEquals(5, response.getBody().substring(1, response.getBody().length()-1).split(",").length);
    }

    @Test
    public void sendGetUserByIdRequestValidUser() {
        HttpResponse<String> response  = Unirest.get("http://localhost:7070/users/2").basicAuth("yonela", "iamAdmin-12345").asString();
        assertEquals("{\"name\":\"Bob\",\"email\":\"bob@bob.kt\"}", response.getBody());
    }

    @Test
    public void sendGetUserByIdRequestInvalidUser() {
        HttpResponse<String> response  = Unirest.get("http://localhost:7070/users/1").basicAuth("test", "invalid").asString();
        assertEquals("Unauthorized", response.getBody());
    }
    
    @Test
    public void sendPostRequestCreateUserByValidUser() {
        HttpResponse<String> response = Unirest.post("http://localhost:7070/users/").body("{\"name\":\"testName\", \"email\":\"test@email\"}").basicAuth("yonela", "iamAdmin-12345").asString();
        assertEquals("User created successfully", response.getBody());
    }

    @Test
    public void sendPostRequestCreateUserByInvalidUser() {
        HttpResponse<String> response = Unirest.post("http://localhost:7070/users/").body("{\"name\":\"testName\", \"email\":\"test@email\"}").basicAuth("mm", "invalidPassword").asString();
        assertEquals("Unauthorized", response.getBody());
    }

    @Test
    public void sendPatchRequestInvalidUser() {
        HttpResponse<String> response = Unirest.patch("http://localhost:7070/users/1").body("{\"name\":\"testName\", \"email\":\"test@email\"}").basicAuth("mm", "invalidPassword").asString();
        assertEquals("Unauthorized", response.getBody());
    }

    @Test
    public void sendPatchRequestValidUser() {
        HttpResponse<String> response = Unirest.patch("http://localhost:7070/users/1").body("{\"name\":\"testName\", \"email\":\"test@email\"}").basicAuth("yonela", "iamAdmin-12345").asString();
        assertEquals("User details updated successfully", response.getBody());
    }

    @Test
    public void sendDeleteRequestInvalidUser() {
        HttpResponse<String> response = Unirest.delete("http://localhost:7070/users/1").body("{\"name\":\"testName\", \"email\":\"test@email\"}").basicAuth("mm", "invalidPassword").asString();
        assertEquals("Unauthorized", response.getBody());
    }

    @Test
    public void sendDeleteRequestValidUser() {
        HttpResponse<String> response = Unirest.delete("http://localhost:7070/users/1").body("{\"name\":\"testName\", \"email\":\"test@email\"}").basicAuth("yonela", "iamAdmin-12345").asString();
        assertEquals("User deleted successfully", response.getBody());
    }

}
