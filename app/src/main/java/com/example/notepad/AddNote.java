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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    Button stop, record;
    private MediaRecorder myAudioRecorder;
    private String outputFile = null;
    ImageView ims;
    private RadioGroup radioGroup;
    private RadioButton radioButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        final DBHelper db = new DBHelper(this);

        saves = (Button) findViewById(R.id.savebutton);
        saves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = (EditText) findViewById(R.id.titles);
                contents = (EditText) findViewById(R.id.Contents);
                radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
                radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());

                String selectedId = radioButton.getText().toString();
                String pho = "R.drawable." + selectedId;
                db.addContact(new Contact(title.getText().toString(), pho, contents.getText().toString(), filepath, selectedImagePath, soundfile));
                List<Contact> contacts = db.getAllContacts();
                for (Contact cn : contacts) {
                    String log = "Id: " + cn.get_id() + " ,Name: " + cn.get_name() + " ,AUDIOS: " + cn.get_audios() + " ,CAPTURED_IMAGE: " + cn.get_captured_image() + " ,GALLERY: " + cn.get_gallery_image() + " ,PHOTO: " + cn.get_photo() + " deletevar :" + cn.get_delelte_var() + "\n";
                    Log.e("hhh", log);
                }
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        add_from_gallery = (Button) findViewById(R.id.add_from_gallery);
        add_from_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

        final ImageView audiorecorder = (ImageView) findViewById(R.id.audiorecordselection);

        stop = (Button) findViewById(R.id.stopbutton);
        stop.setEnabled(false);
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
        //
        // is not working presently because of the stop button
        // this comment is for hit

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == TAKE_IMAGE && resultCode == RESULT_OK) {
            Bitmap bps = (Bitmap) data.getExtras().get("data");
            Drawable ds = new BitmapDrawable(getResources(), bps);
            capture_view = (ImageView) findViewById(R.id.captureselection);
            capture_view.setImageBitmap(bps);
            captureimageflag = true;
            Uri selectedImageUri = data.getData();
            selectedImagePath = getRealPathFromURI(selectedImageUri);
            final ImageView cancel_capture = (ImageView) findViewById(R.id.cancel2);
            cancel_capture.setImageResource(R.drawable.cancel);
            cancel_capture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    captureimageflag = false;
                    Bitmap bq = null;
                    selectedImagePath = null;
                    capture_view.setImageBitmap(bq);
                    cancel_capture.setImageBitmap(bq);

                }
            });
        }

        if (requestCode == TAKE_IMAGE && resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(), "You have not captured image.", Toast.LENGTH_SHORT).show();

        }
        if (requestCode == SELECTED_AUDIO && resultCode == RESULT_OK) {
            uriSound = data.getData();
            soundfile = uriSound.toString();
            audioflag = true;
            audio_view = (ImageView) findViewById(R.id.audioselection);
            audio_view.setImageResource(R.drawable.sound);
            final ImageView cancel_audio = (ImageView) findViewById(R.id.cancel3);
            cancel_audio.setImageResource(R.drawable.cancel);
            cancel_audio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    audioflag = false;
                    Bitmap bi = null;
                    audio_view.setImageBitmap(bi);
                    soundfile = null;
                    cancel_audio.setImageBitmap(bi);


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

            ims = (ImageView) findViewById(R.id.galleryselection);
            ims.setImageResource(R.drawable.imaget);
            Bitmap yourSelectedImages = null;
            yourSelectedImages = BitmapFactory.decodeFile(filepath);
            ims.setImageBitmap(yourSelectedImages);

            Toast.makeText(getApplicationContext(), "image selected", Toast.LENGTH_LONG).show();

            final ImageView cancel1 = (ImageView) findViewById(R.id.cancel1);
            cancel1.setImageResource(R.drawable.cancel);
            cancel1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    galleryimageflag = false;
                    Bitmap b = null;
                    ims.setImageBitmap(b);
                    filepath = null;
                    cancel1.setImageBitmap(b);


                }
            });

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
