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
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.Calendar;
import java.util.HashMap;


import static com.example.myapplication.R.id.Pictures_Gallery;
import static com.example.myapplication.R.id.Pictures_from_Gallery;
import static com.example.myapplication.R.id.descriptiontext;
import static com.example.myapplication.R.id.localisationtext;

public class PostActivity extends AppCompatActivity {

    private static final int MY_CAMERA_REQUEST_CODE = 100;
    public static int CAMERA_REQUEST_CODE = 1;
    private static UserApi userApi;
    public static int GALLERY_REQUEST_CODE;
    private String currentPhotoPath;
    public String Image;
    private static final String TAG = "MainActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Button button = (Button) findViewById(R.id.bottom_actu);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        Button button5 = (Button) findViewById(R.id.Picbtn);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pic();
            }
        });

        this.configureRetrofit();


        Button button_pictures_from_gallery = (Button) findViewById(Pictures_Gallery);
        button_pictures_from_gallery.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                pickFromGallery();
            }
        });
        Button button_pictures = (Button) findViewById(R.id.picturefromcamera);
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
                ImageView picture = findViewById(R.id.picture);
                picture.setImageURI(selectedImage);
            }
            else if (requestCode == CAMERA_REQUEST_CODE )
            {

                ImageView Profil_picture = findViewById(R.id.picture);
                Profil_picture.setImageURI( Uri.parse(currentPhotoPath) );
                File file = new File(currentPhotoPath);
                this.Image = file.toString();
            }
        }
    }

    /*GO BACK */
    private void logout()
    {
        Intent intent = new Intent(this, ActuActivity.class);
        startActivity(intent);
    }

    private String getRealPathFromURIpath(Uri contentURI, Activity activity)
    {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
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

    public void Pic()
    {
        uploadToServer(this.Image);

    }

    private void uploadToServer(String filePath)
    {
        Date date2 = Calendar.getInstance().getTime();

        @SuppressLint("SimpleDateFormat") DateFormat formatter = new SimpleDateFormat("yy/MM/dd, hh:mm:ss");
        String date = formatter.format(date2);

        EditText etloc = findViewById(localisationtext);
        String localisation = etloc.getText().toString().trim();

        EditText etloc2 = findViewById(descriptiontext);
        String description = etloc2.getText().toString().trim();
        String name = filePath;

        File photo = new File(filePath);

        RequestBody fileReqBody =
                RequestBody.create(
                        MediaType.parse("image/*"), photo);
        MultipartBody.Part part = MultipartBody.Part.createFormData("photo", photo.getName(), fileReqBody);

        RequestBody description1 = createPartFromString(description);
        RequestBody date1 = createPartFromString(date);
        RequestBody localisation1 = createPartFromString(localisation);
        RequestBody name1 = createPartFromString(name);

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("description", description1);
        map.put("date", date1);
        map.put("localisation", localisation1);
        map.put("name", name1);
        String token = MainActivity.token;
        Toast.makeText(getApplicationContext(), "Please wait, it may take a few minute...", Toast.LENGTH_LONG).show();

        Call<ResponseBody> call = userApi.uploadPicture(token, map, part);
        call.enqueue(new Callback<ResponseBody>()
        {
            @Override
            public void onResponse(retrofit2.Call call, Response response) {
                functiontest();
                Log.d(TAG, "onResponse: " + response);
            }
            @Override
            public void onFailure(Call call, Throwable t) { Log.e(TAG, "onFailure: " + t.getMessage().toString()); }
        });

    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        if (descriptionString == null)
            return RequestBody.create(MultipartBody.FORM, "");
        return RequestBody.create(
                MultipartBody.FORM, descriptionString);

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

    private void functiontest()
    {
        Intent intent3 = new Intent(this, ActuActivity.class);
        startActivity(intent3);
    }
}
