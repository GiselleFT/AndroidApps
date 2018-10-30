package com.movil.android.mydoctor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class RecordatorioActivity extends AppCompatActivity{
    private TimePicker timePickerRecordatorio;
    private Calendar calendar;
    private String format = "";
    private TextView time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordatorio);

        timePickerRecordatorio = (TimePicker) findViewById(R.id.timePickerRecordatorio);
        time = (TextView) findViewById(R.id.textView1);
        calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        showTime(hour, min);
    }


    public void setTime(View view) {
        int hour = timePickerRecordatorio.getCurrentHour();
        int min = timePickerRecordatorio.getCurrentMinute();
        showTime(hour, min);
    }

    public void showTime(int hour, int min) {
        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }

        time.setText(new StringBuilder().append(hour).append(" : ").append(min)
                .append(" ").append(format));

        System.out.println(new StringBuilder().append(hour).append(" : ").append(min)
                .append(" ").append(format));
    }


    /*Al presionar el boton Aceptar
    Regresa a AgregarMedicamentoActivity*/
    public void aceptar(View v) {
        Intent intentAceptar = new Intent(this, AgregarMedicamentoActivity.class);
        startActivity(intentAceptar);
    }

    /*Al presionar el boton Cancelar
    Regresa a AgregarMedicamentoActivity*/
    public void cancelar(View v) {
        Intent intentCancelar = new Intent(this, AgregarMedicamentoActivity.class);
        startActivity(intentCancelar);
    }


}
