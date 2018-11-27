package com.movil.android.mydoctor;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

public class RecordatorioActivity extends AppCompatActivity {
    private TimePicker timePickerRecordatorio;
    private Calendar calendar;
    private String format = "";
    ArrayList<String> datosS = new ArrayList<String>();
    int hour;
    int min;
    public int banderaCambio;
    String idS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordatorio);

        TimePicker timePickerRecordatorio = (TimePicker) findViewById(R.id.timePickerRecordatorio);
        calendar = Calendar.getInstance();

        String valor1 = getIntent().getStringExtra("nombre");
        datosS.add(valor1);
        String valor2 = getIntent().getStringExtra("padecimiento");
        datosS.add(valor2);
        String valor3 = getIntent().getStringExtra("nombreDoctor");
        datosS.add(valor3);
        String valor4 = getIntent().getStringExtra("direccionDoctor");
        datosS.add(valor4);
        String valor5 = getIntent().getStringExtra("telDoctor");
        datosS.add(valor5);
        String valor6 = getIntent().getStringExtra("horas");
        datosS.add(valor6);
        String valor7 = getIntent().getStringExtra("minutos");
        datosS.add(valor7);

        //hour = calendar.get(Calendar.HOUR_OF_DAY);
        //min = calendar.get(Calendar.MINUTE);


        idS = getIntent().getStringExtra("medicamentoId");
        System.out.println("IDS---------------------"+idS);
        if (idS!=null){
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"myDoctorBD2",null,1);
            SQLiteDatabase baseDeDatos = admin.getReadableDatabase();
            Cursor medicamento = baseDeDatos.rawQuery("select * from medicamento where idMedicamento = "+idS+";",null);
            if (medicamento.moveToFirst()){
                hour=(Integer.valueOf(medicamento.getString(5)));
                min=(Integer.valueOf(medicamento.getString(6)));
                timePickerRecordatorio.setCurrentHour(hour);
                timePickerRecordatorio.setCurrentMinute(min);
            }
            banderaCambio=1;
        }

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

        System.out.println(new StringBuilder().append(hour).append(" : ").append(min)
                .append(" ").append(format));
    }

    /*Al presionar el boton Continuar
    Muestra activity del periodo que durará el tratamiento*/
    public void continuar(View v) {
        Intent intentContinuar = new Intent(this, PeriodoActivity.class);
        timePickerRecordatorio = (TimePicker) findViewById(R.id.timePickerRecordatorio);
        hour = timePickerRecordatorio.getCurrentHour();
        min = timePickerRecordatorio.getCurrentMinute();
        showTime(hour, min);
        String hoursRecordatorio = String.valueOf(hour);
        datosS.add(hoursRecordatorio);
        String minRecordatorio = String.valueOf(min);
        datosS.add(minRecordatorio);
        intentContinuar.putExtra("nombre",datosS.get(0));
        intentContinuar.putExtra("padecimiento",datosS.get(1));
        intentContinuar.putExtra("nombreDoctor",datosS.get(2));
        intentContinuar.putExtra("direccionDoctor",datosS.get(3));
        intentContinuar.putExtra("telDoctor",datosS.get(4));
        intentContinuar.putExtra("horas",datosS.get(5));
        intentContinuar.putExtra("minutos",datosS.get(6));
        intentContinuar.putExtra("horasRecordatorio",datosS.get(7));
        intentContinuar.putExtra("minutosRecordatorio",datosS.get(8));
        System.out.println("horas del reloj --- "+datosS.get(7));
        System.out.println("minutos del reloj --- "+datosS.get(8));
        if (banderaCambio==1){
            intentContinuar.putExtra("medicamentoId",idS);
        }
        startActivity(intentContinuar);
    }

    /*Al presionar el boton Cancelar
        Regresa al menú principal, no se crea un registro en la BD
    */
    public void cancelar(View v) {
        Intent intentCancelar = new Intent(this, PrincipalActivity.class);
        startActivity(intentCancelar);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_popup, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.ItemAgregarMedicamento:
                Intent intentAltaMedicamento = new Intent(this, AgregarMedicamentoActivity.class);
                startActivity(intentAltaMedicamento);
                return true;
            case R.id.ItemVerMedicamentos:
                Intent intentVerMedicamentos = new Intent(this, VerMedicamentosActivity.class);
                startActivity(intentVerMedicamentos);
                return true;
            case R.id.ItemVerFarmacias:
                Intent intentVerFarmacias = new Intent(this, VerFarmaciasActivity.class);
                startActivity(intentVerFarmacias);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




}
