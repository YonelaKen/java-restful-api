package com.example.controllers;

import io.javalin.security.RouteRole;

public enum Role implements RouteRole {
    ADMIM, ANYONE, USER_READ, USER_WRITE;
}
