package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Setup the database for the application.
 * Populate the screen with all the items.
 */
public class ItemListActivity extends AppCompatActivity implements DataBase.ItemListUpdateListener {

    private RecyclerView itemListView;
    private ItemArrayAdapter itemListAdapter;
    private DataBase db; //Source of item list


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

        //Create the viewable item list
        itemListView = findViewById(R.id.item_list);
        itemListView.setLayoutManager(new LinearLayoutManager(this));
        itemListAdapter = new ItemArrayAdapter(this, db.getItemList());
        itemListView.setAdapter(itemListAdapter);

        //Set the total value
        Double total = calculateTotal(db.getItemList());
        updateTotalBox(total);
    }

    /**
     * Updates the screen when the itemList is changed in teh database.
     * This can happen due to factors internal or external to this application.
     */
    public void onItemListUpdate(){
        //Update the items shown on the screen
        itemListAdapter.notifyDataSetChanged();

        //Update the total value
        Double total = calculateTotal(db.getItemList());
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
}


