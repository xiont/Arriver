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

public class StudentLogin extends AppCompatActivity implements View.OnClickListener{
    public String Data;
    public String username_;
    public String passwd_ ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);
        Button student_login= (Button)findViewById(R.id.student_login);
        student_login.setOnClickListener(this);

        Button student_regist = (Button) findViewById(R.id.student_regist);
        student_regist.setOnClickListener(this);
    }

    public void onClick(View v){
        switch (v.getId()){
            case(R.id.student_regist):
                Intent teacher_regist = new Intent(StudentLogin.this,StudentRegist.class);
                startActivity(teacher_regist);
                break;
            case(R.id.student_login):
                EditText username =(EditText)findViewById(R.id.student_login_username);
                EditText passwd = (EditText)findViewById(R.id.student_login_password);
                username_ = username.getText().toString();
                passwd_ = passwd.getText().toString();
                sendRequest();
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
                    Log.d("TeacherLogin",username_);
                    Log.d("TeacherLogin",passwd_);
                    FormBody formBody = new FormBody.Builder().add("username",username_).add("passwd",passwd_).build();
                    OkHttpClient client = new OkHttpClient();
                    String url0 = MainActivity.url + "/Student/Slogin/";
                    Request request = new Request.Builder()
                            .url(url0)
                            //.url("http://104.225.157.237:8000/Student/Slogin/")
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
            Intent intent = new Intent(StudentLogin.this, StudentHandle.class);
            startActivity(intent);
        }
        else{
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //放在UI线程弹Toast
                    Toast.makeText(StudentLogin.this, "Failed", Toast.LENGTH_LONG).show();
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
