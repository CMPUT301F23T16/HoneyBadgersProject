package com.example.myapplication;

import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;


import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.time.format.DateTimeFormatter;


/**This class represents the cloud database the app is connecting to.
 * All communications with the database are handled here.
 */
public class DataBase {
    private FirebaseFirestore db;
    private ArrayList<Item> itemList;
    private CollectionReference itemsRef;

    /**
     * Connect to the database and get the user's collection. Setup automatic
     * updates from database
     * @param userName This is the username of the user
     * @param context Parent object
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

                        Item temp = new Item(name, price, date, description, make,
                                model, serial, comment, tag);

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
     * itemList getter
     * @return list containing all items in database.
     * This will be updated automatically as the list changes
     */
    public ArrayList<Item> getItemList() {
        return itemList;
    }


    public void addItem(Item item){
        itemsRef.document(item.getName()).set(item);
    }

    public void deleteItem(Item item){
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
    
}