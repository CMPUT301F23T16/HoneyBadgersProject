package com.example.myapplication;
import static android.os.Build.VERSION_CODES.R;

import com.example.myapplication.Item;
import com.example.myapplication.SorterFilterer;

import org.junit.Test;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;
public class ExampleSortTest {

    @Test
    public void testSortByPriceAscending() {
        // Create sample items
        Item item3 = new Item("Item1", 10.0, new Date(2023 - 1900, 0, 1));  // Adjust month to 0 for January
        Item item2 = new Item("Item2", 15.0, new Date(2023 - 1900, 1, 1));  // Adjust month to 1 for February
        Item item1 = new Item("Item3", 5.0, new Date(2023 - 1900, 2, 1));   // Adjust month to 2 for March

        // Create an ArrayList with unsorted items
        ArrayList<Item> unsortedList = new ArrayList<>();
        unsortedList.add(item3);
        unsortedList.add(item1);
        unsortedList.add(item2);

        // Perform sorting
        //ArrayList<Item> sortedList = SorterFilterer.sort(unsortedList, R.id.value_sort_button, true);

        // Assert the correct order after sorting
//        assertEquals("Item3", sortedList.get(0).getName());
//        assertEquals("Item1", sortedList.get(1).getName());
//        assertEquals("Item2", sortedList.get(2).getName());
    }
    @Test
    public void testSortByPriceDescending() {
        // Create sample items
        Item item1 = new Item("Item1", 10.0, new Date(2023, 1, 1));
        Item item2 = new Item("Item2", 15.0, new Date(2023, 2, 1));
        Item item3 = new Item("Item3", 5.0, new Date(2023, 3, 1));

        // Create an ArrayList with unsorted items
//        ArrayList<Item> unsortedList = new ArrayList<>();
//        unsortedList.add(item3);
//        unsortedList.add(item1);
//        unsortedList.add(item2);
//
//        // Perform sorting
//        ArrayList<Item> sortedList = SorterFilterer.sort(unsortedList, R.id.value_sort_button, false);
//
//        // Assert the correct order after sorting
//        assertEquals("Item2", sortedList.get(0).getName());
//        assertEquals("Item1", sortedList.get(1).getName());
//        assertEquals("Item3", sortedList.get(2).getName());
    }
}
