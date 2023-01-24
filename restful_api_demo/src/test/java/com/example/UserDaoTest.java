package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.example.controllers.Role;
import com.example.controllers.User.User;
import com.example.controllers.User.DAO.UserDAO;

import kotlin.Pair;
public class UserDaoTest {

    @BeforeAll
    public static void setUp(){
        UserDAO.setupUsers();
    }


    @Test
    public void testGetUsers(){
        Set<String> result = UserDAO.getUsers();
        assertEquals(4, result.size());
    }

    @Test
    public void testGetUsersByIdValidId(){
        Object result = UserDAO.getUserById("1");
        assertEquals(User.class, result.getClass());
        assertEquals("Alice",( (User) result).getName());
        assertEquals("alice@mail.kt",( (User) result).getEmail());
    }

    @Test
    public void testGetUsersByIdInvalidId(){
        Object result = UserDAO.getUserById("1000");
        assertEquals("{\"message\": \"User does not exist.\"}", result);
    }

    @Test
    public void testAddUser(){
        UserDAO.addUser(new User("Bobby", "bobby@pit.mail"));
        Set<String> result = UserDAO.getUsers();
        assertEquals(5, result.size());

    }

    @Test
    public void testUpdateUserValidId(){
        String result = UserDAO.updateUser("2", new User("Bob update", "bob@testing"));
        assertEquals(result, "User details updated successfully");
    }

    @Test
    public void testUpdateUserInvalidId(){
        String result = UserDAO.updateUser("12", new User("Alice 1", "alice@testing"));
        assertEquals(result, "Unable to update user details.Could not find user with ID : 12");
    }

    @Test
    public void testDeleteUserInvalidId(){
        String result = UserDAO.deleteUser("12");
        assertEquals("Could not find user with ID : 12", result);
    }

    @Test
    public void testDeleteUserValidId() {
        String result = UserDAO.deleteUser("3");
        assertEquals("User deleted successfully", result);
    }

    @Test
    public void testGetCredsFromUserRolesMap(){
        List<Role> roles = UserDAO.getCredsFromUserRolesMap(new Pair<String,String>("alice@mail.kt", "weak-1234"));
        assertTrue(roles.contains(Role.USER_READ));
    }
}
