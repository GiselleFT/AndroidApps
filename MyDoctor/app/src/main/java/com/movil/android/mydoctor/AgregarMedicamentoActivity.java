package com.movil.android.mydoctor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AgregarMedicamentoActivity extends AppCompatActivity {
    public EditText txtNombre;
    public EditText txtPadecimiento;
    public EditText txtNombreDoctor;
    ArrayList<String> datos=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_medicamento);
        txtNombre = (EditText)findViewById(R.id.txt_nombre);
        txtPadecimiento = (EditText)findViewById(R.id.txt_padecimiento);
        txtNombreDoctor = (EditText)findViewById(R.id.txt_nombreDoctor);
    }


    /*Al presionar el boton Agregar*/
    public void onClickButton(View view){


        //Toast.makeText(this, datos, Toast.LENGTH_SHORT).show();



    }

    /*Al presionar el boton Continuar
    Muestra activity del selector de Recordar cada (numero de veces que se tomará
    el medicamento por día, cada cuanto tiempo en un dia)*/
    public void continuar(View v) {
        Intent intentContinuar = new Intent(this, HorarioActivity.class);
        String nombre = txtNombre.getText().toString();
        datos.add(nombre);
        String padecimiento = txtPadecimiento.getText().toString();
        datos.add(padecimiento);
        String nombreDoctor = txtNombreDoctor.getText().toString();
        datos.add(nombreDoctor);
        intentContinuar.putExtra("nombre",datos.get(0));
        intentContinuar.putExtra("padecimiento",datos.get(1));
        intentContinuar.putExtra("nombreDoctor",datos.get(2));
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
