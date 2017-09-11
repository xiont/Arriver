package com.example.tr.arrivercli;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TeacherHandle extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_handle);
        Button addchat = (Button)findViewById(R.id.cre_chat);
        Button pubarri = (Button)findViewById(R.id.pub_arri);
        Button printarri = (Button)findViewById(R.id.printanddel_arri);
        addchat.setOnClickListener(this);
        pubarri.setOnClickListener(this);
        printarri.setOnClickListener(this);
    }

    public void onClick(View v){
        switch (v.getId()){
            case(R.id.cre_chat):
                Intent addchat_intent = new Intent(TeacherHandle.this,CreChat.class);
                startActivity(addchat_intent);
                break;
            case(R.id.pub_arri):
                Intent puarri_intent = new Intent(TeacherHandle.this,PubArri.class);
                startActivity(puarri_intent);
                break;
            case(R.id.printanddel_arri):
                Intent print_intent = new Intent(TeacherHandle.this,PrintDelArri.class);
                startActivity(print_intent);
                break;
            default:
                break;
        }
    }
}
