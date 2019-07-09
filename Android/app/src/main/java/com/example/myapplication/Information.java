package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.text.IDNA;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Callback;
import retrofit2.Call;

public class Information extends AppCompatActivity {
    private static UserApi userApi;
    private static final String TAG = "MainActivity";

    public  String username, password , confirm_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        configureRetrofit();
        final EditText edit_NewUsername = (EditText) findViewById(R.id.ChangeUsername);
        final EditText edit_NewPassword = (EditText) findViewById(R.id.NewPassword);
        final EditText edit_NewPasswordConfirm = (EditText) findViewById(R.id.ConfirmNewPassword);
        Button btn_bottom_actu_information = (Button) findViewById(R.id.bottom_actu_information);
        btn_bottom_actu_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToProfil();
            }
        });
        Button button = (Button) findViewById(R.id.change_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = edit_NewUsername.getText().toString().trim();
                password = edit_NewPassword.getText().toString().trim();
                confirm_password = edit_NewPasswordConfirm.getText().toString().trim();

                if (password.equals(confirm_password) ) { changeInformation(); }
                else { Toast.makeText(getApplicationContext(), "Password and Confirm Password should be the same.", Toast.LENGTH_SHORT).show(); }
            }
        });
    }
    private  void redirectToProfil()
    {
        Intent intent = new Intent(this, ProfilActivity.class);
        startActivity(intent);
    }
    private void changeInformation()
    {


        userApi.setInformation(MainActivity.token, username, password, confirm_password).enqueue(new Callback<String>()
        {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(retrofit2.Call<String> call, Response<String> response) {
                if (response.body() != null) {
                    Toast.makeText(getApplicationContext(), "Your profil have been updated", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
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

}
