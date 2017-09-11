package com.example.tr.arrivercli;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public String url = "http://104.225.157.237:8000";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button teacher = (Button) findViewById(R.id.teacher);
        Button student = (Button) findViewById(R.id.student);
        teacher.setOnClickListener(this);
        student.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case(R.id.teacher):
                Intent teacher_intent = new Intent(MainActivity.this,TeacherLogin.class);
                startActivity(teacher_intent);
                break;
            case(R.id.student):
                Intent student_intent = new Intent(MainActivity.this,StudentLogin.class);
                startActivity(student_intent);
                break;
            default:
                break;
        }
    }
}
