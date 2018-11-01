package com.movil.android.mydoctor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class DosisActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dosis);
    }

    /*Al presionar el boton Aceptar
    Muestra activity para tomar foto del envase del medicamento*/
    public void continuar(View v) {
        Intent intentContinuar = new Intent(this, FotoEnvaseActivity.class);
        startActivity(intentContinuar);
    }

    /*Al presionar el boton Cancelar
    Regresa a AgregarMedicamentoActivity*/
    public void cancelar(View v) {
        Intent intentCancelar = new Intent(this, AgregarMedicamentoActivity.class);
        startActivity(intentCancelar);
    }
}
