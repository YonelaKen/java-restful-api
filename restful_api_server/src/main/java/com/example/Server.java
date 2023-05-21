package com.example;

import java.util.Scanner;

import com.example.controllers.Auth;
import com.example.controllers.Role;
import com.example.controllers.UserController;

import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder;
import io.javalin.apibuilder.EndpointGroup;

public class Server {
    private int port = 5050;

    public static void main(String[] args) {
        Server app = new Server();
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
        }).routes(configEndpointsGroup());
    }

    private EndpointGroup configEndpointsGroup(){
        return () -> {
            ApiBuilder.get("/", ctx -> ctx.redirect("/users"), Role.ANYONE);
            ApiBuilder.path("users", () -> {
                ApiBuilder.get(UserController::getAllUserIds, Role.ADMIN);
                ApiBuilder.post(UserController::createUser, Role.ADMIN);
                ApiBuilder.path("roles", ()->{
                    ApiBuilder.patch(UserController::adminEditUserRoles,Role.ADMIN);
                });
                ApiBuilder.path("login", ()->{
                    ApiBuilder.get(UserController::login,Role.USER);
                });
                ApiBuilder.path("{userId}", () -> {
                    ApiBuilder.get(UserController::getUserById, Role.USER_READ);
                    ApiBuilder.patch(UserController::updateUser, Role.USER_WRITE);
                    ApiBuilder.delete(UserController::deleteUser, Role.ADMIN);
                });
            });
        };
    }

    public void start(){
        server.start(port);
    }

    public void stop(){
        server.stop();
    }
}
