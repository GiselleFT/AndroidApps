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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

        //Datos a recuperar para registrar en calendario
        String todojunto = "";
        String [] todoseparado = null;
        String fecha = "";
        String numeroPeriodo = "";
        String periodo = "";

        //Datos a mostrar en el calendariio
        String medicamentoData = "";

        //Aqui recuperamos el id que nos mandan y podemos hacer consultas con él
        idS = getIntent().getStringExtra("medicamentoId");
        System.out.println("IDS---------------------"+idS);
        if (idS!=null){
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"myDoctorBD2",null,1);
            SQLiteDatabase baseDeDatos = admin.getReadableDatabase();
            Cursor medicamento = baseDeDatos.rawQuery("select * from medicamento where idMedicamento = "+idS+";",null);
            if (medicamento.moveToFirst()){
                todojunto =medicamento.getString(12);
                todoseparado = todojunto.split(",");
                fecha = todoseparado[1];
                //Suponiendo que el formato fecha = MM/DD/YYYY
                System.out.println("FECHA - - - - - - - "+fecha);
                //La fecha nos dice qué día se hizo el registro del medicamento y a partir de ahí comienza el tratamiento
                numeroPeriodo = medicamento.getString(7);
                //numeroPeriodo nos indica una cantidad Ejemplo: 1, 2, 3, 4 ...
                periodo = medicamento.getString(8);
                //periodo nos indica si hablamos de años, meses, semanas o dias
                System.out.println("numeroPeriodo: ---- "+numeroPeriodo+"          periodo"+periodo);
                medicamentoData = "Tomar: " + medicamento.getString(1)
                        + " para: " + medicamento.getString(2)
                        + " cantidad: " +  medicamento.getString(9) +  medicamento.getString(10)
                        + " cada: " + medicamento.getString(3)
                        + " horas con " + medicamento.getString(4) + " minutos" ;
                System.out.println("Medicamento data: -- " + medicamentoData);

            }
            baseDeDatos.close();
        }

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        Date date = new Date();
        actionBar.setTitle(dateFormatMonth.format(date));
        compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);

        /*Para generar los timestamp https://www.epochconverter.com/ como ejemplo
         * en esta parte se tienen que recuperar los timestamp y crearlos como eventos y añadirlos al calendario
         * el timestamp esta en milisegundos
         * AQUI SE LLENA EL CALENDARIO CON LOS EVENTOS*/

        //Conversion de periodo a numero de días (cuanto dura el tratamiento)
        int numPeriodo = Integer.parseInt(numeroPeriodo);
        int factorPeriodo = 0;
        if(periodo.startsWith("D")){//dias
            factorPeriodo = numPeriodo;
        }
        else if(periodo.startsWith("S")){//semanas
            factorPeriodo = numPeriodo * 7;
        }
        else if(periodo.startsWith("M")){//meses
            factorPeriodo = numPeriodo * 30;
        }
        else{//años
            factorPeriodo = numPeriodo * 365;
        }

        //Se modifica el formato del string fecha "MM/DD/YYYY"
        String [] infoFecha = fecha.split("/");
        fecha = infoFecha[1] +"/"+ infoFecha[0] +"/"+ infoFecha[2];

        //Se convierte la fecha a un tipo Date
        Date dateToConvert = new Date(fecha);
        //Se crea un calendario con la fecha actual de creacion de medicamento, para poder iterar la cantidad de dias solicitados
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(dateToConvert);
        //Se itera el número de dias que dura el tratamiento
        Event ev1 = null;
        for (int i = 0; i < factorPeriodo; i++) {
            //Se obtiene timestamp de cada día
            Long timestamp = calendar.getTimeInMillis();
            System.out.println("Timestamp: ---- " + timestamp);
            //Checar si tengo que agregarle la "L" al final de la cantidad
            //Se crea evento y se añade al calendario
            ev1 = new Event(Color.BLACK, timestamp, medicamentoData);
            compactCalendar.addEvent(ev1);
            //Se le agrega incrementa un día al calendario
            calendar.add(Calendar.DATE, 1);
        }



        /*Event ev1 = new Event(Color.BLACK, 1541376000000L, "Medicamento 1");
        Event ev2 = new Event(Color.BLACK, 1541462400000L, "Medicamento 2");
        Event ev3 = new Event(Color.BLACK, 1541534400000L, "Medicamento 3");
        compactCalendar.addEvent(ev1);
        compactCalendar.addEvent(ev2);
        compactCalendar.addEvent(ev3);*/

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
