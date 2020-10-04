package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.swing.text.html.Option;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemControllerTest {

    private ItemController itemController;

    private static Item itemA;
    private static Item itemB;
    private static List<Item> items;

    private ItemRepository itemRepo = mock(ItemRepository.class);

    @BeforeClass
    public static void initEntities() {
        itemA = new Item();
        itemA.setId(1L);
        itemA.setName("milk");

        itemB = new Item();
        itemB.setId(2L);
        itemB.setName("choko");

        items = new ArrayList<Item>();
        items.add(itemA);
    }

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.InjectObjects(itemController,"itemRepository", itemRepo);
    }

    @Test
    public void get_Items_Test() {
        when(itemRepo.findAll()).thenReturn(items);

        final ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());

        List<Item> responseItems = response.getBody();
        assertNotNull(responseItems);
        assertEquals(1,responseItems.size());
        assertEquals(itemA.getId(),responseItems.get(0).getId());
    }

    @Test
    public void get_item_by_id() {
        when(itemRepo.findById(itemA.getId())).thenReturn(Optional.ofNullable(itemA));

        final ResponseEntity<Item> response = itemController.getItemById(itemA.getId());

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());

        Item item = response.getBody();
        assertNotNull(item);
        assertEquals(itemA,item);
    }

    @Test
    public void get_items_by_name() {
        when(itemRepo.findByName(itemA.getName())).thenReturn(items);

        final ResponseEntity<List<Item>> response = itemController.getItemsByName(itemA.getName());

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());

        List<Item> responseItems = response.getBody();
        assertNotNull(responseItems);
        assertEquals(itemA.getName(),responseItems.get(0).getName());
    }
}
