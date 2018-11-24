package com.movil.android.mydoctor.VerMedicamento;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.movil.android.mydoctor.AdminSQLiteOpenHelper;
import com.movil.android.mydoctor.AgregarMedicamentoActivity;
import com.movil.android.mydoctor.R;
import com.movil.android.mydoctor.VerMedicamentosActivity;

import java.io.IOException;
import java.util.ArrayList;

public class MedicamentosAdapter extends RecyclerView.Adapter<MedicamentosAdapter.ViewHolder> {

    Activity activity;
    ArrayList <MedicamentoModel> medicamentos;

    public MedicamentosAdapter(ArrayList<MedicamentoModel> medicamentos, Activity activity) {
        this.medicamentos = medicamentos;
        this.activity = activity;
    }

    @Override
    public MedicamentosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_medicamento, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(MedicamentosAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        final MedicamentoModel medicamentoModel = medicamentos.get(position);

        // Set item views based on your views and data model
        TextView textView = viewHolder.nombreMedicamento;
        textView.setText(medicamentoModel.getNombre());
        //FALTAN METODOS ONCLICK PARA AMBOS BOTONES
        Button botonEliminar = viewHolder.botonEliminar;
        botonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity,VerMedicamentosActivity.class);
                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(activity,"mydoctorBD",null,1);
                SQLiteDatabase baseDeDatos = admin.getReadableDatabase();
                int cantidad = baseDeDatos.delete("medicamento","idmedicamento="+medicamentoModel.getIdMedicamento(),null);
                activity.startActivity(intent);
            }
        });
        Button botonModificar = viewHolder.botonModificar;
        botonModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity,AgregarMedicamentoActivity.class);
                intent.putExtra("medicamentoId",String.valueOf(medicamentoModel.getIdMedicamento()));
                activity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return medicamentos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView nombreMedicamento;
        public Button botonModificar;
        public Button botonEliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreMedicamento = (TextView) itemView.findViewById(R.id.nombreMedicamento);
            botonModificar = (Button) itemView.findViewById(R.id.modifica_button);
            botonEliminar = (Button) itemView.findViewById(R.id.elimina_button);

        }
    }

}
