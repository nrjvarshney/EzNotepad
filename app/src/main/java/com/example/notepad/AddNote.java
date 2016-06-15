package com.example.notepad;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.BoolRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

public class AddNote extends AppCompatActivity {

    Button add_from_gallery;
    Button captured;
    Button audio_captured;
    Button saves;
    Button audiorecord;
    ImageView im, capture_view, audio_view;
    Boolean galleryimageflag = false;
    Boolean captureimageflag = false;
    Boolean audioflag = false;
    Boolean recordaudioflag = false;
    private static final int SELECTED_PICTURE = 2;
    private static final int TAKE_IMAGE = 1;
    private static final int SELECTED_AUDIO = 3;
    EditText title, contents;
    String filepath = null;
    String selectedImagePath = null;
    Uri uriSound = null;
    String soundfile = null;
    Button play, stop, record;
    private MediaRecorder myAudioRecorder;
    private String outputFile = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        // code for database
        final DBHelper db = new DBHelper(this);

        //code for database ends

        saves = (Button) findViewById(R.id.savebutton);
        saves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                title = (EditText) findViewById(R.id.titles);
                contents = (EditText) findViewById(R.id.Contents);
                db.addContact(new Contact(title.getText().toString(), filepath, contents.getText().toString(), filepath, selectedImagePath, soundfile));
                List<Contact> contacts = db.getAllContacts();
                for (Contact cn : contacts) {
                    String log = "Id: " + cn.get_id() + " ,Name: " + cn.get_name() + " ,AUDIOS: " + cn.get_audios() + " ,CAPTURED_IMAGE: " + cn.get_captured_image() + " ,GALLERY: " + cn.get_gallery_image() + " ,PHOTO: " + cn.get_photo() + "\n";
                    Log.e("hhh", log);
                }
                Intent intent = new Intent(v.getContext(), SeeNote.class);
                // send all the contents through intent to SEeNote.java
                //build a good UI for displaying the note
                //
                intent.putExtra("name", title.getText().toString());
                //  intent.putExtra("");
                startActivity(intent);


            }
        });

        add_from_gallery = (Button) findViewById(R.id.add_from_gallery);
        // final LinearLayout mViews = (LinearLayout) findViewById(R.id.moreViews);
        add_from_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Button b1 = new Button(AddNote.this);
                //b1.setText("add another");
                //b1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                //mViews.addView(b1);
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, SELECTED_PICTURE);


            }
        });
        captured = (Button) findViewById(R.id.capture_add);
        captured.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, TAKE_IMAGE);

            }
        });
        audio_captured = (Button) findViewById(R.id.audio_add);
        audio_captured.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, SELECTED_AUDIO);
            }
        });
// for recording audio
        final ImageView audiorecorder = (ImageView) findViewById(R.id.audiorecordselection);

        stop = (Button) findViewById(R.id.stopbutton);
        play = (Button) findViewById(R.id.playbutton);
        stop.setEnabled(false);
        play.setEnabled(false);
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";

        Log.e("file", outputFile);
        record = (Button) findViewById(R.id.recordbutton);
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    myAudioRecorder.prepare();
                    myAudioRecorder.start();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


                record.setEnabled(false);
                stop.setEnabled(true);

                Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    myAudioRecorder.stop();
                    myAudioRecorder.release();
                    myAudioRecorder = null;
                } catch (Exception e) {
                    //Log.e("error here",e.getMessage());
                }
                stop.setEnabled(false);
                play.setEnabled(true);
                audiorecorder.setImageResource(R.drawable.sound);
                ImageView cancel_record = (ImageView) findViewById(R.id.cancel4);
                cancel_record.setImageResource(R.drawable.cancel);
                cancel_record.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        recordaudioflag = false;
                        Bitmap b = null;
                        audiorecorder.setImageBitmap(b);

                    }
                });
                Toast.makeText(getApplicationContext(), "Audio recorded successfully", Toast.LENGTH_LONG).show();
            }
        });
        //play is not working presently because of the stop button
        // this comment is for hit
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) throws IllegalArgumentException, SecurityException, IllegalStateException {
                MediaPlayer m = new MediaPlayer();

                try {
                    m.setDataSource(outputFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    m.prepare();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                m.start();
                Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_LONG).show();
            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        // taking imag
        if (requestCode == TAKE_IMAGE && resultCode == RESULT_OK) {
            Bitmap bp = (Bitmap) data.getExtras().get("data");
            Drawable d = new BitmapDrawable(getResources(), bp);
            capture_view = (ImageView) findViewById(R.id.captureselection);
            capture_view.setImageBitmap(bp);
            captureimageflag = true;
            //
            Uri selectedImageUri = data.getData();
            selectedImagePath = getRealPathFromURI(selectedImageUri);
            //
            ImageView cancel_capture = (ImageView) findViewById(R.id.cancel2);
            cancel_capture.setImageResource(R.drawable.cancel);
            cancel_capture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    captureimageflag = false;
                    Bitmap b = null;
                    capture_view.setImageBitmap(b);

                }
            });
        }

        if (requestCode == TAKE_IMAGE && resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(), "You have not captured image.", Toast.LENGTH_SHORT).show();

        }
        if (requestCode == SELECTED_AUDIO && resultCode == RESULT_OK) {
            uriSound = data.getData();
            soundfile = uriSound.toString();
            // EditText song=(EditText)findViewById(R.id.song);
            // song.setText(uriSound.toString());
            //play(this, uriSound);
            audioflag = true;
            audio_view = (ImageView) findViewById(R.id.audioselection);
            audio_view.setImageResource(R.drawable.sound);
            ImageView cancel_audio = (ImageView) findViewById(R.id.cancel3);
            cancel_audio.setImageResource(R.drawable.cancel);
            cancel_audio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    audioflag = false;
                    Bitmap b = null;
                    audio_view.setImageBitmap(b);


                }
            });


        }

        if (requestCode == SELECTED_AUDIO && resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(), "You have not selected audio.", Toast.LENGTH_SHORT).show();

        }
        if (requestCode == SELECTED_PICTURE && resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(), "You have not selected image.", Toast.LENGTH_SHORT).show();

        }
        if (requestCode == SELECTED_PICTURE && resultCode == RESULT_OK) {
            galleryimageflag = true;
            Uri uri = data.getData();
            String projection[] = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(projection[0]);
            filepath = cursor.getString(columnIndex);
            cursor.close();

            im = (ImageView) findViewById(R.id.galleryselection);
            im.setImageResource(R.drawable.imaget);
            Bitmap yourSelectedImage = null;
            yourSelectedImage = BitmapFactory.decodeFile(filepath);
            im.setImageBitmap(yourSelectedImage);

//            Drawable d = new BitmapDrawable(yourSelectedImage);
            Toast.makeText(getApplicationContext(), "image selected", Toast.LENGTH_LONG).show();

            final ImageView cancel1 = (ImageView) findViewById(R.id.cancel1);
            cancel1.setImageResource(R.drawable.cancel);
            cancel1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    galleryimageflag = false;
                    //                   cancel1.setBackgroundColor(Color.WHITE);
                    Bitmap b = null;
                    im.setImageBitmap(b);


                }
            });

        }


    }

    private void play(Context context, Uri uri) {

        try {
            MediaPlayer mp = new MediaPlayer();
            mp.setDataSource(context, uri);
            mp.prepare();

            mp.start();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return contentUri.getPath();
        }
    }


}
