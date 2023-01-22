package com.example.controllers.User.DAO;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import com.example.controllers.User.User;

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
        return users.get(userID) != null ? users.get(userID) : "{\"message\": \"User does not exist.\"}";
    }

    public static String createUser(User user) {
        users.put(randomId(), user);
        return "User created successfully";
    }

    public static String updateUser(String userId, User user) {
        if (users.get(userId) == null)
            return "Unable to update user details.Could not find user with ID : "+ userId;
        users.put(userId, user);
        return "User details updated successfully";
    }

    public static String deleteUser(String userId) {
        if (!getUserById(userId).getClass().equals(User.class)) return "Could not find user with ID : "+ userId;
        users.remove(userId);
        return "User deleted successfully";
    }
}
