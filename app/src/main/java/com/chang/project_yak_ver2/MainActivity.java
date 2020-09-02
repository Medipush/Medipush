package com.chang.project_yak_ver2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chang.project_yak_ver2.Api.inputData;
import com.google.gson.Gson;


import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private inputData.Post UserData;
    private String SSN;
    private boolean inputSuccess;
    public static Context mContext;

    public static final String IsFirstLogin = "firstLogin";
    public static final String  IsSSN= "IsSSN";
    public static final String  Yes= "yes";


    public  String getSSN()
    {
        return SSN;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        SharedPreferences sharedPref = getSharedPreferences(IsFirstLogin,MODE_PRIVATE);
        SSN = sharedPref.getString(IsSSN, Yes);

        if(SSN!=Yes)
        {
            FetchPostTask fetchPostTask = new FetchPostTask();
            fetchPostTask.execute(inputData.Get_Post + SSN);
        }



        final EditText inputSSN = findViewById(R.id.et_inputSSN);
        Button button = findViewById(R.id.btn_login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SSN = String.valueOf(inputSSN.getText());
                fetchAsyncPosts();

            }

            private void fetchAsyncPosts() {
                FetchPostTask fetchPostTask = new FetchPostTask();
                fetchPostTask.execute(inputData.Get_Post + SSN);

            }


        });
    }

    class FetchPostTask extends AsyncTask<String, Void, inputData.Post> {

        @Override
        protected inputData.Post doInBackground(String... strings) {

            String url = strings[0];
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                Gson gson = new Gson();
                inputData.Post post = gson.fromJson(response.body().charStream(), inputData.Post.class);
                Log.d("FetchPostTask", response.body().string());
                Log.d("FetchPostTask", "response success");
                return post;
            } catch (IOException e) {
                Log.d("asd", e.getMessage());
                Log.d("FetchPostTask", "response fail");
                return null;
            }
        }

        @Override
        protected void onPostExecute(inputData.Post post) {
            super.onPostExecute(post);
            if (post == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("주의!");
                builder.setMessage("아직 등록되지 않은 주민번호입니다.");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        inputSuccess = false;
                    }
                });
                builder.create().show();
            } else {
                inputSuccess = true;
                UserData = post;

                SharedPreferences test = getSharedPreferences(IsFirstLogin, MODE_PRIVATE);
                SharedPreferences.Editor editor = test.edit();
                editor.putString(IsSSN, SSN);
                editor.commit();

                Intent loginIntent = new Intent(getApplicationContext(), mainScreen.class);
                loginIntent.putExtra("userDataCautions", (Serializable) UserData.getCautions());
                loginIntent.putExtra("userDataTake_med", (Serializable) UserData.getTake_med());
                startActivity(loginIntent);
                finish();
            }

        }
    }


}