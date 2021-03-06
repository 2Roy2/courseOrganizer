package com.example.courseorganiser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.net.URLConnection;
import java.util.List;

public class CourseDetails extends AppCompatActivity {
    private String courseName=null;
    private Button btn_deleteCourse;
    private ListView lv_customers;
    private Button btn_addParticipant;
    private Button btn_returnToAllCourses;
    private Button btn_saveCourseAsFile;
    private TextView tv_numOfParticipantsDidntPay;
    private TextView tv_numOfParticipantsPayed;
    private TextView tv_participants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        btn_deleteCourse= (Button) findViewById(R.id.btn_deleteCourse);
        lv_customers =(ListView) findViewById(R.id.lv_customers);
        btn_addParticipant= (Button) findViewById(R.id.btn_addParticipant);
        btn_returnToAllCourses = (Button) findViewById(R.id.btn_returnToAllCourses);
        btn_saveCourseAsFile=(Button) findViewById(R.id.btn_saveCourseAsFile);
        tv_numOfParticipantsDidntPay = (TextView) findViewById(R.id.tv_numOfParticipantsDidntPay);
        tv_numOfParticipantsPayed = (TextView) findViewById(R.id.tv_numOfParticipantsPayed);
        tv_participants=(TextView) findViewById(R.id.tv_participants);

        MyDB db= new MyDB(CourseDetails.this);
        db.close();

        Intent intent= getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle!=null)
            courseName =(String) bundle.get("courseName");

        showDataOnActivity();

        btn_deleteCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    MyDB db = new MyDB(CourseDetails.this);
                    db.deleteOne(courseName);
                    db.close();

                    Intent intent = new Intent(CourseDetails.this, MainActivity.class);
                    startActivity(intent);
                }
                catch (Exception e){
                    Toast.makeText(CourseDetails.this,"ERROR",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_addParticipant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDB db = new MyDB(CourseDetails.this);

                Intent intent = new Intent(CourseDetails.this, ParticipantEdit.class);
                intent.putExtra("courseName", courseName);
                startActivity(intent);
            }
        });
        lv_customers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    MyDB db= new MyDB(CourseDetails.this);
                    ParticipantModel participant= (ParticipantModel) parent.getItemAtPosition(position);
                    String participantName=participant.getName();

                    Intent intent = new Intent(CourseDetails.this, ParticipantEdit.class);
                    intent.putExtra("courseName", courseName);
                    intent.putExtra("participantName", participantName);
                    intent.putExtra("isPayed",db.getParticipantIsPayed(participantName,courseName));
                    startActivity(intent);
                }
                catch (Exception e){
                    Toast.makeText(CourseDetails.this,"ERROR",Toast.LENGTH_SHORT).show();

                }
            }
        });

        btn_returnToAllCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseDetails.this, MainActivity.class);
                startActivity(intent);
            }
        });
        btn_saveCourseAsFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDB db= new MyDB(CourseDetails.this);
                if(db.downloadCourseDetails(courseName)){
                    Toast.makeText(CourseDetails.this,"saved, go to downloads to view it",Toast.LENGTH_SHORT).show();
                }

                else
                    Toast.makeText(CourseDetails.this,"ERROR, try again",Toast.LENGTH_SHORT).show();

                db.close();
            }
        });

    }
    public void showParticipantsOnLV(List<ParticipantModel> participants){
        ParticipantAdapter adapter= new ParticipantAdapter(this,R.layout.adapter_list_view_for_course_details,participants);
        lv_customers.setAdapter(adapter);

    }
    private void shareFile(File file) {

        Intent intentShareFile = new Intent(Intent.ACTION_SEND);

        intentShareFile.setAction(Intent.ACTION_SEND);
        intentShareFile.setType("text/*");
        intentShareFile.putExtra(Intent.EXTRA_STREAM,
                Uri.parse("content://"+file.getAbsolutePath()));
        intentShareFile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        startActivity(Intent.createChooser(intentShareFile, "share file with"));

    }

    public void showDataOnActivity(){
        MyDB db= new MyDB(CourseDetails.this);

        showParticipantsOnLV(db.getParticipantModelListOfCourse(courseName));

        tv_numOfParticipantsPayed.setText("Payed: "+ db.getHowMuchParticipantsPayed(courseName));
        tv_numOfParticipantsDidntPay.setText("Didn't Pay: "+db.getHowMuchParticipantsDidntPayed(courseName));
        tv_participants.setText("Participants: "+db.getHowMuchParticipantsInCourse(courseName));

        db.close();
    }

}