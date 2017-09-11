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

public class TeacherLogin extends AppCompatActivity implements View.OnClickListener{
    public String Data;
    public String user_;
    public String passwd_ ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_login);
        Button teacher_login= (Button)findViewById(R.id.teacher_login);
        teacher_login.setOnClickListener(this);

        Button teacher_regist = (Button) findViewById(R.id.teacher_regist);
        teacher_regist.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case(R.id.teacher_login):
                EditText user =(EditText)findViewById(R.id.teacher_login_username);
                EditText passwd = (EditText)findViewById(R.id.teacher_login_password);
                user_ = user.getText().toString();
                passwd_ = passwd.getText().toString();
                sendRequest();
                break;
            case(R.id.teacher_regist):
                Intent teacher_regist = new Intent(TeacherLogin.this,TeacherRegist.class);
                startActivity(teacher_regist);
                break;
            default:
                break;
        }
    }

    private void sendRequest(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("TeacherLogin",user_);
                    Log.d("TeacherLogin",passwd_);
                    FormBody formBody = new FormBody.Builder().add("username",user_).add("passwd",passwd_).build();
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://104.225.157.237:8000/Teacher/Slogin/")
                            .post(formBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    Data = response.body().string();
                    Log.d("TeacherLogin","###############################");
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
        TeacherLog T =TL(Data);
        Log.d("TeacherLogin",T.data);
        if(T.data.equals("True")) {
            Intent intent = new Intent(TeacherLogin.this, TeacherHandle.class);
            startActivity(intent);
        }
        else{
            Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //放在UI线程弹Toast
                            Toast.makeText(TeacherLogin.this, "Failed", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }
    private TeacherLog TL(String jsonData){
        Gson gson = new Gson();
        TeacherLog Te = gson.fromJson(jsonData, TeacherLog.class);
        return Te;
    }
    class TeacherLog {
        public String data;
        //public String passwd;

        TeacherLog(String u ){
            this.data = u;
            //this.passwd = p;
        }
    }
}

