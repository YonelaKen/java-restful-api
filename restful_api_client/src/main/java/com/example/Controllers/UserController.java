package com.example.Controllers;

import io.javalin.http.Handler;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

public class UserController {
    public static Handler login = context -> {     
        String username = context.formParam("name");
        String password = context.formParam("password");
        HttpResponse<String> response  = Unirest.get("http://localhost:5050/users/login").basicAuth(username, password).asString();
        if(response.getBody().equals("Unauthorized")){
            context.result(response.getBody());
        } else {
            // Set custom headers in the response
            context.header("X-Username", username);
            context.header("X-Password", password);

        }
    };

    public static Handler loginForm = context -> {
        context.redirect("/login.html");
    };

    public static Handler registerForm = context -> {
        context.redirect("/register.html");
    };

    public static Handler logout = context -> {
        context.header("X-Username","");
        context.header("X-Password","");
        context.redirect("/");
    };

    public static Handler register = context -> {
        context.redirect("/register.html");
    };
    
    public static Handler add = context -> {
        context.redirect("/register.html");
    }; 

    public static Handler all = context -> {
        context.redirect("/register.html");
    }; 
    public static Handler edit = context -> {
        context.redirect("/register.html");
    };
}

