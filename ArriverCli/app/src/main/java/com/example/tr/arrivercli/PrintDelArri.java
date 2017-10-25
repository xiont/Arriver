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

public class PrintDelArri extends AppCompatActivity implements View.OnClickListener{

    public String title_;
    public String teacher_;
    public String chat_;
    public String send_email_;
    public String Data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_del_arri);
        Button submit= (Button)findViewById(R.id.printdel_submit);
        submit.setOnClickListener(this);
    }

    public void onClick(View v){
        switch (v.getId()){
            case(R.id.printdel_submit):
                EditText title =(EditText)findViewById(R.id.printdel_title);
                EditText teacher =(EditText)findViewById(R.id.printdel_teacher);
                EditText chat =(EditText)findViewById(R.id.printdel_chat);
                EditText send_email = (EditText)findViewById(R.id.printdel_send_email);
                title_ = title.getText().toString();
                teacher_ = teacher.getText().toString();
                chat_ = chat.getText().toString();
                send_email_ = send_email.getText().toString();
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
                    FormBody formBody = new FormBody.Builder().add("title", title_).add("chat", chat_).add("teacher", teacher_).add("send_email", send_email_).build();
                    OkHttpClient client = new OkHttpClient();
                    String url0 = MainActivity.url + "/Teacher/Sprintdelarri/";
                    Request request = new Request.Builder()
                            .url(url0)
                            //.url("http://104.225.157.237:8000/Teacher/Sprintdelarri/")
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
                    Toast.makeText(PrintDelArri.this, "签到信息发送并删除成功", Toast.LENGTH_SHORT).show();
                }
            });
            Intent intent = new Intent(PrintDelArri.this, TeacherHandle.class);
            startActivity(intent);
        }
        else{
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //放在UI线程弹Toast
                    Toast.makeText(PrintDelArri.this, "任务处理失败", Toast.LENGTH_LONG).show();
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
