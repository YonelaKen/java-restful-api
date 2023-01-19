package com.example;

import com.example.controllers.Auth;
import com.example.controllers.Role;
import com.example.controllers.User.UserController;

import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder;

public class App {

    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.accessManager(new Auth());
        }).routes(() -> {
            ApiBuilder.get("/", ctx -> ctx.redirect("/users"), Role.ANYONE);
            ApiBuilder.path("users", () -> {
                ApiBuilder.get(UserController::getAllUserIds, Role.ANYONE);
                ApiBuilder.post(UserController::createUser, Role.USER_WRITE);
                ApiBuilder.path("{userId}", () -> {
                    ApiBuilder.get(UserController::getUser, Role.USER_READ);
                    ApiBuilder.patch(UserController::updateUser, Role.USER_WRITE);
                    ApiBuilder.delete(UserController::deleteUser, Role.ADMIM);
                });
            });
        }).start(7070);
    }

    
}
