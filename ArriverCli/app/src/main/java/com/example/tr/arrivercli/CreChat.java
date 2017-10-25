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

public class CreChat extends AppCompatActivity implements View.OnClickListener{
    public String name_;
    public String teacher_;
    public String passwd_;
    public String advise_;
    public String Data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cre_chat);
        Button submit = (Button)findViewById(R.id.crechat_submit);
        submit.setOnClickListener(this);
    }

    public void onClick(View v){
        switch (v.getId()){
            case(R.id.crechat_submit):
                EditText name =(EditText)findViewById(R.id.crechat_name);
                EditText teacher =(EditText)findViewById(R.id.crechat_teacher);
                EditText passwd =(EditText)findViewById(R.id.crechat_password);
                EditText advise = (EditText)findViewById(R.id.crechat_advise);
                name_ = name.getText().toString();
                teacher_ = teacher.getText().toString();
                passwd_ = passwd.getText().toString();
                advise_ = advise.getText().toString();
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
                    Log.d("TeacherLogin", teacher_);
                    FormBody formBody = new FormBody.Builder().add("name", name_).add("teacher", teacher_).add("passwd", passwd_).add("advise", advise_).build();
                    OkHttpClient client = new OkHttpClient();
                    String url0 = MainActivity.url + "/Teacher/Screchat/";
                    Request request = new Request.Builder()
                            .url(url0)
                            //.url("http://104.225.157.237:8000/Teacher/Screchat/")
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
                    Toast.makeText(CreChat.this, "群组创建成功", Toast.LENGTH_SHORT).show();
                }
            });
            Intent intent = new Intent(CreChat.this, TeacherHandle.class);
            startActivity(intent);
        }
        else{
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //放在UI线程弹Toast
                    Toast.makeText(CreChat.this, "群组创建失败,重名或老师密码错误", Toast.LENGTH_LONG).show();
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
