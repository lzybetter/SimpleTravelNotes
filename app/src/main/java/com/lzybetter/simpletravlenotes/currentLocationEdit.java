package com.lzybetter.simpletravlenotes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class currentLocationEdit extends AppCompatActivity {

    private final static int TAKE_PHOTO = 1;

    private Button takePhotoButton, saveButton;
    private EditText inputLocationName, inputLocationDescribe;
    private ImageView locationPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_location_edit);

        takePhotoButton = (Button)findViewById(R.id.takePhoto);
        saveButton = (Button)findViewById(R.id.saveButton);
        inputLocationName = (EditText)findViewById(R.id.inputLocationName);
        inputLocationDescribe = (EditText)findViewById(R.id.inputLocationDescribe);
        locationPicture = (ImageView)findViewById(R.id.locationPhoto);


    }
}
