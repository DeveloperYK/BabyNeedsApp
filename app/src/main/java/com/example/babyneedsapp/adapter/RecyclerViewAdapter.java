package com.example.babyneedsapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babyneedsapp.ActivityList;
import com.example.babyneedsapp.MainActivity;
import com.example.babyneedsapp.R;
import com.example.babyneedsapp.data.DatabaseHandler;
import com.example.babyneedsapp.model.BabyItem;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<BabyItem> items;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater inflater;



    public RecyclerViewAdapter(Context context, List<BabyItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {         // gets tne row we created in list_row.xml we create the view here
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull  ViewHolder holder, int position) {
        BabyItem item = items.get(position);
        holder.BabyItemName.setText("Item: " + item.getBabyItem());
        holder.ItemQuantity.setText("Quantity: " + String.valueOf(item.getQuantity()));
        holder.ItemColour.setText("Colour: "+item.getColour());
        holder.ItemSize.setText("Size: "+ String.valueOf(item.getSize()));
        holder.ItemDate.setText("Date Added: "+item.getDateItemAdded());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        public TextView BabyItemName;
        public TextView ItemQuantity;
        public TextView ItemColour;
        public TextView ItemSize;
        public TextView ItemDate;
        public int id; // used to pass a select object to the next activity
        public Button editButton;
        public Button deleteButton;


        public ViewHolder(@NonNull View itemView, Context ctx) {

            super(itemView);

            context = ctx;

            itemView.setOnClickListener(this);

            BabyItemName = itemView.findViewById(R.id.item_name);
            ItemQuantity = itemView.findViewById(R.id.item_quantity);
            ItemColour = itemView.findViewById(R.id.item_colour);
            ItemSize = itemView.findViewById(R.id.item_size);
            ItemDate = itemView.findViewById(R.id.item_date);

            editButton = itemView.findViewById(R.id.editButton);

            deleteButton = itemView.findViewById(R.id.deleteButton);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();    // the item which was selected from the view
            BabyItem item = items.get(position);

            switch (view.getId()) {
                case R.id.editButton:

                    editItem(item);
                    break;

                case R.id.deleteButton:
                    
                    deleteItem(item.getId());
                    break;
            }

        }

        private void editItem(final BabyItem item) { // pass in the item that is being edited


            builder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.popup, null); // popup screen will be used to edit values of an existing item

             Button saveButton;
             final EditText babyItem;
             final EditText itemQuantity;
             final EditText itemColour;
             final EditText itemSize;
             TextView title;

            babyItem = view.findViewById(R.id.babyItem);
            itemQuantity = view.findViewById(R.id.itemQuantity);
            itemColour = view.findViewById(R.id.itemColor);
            itemSize = view.findViewById(R.id.itemSize);
            saveButton = view.findViewById(R.id.saveButton);
            saveButton.setText(R.string.Update_Text);
            title = view.findViewById(R.id.title);



            title.setText(R.string.Edit_Text);
            babyItem.setText(item.getBabyItem());
            itemQuantity.setText(String.valueOf(item.getQuantity()));
            itemColour.setText(item.getColour());
            itemSize.setText(String.valueOf(item.getSize()));


            builder.setView(view);
            dialog = builder.create();
            dialog.show();

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // update our item
                    Log.d("Click", "onClick: Success");
                    DatabaseHandler db = new DatabaseHandler(context);
                    item.setBabyItem(babyItem.getText().toString());
                    item.setQuantity(Integer.parseInt(itemQuantity.getText().toString()));
                    item.setColour((itemColour.getText().toString()));
                    item.setSize(Integer.parseInt(itemSize.getText().toString()));

                    if(!babyItem.getText().toString().isEmpty()
                            && !itemQuantity.getText().toString().isEmpty()
                            && !itemColour.getText().toString().isEmpty()
                            && !itemSize.getText().toString().isEmpty()) {

                        db.updateItem(item);

                        notifyItemChanged(getAdapterPosition(), item); // important so update occurs straight away, so users do not have to refresh app to see change
                        Log.d("Click", "onClick: Success");

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // code to run when popup dialog needs to be removed
                                dialog.dismiss();// dismiss popup activity

                                // move to next screen that shows items in db in a "list" format

                                context.startActivity(new Intent(context, ActivityList.class));

                            }
                        }, 1200);
                    }
                    else {
                        Snackbar.make(view, "Fields are Empty", Snackbar.LENGTH_SHORT)
                        .show();
                    }





                }
            });

        }

        private void deleteItem(final int id) {

            builder = new AlertDialog.Builder(context); // we will inflate our confirmation screen so the user can confirm their choice before object is deleted

            inflater = LayoutInflater.from(context);

            View view = inflater.inflate(R.layout.confirmation_pop, null);

            Button yesButton = view.findViewById(R.id.conf_yes_button); // yes and no button to confirm/deny if item will be deleted
            Button noButton = view.findViewById(R.id.conf_no_button);

            builder.setView(view);  // show confirmation popup screen
            dialog = builder.create();
            dialog.show();

            yesButton.setOnClickListener(new View.OnClickListener() {   // user confirms they want to delete item
                @Override
                public void onClick(View view) {


                    DatabaseHandler db = new DatabaseHandler(context);
                    db.deleteItem(id); // item deleted from db


                    items.remove(getAdapterPosition());   // we have to delete the card that the item is shown on to the user
                    notifyItemRemoved(getAdapterPosition());

                    dialog.dismiss();

                }
            });

            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss(); // user does not want to delete item, so confirmation screen is dismissed
                }
            });


        }
    }


}
