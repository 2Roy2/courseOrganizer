package com.example.courseorganiser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

public class ParticipantEdit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_edit);

        EditText et_participantName=(EditText) findViewById(R.id.et_participantName);
        Switch sw_payed=(Switch) findViewById(R.id.sw_payed);
        Button btn_deleteParticipant=(Button) findViewById(R.id.btn_deleteParticipant);
        Button btn_updateParticipantData=(Button) findViewById(R.id.btn_updateParticipantData);


    }
}