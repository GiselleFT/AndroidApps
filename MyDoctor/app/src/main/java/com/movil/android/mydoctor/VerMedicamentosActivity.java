package com.movil.android.mydoctor;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.movil.android.mydoctor.VerMedicamento.MedicamentoModel;
import com.movil.android.mydoctor.VerMedicamento.MedicamentosAdapter;

import java.util.ArrayList;

public class VerMedicamentosActivity extends AppCompatActivity {

    ArrayList<MedicamentoModel> medicamentosList = new ArrayList<MedicamentoModel>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"mydoctorBD",null,1);
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
        MedicamentosAdapter adapter = new MedicamentosAdapter(medicamentosList);
        rvMedicamentos.setAdapter(adapter);
        rvMedicamentos.setLayoutManager(new LinearLayoutManager(this));


    }
}
