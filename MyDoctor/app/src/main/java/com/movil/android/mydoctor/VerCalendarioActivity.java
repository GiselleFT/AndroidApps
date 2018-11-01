package com.movil.android.mydoctor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;

import java.util.TimeZone;

public class VerCalendarioActivity extends AppCompatActivity {
    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_calendario);

        calendarView = (CalendarView)findViewById(R.id.calendarView);
        
    }
}
