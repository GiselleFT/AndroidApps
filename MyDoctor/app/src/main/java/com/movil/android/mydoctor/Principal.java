package com.movil.android.mydoctor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Principal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
    }


    //Metodo del boton Agregar medicamento
    public void agregarMedicamento(View view){
        Intent intentAltaMedicamento = new Intent(this, AltaMedicamentoActivity.class);
        startActivity(intentAltaMedicamento);
    }


}
