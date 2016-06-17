package com.example.notepad;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class SeeNote extends AppCompatActivity {


    ImageView gallery_display;
    ImageView captured_display;
    Button play_song, pause_song;
    Button deletes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.layout_seenote);

//        LinearLayout linearLayoutSeeNote=(LinearLayout)findViewById(R.id.linearLayoutSeeNote);

        Intent intent = getIntent();
        String ids = intent.getStringExtra("id");
       final int id = Integer.parseInt(ids);
        final DBHelper db = new DBHelper(this);
        final Contact cn = db.getContact(id);

        ScrollView sc = new ScrollView(this);
        sc.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        LinearLayout linearLayoutSeeNote = new LinearLayout(this);

        linearLayoutSeeNote.setLayoutParams(new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.MATCH_PARENT));
        linearLayoutSeeNote.setOrientation(LinearLayout.VERTICAL);

        TextView id_display = new TextView(this);
        id_display.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        id_display.setText("\nITEM ID : "+ids+"\n");
        id_display.setTextSize(20);
        id_display.setBackgroundResource(R.drawable.text_back);

        TextView title_display = new TextView(this);
        title_display.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        title_display.setText("\nTITLE : "+cn.get_name()+"\n");
        title_display.setTextSize(20);
        title_display.setBackgroundResource(R.drawable.text_back);
        TextView content_display = new TextView(this);
        content_display.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        content_display.setText("\nCONTENT :"+"\n"+cn.get_contents()+"\n");
        content_display.setTextSize(20);
        content_display.setBackgroundResource(R.drawable.text_back);


        id_display.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        title_display.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        content_display.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        linearLayoutSeeNote.addView(id_display);
        linearLayoutSeeNote.addView(title_display);
        linearLayoutSeeNote.addView(content_display);

        gallery_display = new ImageView(this);
        gallery_display.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        captured_display = new ImageView(this);
        captured_display.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        play_song=new Button(this);
        play_song.setText("PLAY");
        play_song.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        pause_song=new Button(this);
        pause_song.setText("PAUSE");
        pause_song.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        play_song.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        pause_song.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        if (cn.get_gallery_image() != null) {
            Bitmap yourSelectedImage = null;
            yourSelectedImage = BitmapFactory.decodeFile(cn.get_gallery_image());
//            gallery_display.setImageBitmap(yourSelectedImage);
//            gallery_display.setMaxHeight(50);
//            gallery_display.setMaxWidth(50);
            gallery_display.setImageBitmap(getResizedBitmap(yourSelectedImage,1500,1500));
            linearLayoutSeeNote.addView(gallery_display);
        }

        if (cn.get_captured_image() != null) {
            Bitmap yourSelectedImages = null;
            yourSelectedImages = BitmapFactory.decodeFile(cn.get_captured_image());
            captured_display.setImageBitmap(yourSelectedImages);
            captured_display.setImageBitmap(getResizedBitmap(yourSelectedImages,1500,1500));
            linearLayoutSeeNote.addView(captured_display);
        }

        if (cn.get_audios() != null) {
            final MediaPlayer mPlayer = MediaPlayer.create(this, Uri.parse(cn.get_audios() ));


            linearLayoutSeeNote.addView(play_song);
            linearLayoutSeeNote.addView(pause_song);
            play_song.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   try {

                       mPlayer.start();

                   }
                   catch(Exception e)
                   {}
                }
            });
            pause_song.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mPlayer!=null && mPlayer.isPlaying()){
                        mPlayer.pause();
                    }
                }
            });
        }
        deletes=new Button(this);
        deletes.setText("DELETE");
        deletes.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        deletes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               db.updateContact(new Contact(id,cn.get_name(),cn.get_audios(),cn.get_captured_image(),cn.get_contents(),1,cn.get_gallery_image(),cn.get_recycle_delete(),cn.get_photo()));
                Toast.makeText(getApplicationContext(), "You deleted " + id, Toast.LENGTH_SHORT).show();
                Intent intd = new Intent(SeeNote.this, MainActivity.class);
                startActivity(intd);
            }
        });
        linearLayoutSeeNote.addView(deletes);
        sc.addView(linearLayoutSeeNote);
        setContentView(sc);
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }
    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_HOME);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
