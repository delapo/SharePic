package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.myapplication.ProfilPicActivity.GALLERY_REQUEST_CODE;

public class ProfilActivity extends AppCompatActivity {

    private static final int MY_CAMERA_REQUEST_CODE = 100;
    public static int CAMERA_REQUEST_CODE = 1;
    private String currentPhotoPath;
    private static UserApi userApi;
    private static UserApi userApi2;
    private static final String TAG = "MainActivity";
    public static String aboonbr;
    public static String filelien;
    public String Image;
    public static ImageView img;
    public static UserApi getUserApi2() {
        return userApi2;
    }

    public static void setUserApi2(UserApi userApi2) {
        ProfilActivity.userApi2 = userApi2;
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);


        this.configureRetrofit();
        this.getname();
        this.getImagesFromServer();
        this.getabonbr();
        this.NbrMyPic();
        this.getmypicnbr();
        this.getabonnementnbr();
        this.getprofilpic();


        Button button = (Button) findViewById(R.id.bottom_actu);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        Button button_pictures = (Button) findViewById(R.id.testpicturefromcamera);
        button_pictures.setOnClickListener(new View.OnClickListener() {
            @Override
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(View v) {
                captureFromCamera();
            }
        });

        Button button_gallery = (Button) findViewById(R.id.testpicturefromcamera2);
        button_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(View v) {
                pickFromGallery();
            }
        });

        Button update_profil = (Button) findViewById(R.id.updateprofil);
        update_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(View v) {
                redirectToUpdatePage();
            }
        });

        Button btn_followers = (Button) findViewById(R.id.nbOfFollowers);
        btn_followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToFollowers();
            }
        });

        Button btn_following = (Button) findViewById(R.id.nbOfFollowing);
        btn_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToFollowing();
            }
        });

        Button button2 = (Button) findViewById(R.id.post);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post();
            }
        });

    }
    private void post() {
        Intent intent = new Intent(this, PostActivity.class);
        startActivity(intent);
    }
    public void getImagesFromServer() {
        userApi.getmypost(MainActivity.token).enqueue(new Callback<ArrayList>()
        {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<ArrayList> call, Response<ArrayList> response)
            {
                ArrayList<Pictures> arrpic = new ArrayList<Pictures>();

                if(response.body() != null)
                    for(int x = 0; x < response.body().size(); x++) {
                        if (response.body().get(x) != null) {
                                    final Pictures  picture = new Pictures();

                                    Gson gson = new Gson();
                                    JsonObject jsonObject = gson.toJsonTree(response.body().get(x)).getAsJsonObject();
                                    picture.setId(jsonObject.get("id").getAsInt());
                                    Integer id = jsonObject.get("id").getAsInt();

                                    userApi.nbrlike(MainActivity.token, id).enqueue(new Callback<Integer>()
                                    {
                                        @Override
                                        public void onResponse(retrofit2.Call<Integer> call, Response<Integer> response)
                                        {
                                            if(response.body() != null) {
                                                ActuActivity.like = response.body();

                                                picture.setLike(ActuActivity.like);

                                            } else
                                                ActuActivity.like = 0;                                    }
                                        @Override
                                        public void onFailure(Call<Integer> call, Throwable t)
                                        {
                                            MainActivity.access = 1;
                                            Log.e(TAG, "onFailure: " + t.getMessage().toString());
                                        }
                                    });




                                    userApi.nbrcom(MainActivity.token, id).enqueue(new Callback<Integer>()
                                    {
                                        @Override
                                        public void onResponse(retrofit2.Call<Integer> call, Response<Integer> response)
                                        {
                                            if(response.body() != null) {
                                                ActuActivity.com = response.body();
                                                picture.setComments(ActuActivity.com);

                                            }  else
                                                ActuActivity.com = 0;
                                        }
                                        @Override
                                        public void onFailure(Call<Integer> call, Throwable t)
                                        {
                                            MainActivity.access = 1;
                                            Log.e(TAG, "onFailure: " + t.getMessage().toString());
                                        }
                                    });



                                    userApi.getausername(MainActivity.token, id).enqueue(new Callback<String>()
                                    {
                                        @Override
                                        public void onResponse(retrofit2.Call<String> call, Response<String> response)
                                        {
                                            if(response.body() != null) {
                                                ActuActivity.use = response.body();
                                                picture.setUsername(ActuActivity.use);


                                            } else
                                                ActuActivity.use = "error";                                    }
                                        @Override
                                        public void onFailure(Call<String> call, Throwable t)
                                        {
                                            MainActivity.access = 1;
                                            Log.e(TAG, "onFailure: " + t.getMessage().toString());
                                        }
                                    });

                                    picture.setDescription(jsonObject.get("description").getAsString());
                                    picture.setUser_id(jsonObject.get("user_id").getAsInt());
                                    picture.setImage(jsonObject.get("image").toString());
                                    picture.setLocalisation(jsonObject.get("localisation").toString());
                                    picture.setDate(jsonObject.get("date").toString());
                                    arrpic.add(picture);
                                }
                            }
                Fil(arrpic);
            }

            @Override
            public void onFailure(Call<ArrayList> call, Throwable t)
            {
                Log.e(TAG, "onFailure: wsssssssshshshshshs " + t.getMessage().toString());
            }
        });
        //Fil(ActuActivity.arr);
    }

    private void Fil(ArrayList<Pictures> arr) {
        ListView listView = (ListView) findViewById(R.id.profil_picture_list);

        ArrayList<Pictures> picturesList = new ArrayList<>();
        if(arr != null) {
            for (Pictures pic : arr) {
                picturesList.add(pic);
            }
        }else {
            System.out.println("follow nobody");
        }
        ProfilPicturesListAdapter adapter = new ProfilPicturesListAdapter(this, R.layout.image, picturesList);

        listView.setAdapter(adapter);
    }

    /* CALL TO GET THE NUMBER OF FOLLOWERS */
    private void getabonbr() {

        userApi.getFollowNbr(MainActivity.token).enqueue(new Callback<Integer>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.body() != null) {
                    ProfilActivity.aboonbr = response.body().toString();
                    Button textView = (Button) findViewById(R.id.nbOfFollowers);
                    textView.setText("Follower : " + ProfilActivity.aboonbr);
                } else {
                    Button textView = (Button) findViewById(R.id.nbOfFollowers);
                    textView.setText("Follower : 0");
                }
            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    /* CALL TO GET THE NUMBER OF FOLLOWED */
    private void getabonnementnbr()
    {
        userApi.getFollowingNbr(MainActivity.token).enqueue(new Callback<Integer>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.body() != null) {
                    ProfilActivity.aboonbr = response.body().toString();
                    Button textView = (Button) findViewById(R.id.nbOfFollowing);
                    textView.setText("Following : " + ProfilActivity.aboonbr);
                } else {
                    Button textView = (Button) findViewById(R.id.nbOfFollowing);
                    textView.setText("Following : 0");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());

            }
        });
    }

    /* CALL TO GET THE NUMBER OF FOLLOWED */
    private void getmypicnbr() {
        userApi.getMyPicNbr(MainActivity.token).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(retrofit2.Call<Integer> call, Response<Integer> response) {
                if (response.body() != null) {
                    String nbr = response.body().toString();
                    Button textView = (Button) findViewById(R.id.publication);
                    textView.setText("pictures : " + nbr);
                } else {
                    Button textView = (Button) findViewById(R.id.publication);
                    textView.setText("pictures : 0");
                }
            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    /* CALL TO GET THE USERNAME */
    private void getname()
    {
        userApi.getUserName(MainActivity.token).enqueue(new Callback<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) {
                    TextView textView = (TextView) findViewById(R.id.username);
                    textView.setText(response.body());
                } else {

                    TextView textView = (TextView) findViewById(R.id.username);
                    textView.setText("error");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());

            }
        });
    }

    /* CALL TO GET THE PROFILE PICTURE  */
       private void getprofilpic()
    {

        userApi.getMyProfilPic(MainActivity.token).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    picpic("http://35.180.120.219:3000/uploads/" + response.body().get("img").getAsString());
                } catch (Exception e){return ;}
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());

            }
        });
    }

    public void picpic(String file) {
        ImageView img = findViewById(R.id.profil_picture);
        Picasso.with(this).load(file).into(img);
    }

    /*GO BACK */
    private void logout()
    {
        Intent intent = new Intent(this, ActuActivity.class);
        startActivity(intent);
    }

    /* RESULT OF CHANGES OF PROFILE PICTURE */
    public void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == GALLERY_REQUEST_CODE)
            {
                //data.getData returns the content URI for the selected Image
                if (data.getData() != null)
                {
                    this.Image = getRealPathFromURIpath(data.getData(), this);
                }
                else { System.out.println("SelectedImage is NULL"); }
                ImageView Profil_picture = findViewById(R.id.profil_picture);
                Profil_picture.setImageURI(data.getData());
                uploadToServer(this.Image);
            }
            else if (requestCode == CAMERA_REQUEST_CODE )
            {

                ImageView Profil_picture = findViewById(R.id.profil_picture);
                Profil_picture.setImageURI( Uri.parse(currentPhotoPath) );
                File file = new File(currentPhotoPath);
                this.Image = file.toString();
                uploadToServer(this.Image);
            }
        }
    }
    /* UPLOAD THE IMAGE ON THE SERVER */
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
                Log.d(TAG, "onResponse: " + response);
            }
            @Override
            public void onFailure(Call call, Throwable t) { Log.e(TAG, "onFailure: " + t.getMessage().toString()); }
        });
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
    private File createImageFile() throws IOException
    {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyMMddHHmmssZ").format(new Date());
        String imageFileName = "_user" /*.getName()*/ + timeStamp + "_";

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File img = File.createTempFile(imageFileName, ".jpg", storageDir);

        currentPhotoPath = img.getAbsolutePath();
        return img;
    }

    private void redirectToUpdatePage()
    {
        Intent moveToUpdate = new Intent(this, Information.class);
        startActivity(moveToUpdate);
    }

    public void NbrMyPic() {
        userApi.getMyPicNbr(MainActivity.token).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                try {
                    String nbr = response.body().toString();
                    Button textView = (Button) findViewById(R.id.publication);
                    textView.setText("posts : " + nbr);
                } catch (Exception e){return ;}
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());

            }
        });
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
    private void redirectToFollowers(){
        Intent moveToFollowers = new Intent(this, FollowersActivity.class);
        startActivity(moveToFollowers);
    }
    public void redirectToFollowing()
    {
        Intent moveToFollowers = new Intent(this, following_list.class);
        startActivity(moveToFollowers);
    }

}
