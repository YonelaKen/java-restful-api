package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import kong.unirest.GetRequest;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

public class AppTest {

    @Test
    public void sendGetRequest() {
        HttpResponse<String> response = Unirest.get("http://localhost:7070/").basicAuth("test", "invalid").asString();
        assertEquals("", response.getBody());
    }

}
