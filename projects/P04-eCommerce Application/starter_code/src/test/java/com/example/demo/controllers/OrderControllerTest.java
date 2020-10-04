package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runners.Parameterized;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController;

    private UserRepository userRepo = mock(UserRepository.class);
    private OrderRepository orderRepo = mock(OrderRepository.class);

    private static User user;
    private static Cart cart;
    private static Item item;

    @BeforeClass
    public static void initEntities() {
        user = new User();
        user.setId(1L);
        user.setUsername("songoku");

        item = new Item();
        item.setId(1L);
        item.setName("choko");
        item.setDescription("dark and sweet");
        item.setPrice(new BigDecimal(0.99));
    }

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.InjectObjects(orderController,"orderRepository", orderRepo);
        TestUtils.InjectObjects(orderController,"userRepository", userRepo);

        cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        cart.addItem(item);

        user.setCart(cart);
    }

    @Test
    public void submit_by_user_test() {
        when(userRepo.findByUsername(user.getUsername())).thenReturn(user);

        final ResponseEntity<UserOrder> response = orderController.submit(user.getUsername());

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());

        UserOrder uor = response.getBody();
        assertNotNull(uor);
        assertEquals(cart.getItems().size(),uor.getItems().size());
        assertEquals(cart.getItems().get(0),uor.getItems().get(0));
    }
}
