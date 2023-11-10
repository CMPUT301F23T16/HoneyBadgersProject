package com.example.myapplication;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

public class SorterFilterer {
    public static ArrayList<Item> sort_and_filter(ArrayList<Item> list,int sorting_option,boolean ascending, int filter_option, String date_from, String date_to)
    {
        Log.d("INSORTER", ""+sorting_option+" "+ascending);

        return filter(sort(list,sorting_option,ascending),filter_option, date_from, date_to);
    }
    public static ArrayList<Item> sort(ArrayList<Item> list,int sorting_option,boolean ascending)
    {
        Log.d("Checker", ""+sorting_option+" "+R.id.value_sort_button);
        if(sorting_option==R.id.date_sort_button) // sort on date
        {
            Collections.sort(list, new Comparator<Item>() {
                @Override
                public int compare(Item o1, Item o2) {
                    return (ascending)?o1.getDateAdded().compareTo(o2.getDateAdded()):-o1.getDateAdded().compareTo(o2.getDateAdded());
                }
            });
        }
        else if(sorting_option==R.id.description_sort_button)
        {
            Collections.sort(list, new Comparator<Item>() {
                @Override
                public int compare(Item o1, Item o2) {
                    return (ascending)?o1.getDescription().compareTo(o2.getDescription()):-o1.getDescription().compareTo(o2.getDescription());
                }
            });
        }
        else if(sorting_option==R.id.make_sort_button)
        {
            Collections.sort(list, new Comparator<Item>() {
                @Override
                public int compare(Item o1, Item o2) {
                    return (ascending)?o1.getMake().compareTo(o2.getMake()):-o1.getMake().compareTo(o2.getMake());
                }
            });
        }
        else if(sorting_option==R.id.value_sort_button)
        {
            Collections.sort(list, new Comparator<Item>() {
                @Override
                public int compare(Item o1, Item o2) {
                    return (ascending)?o1.getPrice().compareTo(o2.getPrice()):-o1.getPrice().compareTo(o2.getPrice());
                }
            });
        }
        return list;

    }
    public static ArrayList<Item> filter(ArrayList<Item> list,int filter_option, String date_from, String date_to)
    {
        Log.d("dj", "filter: HereinFilter");
    // Filter Code Here
        // Filter based on date range
        if (filter_option == R.id.date_from) {
            Log.d("dj", "filter: HereinFilter");
            ArrayList<Item> filteredList = new ArrayList<>();
            for (Item item : list) {
                String itemDate = item.getDateAdded();

                // Check if the item's date is within the specified range
                if (isDateInRange(itemDate, date_from, date_to)) {
                    filteredList.add(item);
                }
            }
            return filteredList;
        }

        //If no filtering is applied, original list is returned
        return list;
    }
    private static boolean isDateInRange(String itemDate, String date_from, String date_to) {


        // Check if the item's date is within the specified range
        return itemDate.compareTo(date_from) >= 0 && itemDate.compareTo(date_to) <= 0;
    }

}
