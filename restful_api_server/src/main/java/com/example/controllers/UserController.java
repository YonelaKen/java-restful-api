package com.example.controllers;

import com.example.User.User;
import com.example.User.DAO.UserDAO;

import io.javalin.http.Context;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

public class UserController {

    public static void getAllUserIds(Context ctx) {
        ctx.json(UserDAO.getUsers());
    }

    public static void createUser(Context ctx) {
        ctx.json(UserDAO.createUser(ctx));
    }

    public static void getUserById(Context ctx) {
        ctx.json(UserDAO.getUserById(ctx.pathParam("userId")));
    }

    public static void updateUser(Context ctx) {
        ctx.json(UserDAO.updateUser(ctx.pathParam("userId"), ctx.bodyAsClass(User.class)));
    }

    public static void deleteUser(Context ctx) {
        ctx.json(UserDAO.deleteUser(ctx.pathParam("userId")));
    }

    public static void adminEditUserRoles(Context ctx){
        String body = ctx.body();
        JSONObject jsonObject = new JSONObject(body);
        String flag = jsonObject.getString("flag");
        String useremail = jsonObject.getString("useremail");
        JSONArray roles = jsonObject.getJSONArray("roles");

        if (flag.equalsIgnoreCase("add")){
            ctx.json(UserDAO.adminAddUserRoles(roles.toList(),useremail ));
        } else if (flag.equalsIgnoreCase("remove")){
            ctx.json(UserDAO.adminRemoveUserRoles(roles.toList(), useremail));
        }        
    }
    
    public static void login(Context ctx){
    }
}
