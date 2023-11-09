package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;

import java.util.ArrayList;
import java.util.List;

/**
 * Setup the database for the application.
 * Populate the screen with all the items.
 */

public class ItemListActivity extends AppCompatActivity
        implements DataBase.ItemListUpdateListener,
        AddItemFragment.AddItemInteractionInterface,
        EditItemFragment.EditItemInteractionInterface{

    private RecyclerView itemListView;
    private ItemArrayAdapter itemListAdapter;
    private DataBase db; //Source of item list

    private List<Item> itemList;
    private Button addItemButton;
    private Button deleteItemButton;


    private int clickedItemIndex; // Index of item that is recently clicked on

    /**
     * When activity is created, setup database and user's items
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list_activity);

        //Data base instance with userName
        String userName = "Adi";
        db = new DataBase (userName, this);

        addItemButton = findViewById(R.id.add_item_button);
        deleteItemButton = findViewById(R.id.delete_item_button);
        deleteItemButton.setOnClickListener(view ->{
            deleteConfirmDialog();
        });


        //Create the viewable item list
        itemListView = findViewById(R.id.item_list);
        itemListView.setLayoutManager(new LinearLayoutManager(this));
        itemListAdapter = new ItemArrayAdapter(this, db.getItemList(), new ItemArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Item item, int position) {
                clickedItemIndex = position;
                EditItemFragment.newInstance(item).show(getSupportFragmentManager(), "Edit Item");
            }
        });


        itemListView.setAdapter(itemListAdapter);

        //Set the total value
        Double total = calculateTotal(db.getItemList());
        updateTotalBox(total);

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddItemFragment().show(getSupportFragmentManager(), "ADD_ITEM");
            }
        });

    }

    public void deleteConfirmDialog(){

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete the selected item(s)?")
                .setPositiveButton("Delete", (dialogInterface, which) -> {
                    deleteSelItem();
                    finish();
                })
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnDismissListener(dialogInterface -> {
            itemListAdapter.unSelectCheckBox();
        });

        dialog.show();
    }

    private void deleteSelItem() {
        // Use a List to keep track of items to delete to avoid ConcurrentModificationException
        List<Item> itemsToDelete = new ArrayList<>();

        for (Item item : itemList) {
            if (item.isSelected()) {
                itemsToDelete.add(item);
            }
        }

        // Delete the selected items from the database
        try {
            for (Item item : itemsToDelete) {
                db.deleteSelectedItem(item.getId(), this);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error deleting item", Toast.LENGTH_SHORT).show();
        }
    }

    public void onItemDelete(String itemId){
        for (int i = itemList.size() - 1 ; i >= 0; i--){
            if(itemList.get(i).getId().equals(itemId)){
                itemList.remove(i);
                break;
            }
        }
        itemListAdapter.notifyDataSetChanged();

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
        db.deleteItem(db.getItemList().get(clickedItemIndex));

        // Add the updated item to DB
        db.addItem(item);
    }
}

