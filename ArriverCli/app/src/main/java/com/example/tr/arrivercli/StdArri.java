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

public class StdArri extends AppCompatActivity implements View.OnClickListener{
    public String user_id_;
    public String name_;
    public String std_name_;
    public String chats_;
    public String late_reason_;
    public String Data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_std_arri);
        Button submit = (Button)findViewById(R.id.stdarri_submit);
        submit.setOnClickListener(this);
    }
    public void onClick(View v){
        switch (v.getId()){
            case(R.id.stdarri_submit):
                EditText user_id =(EditText)findViewById(R.id.stdarri_user_id);
                EditText name =(EditText)findViewById(R.id.stdarri_name);
                EditText std_name =(EditText)findViewById(R.id.stdarri_std_name);
                EditText chats = (EditText)findViewById(R.id.stdarri_chats);
                EditText late_reason = (EditText)findViewById(R.id.stdarri_late_reason);
                user_id_ = user_id.getText().toString();
                name_ = name.getText().toString();
                std_name_ = std_name.getText().toString();
                chats_ = chats.getText().toString();
                late_reason_ = late_reason.getText().toString();
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
                    Log.d("TeacherLogin", name_);
                    Log.d("TeacherLogin", chats_);
                    FormBody formBody = new FormBody.Builder().add("user_id",user_id_).add("name", name_).add("std_name", std_name_).add("chats", chats_).add("late_reason",late_reason_).build();
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://104.225.157.237:8000/Student/Sstdarri/")
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
                    Toast.makeText(StdArri.this, "签到成功", Toast.LENGTH_SHORT).show();
                }
            });
            Intent intent = new Intent(StdArri.this, StudentHandle.class);
            startActivity(intent);
        }
        else{
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //放在UI线程弹Toast
                    Toast.makeText(StdArri.this, "签到失败，信息错误或重复签到", Toast.LENGTH_LONG).show();
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
