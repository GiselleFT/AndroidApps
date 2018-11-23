package com.movil.android.mydoctor.VerMedicamento;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.movil.android.mydoctor.R;

import java.util.ArrayList;

public class MedicamentosAdapter extends RecyclerView.Adapter<MedicamentosAdapter.ViewHolder> {

    ArrayList <MedicamentoModel> medicamentos;

    public MedicamentosAdapter(ArrayList<MedicamentoModel> medicamentos) {
        this.medicamentos = medicamentos;
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
        MedicamentoModel medicamentoModel = medicamentos.get(position);

        // Set item views based on your views and data model
        TextView textView = viewHolder.nombreMedicamento;
        textView.setText(medicamentoModel.getNombre());
        //FALTAN METODOS ONCLICK PARA AMBOS BOTONES
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
