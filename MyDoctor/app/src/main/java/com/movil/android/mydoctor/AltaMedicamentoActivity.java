package com.movil.android.mydoctor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AltaMedicamentoActivity extends AppCompatActivity {
    public EditText txtNombre;
    public EditText txtPadecimiento;
    public EditText txtFotoEnvase;
    public EditText txtFotoMedicamento;
    public EditText txtHorario;
    public EditText txtDosis;
    public EditText txtPeriodo;
    public EditText txtNombreDoctor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_medicamento);

        txtNombre = (EditText)findViewById(R.id.txt_nombre);
        txtPadecimiento = (EditText)findViewById(R.id.txt_padecimiento);
        txtFotoEnvase = (EditText)findViewById(R.id.txt_fotoEnvase);
        txtFotoMedicamento = (EditText)findViewById(R.id.txt_fotoMedicamento);
        txtHorario = (EditText)findViewById(R.id.txt_horario);
        txtDosis = (EditText)findViewById(R.id.txt_dosis);
        txtPeriodo = (EditText)findViewById(R.id.txt_periodo);
        txtNombreDoctor = (EditText)findViewById(R.id.txt_nombreDoctor);

    }


    public void onClickButton(View view){
        String nombre = txtNombre.getText().toString();
        String padecimiento = txtPadecimiento.getText().toString();
        String fotoEnvase = txtNombre.getText().toString();
        String fotoMedicamento = txtNombre.getText().toString();
        String horario = txtNombre.getText().toString();
        String dosis = txtNombre.getText().toString();
        String periodo = txtNombre.getText().toString();
        String nombreDoctor = txtNombre.getText().toString();

        String msg = "Nombre: " + nombre + " Padecimiento: " + padecimiento + " foto Envase: " + fotoEnvase
                + "foto Medicamento: " + fotoMedicamento + "horario: " + horario + " dosis: " + dosis
                + "periodo: " + periodo + "Nombre doctor: " + nombreDoctor;
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
