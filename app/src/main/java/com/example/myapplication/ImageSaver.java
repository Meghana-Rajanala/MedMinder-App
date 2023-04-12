package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class ImageSaver {

    private Context context;
    private Activity activity;
    Bitmap bitmap;

    public ImageSaver(Context context, Activity activity) {

        this.context = context;
        this.activity = activity;
    }

    public void saveImage(Bitmap imageBitmap, String fileName) {

        createDirectory();

        File file = new File(new File(Environment.getExternalStorageDirectory() + File.separator + "MedicineAlert"), fileName);

        if (file.exists()){

            file.delete();
        }

        try {

            if (!file.exists()){

                FileOutputStream outputStream = new FileOutputStream(file);
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.flush();
                outputStream.close();
            }

        }catch (Exception e){

            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.w("CreateDir", e.getMessage());
        }
    }

    private void createDirectory() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, StaticVariables.MY_STORAGE_PERMISSION_CODE);

            } else {

                File myAppDirectory = new File(Environment.getExternalStorageDirectory() + File.separator + "MedicineAlert");

                if (!myAppDirectory.exists() && !myAppDirectory.isDirectory()) {

                    // create empty directory
                    if (myAppDirectory.mkdirs()) {

                        Log.i("CreateDir", "App dir created");

                    } else {


                        Log.w("CreateDir", "Unable to create app dir!" + "\n");
                    }

                } else {

                    Log.i("CreateDir", "App dir already exists");
                }

            }

        }else {

            File myAppDirectory = new File(Environment.getExternalStorageDirectory() + File.separator + "MedicineAlert");

            if (!myAppDirectory.exists() && !myAppDirectory.isDirectory()) {

                // create empty directory
                if (myAppDirectory.mkdirs()) {

                    Log.i("CreateDir", "App dir created");

                } else {


                    Log.w("CreateDir", "Unable to create app dir!" + "\n");
                }

            } else {

                Log.i("CreateDir", "App dir already exists");
            }

        }
    }


    public void loadImage(String fileName, ImageView imageView, String type){

        File imageFile = new File(Environment.getExternalStorageDirectory()
                + File.separator + "MedicineAlert"+"/" + fileName);

        if (imageFile.exists()){

            bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            Bitmap bMapScaled = Bitmap.createScaledBitmap(bitmap, 500, 400, true);

            imageView.setImageBitmap(bMapScaled);

        }else {
            Bitmap bMapScaled = Bitmap.createScaledBitmap(bitmap, 500, 400, true);

            imageView.setImageBitmap(bMapScaled);

        }

    }

}
