package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Setup the database for the application.
 * Populate the screen with all the items.
 */
public class ItemListActivity extends AppCompatActivity
        implements DataBase.ItemListUpdateListener,
        AddItemFragment.AddItemInteractionInterface,
        EditItemFragment.EditItemInteractionInterface,
        SortFilterFragment.SortFilterInteractionInterface{

    private RecyclerView itemListView;
    private ItemArrayAdapter itemListAdapter;

    private Button addItemButton;
    private Button sortFilterButton;
    private DataBase db; //Source of item list
    private int clickedItemIndex; // Index of item that is recently clicked on
    private ArrayList<Item> visibleItems;


    /**
     * When activity is created, setup database and user's items
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list_activity);

        //Data base instance with userName
        db = new DataBase("Adi", this);

        addItemButton = findViewById(R.id.add_item_button);
        sortFilterButton = findViewById(R.id.sort_filter_button);
        visibleItems = db.getItemList();

        //Create the viewable item list
        itemListView = findViewById(R.id.item_list);
        itemListView.setLayoutManager(new LinearLayoutManager(this));
        itemListAdapter = new ItemArrayAdapter(this, visibleItems, new ItemArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Item item, int position) {
                clickedItemIndex = position;
                EditItemFragment.newInstance(item).show(getSupportFragmentManager(), "Edit Item");
            }
        });


        itemListView.setAdapter(itemListAdapter);

        //Set the total value
        Double total = calculateTotal(visibleItems);
        updateTotalBox(total);

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddItemFragment().show(getSupportFragmentManager(), "ADD_ITEM");
            }
        });
        sortFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SortFilterFragment().show(getSupportFragmentManager(), "Sort_Filter");
            }
        });
    }

    /**
     * Updates the screen when the itemList is changed in teh database.
     * This can happen due to factors internal or external to this application.
     */
    public void onItemListUpdate(){
        //Update the items shown on the screen
        itemListAdapter.notifyDataSetChanged();

        //Update the total value
        Double total = calculateTotal(visibleItems);
        updateTotalBox(total);
    }

    /**
     * Accesses the last received items list from database and
     * calculates the total price of the items
     * @param itemList List of items to calculate total of
     * @return The total price
     */
    private Double calculateTotal(ArrayList<Item> itemList) {
        Double totalPrice = 0.0;
        for(Item i : itemList){
            totalPrice += i.getPrice();
        }
        return totalPrice;
    }

    /**
     * Sets the value of the total box on the screen
     * @param total value that the total is set to
     */
    private void updateTotalBox(double total) {
        TextView totalBox = findViewById(R.id.total_amount);
        totalBox.setText(String.format("$%.2f", total));
    }

    /**
     * Adds the item received from the AddItemFragment to the user's item collection in DB
     * @param item item to be added to users item collection in DB
     */
    @Override
    public void AddFragmentOKPressed(Item item) {
        db.addItem(item);
    }

    /**
     * Updates an item in the user's item collection (in DB)
     *      using updated item received from EditItemFragment
     * @param item item to be replaced in the user's item collection in DB
     */
    @Override
    public void EditFragmentOKPressed(Item item) {
        // Remove the existing outdated item from DB
        db.deleteItem(visibleItems.get(clickedItemIndex));

        // Add the updated item to DB
        db.addItem(item);
    }

    @Override
    public void SortFilterFragmentOKPressed(int sort_option, boolean ascending, int filter_option) {
        Log.d("INMAIN", ""+sort_option+" "+ascending);
        visibleItems = SorterFilterer.sort_and_filter(db.getItemList(),sort_option,ascending,filter_option);
        onItemListUpdate();

    }
}

