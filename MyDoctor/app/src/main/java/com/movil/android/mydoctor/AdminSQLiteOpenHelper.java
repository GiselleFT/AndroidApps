package com.movil.android.mydoctor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper{

    public AdminSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase baseDeDatos) {
        baseDeDatos.execSQL("create table doctor (iddoctor int primary key, nombre text, telefono text, direccion text)");
        baseDeDatos.execSQL("create table medicamento (" +
                "idMedicamento int primary key, " +
                "nombre text, " +
                "padecimiento text, " +
                "horas int," +
                "minutos int," +
                "horasRecordatorio int," +
                "minutosRecordatorio int," +
                "numeroPeriodo int," +
                "periodo text," +
                "numeroDosis int," +
                "dosis text," +
                "fotoEnvase text," +
                "fotoMedicamento text," +
                "iddoctor int)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}