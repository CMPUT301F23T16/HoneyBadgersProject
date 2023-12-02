package com.example.myapplication;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ItemListActivityTest {

    private ArrayList<Item> mockItemList() {
        ArrayList<Item> itemList = new ArrayList<Item>();
        itemList.add(new Item("TV", 200));
        itemList.add(new Item("Fridge", 250.50));
        itemList.add(new Item ("Speaker", 49.50));
        return itemList;
    }

    @Test
    public void testCalculateTotal() {
        ArrayList<Item> itemList = mockItemList();
        Double expectedTotal = 500.0;
        assertEquals(expectedTotal, ItemListActivity.calculateTotal(itemList));
    }

}
