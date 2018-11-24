package com.movil.android.mydoctor;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    public EditText txtDireccionDoctor;
    public EditText txtTelDoctor;
    ArrayList<String> datos=new ArrayList<String>();
    public int banderaCambio;
    String idS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_medicamento);
        txtNombre = (EditText)findViewById(R.id.txt_nombre);
        txtPadecimiento = (EditText)findViewById(R.id.txt_padecimiento);
        txtNombreDoctor = (EditText)findViewById(R.id.txt_nombreDoctor);
        txtDireccionDoctor = (EditText)findViewById(R.id.txt_direccionDoctor);
        txtTelDoctor = (EditText)findViewById(R.id.editTextTelefonoDoctor);
        idS = getIntent().getStringExtra("medicamentoId");
        System.out.println("IDS---------------------"+idS);
        if (idS!=null){
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"mydoctorBD",null,1);
            SQLiteDatabase baseDeDatos = admin.getReadableDatabase();
            Cursor medicamento = baseDeDatos.rawQuery("select * from medicamento where idMedicamento = "+idS+";",null);
            if (medicamento.moveToFirst()){
                txtNombre.setText(medicamento.getString(1));
                txtPadecimiento.setText(medicamento.getString(2));
                String idDoc = medicamento.getString(13);
                Cursor doctor = baseDeDatos.rawQuery("select * from doctor where iddoctor = "+idDoc+";",null);
                if (doctor.moveToFirst()){
                    txtNombreDoctor.setText(doctor.getString(1));
                    txtDireccionDoctor.setText(doctor.getString(3));
                    txtTelDoctor.setText(doctor.getString(2));
                }
            }
            banderaCambio=1;
        }


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
        String direccionDoctor = txtDireccionDoctor.getText().toString();
        datos.add(direccionDoctor);
        String telDoctor = txtTelDoctor.getText().toString();
        datos.add(telDoctor);
        intentContinuar.putExtra("nombre",datos.get(0));
        intentContinuar.putExtra("padecimiento",datos.get(1));
        intentContinuar.putExtra("nombreDoctor",datos.get(2));
        intentContinuar.putExtra("direccionDoctor",datos.get(3));
        intentContinuar.putExtra("telDoctor",datos.get(4));
        if (banderaCambio==1){
            intentContinuar.putExtra("medicamentoId",idS);
        }
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
