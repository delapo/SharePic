package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Callback;
import retrofit2.Call;


public class RegisterActivity extends AppCompatActivity {

    private static UserApi userApi;
    private static final String TAG = "MainActivity";
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final EditText edit_RegisterName = (EditText) findViewById(R.id.RegisterName);
        final EditText edit_LastName = (EditText) findViewById(R.id.LastName);
        final EditText edit_Username = (EditText) findViewById(R.id.Username);
        final EditText edit_Email = (EditText) findViewById(R.id.Email);
        final EditText edit_Password = (EditText) findViewById(R.id.Password);
        final EditText edit_confirm_Password = (EditText) findViewById(R.id.ConfirmPassword);

        this.configureRetrofit();

        Button button = (Button) findViewById(R.id.Registerbtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                user.setName(edit_RegisterName.getText().toString().trim());
                user.setLastname(edit_LastName.getText().toString().trim());
                user.setUsername(edit_Username.getText().toString().trim());
                user.setEmail(edit_Email.getText().toString().trim());
                user.setPassword(edit_Password.getText().toString().trim());
                user.setPassword_confirmation(edit_confirm_Password.getText().toString().trim());
                user.setIsconnect(1);
                addUser(user);
                Login login = new Login();
                login.setEmail(edit_Email.getText().toString().trim());
                login.setPassword(edit_Password.getText().toString().trim());
                Logine(login);
            }
        });

    }

    private void Logine(Login login)
    {
        userApi.postLogin(login).enqueue(new Callback<String>()
        {
            @Override
            public void onResponse(retrofit2.Call<String> call, Response<String> response)
            {
                if (response.body() != null) {
                    MainActivity.access = 0;
                    Log.d(TAG, "onResponse: " + response.body());
                    MainActivity.token = "Bearer " + response.body();

                    redirection(response.body());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t)
            {
                MainActivity.access = 1;
                Log.e(TAG, "onFailure: " + t.getMessage().toString());
            }
        });
    }

        public void redirection(String str)
        {
            String str2 = "Wrong";
            if(str.equals(str2)) {
                Toast.makeText(getApplicationContext(), "Wrong parameters, sorry", Toast.LENGTH_LONG).show();
            } else {
                Intent intent1 = new Intent(this, ProfilPicActivity.class);
                startActivity(intent1);
            }
        }

    public void addUser(User user)
    {
        userApi.postUser(user).enqueue(new Callback<String>()
        {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) {
                    Log.d(TAG, "onResponse: ->" + response);
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage().toString());
            }
        });
    }

    private void configureRetrofit()
    {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://35.180.120.219:3000/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        userApi = retrofit.create(UserApi.class);
    }
}

