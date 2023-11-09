package com.example.myapplication;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SorterFilterer {
    public static ArrayList<Item> sort_and_filter(ArrayList<Item> list,int sorting_option,boolean ascending, int filter_option)
    {
        Log.d("INSORTER", ""+sorting_option+" "+ascending);

        return filter(sort(list,sorting_option,ascending),filter_option);
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
    public static ArrayList<Item> filter(ArrayList<Item> list,int filter_option)
    {
    // Filter Code Here
        return list;
    }

}
