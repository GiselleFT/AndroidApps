package com.movil.android.mydoctor;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Spinner;

import java.util.ArrayList;

public class PeriodoActivity extends AppCompatActivity{

    ArrayList<String> datosS = new ArrayList<String>();
    NumberPicker numberPickerPeriodo;
    Spinner spinnerPeriodo;
    public int banderaCambio;
    String idS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_periodo);
        numberPickerPeriodo = (NumberPicker) findViewById(R.id.numberPickerPeriodo);
        numberPickerPeriodo.setMaxValue(90);
        numberPickerPeriodo.setMinValue(1);
        spinnerPeriodo = (Spinner) findViewById(R.id.spinnerPeriodo);
        idS = getIntent().getStringExtra("medicamentoId");
        System.out.println("IDS---------------------"+idS);
        if (idS!=null){
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"myDoctorBD2",null,1);
            SQLiteDatabase baseDeDatos = admin.getReadableDatabase();
            Cursor medicamento = baseDeDatos.rawQuery("select * from medicamento where idMedicamento = "+idS+";",null);
            if (medicamento.moveToFirst()){
                numberPickerPeriodo.setValue(Integer.valueOf(medicamento.getString(7)));
                int opcion;
                if (medicamento.getString(8).startsWith("D")){
                    opcion=0;
                }
                else if (medicamento.getString(8).startsWith("S")){
                    opcion=1;
                }
                else if (medicamento.getString(8).startsWith("M")){
                    opcion=2;
                }
                else{
                    opcion=3;
                }
                spinnerPeriodo.setSelection(opcion);
            }
            banderaCambio=1;
        }

    }


    /*Al presionar el boton Aceptar
    Muestra activity para agregar la dosis a tomar del medicamento*/
    public void continuar(View v) {
        Intent intentContinuar = new Intent(this, DosisActivity.class);
        numberPickerPeriodo = (NumberPicker) findViewById(R.id.numberPickerPeriodo);
        spinnerPeriodo = (Spinner) findViewById(R.id.spinnerPeriodo);
        String numeroPeriodo = String.valueOf(numberPickerPeriodo.getValue());
        String periodo = (String)spinnerPeriodo.getSelectedItem();
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
        String valor10 = numeroPeriodo;
        datosS.add(valor10);
        String valor11 = periodo;
        datosS.add(valor11);
        for (int i=0; i<datosS.size();i++){
            System.out.println(i+"   ------   "+datosS.get(i));
        }
        //System.out.println("Esto es lo que buscaba" + numeroPeriodo);
        intentContinuar.putExtra("nombre",datosS.get(0));
        intentContinuar.putExtra("padecimiento",datosS.get(1));
        intentContinuar.putExtra("nombreDoctor",datosS.get(2));
        intentContinuar.putExtra("direccionDoctor",datosS.get(3));
        intentContinuar.putExtra("telDoctor",datosS.get(4));
        intentContinuar.putExtra("horas",datosS.get(5));
        intentContinuar.putExtra("minutos",datosS.get(6));
        intentContinuar.putExtra("horasRecordatorio",datosS.get(7));
        intentContinuar.putExtra("minutosRecordatorio",datosS.get(8));
        intentContinuar.putExtra("numeroPeriodo",datosS.get(9));
        intentContinuar.putExtra("periodo",datosS.get(10));
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

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_popup, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.ItemAgregarMedicamento:
                Intent intentAltaMedicamento = new Intent(this, AgregarMedicamentoActivity.class);
                startActivity(intentAltaMedicamento);
                return true;
            case R.id.ItemVerMedicamentos:
                Intent intentVerMedicamentos = new Intent(this, VerMedicamentosActivity.class);
                startActivity(intentVerMedicamentos);
                return true;
            case R.id.ItemVerFarmacias:
                Intent intentVerFarmacias = new Intent(this, VerFarmaciasActivity.class);
                startActivity(intentVerFarmacias);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
