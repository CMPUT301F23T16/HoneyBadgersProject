package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.DialogFragment;


import android.content.Intent;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Setup the database for the application.
 * Populate the screen with all the items.
 */

public class ItemListActivity extends AppCompatActivity
        implements DataBase.ItemListUpdateListener,
        AddItemFragment.AddItemInteractionInterface,
        EditItemFragment.EditItemInteractionInterface,
        SortFilterFragment.SortFilterInteractionInterface,
        TagFragmentListener,
        TagFragment.TagSelectionListener,
        PhotosFragment.PhotosInteractionInterface{

    private RecyclerView itemListView;
    private ItemArrayAdapter itemListAdapter;
    private DataBase db; //Source of item list

    private Button addItemButton;
    private Button deleteItemButton;
    private Button sortFilterButton;
    private int clickedItemIndex; // Index of item that is recently clicked on
    private ArrayList<Item> visibleItems;

    // MOST RECENT SORT OPTIONS
    int sort_option = R.id.value_sort_button;
    boolean ascending = false;
    int filter_option = 0;

    private String date_from;
    private String date_to;
    public EditItemFragment editItemFragment;
    private File directory;
    private ArrayList<ImageView> photos;
    private boolean editing_item = false;
    private Item temporary_item = null;
    private boolean reloading_images=false;


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
        String userName = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db = new DataBase (userName, this);

        addItemButton = findViewById(R.id.add_item_button);
        deleteItemButton = findViewById(R.id.delete_item_button);
        deleteItemButton.setOnClickListener(view ->{
            deleteConfirmDialog();
        });
        directory = new File(getFilesDir(), "item_images");
        if(!directory.exists()){
            directory.mkdir();
        }

        sortFilterButton = findViewById(R.id.sort_filter_button);
        visibleItems = db.getItemList();

        //initialize the list of tags
        List<String> tags = new ArrayList<>();
        tags.add("Bedroom");
        tags.add("Kitchen");

        //Create the viewable item list
        itemListView = findViewById(R.id.item_list);
        itemListView.setLayoutManager(new LinearLayoutManager(this));
        itemListAdapter = new ItemArrayAdapter(this, visibleItems, new ItemArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Item item, int position) {
                clickedItemIndex = position;
                editing_item = true;
                reloading_images=false;
                temporary_item = null;
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
                editing_item=false;
                reloading_images=false;
                temporary_item = null;
                new AddItemFragment().show(getSupportFragmentManager(), "ADD_ITEM");
            }
        });

        sortFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SortFilterFragment().show(getSupportFragmentManager(), "Sort_Filter");
            }
        });
        Button logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }




    /**
     * This method creates and displays a confirmation dialog for deleting items. It asks the user to confirm/cancel the deletion action before proceeding further.
     */
    public void deleteConfirmDialog(){

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete the selected item(s)?")
                .setPositiveButton("Delete", (dialogInterface, which) -> {

                    deleteSelectedItems();

                })
                .setNegativeButton("Cancel", null)
                .create();

        //Set a listener that unselects all checkboxes when the dialog is dismissed
        dialog.setOnDismissListener(dialogInterface -> {
            itemListAdapter.unSelectCheckBox();
        });

        dialog.show();
    }

    /**
     * This method iterates through a list of items and deletes the ones that are selected
     */

    private void deleteSelectedItems(){
        try{
            ArrayList<Item> itemList = db.getItemList();
            for (int i = itemList.size() - 1; i>=0 ; i--){
                Item item = itemList.get(i);
                if (item.isSelected()){
                    //If the item is selected, delete it from the database using its name.
                    db.deleteSelectedItem(item,this);
                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    /**
     * Updates the screen when the itemList is changed in teh database.
     * This can happen due to factors internal or external to this application.
     */
    public void onItemListUpdate(){

        Log.d("INMAIN", ""+sort_option+" "+ascending);

        // KEEP VISIBLE ITEMS SORTED WITH MOST RECENT SORTING CHOICES
        visibleItems = SorterFilterer.sort_and_filter(db.getItemList(),sort_option,ascending,filter_option, date_from, date_to);

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
    static Double calculateTotal(ArrayList<Item> itemList) {
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
     * Adds the item received from the AddItemFragment to the user's item collection in DB, also adds its photos
     * @param item item to be added to users item collection in DB
     */
    @Override
    public void AddFragmentOKPressed(Item item) {
        db.addItem(item,photos);

        Log.d("xxx sort-option", String.format("%s",sort_option));
        Log.d("xxx ascending", String.format("%s",ascending));
        Log.d("filter-option", String.format("%s",filter_option));
    }

    /**
     * Creates a list of strings for photo references corresponding to the local List of ImageViews
     * @return a list of strings for photo references
     */
    @Override
    public List<String> getPhotoReferences() {
        if(photos==null)
            return null;
        ArrayList<String> references = new ArrayList<>();
        for(ImageView i:photos)
        {
            references.add((String) i.getTag());
        }
        return references;
    }

    /**
     * Setter method for Temporary Itemobject
     * @param item temporary state of the item
     */
    @Override
    public void saveTemporaryState(Item item) {
        temporary_item=item;
    }

    /**
     * Return the Temporary Item object stored
     * @return
     */
    @Override
    public Item getTemporaryState() {
        return temporary_item;
    }

    /**
     * Updates an item in the user's item collection (in DB)
     *      using updated item received from EditItemFragment, also changes the imaages in DB
     * @param item item to be replaced in the user's item collection in DB
     */
    @Override
    public void EditFragmentOKPressed(Item item) {
        // Remove the existing outdated item from DB
        db.deleteItem(visibleItems.get(clickedItemIndex));
        Log.d("xxx sort-option", String.format("%s",sort_option));
        Log.d("xxx ascending", String.format("%s",ascending));
        Log.d("filter-option", String.format("%s",filter_option));

        // Add the updated item to DB
        db.addItem(item,photos);
        editing_item = false;
        Log.d("xxx sort-option", String.format("%s",sort_option));
        Log.d("xxx ascending", String.format("%s",ascending));
        Log.d("filter-option", String.format("%s",filter_option));
    }


    /**
     * Sorts the visible item list using sort options received from the SortFilterFragment
     * @param sort_option whether to sort by date, value, description or name
     * @param ascending Whether to sort in ascending or descending order
     * @param filter_option not implemented yet
     */
    @Override
    public void SortFilterFragmentOKPressed(int sort_option, boolean ascending, int filter_option, String date_from, String date_to) {
        // SAVE THE MOST RECENT SORT/FILTER OPTIONS
        // ADD AND EDIT FEATURE CAN USE THIS
        this.sort_option = sort_option;
        this.ascending = ascending;
        this.filter_option = filter_option;
        this.date_from = date_from;
        this.date_to = date_to;

        // THIS WILL ALSO APPLY THE MOST RECENT SORTING/FILTERING OPTIONS TO VISIBLE LIST
        onItemListUpdate();
    }
    private void logout() {
        FirebaseAuth.getInstance().signOut(); // Firebase sign out
        navigateToLoginActivity();
    }
    private void navigateToLoginActivity() {
        Intent intent = new Intent(this, LoginSignupActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish(); // Call this when your activity is done and should be closed.
    }

    @Override
    public void onOpenTagFragment() {
        // Create an instance of TagFragment
        TagFragment tagFragment = new TagFragment();
        // Pass the clicked item index to the TagFragment
        Bundle bundle = new Bundle();
        bundle.putInt("clickedItemIndex", clickedItemIndex);
        tagFragment.setArguments(bundle);

        tagFragment.show(getSupportFragmentManager(), "Add Tag(s)");
    }

    @Override
    public void onTagSelected(String tag) {
        //System.out.println(tag);
        // Create an instance of TagFragment
        TagFragment tagFragment = new TagFragment();

        // Pass the clicked item index to the TagFragment
        Bundle bundle = new Bundle();
        bundle.putInt("clickedItemIndex", clickedItemIndex);
        tagFragment.setArguments(bundle);

        // Retrieve the clicked item index from the arguments
        Bundle receivedBundle = tagFragment.getArguments();
        if (receivedBundle != null) {
            int clickedItemIndex = receivedBundle.getInt("clickedItemIndex", -1);

            // Check if the index is valid
            if (clickedItemIndex != -1 && clickedItemIndex < visibleItems.size()) {
                Item clickedItem = visibleItems.get(clickedItemIndex);

                // Add the selected tag to the clicked item

                clickedItem.setTag(tag);

                // Update the item in the database or perform any necessary actions
                db.updateItem(clickedItem);
                String currentTag = editItemFragment.itemTag.getText().toString();
                String newTag = currentTag.length() == 0 ?tag:currentTag + ',' + tag;
                editItemFragment.itemTag.setText(newTag);


            }
        }
    }
    /**
     * Returns the working directory of the app
     * @return File object for directory
     */
    @Override
    public File getDirectory() {
        return directory;
    }

    /**
     * Adds an ImageView to the local list
     * @param uri Uri of the Image to be added
     */
    @Override
    public void addPhoto(Uri uri) {
        ImageView photo = (ImageView) LayoutInflater.from(this).inflate(R.layout.item_image,null,false);
        photo.setImageURI(uri);
        photo.setTag(uri.getLastPathSegment().toString());
        Log.d(uri.getLastPathSegment().toString(), "addPhoto: ");
        photos.add(photo);
    }

    /**
     * Returns PhotoArrayAdapter object linked to the local list of ImageViews photos
     * @return PhotoArrayAdapter object
     */
    @Override
    public PhotoArrayAdapter getGridAdapter() {
        if(!reloading_images)
            photos = editing_item?db.getItemImages(this,visibleItems.get(clickedItemIndex)):new ArrayList<ImageView>();
        return new PhotoArrayAdapter(this,photos);
    }

    /**
     * Resets the local list of ImageViews to default
     */
    @Override
    public void resetPhotos() {
        photos = editing_item?db.getItemImages(this,visibleItems.get(clickedItemIndex)):new ArrayList<ImageView>();
    }

    /**
     * Removes the selected photo from local list and calls db.deletePhoto() to remove it from the database
     * @param position index of photo to be removed in the local list
     */
    @Override
    public void removePhoto(int position)
    {
        if(editing_item) {

            db.deletePhoto(visibleItems.get(clickedItemIndex), photos.get(position));
            visibleItems.get(clickedItemIndex).getImageRefs().remove(position);
        }
        photos.remove(position);
    }

    /**
     * Sets Reloading Images to true to tell user is not opening the add/edit item fragment for the first time
     */
    @Override
    public void setReloadingImagesToTrue() {
        reloading_images=true;
    }

    /**
     * Method to see if the user is editing an Item
     * @return true if user is editing Item, false otherwise
     *
     */
    @Override
    public boolean getEditingItem() {
        return editing_item;
    }
}

