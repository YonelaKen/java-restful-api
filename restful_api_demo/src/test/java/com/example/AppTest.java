package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.example.controllers.User.DAO.UserDAO;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
public class AppTest {

    static App app;
    @BeforeAll
    public static void setUp(){
        app = new App();
        app.setUpServer();
        app.start();
        UserDAO.setupUsers();
    }

    @AfterAll
    public static void tearDown() throws InterruptedException{
        app.stop();
        app = null;
    }

    @Test
    public void sendGetAllUsersRequest() {
        HttpResponse<String> response  = Unirest.get("http://localhost:7070/users/").basicAuth("test", "invalid").asString();
        assertEquals(4, response.getBody().substring(1, response.getBody().length()-1).split(",").length);
    }

    @Test
    public void sendGetUserByIdRequestValidUser() {
        HttpResponse<String> response  = Unirest.get("http://localhost:7070/users/2").basicAuth("yonela@admin.kt", "iamAdmin-12345").asString();
        assertEquals("{\"name\":\"Bob\",\"email\":\"bob@bob.kt\"}", response.getBody());
    }

    @Test
    public void sendGetUserByIdRequestValidUserInvalidId() {
        HttpResponse<String> response  = Unirest.get("http://localhost:7070/users/200").basicAuth("yonela@admin.kt", "iamAdmin-12345").asString();
        assertEquals("{\"message\": \"User does not exist.\"}", response.getBody());
    }

    @Test
    public void sendGetUserByIdRequestInvalidUser() {
        HttpResponse<String> response  = Unirest.get("http://localhost:7070/users/1").basicAuth("test", "invalid").asString();
        assertEquals("Unauthorized", response.getBody());
    }
    
    @Test
    public void sendPostRequestCreateUserByValidUser() {
        HttpResponse<String> response = Unirest.post("http://localhost:7070/users/").body("{\"name\":\"testName\", \"email\":\"test@email\"}").basicAuth("yonela@admin.kt", "iamAdmin-12345").asString();
        assertEquals("User created successfully", response.getBody());
    }

    @Test
    public void sendPostRequestCreateUserByInvalidUser() {
        HttpResponse<String> response = Unirest.post("http://localhost:7070/users/").body("{\"name\":\"testName\", \"email\":\"test@email\"}").basicAuth("mm", "invalidPassword").asString();
        assertEquals("Unauthorized", response.getBody());
    }

    @Test
    public void sendPatchRequestInvalidUser() {
        HttpResponse<String> response = Unirest.patch("http://localhost:7070/users/3").body("{\"name\":\"testName\", \"email\":\"test@email\"}").basicAuth("mm", "invalidPassword").asString();
        assertEquals("Unauthorized", response.getBody());
    }

    @Test
    public void sendPatchRequestValidUser() {
        HttpResponse<String> response = Unirest.patch("http://localhost:7070/users/3").body("{\"name\":\"testName\", \"email\":\"test@email\"}").basicAuth("yonela@admin.kt", "iamAdmin-12345").asString();
        assertEquals("User details updated successfully", response.getBody());
    }

    @Test
    public void sendPatchRequestValidUserButInvalidUserId() {
        HttpResponse<String> response = Unirest.patch("http://localhost:7070/users/5000").body("{\"name\":\"testName\", \"email\":\"test@email\"}").basicAuth("yonela@admin.kt", "iamAdmin-12345").asString();
        assertEquals("Unable to update user details.Could not find user with ID : 5000", response.getBody());
    }

    @Test
    public void sendDeleteRequestInvalidUser() {
        HttpResponse<String> response = Unirest.delete("http://localhost:7070/users/1").body("{\"name\":\"testName\", \"email\":\"test@email\"}").basicAuth("mm", "invalidPassword").asString();
        assertEquals("Unauthorized", response.getBody());
    }

    @Test
    public void sendDeleteRequestValidUser() {
        HttpResponse<String> response = Unirest.delete("http://localhost:7070/users/1").body("{\"name\":\"testName\", \"email\":\"test@email\"}").basicAuth("yonela@admin.kt", "iamAdmin-12345").asString();
        assertEquals("User deleted successfully", response.getBody());
    }

    @Test
    public void sendDeleteRequestValidUserButInvalidUserId() {
        HttpResponse<String> response = Unirest.delete("http://localhost:7070/users/1000").body("{\"name\":\"testName\", \"email\":\"test@email\"}").basicAuth("yonela@admin.kt", "iamAdmin-12345").asString();
        assertEquals("Could not find user with ID : 1000", response.getBody());
    }

}
