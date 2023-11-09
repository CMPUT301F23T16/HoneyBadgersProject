package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Adapter for items array list. Maps each item's attributes to the screen.
 */
public class ItemArrayAdapter extends RecyclerView.Adapter {

    /*
        See:
        https://stackoverflow.com/questions/49969278/recyclerview-item-click-listener-the-right-way
        - Easiest way to make onClick listener functionality for recycler view
     */
    public interface OnItemClickListener {
        void onItemClick(Item item, int position);
    }
    private ArrayList<Item> items;
    private Context context;
    private final OnItemClickListener listener;


    /**
     * Initializes class members only
     * @param context parent object
     * @param items list of items
     */
    public ItemArrayAdapter(Context context, ArrayList<Item> items, OnItemClickListener listener) {
        this.items = items;
        this.context = context;
        this.listener = listener;
    }

    /**
     * Inflates item_content
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return item_content view holder
     */
    @NonNull
    @Override
    public ItemContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_content, parent, false);
        return new ItemContentViewHolder(view);
    }

    /**
     * Populates the details of each item in the textViews
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Item item = items.get(position);
        ItemContentViewHolder itemContent = (ItemContentViewHolder) holder;

        // This calls the bind function to populate the values of each View of the Item
        itemContent.bind(items.get(position), position, listener);
    }

    /**
     * Counts the number of items in items list
     * @return number of items in items list
     */
    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * This class maps each text view (item attribute) in item_content to its attributes
     */
    public class ItemContentViewHolder extends RecyclerView.ViewHolder{
        TextView itemName;
        TextView itemDateAdded;
        TextView itemPrice;


        /**
         * Initializes all text views in class
         * @param itemView view holding all the items list screen item attributes
         */
        public ItemContentViewHolder(View itemView) {
            super(itemView);
            //Populate the textView variables with the view
            itemName = itemView.findViewById(R.id.item).findViewById(R.id.item_name);
            itemDateAdded = itemView.findViewById(R.id.item).findViewById(R.id.item_date);
            itemPrice = itemView.findViewById(R.id.item).findViewById(R.id.item_price);

        }

        /**
         * Populates all fields in the item's content layout with proper values
         * @param item Item object that will be used to populate text views for the current item
         * @param position Index of the item Object in the datalist/ArrayAdapter
         * @param listener listener object that will provide onClick functionality to each
         *                 item in recyler view
         */
        public void bind(final Item item, final int position, final OnItemClickListener listener) {
            //Set the TextView text for the item
            itemName.setText(item.getName());

            itemDateAdded.setText(item.getDateAdded());

            itemPrice.setText(String.format("$%.2f", item.getPrice()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item, position);
                }
            });
        }
    }

}
