package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FollowersActivity extends AppCompatActivity {

    private UserApi userApi;
    private ListView followers_list;
    private ArrayList<User> userList = new ArrayList<>();
    private FollowersListAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.followers_list);

        configureRetrofit();
        Button btn_exit = findViewById(R.id.button5);
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirect();
            }
        });
        followers_list = (ListView) findViewById(R.id.list_followers);
        getAbonnement_function();
    }

    private void redirect()
    {
        Intent intent = new Intent(this, ProfilActivity.class);
        startActivity(intent);
    }
    private  void getAbonnement_function()
    {
        userApi.getAbonne(MainActivity.token).enqueue(new Callback<ArrayList>() {
            @Override
            public void onResponse(Call<ArrayList> call, Response<ArrayList> response) {
                if( response.body() != null) {
                    for(int x = 0; x < response.body().size(); x++) {
                        Gson gson = new Gson();
                        JsonObject jsonObject = gson.toJsonTree(response.body().get(x)).getAsJsonObject();
                        int id = jsonObject.get("follower_id").getAsInt();
                        getUsername(id);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList> call, Throwable t) {

            }
        });
    }
    private void getUsername(int id)
    {
        userApi.getausername(MainActivity.token, id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                User user = new User();
                user.setUsername( response.body() );

                userList.add(user);
                adapter = new FollowersListAdapter(FollowersActivity.this, R.layout.search_profil_adapter, userList);
                followers_list.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

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
}
