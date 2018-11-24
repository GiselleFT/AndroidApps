package com.movil.android.mydoctor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class PrincipalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
    }

    //Metodo del boton Agregar medicamento
    public void agregarMedicamento(View view){
        Intent intentAltaMedicamento = new Intent(this, AgregarMedicamentoActivity.class);
        startActivity(intentAltaMedicamento);
    }

    //Metodo del boton Ver calendario
    public void verCalendario(View view){
        Intent intentVerCalendario = new Intent(this, VerCalendarioActivity.class);
        startActivity(intentVerCalendario);
    }

    //Metodo del boton Ver medicamentos
    public void verMedicamentos(View view){
        Intent intentVerMedicamentos = new Intent(this, VerMedicamentosActivity.class);
        startActivity(intentVerMedicamentos);
    }

    //Metodo del boton Ver farmacias
    public void verFarmacias(View view){
        Intent intentVerFarmacias = new Intent(this, VerFarmaciasActivity.class);
        startActivity(intentVerFarmacias);
    }


}
