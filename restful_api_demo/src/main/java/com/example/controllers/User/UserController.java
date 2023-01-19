package com.example.controllers.User;

import io.javalin.http.Context;
import java.util.*;

public class UserController {
    private static Map<String, User> users = new HashMap<>();

    static {
        users.put(randomId(), new User("Alice", "alice@alice.kt"));
        users.put(randomId(), new User("Bob", "bob@bob.kt"));
        users.put(randomId(), new User("Carol", "carol@carol.kt"));
        users.put(randomId(), new User("Dave", "dave@dave.kt"));
    }

    public static void getAllUserIds(Context ctx) {
        ctx.json(users.keySet());
    }

    public static void createUser(Context ctx) {
        users.put(randomId(), ctx.bodyAsClass(User.class));
        ctx.result("User created successfully");
    }

    public static void getUser(Context ctx) {
        ctx.json(users.get(ctx.pathParam("userId")));
    }

    public static void updateUser(Context ctx) {
        users.put(ctx.pathParam("userId"), ctx.bodyAsClass(User.class));
        ctx.result("User details updated successfully");
    }

    public static void deleteUser(Context ctx) {
        users.remove(ctx.pathParam("userId"));
        ctx.result("User deleted successfully");
    }

    private static String randomId() {
        return UUID.randomUUID().toString();
    }
}
