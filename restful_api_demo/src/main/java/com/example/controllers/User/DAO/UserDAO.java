package com.example.controllers.User.DAO;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.example.controllers.Auth;
import com.example.controllers.Role;
import com.example.controllers.User.User;
import com.google.common.annotations.VisibleForTesting;

import io.javalin.http.Context;
import kotlin.Pair;

public class UserDAO {
    private static HashMap<String, User> users = new HashMap<>();

    static {
        users.put("1", new User( "Alice","alice@mail.kt"));
        users.put("2", new User("Bob","bob@bob.kt" ));
        users.put("3", new User("John","john@bob.kt"));
        users.put(randomId(), new User("Yonela", "yonela@admin.kt"));
    }

    private static Map<Pair<String, String>, List<Role>> UserRolesMap = new HashMap<>();
    static {
        UserRolesMap.put(new Pair<>("alice@mail.kt", "weak-1234"), Arrays.asList(Role.USER_READ));
        UserRolesMap.put(new Pair<>("bob@bob.kt", "weak-123456"), Arrays.asList(Role.USER_READ, Role.USER_WRITE));
        UserRolesMap.put(new Pair<>("yonela@admin.kt", "iamAdmin-12345"), Arrays.asList(Role.ADMIN));
    }

    @VisibleForTesting
    public static void setupUsers() {
        users.clear();
        users.put("1", new User( "Alice","alice@mail.kt"));
        users.put("2", new User("Bob","bob@bob.kt" ));
        users.put("3", new User("John","john@bob.kt"));
        users.put(randomId(), new User("Yonela", "yonela@admin.kt"));
    }

    public static String randomId() {
        return UUID.randomUUID().toString();
    }

    public static Set<String> getUsers() {
        return users.keySet();
    }

    public static Object getUserById(String userID) {
        return users.get(userID) != null ?  users.get(userID) : "{\"message\": \"User does not exist.\"}";
    }

    public static String createUser(Context ctx) {
        Pair <String, String> userDetails = Auth.getBasicAuthCredentials(ctx);
        User user = ctx.bodyAsClass(User.class);
        addUser(user);
        giveUserDefaultRoles(user,userDetails.getSecond());
        return "User created successfully";
    }
    
    public static void addUser(User user){
        users.put(randomId(), user);
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

    private static void giveUserDefaultRoles(User user, String password){
        UserRolesMap.put(new Pair<>(user.getName(), password), Arrays.asList(Role.USER_READ));
    }

    private static void adminAddUserRoles(List<String> roles, String useremail){
        for (Pair<String, String> userDetails : UserRolesMap.keySet()) {
            if(userDetails.getFirst().equals(useremail)){
                while(roles.iterator().hasNext()){
                    String role = roles.iterator().next();
                    if(role.equalsIgnoreCase("read")){
                        UserRolesMap.get(userDetails).add(Role.USER_READ);
                    } else if(role.equalsIgnoreCase("write")){
                        UserRolesMap.get(userDetails).add(Role.USER_WRITE);
                    } else if(role.equalsIgnoreCase("admin")){
                        UserRolesMap.get(userDetails).add(Role.ADMIN);
                    }
                } 
            }
        }
    }

    private static void adminRemoveUserRoles(List<String> roles, String userId){
        UserRolesMap.keySet().forEach(e->e.getFirst().equalsIgnoreCase(userId));
        for (Pair<String, String> userDetails : UserRolesMap.keySet()) {
            if(userDetails.getFirst().equals(userId)){
                while(roles.iterator().hasNext()){
                    String role = roles.iterator().next();
                    if(role.equalsIgnoreCase("read")){
                        UserRolesMap.get(userDetails).add(Role.USER_READ);
                    } else if(role.equalsIgnoreCase("write")){
                        UserRolesMap.get(userDetails).add(Role.USER_WRITE);
                    } else if(role.equalsIgnoreCase("admin")){
                        UserRolesMap.get(userDetails).add(Role.ADMIN);
                    }
                } 
            }
        }
    }

    public static List<Role> getCredsFromUserRolesMap(Pair<String, String> credentials) {
        return UserRolesMap.get(credentials);
    }
}
