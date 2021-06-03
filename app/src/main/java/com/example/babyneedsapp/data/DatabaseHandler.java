package com.example.babyneedsapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.babyneedsapp.model.BabyItem;
import com.example.babyneedsapp.util.Util;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private final Context context;

    public DatabaseHandler(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
        this.context = context;
    }

    @Override // create the table
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_BABYITEM_TABLE = "CREATE TABLE " + Util.TABLE_NAME + "(" + Util.KEY_ID + " INTEGER PRIMARY KEY,"
                + Util.KEY_BABY_ITEM + " TEXT,"
                + Util.KEY_QUANTITY + " INTEGER,"
                + Util.KEY_COLOUR + " TEXT,"
                + Util.KEY_SIZE + " INTEGER,"
                + Util.KEY_DATE_NAME + " LONG"
                + ")"; // table created with columns that were created in the util class

        sqLiteDatabase.execSQL(CREATE_BABYITEM_TABLE); // table created here

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Util.TABLE_NAME);


        onCreate(sqLiteDatabase); // create table again
    }

    public void addBabyItem(BabyItem babyItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Util.KEY_BABY_ITEM, babyItem.getBabyItem());
        values.put(Util.KEY_QUANTITY, babyItem.getQuantity());
        values.put(Util.KEY_COLOUR, babyItem.getColour());
        values.put(Util.KEY_SIZE, babyItem.getSize());
        values.put(Util.KEY_DATE_NAME, java.lang.System.currentTimeMillis());


        //Insert to row
        db.insert(Util.TABLE_NAME, null, values);
        Log.d("DBHandler", "addContact: Item Added");



    }

    // get an Item

    public BabyItem getBabyItem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.query(Util.TABLE_NAME,
                new String[]{Util.KEY_ID,
                        Util.KEY_BABY_ITEM,
                        Util.KEY_QUANTITY,
                        Util.KEY_COLOUR,
                        Util.KEY_SIZE,
                        Util.KEY_DATE_NAME},
                Util.KEY_ID + "=?",
                new String[]{
                        String.valueOf(id)}, null, null, null, null);


        if (cursor != null) { // if the item we are tring to get is present in db
            cursor.moveToFirst();

            BabyItem item = new BabyItem();

            item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Util.KEY_ID)))); // we are getting info from db and storing it in a new BabyItem object
            item.setBabyItem(cursor.getString(cursor.getColumnIndex(Util.KEY_BABY_ITEM)));
            item.setQuantity(cursor.getInt(cursor.getColumnIndex(Util.KEY_QUANTITY)));
            item.setColour(cursor.getString(cursor.getColumnIndex(Util.KEY_COLOUR)));
            item.setSize(cursor.getInt(cursor.getColumnIndex(Util.KEY_SIZE)));

            // Convert time-stamp into something readable

            DateFormat dateFormat = DateFormat.getDateInstance();
            String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Util.KEY_DATE_NAME)))
                    .getTime()); // formatted into example (Feb 23, 2020)

            item.setDateItemAdded(formattedDate);

            return item;

        }


        return null;
    }

    // Get All Items

    public List<BabyItem> getAllItems() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<BabyItem> itemList = new ArrayList<>();

        Cursor cursor = db.query(Util.TABLE_NAME,
                new String[]{Util.KEY_ID,
                        Util.KEY_BABY_ITEM,
                        Util.KEY_QUANTITY,
                        Util.KEY_COLOUR,
                        Util.KEY_SIZE,
                        Util.KEY_DATE_NAME},
                null, null, null, null,
                Util.KEY_DATE_NAME + " DESC");

        if (cursor.moveToFirst()) {  // for each object in database loop through and get its colums info and store it in a BabyItem object then store the object in the list

            do {
                BabyItem item = new BabyItem();

                item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Util.KEY_ID)))); // we are getting info from db and storing it in a new BabyItem object
                item.setBabyItem(cursor.getString(cursor.getColumnIndex(Util.KEY_BABY_ITEM)));
                item.setQuantity(cursor.getInt(cursor.getColumnIndex(Util.KEY_QUANTITY)));
                item.setColour(cursor.getString(cursor.getColumnIndex(Util.KEY_COLOUR)));
                item.setSize(cursor.getInt(cursor.getColumnIndex(Util.KEY_SIZE)));

                DateFormat dateFormat = DateFormat.getDateInstance();
                String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Util.KEY_DATE_NAME)))
                        .getTime()); // formatted into example (Feb 23, 2020)

                item.setDateItemAdded(formattedDate);

                itemList.add(item);


            } while (cursor.moveToNext());


        }

        return itemList;
    }

    public int updateItem(BabyItem babyItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();             // put new info into respective columns
        values.put(Util.KEY_BABY_ITEM, babyItem.getBabyItem());
        values.put(Util.KEY_QUANTITY, babyItem.getQuantity());
        values.put(Util.KEY_COLOUR, babyItem.getColour());
        values.put(Util.KEY_SIZE, babyItem.getSize());
        values.put(Util.KEY_DATE_NAME, java.lang.System.currentTimeMillis());

        // update row

        return db.update(Util.TABLE_NAME, values,       // replace the item that is being updated info with the new info above
                Util.KEY_ID + "=?",
                new String[]{String.valueOf(babyItem.getId())});




    }

    public void deleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(Util.TABLE_NAME,
                Util.KEY_ID + "=?",
                new String[]{String.valueOf(id)});

        db.close();
    }

    public int getCount() {
        SQLiteDatabase db = this.getReadableDatabase();

        String countQuery = "SELECT * FROM " + Util.TABLE_NAME;

        Cursor cursor = db.rawQuery(countQuery, null);

        return cursor.getCount();


    }

}
