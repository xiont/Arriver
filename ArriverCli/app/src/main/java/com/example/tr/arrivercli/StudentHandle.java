package com.example.tr.arrivercli;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StudentHandle extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_handle);
        Button addchat = (Button)findViewById(R.id.add_chat);
        Button stdarri = (Button)findViewById(R.id.std_arri);
        addchat.setOnClickListener(this);
        stdarri.setOnClickListener(this);
    }

    public void onClick(View v){
        switch (v.getId()){
            case(R.id.add_chat):
                Intent addchat_intent = new Intent(StudentHandle.this,AddChat.class);
                startActivity(addchat_intent);
                break;
            case(R.id.std_arri):
                Intent puarri_intent = new Intent(StudentHandle.this,StdArri.class);
                startActivity(puarri_intent);
                break;
            default:
                break;
        }
    }
}
