package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TagFragment extends DialogFragment {

    private TagSelectionListener tagSelectionListener;
    private Spinner tagSpinner;
    private EditText newTagEditText;
    private Button createTagButton;
    private List<String> tagsList;

    public void setTagSelectionListener(TagSelectionListener listener){
        this.tagSelectionListener = listener;
    }


    public interface TagSelectionListener {
        void onTagSelected(String tag);
        void onTagListUpdated(List<String> updatedTagList);
    }


    public void onAttach(@NonNull Context context) {

        super.onAttach(context);
        if(context instanceof TagSelectionListener){
            tagSelectionListener = (TagSelectionListener) context;
        }
        else{
            throw new RuntimeException(context.toString()+"TagSelectionListener is not implemented");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState){
        View dialogview = LayoutInflater.from(getActivity()).inflate(R.layout.tag_fragment,null);

        tagSpinner = dialogview.findViewById(R.id.add_item_tag_spinner);
        newTagEditText = dialogview.findViewById(R.id.tag_keyword);
        createTagButton = dialogview.findViewById(R.id.add_tag_button);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        tagsList = new ArrayList<>();
        populateDefaultTags();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, tagsList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        tagSpinner.setAdapter(dataAdapter);

        createTagButton.setOnClickListener(v -> {
            String newTag = newTagEditText.getText().toString().trim();
            if(!newTag.isEmpty()){
                tagsList.add(newTag);
                dataAdapter.notifyDataSetChanged();
                tagSpinner.setSelection(dataAdapter.getPosition(newTag));
                if(tagSelectionListener != null){
                    tagSelectionListener.onTagSelected(newTag);
                }

                newTagEditText.setText("");
            }
        });

        builder.setView(dialogview)
                .setTitle("Add Tag(s)")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectedTag = tagSpinner.getSelectedItem().toString();
                        tagSelectionListener.onTagSelected(selectedTag);
                        tagSelectionListener.onTagListUpdated(tagsList);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TagFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    private void populateDefaultTags(){
        tagsList.add("Bedroom");
        tagsList.add("Kitchen");
        tagsList.add("Bathroom");
        tagsList.add("Entertainment");
    }

}
