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

public class StudentRegist extends AppCompatActivity implements View.OnClickListener{
    public String username_;
    public String name_;
    public String std_id_;
    public String passwd_;
    public String passwd2_;
    public String Data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_regist);
        Button submit = (Button)findViewById(R.id.student_reg_submit);
        submit.setOnClickListener(this);
    }
    public void onClick(View v){
        switch (v.getId()){
            case(R.id.student_reg_submit):
                EditText username =(EditText)findViewById(R.id.student_reg_username);
                EditText name =(EditText)findViewById(R.id.student_reg_name);
                EditText std_id =(EditText)findViewById(R.id.student_stid);
                EditText passwd =(EditText)findViewById(R.id.student_reg_password);
                EditText passwd2 = (EditText)findViewById(R.id.student_reg_password2);
                username_ = username.getText().toString();
                name_ = name.getText().toString();
                std_id_ = std_id.getText().toString();
                passwd_ = passwd.getText().toString();
                passwd2_ = passwd2.getText().toString();
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
                    Log.d("TeacherLogin", passwd_);
                    FormBody formBody = new FormBody.Builder().add("username", username_).add("name", name_).add("std_id",std_id_).add("passwd2", passwd2_).add("passwd", passwd_).build();
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://104.225.157.237:8000/Student/Sregist/")
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
        TeacherReg T =TL(Data);
        Log.d("TeacherLogin",T.data);
        if(T.data.equals("True")) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //放在UI线程弹Toast
                    Toast.makeText(StudentRegist.this, "Regist Successfully", Toast.LENGTH_SHORT).show();
                }
            });
            Intent intent = new Intent(StudentRegist.this, StudentLogin.class);
            startActivity(intent);
        }
        else{
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //放在UI线程弹Toast
                    Toast.makeText(StudentRegist.this, "Regist Failed", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private TeacherReg TL(String jsonData){
        Gson gson = new Gson();
        TeacherReg Te = gson.fromJson(jsonData, TeacherReg.class);
        return Te;
    }
    class TeacherReg {
        public String data;
        //public String passwd;

        TeacherReg(String u ){
            this.data = u;
            //this.passwd = p;
        }
    }
}
