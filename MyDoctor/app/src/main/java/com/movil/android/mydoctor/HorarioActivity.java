package com.movil.android.mydoctor;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horario);
        datos = (TextView)findViewById(R.id.datos);
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
        datos.setText(valor1+" - "+valor2+" - "+valor3+" - "+valor4+" - "+valor5);
        horas = (NumberPicker)findViewById(R.id.numberPickerHoras);
        minutos = (NumberPicker)findViewById(R.id.numberPickerMinuto);
    }


    /*Al presionar el boton Continuar
    Manda a activity Hora inicio del primer recordatorio del día*/
    public void continuar(View v) {
        Intent intentContinuar = new Intent(this, RecordatorioActivity.class);
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
