package com.example.myapplication;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.time.format.DateTimeFormatter;


/**This class represents the cloud database the app is connecting to.
 * All communications with the database are handled here.
 */
public class DataBase {
    private FirebaseFirestore db;
    private ArrayList<Item> itemList;
    private CollectionReference itemsRef;
    private FirebaseStorage storage;

    public DataBase(String userName) {
        this.db = FirebaseFirestore.getInstance();
        this.storage = FirebaseStorage.getInstance();
        this.itemsRef = db.collection(userName);
    }

    /**
     * Connect to the database and get the user's collection. Setup automatic
     * updates from database
     *
     * @param userName This is the username of the user
     * @param context  Parent object
     */
    public DataBase(String userName, ItemListActivity context) {
        this.itemList = new ArrayList<>();

        this.db = FirebaseFirestore.getInstance();
        this.itemsRef = db.collection(userName); // Each user has their own collection

        itemsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {

            @RequiresApi(api = Build.VERSION_CODES.O)

            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshots,
                                @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                    return;
                }
                if (querySnapshots != null) {
                    itemList.clear();
                    //For each item in the cloud db, add it to the list

                    for (QueryDocumentSnapshot doc: querySnapshots) {

                        String name = doc.getId();
                        Date date;
                        try {
                            date = new SimpleDateFormat("yyyy-MM-dd").parse(doc.getString("dateAdded"));
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }

                        Double price = doc.getDouble("price");
                        String description = doc.getString("description");
                        String make = doc.getString("make");
                        String model = doc.getString("model");
                        String serial = doc.getString("serial");
                        String comment = doc.getString("comment");
                        String tag = doc.getString("tag");
                        String[] image_refs = (String[])doc.get("imageRefs");

                        Item temp = new Item(name, price, date, description, make,
                                model, serial, comment, tag,image_refs);

                        Log.d("Firestore", String.format("Item(%s, %s, %s) fetched",
                                name, doc.getString("dateAdded"), price));
                        itemList.add(temp);
                    }

                    //This will notify any listeners via ItemListUpdateListener interface
                    context.onItemListUpdate();
                }
            }
        });

    }

    /**
     * This will delete an item from Firestore database
     * @param itemName The name os the item which is to be deleted
     * @param context The context from which this method is called, typically the current Activity.
     */
    public void deleteSelectedItem(String itemName, ItemListActivity context){
        itemsRef.document(itemName).delete() //also delete their images
                //This will be executed if the document deletion is successful.

                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();

                })

                //This will be executed if the document deletion fails.
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Failed to Delete item(s)", Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * itemList getter
     *
     * @return list containing all items in database.
     * This will be updated automatically as the list changes
     */
    public ArrayList<Item> getItemList() {
        return itemList;
    }


    /**
     * Function adds the input item to the database
     *
     * @param item Item to be added to the user's item collection in the database
     */

    public void addItem(Item item){

        itemsRef.document(item.getName()).set(item);
    }

    /**
     * Function deletes the input item to the database
     *
     * @param item Item to be deleted from the user's item collection in the database
     */
    public void deleteItem(Item item) {
        //also delete their images
        itemsRef.document(item.getName()).delete();
    }

    /**
     * Interface for itemList listeners
     */
    public interface ItemListUpdateListener {
        /**
         * This method is called whenever an update to itemList happens
         */
        public void onItemListUpdate();
    }
    public ArrayList<ImageView> getItemImages(Context context, Item item)
    {
        ArrayList<ImageView> image_views = new ArrayList<>();
        for(String s: item.getImageRefs()) {
            StorageReference storageReference = storage.getReference().child("images/"+item.getName()+"/"+s);

            ImageView image_view = (ImageView) LayoutInflater.from(context).inflate(R.layout.item_image, null, false);


            Glide.with(context)
                    .load(storageReference)
                    .into(image_view);
            image_view.setTag(s);
            image_views.add(image_view);
        }
        return image_views;


    }
}
