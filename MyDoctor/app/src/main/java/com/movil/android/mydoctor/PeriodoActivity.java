package com.movil.android.mydoctor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;

import java.util.ArrayList;

public class PeriodoActivity extends AppCompatActivity{

    ArrayList<String> datosS = new ArrayList<String>();
    NumberPicker numberPickerPeriodo;
    Spinner spinnerPeriodo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        numberPickerPeriodo = (NumberPicker) findViewById(R.id.numberPickerDosis);
        spinnerPeriodo = (Spinner) findViewById(R.id.spinnerPeriodo);
        setContentView(R.layout.activity_periodo);
    }


    /*Al presionar el boton Aceptar
    Muestra activity para agregar la dosis a tomar del medicamento*/
    public void continuar(View v) {
        Intent intentContinuar = new Intent(this, DosisActivity.class);
        String numeroPeriodo = String.valueOf(numberPickerPeriodo.getValue());
        //String periodo = (String)spinnerPeriodo.getSelectedItem();//checa esto, úede que no esté regresando la cadena que quiero
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
        String valor8 = getIntent().getStringExtra("horasRecordatorio");
        datosS.add(valor8);
        String valor9 = getIntent().getStringExtra("minutosRecordatorio");
        datosS.add(valor9);
        for (int i=0; i<datosS.size();i++){
            System.out.println(i+"   ------   "+datosS.get(i));
        }
        System.out.println("Esto es lo que buscaba" + numeroPeriodo);
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
