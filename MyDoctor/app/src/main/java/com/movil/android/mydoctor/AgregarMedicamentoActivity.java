package com.movil.android.mydoctor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AgregarMedicamentoActivity extends AppCompatActivity {
    public EditText txtNombre;
    public EditText txtPadecimiento;
    public EditText txtNombreDoctor;

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
        String nombre = txtNombre.getText().toString();
        String padecimiento = txtPadecimiento.getText().toString();
        String fotoEnvase = txtNombre.getText().toString();
        String fotoMedicamento = txtNombre.getText().toString();
        String dosis = txtNombre.getText().toString();
        String periodo = txtNombre.getText().toString();
        String nombreDoctor = txtNombre.getText().toString();

        String msg = "Nombre: " + nombre + " Padecimiento: " + padecimiento + " foto Envase: " + fotoEnvase
                + "foto Medicamento: " + fotoMedicamento + " dosis: " + dosis
                + "periodo: " + periodo + "Nombre doctor: " + nombreDoctor;
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /*Al presionar el boton Horario
    Muestra el selector de horario (numero de veces que se tomará el medicamento por día, cada cuanto tiempo en un dia)*/
    public void muestraSelectorHorario(View v) {
        Intent intentHorario = new Intent(this, HorarioActivity.class);
        startActivity(intentHorario);
    }


    /*Al presionar el boton Recordatorio
    Muestra el selector de recordatorio (Primera hora en la que se tiene que tomar o administrar)*/
    public void muestraSelectorRecordatorio(View v) {
        Intent intentRecordatorio = new Intent(this, RecordatorioActivity.class);
        startActivity(intentRecordatorio);
    }


    /*Al presionar el boton Periodo
    Muestra el selector de periodo (Duracion del tratamiento)*/
    public void muestraSelectorPeriodo(View v) {
        Intent intentPeriodo = new Intent(this, PeriodoActivity.class);
        startActivity(intentPeriodo);
    }


    /*Al presionar el boton Dosis
    Muestra el selector de dosis (miligramos o mililitros)*/
    public void muestraSelectorDosis(View v) {
        Intent intentDosis = new Intent(this, DosisActivity.class);
        startActivity(intentDosis);
    }

    /*Al presionar el boton Foto Envase
    Permite tomar la foto del envase del medicamento*/
    public void tomaFotoEnvase(View v) {
        Intent intentFotoEnvase = new Intent(this, FotoEnvaseActivity.class);
        startActivity(intentFotoEnvase);
    }


    /*Al presionar el boton Foto Medicamento
    Permite tomar la foto del medicamento*/
    public void tomaFotoMedicamento(View v) {
        Intent intentFotoMedicamento = new Intent(this, FotoMedicamentoActivity.class);
        startActivity(intentFotoMedicamento);
    }
}
