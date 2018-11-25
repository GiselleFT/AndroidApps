package com.movil.android.mydoctor;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.R.layout.simple_list_item_1;

public class VerCalendarioActivity extends AppCompatActivity {
    CompactCalendarView compactCalendar;
    ListView listView;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());
    String idS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_calendario);

        //Aqui recuperamos el id que nos mandan y podemos hacer consultas con él

        idS = getIntent().getStringExtra("medicamentoId");
        System.out.println("IDS---------------------"+idS);
        if (idS!=null){
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"myDoctorBD2",null,1);
            SQLiteDatabase baseDeDatos = admin.getReadableDatabase();
            Cursor medicamento = baseDeDatos.rawQuery("select * from medicamento where idMedicamento = "+idS+";",null);
            if (medicamento.moveToFirst()){
                String todojunto =medicamento.getString(12);
                String [] todoseparado = todojunto.split(",");
                String fecha = todoseparado[1];
                System.out.println("FECHA - - - - - - - "+fecha);
                //La fecha nos dice qué día se hizo el registro del medicamento y a partir de ahí comienza el tratamiento
                String numeroPeriodo = medicamento.getString(7);
                //numeroPeriodo nos indica una cantidad Ejemplo: 1, 2, 3, 4 ...
                String periodo = medicamento.getString(8);
                //periodo nos indica si hablamos de años, meses, semanas o dias
                System.out.println("numeroPeriodo: ---- "+numeroPeriodo+"          periodo"+periodo);
            }
        }



        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        Date date = new Date();
        actionBar.setTitle(dateFormatMonth.format(date));
        compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);

        //Set an event for Teachers' Professional Day 2016 which is 21st of October
        /*Para generar los timestamp https://www.epochconverter.com/ como ejemplo
         * en esta parte se tienen que recuperar los timestamp y crearlos como eventos y añadirlos al calendario
         * el timestamp esta en milisegundos
         * AQUI SE LLENA EL CALENDARIO CON LOS EVENTOS
         * hacer distincion de medicamentos con un codigo de colores*/
        Event ev1 = new Event(Color.BLACK, 1541376000000L, "Medicamento 1");
        Event ev2 = new Event(Color.BLACK, 1541462400000L, "Medicamento 2");
        Event ev3 = new Event(Color.BLACK, 1541534400000L, "Medicamento 3");
        compactCalendar.addEvent(ev1);
        compactCalendar.addEvent(ev2);
        compactCalendar.addEvent(ev3);

        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getApplicationContext();
                /*AQUI SE RECUPERAN LOS EVENTOS POR DIA PARA MOSTRAR LOS MENSAJES ADECUADOS*/
                List<Event> listaEventos = compactCalendar.getEvents(dateClicked);
                //Se crea un arreglo segun el numero de eventos recuperados
                String [] infoEventos = new String[listaEventos.size()];
                listView = (ListView) findViewById(R.id.listView);
                //Se llena el arreglo con la informacion de cada evento
                for (int i = 0 ; i < listaEventos.size(); i++){
                    infoEventos[i] = listaEventos.get(i).getData().toString();
                    //Toast.makeText(context, "Recordatorio: " + listaEventos.get(i).getData(), Toast.LENGTH_SHORT).show();
                }
                //Se crea el arreglo que se mostrará en el ListView
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, infoEventos);
                listView.setAdapter(adapter);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                actionBar.setTitle(dateFormatMonth.format(firstDayOfNewMonth));
            }
        });

    }
}
