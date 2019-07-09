package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static String token;
    public static Integer access = 1;
    private static UserApi userApi;
    public Login login;
    private static final String TAG = "MainActivity";

    public Login getLogin(){ return this.login; }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.configureRetrofit();

        Button _button = (Button) findViewById(R.id.buttonRegister_Main);
        _button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        });


        final EditText edittext = (EditText) findViewById(R.id.usernamelogin);
        final EditText edit_passwordLogin = (EditText) findViewById(R.id.passwordlogin);

        Button buttonlogin = (Button) findViewById(R.id.buttonLogin);
        buttonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login = new Login();
                login.setEmail(edittext.getText().toString().trim());
                login.setPassword(edit_passwordLogin.getText().toString().trim());
                Logine(login);
            }
        });

    }

    private void Register() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void Logine(Login login) {
        userApi.postLogin(login).enqueue(new Callback<String>() {
            @Override
            public void onResponse(retrofit2.Call<String> call, Response<String> response) {
                if (response.body() != null) {
                    MainActivity.access = 0;
                    Log.d(TAG, "onResponse: " + response.body());
                    MainActivity.token = "Bearer " + response.body();
                    redirection(response.body());
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                MainActivity.access = 1;
                Toast.makeText(getApplicationContext(), "Please verify your connection", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: " + t.getMessage().toString());
            }
        });

    }

    public void redirection(String str) {
        String str2 = "Wrong";
        if(str.equals(str2)) {
            Toast.makeText(getApplicationContext(), "Wrong parameters, sorry", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent1 = new Intent(this, ActuActivity.class);
            startActivity(intent1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
