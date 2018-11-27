package com.movil.android.mydoctor;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.movil.android.mydoctor.VerMedicamento.MedicamentoModel;
import com.movil.android.mydoctor.VerMedicamento.MedicamentosAdapter;

import java.util.ArrayList;

public class VerMedicamentosActivity extends AppCompatActivity {

    ArrayList<MedicamentoModel> medicamentosList = new ArrayList<MedicamentoModel>();
    MedicamentosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"myDoctorBD2",null,1);
        SQLiteDatabase baseDeDatos = admin.getReadableDatabase();
        Cursor medicamentos = baseDeDatos.rawQuery("select * from medicamento ;",null);
        if (medicamentos.moveToFirst()) {
            while (!medicamentos.isAfterLast()) {
                MedicamentoModel m = new MedicamentoModel();
                String id = medicamentos.getString(0);
                String name = medicamentos.getString(1);
                System.out.println(name);
                m.setIdMedicamento(Integer.valueOf(id));
                m.setNombre(name);
                medicamentosList.add(m);
                medicamentos.moveToNext();
            }
        }

        setContentView(R.layout.activity_ver_medicamentos);

        RecyclerView rvMedicamentos= (RecyclerView) findViewById(R.id.recyclerViewMedicamentos);
        adapter = new MedicamentosAdapter(medicamentosList, this);
        rvMedicamentos.setAdapter(adapter);
        rvMedicamentos.setLayoutManager(new LinearLayoutManager(this));

    }
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
