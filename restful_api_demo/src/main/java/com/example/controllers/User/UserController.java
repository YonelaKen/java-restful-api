package com.example.controllers.User;

import com.example.controllers.User.DAO.UserDAO;

import io.javalin.http.Context;

public class UserController {
    
    public static void getAllUserIds(Context ctx) {
        ctx.json(UserDAO.getUsers());
    }

    public static void createUser(Context ctx) {
        ctx.result(UserDAO.createUser(ctx));
    }

    public static void getUserById(Context ctx) {
        ctx.json(UserDAO.getUserById(ctx.pathParam("userId")));
    }

    public static void updateUser(Context ctx) {
        ctx.result(UserDAO.updateUser(ctx.pathParam("userId"), ctx.bodyAsClass(User.class)));
    }

    public static void deleteUser(Context ctx) {
        ctx.result(UserDAO.deleteUser(ctx.pathParam("userId")));
    }

}
