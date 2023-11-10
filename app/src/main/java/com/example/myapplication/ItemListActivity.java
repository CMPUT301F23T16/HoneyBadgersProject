package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Setup the database for the application.
 * Populate the screen with all the items.
 */

public class ItemListActivity extends AppCompatActivity
        implements DataBase.ItemListUpdateListener,
        AddItemFragment.AddItemInteractionInterface {

    private RecyclerView itemListView;
    private ItemArrayAdapter itemListAdapter;
    private DataBase db; //Source of item list
    private Button addItemButton;
    private Button deleteItemButton;


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
        itemListAdapter = new ItemArrayAdapter(this, db.getItemList());
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
                    deleteSelectedItems();
                })
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnDismissListener(dialogInterface -> {
            itemListAdapter.unSelectCheckBox();
        });

        dialog.show();
    }

    private void deleteSelectedItems(){
        try{
            ArrayList<Item> itemList = db.getItemList();
            for (int i = itemList.size() - 1; i>=0 ; i--){
                Item item = itemList.get(i);
                if (item.isSelected()){
                    db.deleteSelectedItem(item.getName(),this);
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
            Toast.makeText(this,"Error deleting item", Toast.LENGTH_SHORT).show();
        }

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

    @Override
    public void AddFragmentOKPressed(Item item) {
        db.addItem(item);
    }
}

