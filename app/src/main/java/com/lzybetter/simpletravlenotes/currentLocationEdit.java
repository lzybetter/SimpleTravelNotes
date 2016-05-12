package com.lzybetter.simpletravlenotes;

import android.content.Intent;
import android.database.Cursor;
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
import android.widget.Toast;

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
    private double latitude,longitude;
    private double[] location = new double[2];
    private boolean isCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_location_edit);

        Intent intent = getIntent();
        latitude = intent.getDoubleExtra("latitude",0);
        longitude = intent.getDoubleExtra("longitude",0);
        location[0] = latitude;
        location[1] = longitude;
        isCurrentLocation = intent.getBooleanExtra("isCurrentLocation",true);

        takePhotoButton = (Button)findViewById(R.id.takePhoto);
        saveButton = (Button)findViewById(R.id.saveButton);
        inputLocationName = (EditText)findViewById(R.id.inputLocationName);
        inputLocationDescribe = (EditText)findViewById(R.id.inputLocationDescribe);
        locationPicture = (ImageView)findViewById(R.id.locationPhoto);

        if(isCurrentLocation){
            inputLocationName.setHint("请输入地址名称");
            inputLocationDescribe.setHint("请输入地址描述");
        }else{
            //这样设计的一个问题是没有办法立刻修改刚刚保存的内容
            int position = intent.getIntExtra("position",0);
            Cursor cursor = Save_and_Read.Read(this,position,false);
            if(cursor.moveToFirst()) {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String describe = cursor.getString(cursor.getColumnIndex("describe"));
                String pictureAddress = cursor.getString(cursor.getColumnIndex("pictureAddress"));
                inputLocationName.setHint(name);
                inputLocationDescribe.setHint(describe);
                if (pictureAddress.equals("There is no picture")) {
                    locationPicture.setImageResource(R.drawable.nopicture);
                } else {
                    int pictureWidth = locationPicture.getWidth();
                    int pictureHeight = locationPicture.getHeight();
                    Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromFile(pictureAddress, pictureWidth, pictureHeight);
                    locationPicture.setImageBitmap(bitmap);
                }
            }
        }

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
                    String name = inputLocationName.getText().toString();
                    String describe = inputLocationDescribe.getText().toString();
                    if(inputLocationName == null){
                        name = inputLocationName.getHint().toString();
                    }
                    if(inputLocationDescribe == null){
                        describe = inputLocationDescribe.getHint().toString();
                    }
                    if(pictureAddress == null){
                        pictureAddress = "There is no picture";
                    }
                    if(isCurrentLocation){
                        SaveLocationDatabaseHelper saveLocationDatabaseHelper = new SaveLocationDatabaseHelper(currentLocationEdit.this,
                                "SaveLocation.db", null, 1);
                        Save_and_Read.Save(saveLocationDatabaseHelper,location,name,describe,pictureAddress);
                    }else{
                       boolean isSu =  Save_and_Read.Update(currentLocationEdit.this,name,describe,pictureAddress);
                        if(isSu){
                            Toast.makeText(currentLocationEdit.this,"修改成功",Toast.LENGTH_SHORT).show();
                        }
                    }
                    inputLocationName.setText("");
                    inputLocationDescribe.setText("");
                    locationPicture.setBackgroundResource(0);
                    Intent intent1 = new Intent(currentLocationEdit.this, contextDisplay.class);
                    intent1.putExtra("location",location);
                    intent1.putExtra("isDisplay",false);
                    intent1.putExtra("name",name);
                    intent1.putExtra("describe",describe);
                    intent1.putExtra("pictureAddress",pictureAddress);
                    startActivity(intent1);
                    break;
                default:
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
