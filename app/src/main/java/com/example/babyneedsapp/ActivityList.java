package com.example.babyneedsapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.babyneedsapp.adapter.RecyclerViewAdapter;
import com.example.babyneedsapp.data.DatabaseHandler;
import com.example.babyneedsapp.model.BabyItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ActivityList extends AppCompatActivity {   // in this class we show all items stored in db using recyclerView


    private RecyclerViewAdapter recyclerViewAdapter;
    private List<BabyItem> itemList;
    private DatabaseHandler db;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    private Button saveButton;
    private EditText babyItem;
    private EditText itemQuantity;
    private EditText itemColour;
    private EditText itemSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        recyclerView = findViewById(R.id.recyclerView);

        floatingActionButton = findViewById(R.id.fab);

        db = new DatabaseHandler(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemList = new ArrayList<>();

        // get items from database

        itemList = db.getAllItems();

        for(BabyItem item: itemList) {
            Log.d("RetrievingDBData", "onCreate: " + item.getBabyItem());

        }
        recyclerViewAdapter = new RecyclerViewAdapter(this,itemList);
        recyclerView.setAdapter(recyclerViewAdapter); // here the recycleView is actually being created and the ActivityList screen is populated

        recyclerViewAdapter.notifyDataSetChanged(); // updates the recycleView if data is modified

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopupDialog();
                Log.d("AddItem", "onClick: success");
            }


        });
    }

    private void createPopupDialog() {  // here we inflate our popup dialog if the user wishes to add another item

        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);

        babyItem = view.findViewById(R.id.babyItem);
        itemQuantity = view.findViewById(R.id.itemQuantity);
        itemColour = view.findViewById(R.id.itemColor);
        itemSize = view.findViewById(R.id.itemSize);
        saveButton = view.findViewById(R.id.saveButton);


        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!babyItem.getText().toString().isEmpty()
                        && !itemQuantity.getText().toString().isEmpty()
                        && !itemColour.getText().toString().isEmpty()
                        && !itemSize.getText().toString().isEmpty()) {
                    SaveItem(view);

                }

                else {
                    Snackbar.make(view, "Empty Fields Not Allowed", Snackbar.LENGTH_SHORT)
                            .show();
                }

            }
        });



    }

    private void SaveItem(View view) {  // save information inputted into popup activity into object and store object in the db
        BabyItem item = new BabyItem();

        String newItem = babyItem.getText().toString();
        int newQuantity = Integer.parseInt(itemQuantity.getText().toString());
        String newColour = itemColour.getText().toString();
        int newSize = Integer.parseInt(itemSize.getText().toString());

        item.setBabyItem(newItem);
        item.setQuantity(newQuantity);
        item.setColour(newColour);
        item.setSize(newSize);



        db.addBabyItem(item);

        Snackbar.make(view, "Item Saved", Snackbar.LENGTH_SHORT)
                .show();

        Log.d("ItemSaved", "SaveItem: " + newItem + " " + newQuantity + " " + newColour + " " + newSize);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // code to run when popup dialog needs to be removed
                alertDialog.dismiss();// dismiss popup activity

                // move to next screen that shows items in db in a "list" format

                startActivity(new Intent(ActivityList.this, ActivityList.class));
                finish();

            }
        }, 1200);

    }

}