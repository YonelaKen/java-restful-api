package com.example.controllers.User;

import io.javalin.http.Context;

public class UserController {

    public static void getAllUserIds(Context ctx) {
        ctx.json(UserDAO.getUsers());
    }

    public static void createUser(Context ctx) {
        UserDAO.createUser(ctx.bodyAsClass(User.class));
        ctx.result("User created successfully");
    }

    public static void getUserById(Context ctx) {
        ctx.json(UserDAO.getUserById(ctx.pathParam("userId")));
    }

    public static void updateUser(Context ctx) {
        UserDAO.updateUser(ctx.pathParam("userId"), ctx.bodyAsClass(User.class));
        ctx.result("User details updated successfully");
    }

    public static void deleteUser(Context ctx) {
        UserDAO.deleteUser(ctx.pathParam("userId"));
        ctx.result("User deleted successfully");
    }
    
}
