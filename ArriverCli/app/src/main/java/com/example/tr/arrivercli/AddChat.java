package com.example.tr.arrivercli;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddChat extends AppCompatActivity implements View.OnClickListener{
    public String chat_name_;
    public String std_name_;
    public String username_;
    public String teacher_;
    public String Data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chat);
        Button submit = (Button)findViewById(R.id.addchat_submit);
        submit.setOnClickListener(this);
    }
    public void onClick(View v){
        switch (v.getId()){
            case(R.id.addchat_submit):
                EditText chat_name =(EditText)findViewById(R.id.addchat_chat);
                EditText std_name =(EditText)findViewById(R.id.addchat_stdname);
                EditText username =(EditText)findViewById(R.id.addchat_username);
                EditText teacher = (EditText)findViewById(R.id.addchat_teacher);
                chat_name_ = chat_name.getText().toString();
                std_name_ = std_name.getText().toString();
                username_ = username.getText().toString();
                teacher_ = teacher.getText().toString();
                sendRequest();
                break;
            default:
                break;
        }
    }

    private void sendRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("TeacherLogin", username_);
                    Log.d("TeacherLogin", teacher_);
                    FormBody formBody = new FormBody.Builder().add("chat_name", chat_name_).add("std_name", std_name_).add("username", username_).add("teacher", teacher_).build();
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://104.225.157.237:8000/Student/Saddchat/")
                            .post(formBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    Data = response.body().string();
                    Log.d("TeacherLogin", "###############################");
                    //Data = responseData;
                    Next();
                    //TeacherLog te = TL(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void Next(){
        Log.d("TeacherLogin","here");
        Log.d("TeacherLogin","###############################");
        Log.d("TeacherLogin",Data);
        DataClass T =TL(Data);
        Log.d("TeacherLogin",T.data);
        if(T.data.equals("True")) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //放在UI线程弹Toast
                    Toast.makeText(AddChat.this, "成功加入群组", Toast.LENGTH_SHORT).show();
                }
            });
            Intent intent = new Intent(AddChat.this, StudentHandle.class);
            startActivity(intent);
        }
        else{
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //放在UI线程弹Toast
                    Toast.makeText(AddChat.this, "加入群组失败", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private DataClass TL(String jsonData){
        Gson gson = new Gson();
        DataClass Te = gson.fromJson(jsonData, DataClass.class);
        return Te;
    }
    class DataClass {
        public String data;
        //public String passwd;

        DataClass(String u ){
            this.data = u;
            //this.passwd = p;
        }
    }
}
