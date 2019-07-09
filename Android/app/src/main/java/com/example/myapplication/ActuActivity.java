package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActuActivity extends AppCompatActivity {
    private static final String TAG = "ActuActivity";
    public  static Integer like = 0;
    public  static Integer com = 4;
    public  static  String use;
    private int items;
    private UserApi userApi;
    private static ArrayList<Pictures> arr;
    public SearchView searchView;
    private ListView searchList;
    public static String path_to_image;
    public static Pictures pictureObj;


    public String search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_actu);
        configureRetrofit();
        this.getImagesFromServer();

        Button button = (Button) findViewById(R.id.myprofil);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profil();
            }
        });

        Button button2 = (Button) findViewById(R.id.post);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post();
            }
        });

        Button searchbtn = (Button) findViewById(R.id.button2);
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToSearch();
            }
        });


        final ListView PicturesList = (ListView) findViewById(R.id.actu_dynamic);

        PicturesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = PicturesList.getItemAtPosition(position);
                pictureObj = (Pictures) o; //As you are using Default String Adapter
                Toast.makeText(getBaseContext(),pictureObj.getImage(),Toast.LENGTH_SHORT).show();
                path_to_image = pictureObj.getImage();
                redirectToPicture();
            }
        });


    }


    public void getImagesFromServer() {
        userApi.getPicturesFil(MainActivity.token).enqueue(new Callback<List<ArrayList>>()
        {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<List<ArrayList>> call, Response<List<ArrayList>> response)
            {
                ArrayList<Pictures> arrpic = new ArrayList<Pictures>();

                if(response.body() != null)
                    for(int x = 0; x < response.body().size(); x++) {
                        if (response.body().get(x) != null) {
                            for (int y = 0; y < response.body().get(x).size(); y++) {
                                if (response.body().get(x).get(y) != null) {
                                    final Pictures  picture = new Pictures();

                                    Gson gson = new Gson();
                                    JsonObject jsonObject = gson.toJsonTree(response.body().get(x).get(y)).getAsJsonObject();
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
                        }
                    }
                Fil(arrpic);
            }

            @Override
            public void onFailure(Call<List<ArrayList>> call, Throwable t)
            {
                Log.e(TAG, "onFailure: zabattam " + t.getMessage().toString());
            }
        });
        //Fil(ActuActivity.arr);
    }

    private void Fil(ArrayList<Pictures> arr) {
        ListView listView = (ListView) findViewById(R.id.actu_dynamic);

        ArrayList<Pictures> picturesList = new ArrayList<>();
        if(arr != null) {
            for (Pictures pic : arr) {
                picturesList.add(pic);
            }
        }else {
            System.out.println("follow nobody");
        }
        PicturesListAdapter adapter = new PicturesListAdapter(this, R.layout.image, picturesList);

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

    private void profil() {
        Intent intent = new Intent(this, ProfilActivity.class);
        startActivity(intent);
    }

    private void post() {
        Intent intent = new Intent(this, PostActivity.class);
        startActivity(intent);
    }

    private void getUser(String query)
    {
        userApi.getUser(MainActivity.token, query).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(retrofit2.Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null && response.body().size() != 0) {
                    String un = response.body().get("username").getAsString();
                    Integer id =  response.body().get("id").getAsInt();
                    calling(un, id);

                } else {
                    Toast.makeText(getApplicationContext(), "This user does'nt exist, sorry !", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());

            }
        });
    }
    public void calling(String username, Integer id) {
        Intent intent = new Intent(this, FindUserActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("id", id);
        startActivity(intent);
    }
    public void redirectToSearch() {
        Intent intent = new Intent(this, search.class);
        startActivity(intent);
    }
    public void redirectToPicture() {
        Intent intent = new Intent(this, zoomOnPictures.class);
        startActivity(intent);
    }

}
