package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
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

        this.storage = FirebaseStorage.getInstance();
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
                        List<String> image_refs = (List<String>) doc.get("imageRefs");

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
     * This will delete an item from Firestore database + its images
     * @param item The item which is to be deleted
     * @param context The context from which this method is called, typically the current Activity.
     */
    public void deleteSelectedItem(Item item, ItemListActivity context){
        List<String> image_reference = item.getImageRefs();
        itemsRef.document(item.getName()).delete() //also delete their images
                //This will be executed if the document deletion is successful.

                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();
                    Log.d("Deleteing Images", "deleteSelectedItem: ");
                    if(image_reference==null)
                        return;
                    StorageReference storageReference = storage.getReference().child("images/"+item.getName());
                    for(String s:image_reference)
                    {
                        storageReference.child(s).delete();
                    }
                    //storageReference.delete();
                    Log.d("Image Deleted", "deleteSelectedItem: ");

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
     * @param photos Imageviews to be stored to firestore storage as images
     */

    public void addItem(Item item,ArrayList<ImageView> photos){

        itemsRef.document(item.getName()).set(item);
        StorageReference storageReference ;
        if(photos==null)
            return;
        for(ImageView imageView:photos)
        {
            storageReference = storage.getReference().child("images/"+item.getName()+"/"+(String)imageView.getTag());
            // Get the data from an ImageView as bytes
            imageView.setDrawingCacheEnabled(true);
            imageView.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 30, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = storageReference.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception exception) {
                    // Handle unsuccessful uploads
                    Log.d((String)imageView.getTag(), "onFailure: upload Failed");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    Log.d((String)imageView.getTag(), "onSuccess: upload Success");
                    // ...
                }
            });
        }

    }

    /**
     * Function deletes the input item to the database + its images
     *
     * @param item Item to be deleted from the user's item collection in the database
     */
    public void deleteItem(Item item) {
        //also delete their images
        List<String> image_reference = item.getImageRefs();
        itemsRef.document(item.getName()).delete();
        Log.d("Deleteing Images", "deleteSelectedItem: ");
        if(image_reference==null)
            return;
        StorageReference storageReference = storage.getReference().child("images/"+item.getName());
        for(String s:image_reference)
        {
            storageReference.child(s).delete();
        }
        //storageReference.delete();
        Log.d("Image Deleted", "deleteSelectedItem: ");
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

    /**
     * Returns an ImageView list corresponding to the Item images stored in firestore storage
     *
     * @param context Parent object
     * @param item Item for which ImageViews are needed
     * @return  an ImageView list for images of that Item
     */
    public ArrayList<ImageView> getItemImages(Context context, Item item)
    {
        ArrayList<ImageView> image_views = new ArrayList<>();
        List<String> image_references = item.getImageRefs();
        if(image_references == null)
            return new ArrayList<ImageView>();
        for(String s: image_references) {
            StorageReference storageReference = storage.getReference().child("images/"+item.getName()+"/"+s);
            Log.d("images/"+item.getName()+"/"+s, "getItemImages: ");

            ImageView image_view = (ImageView) LayoutInflater.from(context).inflate(R.layout.item_image, null, false);


            final long ONE_MEGABYTE = 1024 * 1024;
            storageReference.getBytes(20*ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    // Data for "images/island.jpg" is returns, use this as needed
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    image_view.setImageBitmap(Bitmap.createScaledBitmap(bmp, 1000, 1400, false));
                    image_view.setBackgroundResource(0);
                    Log.d("Image Loaded", "onSuccess: ");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception exception) {
                    // Handle any errors
                    image_view.setBackgroundResource(R.drawable.ic_action_name);

                    Log.d("IMAGE NOT LOADED", "onFailure: ");
                }
            });
            image_view.setBackgroundResource(R.drawable.ic_action_downloading);
            image_view.setTag(s);
            image_views.add(image_view);

        }
        Log.d("DONE", "getItemImages: ");
        return image_views;


    }

    /**
     * Delete a photograph from the firestore storage
     * @param item Item for which image is to be deleted
     * @param imageView ImageView corresponding to the Image to be deleted
     */
    public void deletePhoto(Item item,ImageView imageView)
    {
        storage.getReference().child("images/"+item.getName()+"/"+(String)imageView.getTag()).delete();
    }
}
