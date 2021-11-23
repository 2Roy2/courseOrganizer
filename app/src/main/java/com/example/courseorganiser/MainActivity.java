package com.example.courseorganiser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button btn_createNewCourse;
    EditText et_NameOfCourse;
    ListView lv_courses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_createNewCourse=(Button) findViewById(R.id.btn_createNewCourse);
        et_NameOfCourse= (EditText) findViewById(R.id.et_nameOfCourese);
        lv_courses=(ListView) findViewById(R.id.lv_courses);

        DBHandlerCourses db= new DBHandlerCourses(MainActivity.this);
        showCoursesOnLV(db.getCoursesNames());
        db.close();


        btn_createNewCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    DBHandlerCourses db= new DBHandlerCourses(MainActivity.this);

                    String name = et_NameOfCourse.getText().toString();
                    if(name.length()==0)
                        return;

                    CourseModel course= new CourseModel(name);

                    boolean successfullyAdded=db.addOne(course);
                    Toast.makeText(MainActivity.this,String.valueOf(successfullyAdded),Toast.LENGTH_SHORT).show();

                    showCoursesOnLV(db.getCoursesNames());

                    db.close();
                }
                catch (Exception e){
                    Toast.makeText(MainActivity.this,"ERROR",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    public void showCoursesOnLV(List<String> names){
        ArrayAdapter namesArrayAdapter=new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_dropdown_item_1line,names);
        lv_courses.setAdapter(namesArrayAdapter);

    }
}