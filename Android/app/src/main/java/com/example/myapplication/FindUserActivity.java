package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FindUserActivity extends AppCompatActivity {

    private static UserApi userApi;
    private static final String TAG = "MainActivity";
    private static int i;
    public Integer id;
    private ListView pic_from_user;
    private FindUserAdapter adapter;
    private ArrayList<Pictures> pictureList;


    public static String filelien2;
    public Integer ifab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);

        this.configureRetrofit();

        Integer id = getIntent().getIntExtra("id", 0);
        this.id = id;

        this.ifabo(id);

        Button btn = (Button) findViewById(R.id.followbtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                follow();
            }
        });

        String username = getIntent().getStringExtra("username");

        Button btn_return = findViewById(R.id.bottom_actu);
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToSearch();
            }
        });
        this.getImagesFromServer();
        this.getabonbr(id);
        this.getabonnementnbr(id);
        this.getpicnbr(id);
        this.getprofilpic(id);

        TextView tv = (TextView) findViewById(R.id.username);
        tv.setText(username);




        pic_from_user = (ListView) findViewById(R.id.pic_from_user);

        adapter = new FindUserAdapter(FindUserActivity.this, R.layout.image, pictureList);


    }

    public void getImagesFromServer() {
        userApi.getpost(MainActivity.token, this.id).enqueue(new Callback<ArrayList>()
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
                Log.e(TAG, "onFailure: " + t.getMessage().toString());
            }
        });
        //Fil(ActuActivity.arr);
    }

    private void Fil(ArrayList<Pictures> arr) {
        ListView listView = (ListView) findViewById(R.id.pic_from_user);

        ArrayList<Pictures> picturesList = new ArrayList<>();
        if(arr != null) {
            for (Pictures pic : arr) {
                picturesList.add(pic);
            }
        }else {
            System.out.println("follow nobody");
        }
        FindUserAdapter adapter = new FindUserAdapter(this, R.layout.image, picturesList);

        listView.setAdapter(adapter);
    }

    /* CALL TO GET THE NUMBER OF FOLLOWERS */
    private void getabonbr(Integer id) {


        userApi.getFollowNbrid(MainActivity.token, id).enqueue(new Callback<Integer>() {

            @Override
            public void onResponse(retrofit2.Call<Integer> call, Response<Integer> response) {

                if (response.body() != null) {
                    String abonbr = response.body().toString();
                    TextView textView = (TextView) findViewById(R.id.follower);
                    textView.setText("Follower : " + abonbr);
                } else {
                    TextView textView = (TextView) findViewById(R.id.follower);
                    textView.setText("Follower : 0");
                }
            }
            @Override
            public void onFailure(retrofit2.Call<Integer> call, Throwable t) {
                Log.e(TAG, "onFailure :  " + t.getMessage());
            }
        });
    }

    /* CALL TO GET THE NUMBER OF FOLLOWED */
    private void getabonnementnbr(Integer id)
    {
        userApi.getFollowingNbrid(MainActivity.token, id).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(retrofit2.Call<Integer> call, Response<Integer> response) {
                if (response.body() != null) {
                    String aboonbr = response.body().toString();
                    TextView textView = (TextView) findViewById(R.id.following);
                    textView.setText("Following : " + aboonbr);
                } else {
                    TextView textView = (TextView) findViewById(R.id.following);
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
    private void getpicnbr(Integer id) {
        userApi.getPicNbr(MainActivity.token, id).enqueue(new Callback<Integer>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(retrofit2.Call<Integer> call, Response<Integer> response) {
                if (response.body() != null) {
                    String nbr = response.body().toString();
                    TextView textView = (TextView) findViewById(R.id.publication);
                    textView.setText("pictures : " + nbr);
                } else {
                    TextView textView = (TextView) findViewById(R.id.publication);
                    textView.setText("pictures : 0");
                }
            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }


    /* CALL TO GET THE PROFILE PICTURE  */
    private void getprofilpic(Integer id)
    {
        userApi.getProfilPicId(MainActivity.token, id).enqueue(new Callback<JsonObject>() {
        @Override
        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
            try {
                FindUserActivity.filelien2 = "http://35.180.120.219:3000/uploads/" + response.body().get("img").getAsString();
            } catch (Exception e){
                return ;
            }
            new DownloadImageFromInternet((ImageView) findViewById(R.id.profil_picture))
                    .execute(FindUserActivity.filelien2);
        }

        @Override
        public void onFailure(Call<JsonObject> call, Throwable t) {
            Log.e(TAG, "onFailure: " + t.getMessage());
        }
    });
    }

    private void ifabo(Integer id) {

        userApi.ifabo(MainActivity.token, id).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if(!(response.body().get("img").toString().equals("[]"))){

                    FindUserActivity.i = 1;
                    Button btn = (Button) findViewById(R.id.followbtn);
                    btn.setText("Unfollow");
                } else {
                    FindUserActivity.i = 0;
                    Button btn = (Button) findViewById(R.id.followbtn);
                    btn.setText("Follow");
                }

            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

        @SuppressLint("StaticFieldLeak")
    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap>
    {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
            Toast.makeText(getApplicationContext(), "Please wait, it may take a few minute...", Toast.LENGTH_SHORT).show();
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
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

    public void follow() {
        Integer user_id = id;

        if (FindUserActivity.i != 1 ) {
            FindUserActivity.i = 1;
            Button btn = (Button) findViewById(R.id.followbtn);

            btn.setText("unfollow");
            userApi.follow(MainActivity.token, user_id).enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    Log.e(TAG, "onFailure: " + t.getMessage());
                }
            });
        } else {
            FindUserActivity.i = 0;
            Button btn = (Button) findViewById(R.id.followbtn);
            btn.setText("follow");
            userApi.unfollow(MainActivity.token, user_id).enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {

                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    Log.e(TAG, "onFailure: " + t.getMessage());
                }
            });
        }
    }


    private void redirectToSearch() {
        Intent intent = new Intent(this, search.class);
        startActivity(intent);
    }
}
