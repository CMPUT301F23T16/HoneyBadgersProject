//package com.example.myapplication;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.Optional;
//
//import org.junit.Test;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertThrows;
//import static org.junit.Assert.assertTrue;
//
//public class TestSorterFilterer {
//
//
//    private ArrayList<Item> mockItemList() {
//        ArrayList<Item> itemList = new ArrayList<Item>();
//        itemList.add(new Item("TV", 55.49, new Date(), "test description", "test make", "test model", "test serial", "test comment", "test tag"));
//        itemList.add(new Item("Fridge", 55.38, new Date(), "test description", "test make", "test model", "test serial", "test comment", "test tag"));
//        itemList.add(new Item("Sofa", 57.29, new Date(), "test description", "test make", "test model", "test serial", "test comment", "test tag"));
//        return itemList;
//    }
//
//    @Test
//    public void testSort() {
//        ArrayList<Item> itemList = mockItemList();
//        ArrayList<Item> sortedList = SorterFilterer.sort(itemList, R.id.value_sort_button, false);
//        assertEquals(sortedList.get(0).getName(), "Sofa");
//        assertEquals(sortedList.get(1).getName(), "TV");
//        assertEquals(sortedList.get(2).getName(), "Fridge");
//    }
//
//    @Test
//    public void testIsDateInRange() {
//        assertEquals(false, SorterFilterer.isDateInRange("1999-11-11","2000-11-10","2010-11-12"));
//        assertEquals(true, SorterFilterer.isDateInRange("2000-11-10","2000-11-10","2010-11-12"));
//        assertEquals(true, SorterFilterer.isDateInRange("2002-11-11","2002-11-11","2002-11-11"));
//        assertEquals(true, SorterFilterer.isDateInRange("2002-11-11","2000-11-10","2002-11-11"));
//        assertEquals(false, SorterFilterer.isDateInRange("2020-11-11","2000-11-10","2010-11-12"));
//
//    }
//
//}
