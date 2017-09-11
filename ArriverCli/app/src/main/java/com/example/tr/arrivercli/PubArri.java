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

public class PubArri extends AppCompatActivity implements View.OnClickListener{

    public String title_;
    public String chat_;
    public String teacher_;
    public String sign_;
    public String Data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub_arri);
        Button submit= (Button)findViewById(R.id.pubarri_submit);
        submit.setOnClickListener(this);
    }

    public void onClick(View v){
        switch (v.getId()){
            case(R.id.pubarri_submit):
                EditText title =(EditText)findViewById(R.id.pubarri_title);
                EditText chat =(EditText)findViewById(R.id.pubarri_chat);
                EditText teacher =(EditText)findViewById(R.id.pubarri_teacher);
                EditText sign = (EditText)findViewById(R.id.pubarri_sign);
                title_ = title.getText().toString();
                chat_ = chat.getText().toString();
                teacher_ = teacher.getText().toString();
                sign_ = sign.getText().toString();
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
                    Log.d("TeacherLogin", chat_);
                    Log.d("TeacherLogin", teacher_);
                    FormBody formBody = new FormBody.Builder().add("title", title_).add("chat", chat_).add("teacher", teacher_).add("sign", sign_).build();
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://104.225.157.237:8000/Teacher/Spuarri/")
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
                    Toast.makeText(PubArri.this, "签到发布成功", Toast.LENGTH_SHORT).show();
                }
            });
            Intent intent = new Intent(PubArri.this, TeacherHandle.class);
            startActivity(intent);
        }
        else{
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //放在UI线程弹Toast
                    Toast.makeText(PubArri.this, "签到发布失败,重名或老师群组错误", Toast.LENGTH_LONG).show();
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
