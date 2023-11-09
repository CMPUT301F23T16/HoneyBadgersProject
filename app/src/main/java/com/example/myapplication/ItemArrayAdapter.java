package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Adapter for items array list. Maps each item's attributes to the screen.
 */
public class ItemArrayAdapter extends RecyclerView.Adapter {
    private ArrayList<Item> items;
    private Context context;


    /**
     * Initializes class members only
     * @param context parent object
     * @param items list of items
     */
    public ItemArrayAdapter(Context context, ArrayList<Item> items) {
        this.items = items;
        this.context = context;
    }

    public void unSelectCheckBox(){
        for (Item i : items){
            i.setSelected(false);
        }
        notifyDataSetChanged();
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

        //Set the TextView text for the item
        itemContent.itemName.setText(item.getName());
        itemContent.itemDateAdded.setText(item.getDateAdded().toString());
        itemContent.itemPrice.setText(item.getPrice().toString());
        itemContent.itemSelected.setChecked(item.isSelected());

        itemContent.itemSelected.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            items.get(itemContent.getAdapterPosition()).setSelected(isChecked);
        }));

//        itemContent.itemView.setOnClickListener(v -> {
//            boolean isSelected = item.isSelected();
//            item.setSelected(!isSelected);
//            itemContent.itemSelected.setChecked(!isSelected);
//        });

    }

    /**
     *
=======
        itemContent.itemDateAdded.setText(item.getDateAdded());
        itemContent.itemPrice.setText(String.format("$%.2f", item.getPrice()));
    }

    /**
     * Counts the number of items in items list
>>>>>>> US-01.01.01
     * @return number of items in items list
     */
    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * This class maps each text view (item attribute) in item_content to its attributes
     */
    public class ItemContentViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView itemDateAdded;
        TextView itemPrice;
        CheckBox itemSelected;


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
            itemSelected = itemView.findViewById(R.id.check_box);

//            itemSelected.setOnCheckedChangeListener((buttonView, isChecked) -> {
//                int adapterPosition = getAdapterPosition();
//                if (adapterPosition != RecyclerView.NO_POSITION){
//                    items.get(adapterPosition).setSelected(isChecked);
//                }
//            });
        }
    }
}

