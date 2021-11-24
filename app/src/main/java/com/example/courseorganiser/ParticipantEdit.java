package com.example.courseorganiser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class ParticipantEdit extends AppCompatActivity {
    String courseName=null;
    String participantName=null;
    boolean payed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_edit);

        EditText et_participantName=(EditText) findViewById(R.id.et_participantName);
        Switch sw_payed=(Switch) findViewById(R.id.sw_payed);
        Button btn_deleteParticipant=(Button) findViewById(R.id.btn_deleteParticipant);
        Button btn_updateParticipantData=(Button) findViewById(R.id.btn_updateParticipantData);

        Intent intent= getIntent();
        Bundle bundle = intent.getExtras();

        if(intent.hasExtra("courseName"))
            courseName =(String) bundle.get("name");
        if(intent.hasExtra("participantName"))
            participantName =(String) bundle.get("participantName");



        btn_deleteParticipant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(participantName==null){
                        Toast.makeText(ParticipantEdit.this,"No user to delete",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else {
                        MyDB db= new MyDB(ParticipantEdit.this);

                        db.deleteOneParticipant(participantName,courseName);

                        db.close();
                        Intent intent = new Intent(ParticipantEdit.this, CourseDetails.class);
                        startActivity(intent);
                    }

                }
                catch (Exception e){
                    Toast.makeText(ParticipantEdit.this,"ERROR",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_updateParticipantData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(et_participantName.getText().toString().length()==0){
                        Toast.makeText(ParticipantEdit.this,"please Enter Participant name",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    MyDB db= new MyDB(ParticipantEdit.this);
                    boolean isNewUser=(participantName==null);

                    participantName=et_participantName.getText().toString();
                    payed=sw_payed.getShowText();

                    ParticipantModel participant= new ParticipantModel(participantName,payed);

                    if(isNewUser){
                        db.addOneParticipant(participant,courseName);

                        db.close();
                        Intent intent = new Intent(ParticipantEdit.this, CourseDetails.class);
                        startActivity(intent);
                    }
                    else{
                        db.updateParticipant(participant,courseName);

                        db.close();
                        Intent intent = new Intent(ParticipantEdit.this, CourseDetails.class);
                        startActivity(intent);
                    }

                }
                catch (Exception e){
                    Toast.makeText(ParticipantEdit.this,"ERROR",Toast.LENGTH_SHORT).show();

                }
            }
        });


    }
}