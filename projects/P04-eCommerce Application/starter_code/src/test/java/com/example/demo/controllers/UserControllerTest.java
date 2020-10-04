package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;

    private UserRepository userRepo = mock(UserRepository.class);

    private CartRepository cartRepo = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.InjectObjects(userController, "userRepository", userRepo);
        TestUtils.InjectObjects(userController, "cartRepository", cartRepo);
        TestUtils.InjectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void create_user_happy_path() {
        when(encoder.encode("testPassword")).thenReturn("hashedPassword");

        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("test", u.getUsername());
        assertEquals("hashedPassword",u.getPassword());
    }

    @Test
    public void find_user_by_id() {
        User u = new User();
        u.setUsername("testUsername");

        when(userRepo.findById(0L)).thenReturn(java.util.Optional.of(u));

        final ResponseEntity<User> response = userController.findById(0L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());

        User ur = response.getBody();
        assertNotNull(ur);
        assertEquals(0, ur.getId());
        assertEquals("testUsername", ur.getUsername());
    }

    @Test
    public void find_by_username() {
        User u = new User();
        u.setUsername("testUsername");

        when(userRepo.findByUsername("testUsername")).thenReturn(u);

        final ResponseEntity<User> response = userController.findByUserName("testUsername");

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());

        User ur = response.getBody();
        assertNotNull(ur);
        assertEquals(0, ur.getId());
        assertEquals("testUsername", ur.getUsername());
    }
}
