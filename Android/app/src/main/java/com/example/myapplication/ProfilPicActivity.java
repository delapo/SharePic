package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Response;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Callback;
import retrofit2.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.myapplication.R.id.Pictures_from_Gallery;

public class ProfilPicActivity extends AppCompatActivity
{

    private static final int MY_CAMERA_REQUEST_CODE = 100;
    public static int CAMERA_REQUEST_CODE = 1;
    private String currentPhotoPath;

    private static UserApi userApi;
    public static int GALLERY_REQUEST_CODE;
    public String Image;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_pic);

        Button button = (Button) findViewById(R.id.ProfilPicbtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfilPic();
            }
        });

        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToActu();
            }
        });
        this.configureRetrofit();


        Button button_pictures_from_gallery = (Button) findViewById(Pictures_from_Gallery);
        button_pictures_from_gallery.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                pickFromGallery();
            }
        });
        Button button_pictures = (Button) findViewById(R.id.testpicturefromcamera);
        button_pictures.setOnClickListener(new View.OnClickListener()
        {
            @Override
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(View v) { captureFromCamera(); }
        });
    }


/* TAKE PICTURES FROM THE GALLERY START */
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void pickFromGallery()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_CAMERA_REQUEST_CODE);
        }
        else
            {
                // Create an Intent with action as ACTION_PICK
                Intent intent = new Intent(Intent.ACTION_PICK);
                // Sets the type as image/*. This ensures only components of type image are selected
                intent.setType("image/*");
                // We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
                String[] mimeTypes = {"image/jpeg", "image/png"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                // Launching the Intent
                startActivityForResult(intent, GALLERY_REQUEST_CODE);
            }
    }

    /* TAKE PICTURES WITH CAMERA */
    private File createImageFile() throws IOException
    {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyMMddHHmmssZ").format(new Date());
        String imageFileName = "_user" + timeStamp + "_";

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File img = File.createTempFile(imageFileName, ".jpg", storageDir);

        currentPhotoPath = img.getAbsolutePath();
        return img;
    }

    @SuppressLint("NewApi")
    private void captureFromCamera()
    {
        try
        {

            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
            }
            else
            {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                File photo_file = createImageFile();
                Uri photo_uri = FileProvider.getUriForFile(this, "com.example.myapplication.provider", photo_file);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, photo_uri);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
        } catch (IOException ex) { ex.printStackTrace(); }
    }

    /*CHECK RESULT OF THE REQUEST CODE */
    public void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == GALLERY_REQUEST_CODE)
            {
                //data.getData returns the content URI for the selected Image
                Uri selectedImage = data.getData();
                if (selectedImage != null)
                {
                    this.Image = getRealPathFromURIpath(selectedImage, this);
                }
                else { System.out.println("SelectedImage is NULL"); }
                ImageView Profil_picture = findViewById(R.id.profil_picture);
                Profil_picture.setImageURI(selectedImage);
            }
            else if (requestCode == CAMERA_REQUEST_CODE )
            {

                ImageView Profil_picture = findViewById(R.id.profil_picture);
                Profil_picture.setImageURI( Uri.parse(currentPhotoPath) );
                File file = new File(currentPhotoPath);
                this.Image = file.toString();
            }
        }
    }
    private void configureRetrofit()
    {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://35.180.120.219:3000/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        userApi = retrofit.create(UserApi.class);
    }

    private void uploadToServer(String filePath)
    {

        File file = new File(filePath);
        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part part = MultipartBody.Part.createFormData("profil_picture", file.getName(), fileReqBody);

        userApi.uploadImage(MainActivity.token, part).enqueue(new Callback<String>()
        {
            @Override
            public void onResponse(Call call, Response response) {
                redirectToActu();
                Log.d(TAG, "onResponse: " + response);
            }
            @Override
            public void onFailure(Call call, Throwable t) { Log.e(TAG, "onFailure: " + t.getMessage().toString()); }
        });
    }
    public void ProfilPic()
    {
        uploadToServer(this.Image);
        Intent intent2 = new Intent(this, Loading_Page.class);
        startActivity(intent2);
    }

    private String getRealPathFromURIpath(Uri contentURI, Activity activity)
    {
        @SuppressLint("Recycle") Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if(cursor == null)
        {
            return contentURI.getPath();
        }
        else {
            cursor.moveToFirst();
            int indx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(indx);
        }
    }
    private void redirectToActu()
    {
        Intent intent3 = new Intent(this, ActuActivity.class);
        startActivity(intent3);
    }
}
