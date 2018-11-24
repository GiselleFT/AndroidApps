package com.movil.android.mydoctor;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;

public class HorarioActivity extends AppCompatActivity {

    public TextView datos;
    public NumberPicker horas;
    public NumberPicker minutos;
    ArrayList<String> datosS = new ArrayList<String>();
    public int banderaCambio;
    String idS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horario);
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
        NumberPicker horasP = (NumberPicker)findViewById(R.id.numberPickerHoras);
        horasP.setMaxValue(24);
        NumberPicker minutosP = (NumberPicker)findViewById(R.id.numberPickerMinuto);
        minutosP.setMaxValue(59);
        idS = getIntent().getStringExtra("medicamentoId");
        System.out.println("IDS---------------------"+idS);
        if (idS!=null){
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"myDoctorBD2",null,1);
            SQLiteDatabase baseDeDatos = admin.getReadableDatabase();
            Cursor medicamento = baseDeDatos.rawQuery("select * from medicamento where idMedicamento = "+idS+";",null);
            if (medicamento.moveToFirst()){
                horasP.setValue(Integer.valueOf(medicamento.getString(3)));
                minutosP.setValue(Integer.valueOf(medicamento.getString(4)));
            }
            banderaCambio=1;
        }

    }


    /*Al presionar el boton Continuar
    Manda a activity Hora inicio del primer recordatorio del día*/
    public void continuar(View v) {
        Intent intentContinuar = new Intent(this, RecordatorioActivity.class);
        horas = (NumberPicker)findViewById(R.id.numberPickerHoras);
        minutos = (NumberPicker)findViewById(R.id.numberPickerMinuto);
        String horasS = String.valueOf(horas.getValue());
        datosS.add(horasS);
        String minutosS = String.valueOf(minutos.getValue());
        datosS.add(minutosS);
        intentContinuar.putExtra("nombre",datosS.get(0));
        intentContinuar.putExtra("padecimiento",datosS.get(1));
        intentContinuar.putExtra("nombreDoctor",datosS.get(2));
        intentContinuar.putExtra("direccionDoctor",datosS.get(3));
        intentContinuar.putExtra("telDoctor",datosS.get(4));
        intentContinuar.putExtra("horas",datosS.get(5));
        intentContinuar.putExtra("minutos",datosS.get(6));
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
}
