package com.example.courseorganiser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class CourseDetails extends AppCompatActivity {
    private String courseName=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        Button btn_deleteCourse= (Button) findViewById(R.id.btn_deleteCourse);
        ListView lv_customers =(ListView) findViewById(R.id.lv_customers);
        Button btn_addParticipant= (Button) findViewById(R.id.btn_addParticipant);


        Intent intent= getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle!=null)
            courseName =(String) bundle.get("name");

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

            }
        });
    }


}