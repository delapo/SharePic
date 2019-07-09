package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class search extends AppCompatActivity {

    private static final String TAG = "search";
    private UserApi userApi;
    private SearchView searchView;
    private ListView searchList;
    private String search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        configureRetrofit();
        Button _return = (Button) findViewById(R.id.bottom_actu);
        _return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToActu();
            }
        });

        searchView = (SearchView)findViewById(R.id.searchView);
        searchList = (ListView) findViewById(R.id.search_profil);

        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = searchList.getItemAtPosition(position);
                User userObj = (User) o; //As you are using Default String Adapter
                Toast.makeText(getBaseContext(),userObj.getUsername(),Toast.LENGTH_SHORT).show();
                getUser(userObj.getUsername());
            }
        });
        search_profil();
    }


    public void search_profil() {


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                userApi.searchCall(MainActivity.token, newText).enqueue(new Callback< List<User> >()
                {
                    @Override
                    public void onResponse(retrofit2.Call<List<User>> call, Response<List<User>> response) {

                        Log.d("onQueryTextChange", "str: " + response.body());

                        if (response.body() != null) {
                            List<User> usersList = response.body();

                            UserListAdapter adapter = new UserListAdapter(search.this, R.layout.search_profil_adapter, usersList);
                            searchList.setAdapter(adapter);
                            Log.d(TAG, "onResponse: " + response.body());
                        }
                    }
                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {
                        Log.e(TAG, "onFailure: " + t.getMessage().toString());
                    }
                });
                return false;
            }
        });
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

    private void configureRetrofit() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://35.180.120.219:3000/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        userApi = retrofit.create(UserApi.class);
    }
    private void redirectToActu() {
        Intent intent = new Intent(this, ActuActivity.class);
        startActivity(intent);
    }
}
