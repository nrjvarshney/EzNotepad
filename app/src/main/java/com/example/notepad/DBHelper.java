package com.example.notepad;

/**
 * Created by neeraj.varshney on 6/13/2016.
 */

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "notepad";
    private static final String TABLE_CONTACTS = "mytable";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PHOTO = "photo";
    private static final String KEY_CONTENTS = "contents";
    private static final String KEY_GALLERY_IMAGE = "gallery_image";
    private static final String KEY_CAPTURED_IMAGE = "captured_image";
    private static final String KEY_AUDIOS = "audios";
    private static final String KEY_DELETE_VAR = "delete_var";
    private static final String KEY_RRECYCLE_DELETE = "recycle_delete";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_AUDIOS + " TEXT,"
                + KEY_CAPTURED_IMAGE + " TEXT,"
                + KEY_CONTENTS + " TEXT,"
                + KEY_DELETE_VAR + " INTEGER,"
                + KEY_GALLERY_IMAGE + " TEXT,"
                + KEY_RRECYCLE_DELETE + " INTEGER,"
                + KEY_PHOTO + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
        Log.e("query", CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        // Create tables again
        onCreate(db);
    }

    // code to add the new contact
    void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.get_name()); // Contact Name
        values.put(KEY_AUDIOS, contact.get_audios());
        values.put(KEY_CONTENTS, contact.get_contents());
        values.put(KEY_CAPTURED_IMAGE, contact.get_captured_image());
        values.put(KEY_DELETE_VAR, contact.get_delelte_var());
        values.put(KEY_GALLERY_IMAGE, contact.get_gallery_image());
        values.put(KEY_PHOTO, contact.get_photo());
        values.put(KEY_RRECYCLE_DELETE, contact.get_recycle_delete());

        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    // code to get the single contact
    Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[]{KEY_ID,
                        KEY_NAME, KEY_AUDIOS, KEY_CAPTURED_IMAGE, KEY_CONTENTS, KEY_DELETE_VAR, KEY_GALLERY_IMAGE, KEY_RRECYCLE_DELETE, KEY_PHOTO}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Contact contact = new Contact(cursor.getInt(0),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getString(6), cursor.getInt(7), cursor.getString(8));
        // return contact
        return contact;
    }

    // code to get all contacts in a list view
    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.set_id((cursor.getInt(0)));
                contact.set_name(cursor.getString(1));
                contact.set_audios(cursor.getString(2));
                contact.set_captured_image(cursor.getString(3));
                contact.set_contents(cursor.getString(4));
                contact.set_delelte_var(cursor.getInt(5));
                contact.set_gallery_image(cursor.getString(6));
                contact.set_recycle_delete(cursor.getInt(7));
                contact.set_photo(cursor.getString(8));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    public void showAll() {
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Log.e("set_id", "" + cursor.getInt(0));
                Log.e("set_name", "" + cursor.getString(1));
                Log.e("set_audios", "" + cursor.getString(2));
                Log.e("set_captured_image", "" + cursor.getString(3));
                Log.e("set_contents", "" + cursor.getString(4));
                Log.e("set_delelte_var", "" + cursor.getString(5));
                Log.e("set_contents", "" + cursor.getString(6));
                Log.e("set_gallery_image", "" + cursor.getString(7));
                Log.e("set_recycle_delete", "" + cursor.getString(8));
                Log.e("set_photo", "" + cursor.getString(9));


            } while (cursor.moveToNext());
        }

    }

    // code to update the single contact
    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_NAME, contact.get_name()); // Contact Name
        values.put(KEY_AUDIOS, contact.get_audios());
        values.put(KEY_CONTENTS, contact.get_contents());
        values.put(KEY_CAPTURED_IMAGE, contact.get_captured_image());
        values.put(KEY_DELETE_VAR, contact.get_delelte_var());
        values.put(KEY_GALLERY_IMAGE, contact.get_gallery_image());
        values.put(KEY_PHOTO, contact.get_photo());
        values.put(KEY_RRECYCLE_DELETE, contact.get_recycle_delete());

        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(contact.get_id())});
    }

    // Deleting single contact
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[]{String.valueOf(contact.get_id())});
        db.close();
    }

    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}