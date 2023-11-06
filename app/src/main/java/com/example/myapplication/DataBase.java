package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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
    public DataBase(String userName, MainActivity context) {
        this.itemList = new ArrayList<>();

        this.db = FirebaseFirestore.getInstance();
        this.itemsRef = db.collection(userName); // Each user has their own collection

        itemsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
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

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

                        String name = doc.getId();
                        LocalDate date = LocalDate.parse(doc.getString("dateAdded"), formatter);
                        Double price = doc.getDouble("price");

                        Log.d("Firestore", String.format("Item(%s, %s, %s) fetched",
                                name, doc.getString("dateAdded"), price));
                        itemList.add(new Item(name, price, date));
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
