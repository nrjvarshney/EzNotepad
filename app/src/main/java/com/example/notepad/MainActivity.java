package com.example.notepad;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class MainActivity extends AppCompatActivity {

    final DBHelper db = new DBHelper(this);
    private ListView listView;
    private Vector<String> name = new Vector<>();

    private String[] names = new String[20];
    private ArrayList<ModelNotepad> listNotepad = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listNotepad.add(new ModelNotepad(0,"add a note", "add a note ", R.drawable.addt1));

        List<Contact> contacts = db.getAllContacts();
        for (Contact cn : contacts) {
            String contentss="";
            if(cn.get_contents().length()>=15)
                 contentss=cn.get_contents().substring(0,15)+"...";
            else
                contentss=cn.get_contents();

            if(cn.get_delelte_var()==0) {
            int photoid=0;
                switch(cn.get_photo()){
                    case "R.drawable.css":photoid=R.drawable.css;break;
                    case "R.drawable.js":photoid=R.drawable.js;break;
                    case "R.drawable.html":photoid=R.drawable.html;break;
                    case "R.drawable.wp":photoid=R.drawable.wp;break;

                }
                listNotepad.add(new ModelNotepad(cn.get_id(), cn.get_name(), contentss, photoid));


            }

        }
listNotepad.add(new ModelNotepad(-1,"delete all notes","be careful",R.drawable.delete));


        CustomList customList = new CustomList(this, listNotepad);

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(customList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(getApplicationContext(), "You Clicked " + listNotepad.get(position).getName(), Toast.LENGTH_SHORT).show();
                if (position == 0) {
                    Intent intd = new Intent(MainActivity.this, AddNote.class);
                    startActivity(intd);
                }
                else if(listNotepad.get(position).getId()==-1){
                   // db.deleteAll();
                    alertMessage();
                }
                else
                {
                    Intent intd = new Intent(MainActivity.this, SeeNote.class);
                    intd.putExtra("id",""+listNotepad.get(position).getId());
                    startActivity(intd);
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_HOME);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
  Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
        startActivity(intent);
        finish();
        System.exit(0);
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
    public void alertMessage() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(
                    DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        db.deleteAll();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE: // No button clicked // do nothing
                        // Toast.makeText(AlertDialogActivity.this, "No Clicked", Toast.LENGTH_LONG).show();
                         break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?") .setPositiveButton("Yes", dialogClickListener) .setNegativeButton("No", dialogClickListener).show(); }


}