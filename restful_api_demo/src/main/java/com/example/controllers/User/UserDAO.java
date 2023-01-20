package com.example.controllers.User;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class UserDAO {
    private static HashMap<String, User> users = new HashMap<>();

    static {
        users.put("1", new User("Alice", "alice@alice.kt"));
        users.put("2", new User("Bob", "bob@bob.kt"));
        users.put("3", new User("Carol", "carol@carol.kt"));
        users.put(randomId(), new User("Dave", "dave@dave.kt"));
        users.put(randomId(), new User("Yonela", "yonela@admin.kt"));
    }

    public static String randomId() {
        return UUID.randomUUID().toString();
    }

    public static Set<String> getUsers() {
        return users.keySet();
    }

    public static Object getUserById(String userID) {
        return users.get(userID);
    }

    public static void createUser(User user) {
        users.put(randomId(), user);
    }

    public static void updateUser(String userId, User user) {
        users.put(userId, user);
    }

    public static void deleteUser(String userId) {
        users.remove(userId);
    }
}
