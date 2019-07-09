package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class following_list extends AppCompatActivity {

    private UserApi userApi;
    private ListView list_following;
    private ArrayList<User> userList = new ArrayList<>();
    private FollowersListAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following_list);

        configureRetrofit();

        Button btn_exit = findViewById(R.id.button5);
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirect();
            }
        });

        list_following = (ListView) findViewById(R.id.list_following);
        getAbonnement_function();
    }

    private  void getAbonnement_function()
    {
        userApi.getAbonnement(MainActivity.token).enqueue(new Callback<ArrayList>() {
            @Override
            public void onResponse(retrofit2.Call<ArrayList> call, Response<ArrayList> response) {
                if( response.body() != null) {
                    for(int x = 0; x < response.body().size(); x++) {
                        Gson gson = new Gson();
                        JsonObject jsonObject = gson.toJsonTree(response.body().get(x)).getAsJsonObject();
                        int id = jsonObject.get("user_id").getAsInt();
                        getUsername(id);
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ArrayList> call, Throwable t) {

            }
        });
    }
    private void getUsername(int id)
    {
        userApi.getausername(MainActivity.token, id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(retrofit2.Call<String> call, Response<String> response) {

                User user = new User();
                user.setUsername( response.body() );

                userList.add(user);
                adapter = new FollowersListAdapter(following_list.this, R.layout.search_profil_adapter, userList);
                list_following.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    private void redirect()
    {
        Intent intent = new Intent(this, ProfilActivity.class);
        startActivity(intent);
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
