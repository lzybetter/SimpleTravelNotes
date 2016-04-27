package com.lzybetter.simpletravlenotes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

public class currentLocationEdit extends AppCompatActivity {

    private final static int TAKE_PHOTO = 1;

    private Button takePhotoButton, saveButton;
    private EditText inputLocationName, inputLocationDescribe;
    private ImageView locationPicture;
    private String pictureAddress = null;
    private int width,height;
    private double[] location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_location_edit);

        Intent intent = getIntent();
        location = intent.getDoubleArrayExtra("location");

        takePhotoButton = (Button)findViewById(R.id.takePhoto);
        saveButton = (Button)findViewById(R.id.saveButton);
        inputLocationName = (EditText)findViewById(R.id.inputLocationName);
        inputLocationDescribe = (EditText)findViewById(R.id.inputLocationDescribe);
        locationPicture = (ImageView)findViewById(R.id.locationPhoto);


        EditButtonListener editButtonListener = new EditButtonListener();
        takePhotoButton.setOnClickListener(editButtonListener);
        saveButton.setOnClickListener(editButtonListener);

    }

    class EditButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.takePhoto:
                    Uri imageUri;
                    Random random = new Random();
                    int nameNumber = Math.abs(random.nextInt()*900000+100000);
                    String pictureName = "location_Picture" + "_" + nameNumber + ".jpg";
                    String address = Environment.getExternalStorageDirectory().getAbsolutePath() + "/simpletravlenots/";
                    pictureAddress = address + pictureName;
                    File destDir = new File(address);
                    if (!destDir.exists()) {
                        destDir.mkdirs();
                    }
                    File outputImage = new File(address, pictureName);
                    try {
                        if(outputImage.exists()){
                            outputImage.delete();
                        }
                        outputImage.createNewFile();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    imageUri = Uri.fromFile(outputImage);
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent,TAKE_PHOTO);
                    break;
                case R.id.saveButton:
                    SaveLocationDatabaseHelper saveLocationDatabaseHelper = new SaveLocationDatabaseHelper(currentLocationEdit.this,
                            "SaveLocation.db", null, 1);
                    String name = inputLocationName.getText().toString();
                    String describe = inputLocationDescribe.getText().toString();
                    if(pictureAddress == null){
                        pictureAddress = "There is no picture";
                    }
                    Save_and_Read.Save(saveLocationDatabaseHelper,location,name,describe,pictureAddress);
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case TAKE_PHOTO:
                if(resultCode == RESULT_OK){
                    width = locationPicture.getWidth();
                    height = locationPicture.getHeight();
                    Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromFile(pictureAddress,width,height);
                    locationPicture.setImageBitmap(bitmap);
                }
        }
    }
}
