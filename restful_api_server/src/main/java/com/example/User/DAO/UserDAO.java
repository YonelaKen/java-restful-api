package com.example.User.DAO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.example.User.Message;
import com.example.User.User;
import com.example.controllers.Auth;
import com.example.controllers.Role;
import com.google.common.annotations.VisibleForTesting;

import io.javalin.http.Context;
import kotlin.Pair;

public class UserDAO {
    private static HashMap<String, User> users = new HashMap<>();

    static {
        users.put("1", new User("Alice", "alice@mail.kt"));
        users.put("2", new User("Bob", "bob@bob.kt"));
        users.put("3", new User("John", "john@bob.kt"));
        users.put(randomId(), new User("Yonela", "yonela@admin.kt"));
    }

    private static Map<Pair<String, String>, List<Role>> UserRolesMap = new HashMap<>();
    static {
        UserRolesMap.put(new Pair<>("alice@mail.kt", "weak-1234"), Arrays.asList(Role.USER_READ, Role.USER));
        UserRolesMap.put(new Pair<>("bob@bob.kt", "weak-123456"), Arrays.asList(Role.USER_READ, Role.USER_WRITE, Role.USER));
        UserRolesMap.put(new Pair<>("yonela@admin.kt", "iamAdmin-12345"), Arrays.asList(Role.ADMIN, Role.USER));
    }

    @VisibleForTesting
    public static void setupUsers() {
        users.clear();
        users.put("1", new User("Alice", "alice@mail.kt"));
        users.put("2", new User("Bob", "bob@bob.kt"));
        users.put("3", new User("John", "john@bob.kt"));
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

    public static Message createUser(Context ctx) {
        Pair<String, String> userDetails = Auth.getBasicAuthCredentials(ctx);
        User user = ctx.bodyAsClass(User.class);
        addUser(user);
        giveUserDefaultRoles(user, userDetails.getSecond());
        return new Message("User created successfully");
    }

    public static void addUser(User user) {
        users.put(randomId(), user);
    }

    public static Message updateUser(String userId, User user) {
        if (users.get(userId) == null)
            return new Message("Unable to update user details.Could not find user with ID : " + userId);
        users.put(userId, user);
        return new Message("User details updated successfully");
    }

    public static Message deleteUser(String userId) {
        if (!getUserById(userId).getClass().equals(User.class))
            return new Message("Could not find user with ID : " + userId);
        deleteUserRoles(users.get(userId).getEmail());
        users.remove(userId);
        return new Message("User deleted successfully");
    }

    private static void deleteUserRoles(String useremail) {
        for (Pair<String, String> userDetails : UserRolesMap.keySet()) {
            if (userDetails.getFirst().equals(useremail)) {
                UserRolesMap.remove(userDetails);
                break;
            }
        }
    }

    private static void giveUserDefaultRoles(User user, String password) {
        UserRolesMap.put(new Pair<>(user.getName(), password), Arrays.asList(Role.USER_READ));
    }

    public static Message adminAddUserRoles(List<String> roles, String useremail) {
        for (Pair<String, String> userDetails : UserRolesMap.keySet()) {
            if (userDetails.getFirst().equals(useremail)) {
                List<Role> newRoles = new ArrayList<>(UserRolesMap.get(userDetails));
                for (String role : roles) {
                    if (role.equalsIgnoreCase("read")) {
                        newRoles.add(Role.USER_READ);
                    } else if (role.equalsIgnoreCase("write")) {
                        newRoles.add(Role.USER_WRITE);
                    } else if (role.equalsIgnoreCase("admin")) {
                        newRoles.add(Role.ADMIN);
                    }
                }
                UserRolesMap.put(userDetails, newRoles);
                return new Message("User roles have been updated successfully");
            }
        }
        return new Message("User with useremail : " + useremail + " does not exist");
    }

    public static Message adminRemoveUserRoles(List<String> roles, String useremail) {
        UserRolesMap.keySet().forEach(e -> e.getFirst().equalsIgnoreCase(useremail));
        for (Pair<String, String> userDetails : UserRolesMap.keySet()) {
            if (userDetails.getFirst().equals(useremail)) {
                List<Role> newRoles = new ArrayList<>(UserRolesMap.get(userDetails));
                for (String role : roles) {
                    if (role.equalsIgnoreCase("read")) {
                        newRoles.remove(Role.USER_READ);
                    } else if (role.equalsIgnoreCase("write")) {
                        newRoles.remove(Role.USER_WRITE);
                    } else if (role.equalsIgnoreCase("admin")) {
                        newRoles.remove(Role.ADMIN);
                    }
                }
                UserRolesMap.put(userDetails, newRoles);
                return new Message("User roles have been updated successfully");
            }
        }
        return new Message("User with useremail : " + useremail + " does not exist");
    }

    public static List<Role> getCredsFromUserRolesMap(Pair<String, String> credentials) {
        return UserRolesMap.get(credentials);
    }

}
