package com.movil.android.mydoctor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class HorarioActivity extends AppCompatActivity {

    public TextView datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horario);
        datos = (TextView)findViewById(R.id.datos);
        String valor1 = getIntent().getStringExtra("nombre");
        String valor2 = getIntent().getStringExtra("padecimiento");
        String valor3 = getIntent().getStringExtra("nombreDoctor");
        datos.setText(valor1+" - "+valor2+" - "+valor3);
    }


    /*Al presionar el boton Continuar
    Manda a activity Hora inicio del primer recordatorio del día*/
    public void continuar(View v) {
        Intent intentContinuar = new Intent(this, RecordatorioActivity.class);
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
