package com.example;

import java.util.Scanner;

import com.example.controllers.Auth;
import com.example.controllers.Role;
import com.example.controllers.User.UserController;

import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder;

public class App {

    public static void main(String[] args) {
        App app = new App();
        app.setUpServer();
        app.start();
        Scanner input = new Scanner(System.in);
        while(!input.nextLine().equals("1")){
            System.out.println("Enter 1 to stop the server.");
        }
        app.stop();
        input.close();
    }
    
    private Javalin server;
    public void setUpServer(){
        server = Javalin.create(config -> {
            config.accessManager(new Auth());
        }).routes(() -> {
            ApiBuilder.get("/", ctx -> ctx.redirect("/users"), Role.ANYONE);
            ApiBuilder.path("users", () -> {
                ApiBuilder.get(UserController::getAllUserIds, Role.ANYONE);
                ApiBuilder.post(UserController::createUser, Role.ADMIN);
                ApiBuilder.path("{userId}", () -> {
                    ApiBuilder.get(UserController::getUserById, Role.USER_READ);
                    ApiBuilder.patch(UserController::updateUser, Role.USER_WRITE);
                    ApiBuilder.delete(UserController::deleteUser, Role.ADMIN);
                });
            });
        });
    }

    public void start(){
        server.start(7070);
    }

    public void stop(){
        server.stop();
    }
}
