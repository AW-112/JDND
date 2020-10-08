package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private CartController cartController;

    private CartRepository cartRepo = mock(CartRepository.class);
    private ItemRepository itemRepo = mock(ItemRepository.class);
    private UserRepository userRepo = mock(UserRepository.class);

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
        cartController = new CartController();

        TestUtils.InjectObjects(cartController,"cartRepository", cartRepo);
        TestUtils.InjectObjects(cartController,"itemRepository", itemRepo);
        TestUtils.InjectObjects(cartController,"userRepository", userRepo);

        cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);

        user.setCart(cart);
    }

    @Test
    public void add_to_cart() {
        when(userRepo.findByUsername(user.getUsername())).thenReturn(user);
        when(itemRepo.findById(item.getId())).thenReturn(java.util.Optional.ofNullable(item));

        ModifyCartRequest r = new ModifyCartRequest();
        r.setItemId(item.getId());
        r.setQuantity(2);
        r.setUsername(user.getUsername());

        final ResponseEntity<Cart> response = cartController.addTocart(r);

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());

        Cart cr = response.getBody();
        assertNotNull(cr);
        assertEquals(user,cr.getUser());
        assertEquals(item,cr.getItems().get(0));
        assertEquals(item.getPrice().multiply(new BigDecimal((cr.getItems().size()))),cr.getTotal());
    }

    @Test
    public void remove_from_cart() {
        when(userRepo.findByUsername(user.getUsername())).thenReturn(user);
        when(itemRepo.findById(item.getId())).thenReturn(java.util.Optional.ofNullable(item));

        user.getCart().addItem(item);
        user.getCart().addItem(item);

        assertEquals(2,user.getCart().getItems().size());

        ModifyCartRequest r = new ModifyCartRequest();
        r.setItemId(item.getId());
        r.setQuantity(1);
        r.setUsername(user.getUsername());

        final ResponseEntity<Cart> response = cartController.removeFromcart(r);

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());

        Cart cr = response.getBody();
        assertNotNull(cr);
        assertEquals(user,cr.getUser());
        assertEquals(1,cr.getItems().size());
        assertEquals(item,cr.getItems().get(0));
        assertEquals(item.getPrice().multiply(new BigDecimal((cr.getItems().size()))),cr.getTotal());
    }
}
