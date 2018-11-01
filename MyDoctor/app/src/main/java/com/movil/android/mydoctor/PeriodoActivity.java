package com.movil.android.mydoctor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class PeriodoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_periodo);
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
