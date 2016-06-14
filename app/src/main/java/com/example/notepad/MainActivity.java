package com.example.notepad;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private String names[] = {"Add a Note",
            "HTML",
            "CSS",
            "Java Script",
            "Wordpress"
    };


//      private String []names = new String [20];
//

    private String desc[] = {"Easy way to store data",
            "The Powerful Hypter Text Markup Language 5",
            "Cascading Style Sheets",
            "Code with Java Script",
            "Manage your content with Wordpress"
    };


    private Integer imageid[] = {R.drawable.addt1,
            R.drawable.html,
            R.drawable.css,
            R.drawable.js,
            R.drawable.wp
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        for(int i=0;i<5;i++)
//        {
//            names[i]="asdfsaf";
//        }

        CustomList customList = new CustomList(this, names, desc, imageid);
//        names[5]="error";
//        desc[5]="description goes here";
//        imageid[5]=R.drawable.html;
//
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(customList);
        //listView.

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(),"You Clicked "+names[i],Toast.LENGTH_SHORT).show();
                if(names[i].equals("Add a Note"))
                {
                    Intent intd=new Intent(MainActivity.this,AddNote.class);
                    startActivity(intd);
                }
            }
        });


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