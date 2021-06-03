package com.example.babyneedsapp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;

import com.example.babyneedsapp.data.DatabaseHandler;
import com.example.babyneedsapp.model.BabyItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private Button saveButton;
    private EditText babyItem;
    private EditText itemQuantity;
    private EditText itemColour;
    private EditText itemSize;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        db = new DatabaseHandler(MainActivity.this);

        byPassActivity();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                createPopupDialog();

            }
        });
    }

    private void byPassActivity() {
        if (db.getCount() > 0) {
            startActivity(new Intent(MainActivity.this, ActivityList.class));
            finish();

        }

    }

    private void createPopupDialog() {
        final DatabaseHandler db = new DatabaseHandler(MainActivity.this);
        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null); // inflate the popup screen

        babyItem = view.findViewById(R.id.babyItem);
        itemQuantity = view.findViewById(R.id.itemQuantity);
        itemColour = view.findViewById(R.id.itemColor);
        itemSize = view.findViewById(R.id.itemSize);
        saveButton = view.findViewById(R.id.saveButton);



        builder.setView(view);

        dialog = builder.create(); // creating our dialog option which shows the message to use
        dialog.show();

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
                dialog.dismiss();// dismiss popup activity

                // move to next screen that shows items in db in a "list" format

                startActivity(new Intent(MainActivity.this, ActivityList.class));

            }
        }, 1200);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}