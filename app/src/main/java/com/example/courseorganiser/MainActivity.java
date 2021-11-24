package com.example.courseorganiser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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

        MyDB db= new MyDB(MainActivity.this);
        showCoursesOnLV(db.getCoursesNames());
        db.close();


        btn_createNewCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    MyDB db= new MyDB(MainActivity.this);

                    String name = et_NameOfCourse.getText().toString();
                    if(name.length()==0)
                        return;

                    CourseModel course= new CourseModel(name);

                    if(!db.checkForDuplicateCourse(name))
                        db.addOne(course);
                    else
                        Toast.makeText(MainActivity.this,"Course already existing",Toast.LENGTH_SHORT).show();

                    showCoursesOnLV(db.getCoursesNames());

                    db.close();
                }
                catch (Exception e){
                    Toast.makeText(MainActivity.this,"ERROR",Toast.LENGTH_SHORT).show();
                }

            }
        });

        lv_courses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    MyDB db= new MyDB(MainActivity.this);

                    String name= (String) parent.getItemAtPosition(position);
                    db.deleteOne(name);

                    db.close();

                    Intent intent = new Intent(MainActivity.this, CourseDetails.class);
                    intent.putExtra("courseName", name);
                    startActivity(intent);

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