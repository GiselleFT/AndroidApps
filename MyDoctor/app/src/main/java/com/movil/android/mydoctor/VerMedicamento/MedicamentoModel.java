package com.movil.android.mydoctor.VerMedicamento;

public class MedicamentoModel {
    int idMedicamento;
    String nombre;

    public MedicamentoModel(int idMedicamento, String nombre) {
        this.idMedicamento = idMedicamento;
        this.nombre = nombre;
    }

    public MedicamentoModel() {
    }

    public int getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(int idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
