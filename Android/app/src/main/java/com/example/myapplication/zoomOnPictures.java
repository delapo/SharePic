package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class zoomOnPictures extends AppCompatActivity {

    private static final String TAG = "zoomOnPictures";
    private static String uss = "test42";
    private static int i;
    private static Integer like_id;
    private UserApi userApi;
    private ImageView img_comments;
    private Comment comment;
    private CommentsListAdapter adapter;
    private ArrayList<Comment> commentsList;
    private ListView comments_list;
    private String path_to_profil_image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_on_pictures);
        configureRetrofit();
        this.iflike(ActuActivity.pictureObj.getId());
        this.getComments();
        this.data();

        Button send_comment = (Button) findViewById(R.id.send_comment_btn);
        final EditText edittext = (EditText) findViewById(R.id.editText_comment);


        send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String format = "dd/MM/yy H:mm:ss";
                @SuppressLint("SimpleDateFormat") java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat( format );
                java.util.Date date = new java.util.Date();


                sendComment_function(ActuActivity.pictureObj.getId(), formater.format(date) , edittext.getText().toString());
                edittext.setText("");
                Toast.makeText(getApplicationContext(), "your comment have been added.", Toast.LENGTH_SHORT).show();

                if(commentsList != null)
                    commentsList.removeAll(commentsList);
                getComments();
            }
        });

        ImageButton like = (ImageButton) findViewById(R.id.ImageButton);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeaction();
            }
        });

        ImageView img = (ImageView) findViewById(R.id.ImagePictures);

        String strNew = ActuActivity.path_to_image.replace("\"", "");
        String path = "http://35.180.120.219:3000/uploads/" + strNew;
        Picasso.with(this).load(path).into(img);

    }
    private void iflike(Integer id) {

        userApi.iflike(MainActivity.token, id).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(!(response.body().get("like").toString().equals("[]"))){
                    zoomOnPictures.i = 1;
                    ImageButton btn = (ImageButton) findViewById(R.id.ImageButton);
                    btn.setBackgroundResource(R.drawable.ic_action_liked);
                } else {
                    zoomOnPictures.i = 0;
                    ImageButton btn = (ImageButton) findViewById(R.id.ImageButton);
                    btn.setBackgroundResource(R.drawable.ic_action_name);

                }

            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
    public void likeaction() {

        if (zoomOnPictures.i != 1 ) {
            zoomOnPictures.i = 1;
            ImageButton btn = (ImageButton) findViewById(R.id.ImageButton);
            btn.setBackgroundResource(R.drawable.ic_action_liked);

            userApi.like(MainActivity.token,  ActuActivity.pictureObj.getId()).enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    zoomOnPictures.like_id = response.body();
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    Log.e(TAG, "onFailure: " + t.getMessage());
                }
            });
        } else {

            zoomOnPictures.i = 0;
            ImageButton btn = (ImageButton) findViewById(R.id.ImageButton);
            btn.setBackgroundResource(R.drawable.ic_action_name);

            userApi.dislike(MainActivity.token, zoomOnPictures.like_id).enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    Log.e(TAG, "onFailure: " + t.getMessage());
                }
            });
            this.data();

        }
        this.data();
    }
    public void data() {

        userApi.nbrlike(MainActivity.token, ActuActivity.pictureObj.getId()).enqueue(new Callback<Integer>()
        {
            @Override
            public void onResponse(retrofit2.Call<Integer> call, Response<Integer> response)
            {
                if(response.body() != null) {
                    TextView txt = (TextView) findViewById(R.id.TextView_image);
                    txt.setText(response.body().toString());

                } else {
                    TextView txt = (TextView) findViewById(R.id.TextView_image);
                    txt.setText("0");
                }}

            @Override
            public void onFailure(Call<Integer> call, Throwable t)
            {
                MainActivity.access = 1;
                Log.e(TAG, "onFailure: " + t.getMessage().toString());
            }
        });




        userApi.nbrcom(MainActivity.token, ActuActivity.pictureObj.getId()).enqueue(new Callback<Integer>()
        {
            @Override
            public void onResponse(retrofit2.Call<Integer> call, Response<Integer> response)
            {
                if(response.body() != null) {
                    TextView txt = (TextView) findViewById(R.id.TextView_comments);
                    txt.setText(response.body().toString());

                } else {
                    TextView txt = (TextView) findViewById(R.id.TextView_comments);
                    txt.setText("0");
                }}

            @Override
            public void onFailure(Call<Integer> call, Throwable t)
            {
                MainActivity.access = 1;
                Log.e(TAG, "onFailure: " + t.getMessage().toString());
            }
        });



        userApi.getausername(MainActivity.token, ActuActivity.pictureObj.getId()).enqueue(new Callback<String>()
        {
            @Override
            public void onResponse(retrofit2.Call<String> call, Response<String> response)
            {

                if(response.body() != null) {
                    TextView txt = (TextView) findViewById(R.id.textView_username);
                    txt.setText(response.body());

                } else {
                    TextView txt = (TextView) findViewById(R.id.textView_username);
                    txt.setText("User");
                }}

            @Override
            public void onFailure(Call<String> call, Throwable t)
            {
                MainActivity.access = 1;
                Log.e(TAG, "onFailure: " + t.getMessage().toString());
            }
        });

    }
    private void sendComment_function(int picture_id, String date, String comment)
    {
        userApi.sendComment(MainActivity.token, picture_id, date, comment).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                redirectTocomment();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    private void redirectTocomment()
    {
        Intent moveToFollowers = new Intent(this, zoomOnPictures.class);
        startActivity(moveToFollowers);
    }
    private void getComments()
    {
        int picture_id = ActuActivity.pictureObj.getId();

        userApi.getComments(MainActivity.token, picture_id).enqueue(new Callback<ArrayList>() {
            @Override
            public void onResponse(retrofit2.Call<ArrayList> call, Response<ArrayList> response) {

                final ArrayList<Comment> arrcom = new ArrayList<Comment>();

                if(response.body() != null) {

                    for (int x = 0; x < response.body().size(); x++) {
                        final Comment  com = new Comment();

                        Gson gson = new Gson();
                        final JsonObject jsonObject = (JsonObject) gson.toJsonTree(response.body().get(x));
                        final Integer id = jsonObject.get("user_id").getAsInt();


                        callToGetProfilPicture(id, com);

                        com.setUsername(getUsername_func(id));
                        com.setComments(jsonObject.get("comment").getAsString());
                        com.setId(jsonObject.get("user_id").getAsInt());
                        arrcom.add(com);
                    }
                    Fil(arrcom);
                }
                else
                    System.out.println("no comments");
                }
            @Override
            public void onFailure(Call<ArrayList> call, Throwable t) {

            }
        });
    }
    private void callToGetProfilPicture(Integer user_id_profil, final Comment com)
    {
        userApi.getProfilPictureTo(MainActivity.token, user_id_profil).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                path_to_profil_image = response.body().get("profil_picture").toString();
                com.setImage(path_to_profil_image);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
    private String getUsername_func(Integer id) {
        final String[] user = new String[1];
        userApi.getausername(MainActivity.token, id).enqueue(new Callback<String>()
        {
            @Override
            public void onResponse(retrofit2.Call<String> call, Response<String> response)
            {
                if(response.body() != null) {

                    zoomOnPictures.uss = response.body();
                    user[0] = response.body();
                } else
                    zoomOnPictures.uss = "username not available";
            }
            @Override
            public void onFailure(Call<String> call, Throwable t)
            {
                MainActivity.access = 1;
                Log.e(TAG, "onFailure: " + t.getMessage().toString());
            }
        });
        return zoomOnPictures.uss;
    }
    private void Fil(ArrayList<Comment> arr) {
        ListView listView = (ListView) findViewById(R.id.comments_list);

        ArrayList<Comment> comList = new ArrayList<>();
        if(arr != null) {
            for (Comment com : arr) {
                comList.add(com);
            }
        }else {
            System.out.println("follow nobody");
        }
        CommentsListAdapter adapter = new CommentsListAdapter(this, R.layout.comments, comList);

        listView.setAdapter(adapter);
    }
    private void configureRetrofit() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://35.180.120.219:3000/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        userApi = retrofit.create(UserApi.class);
    }
}
