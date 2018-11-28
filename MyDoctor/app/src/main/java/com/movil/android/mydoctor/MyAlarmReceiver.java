package com.movil.android.mydoctor;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MyAlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    private NotificationManager notificationManager;
    private final int NOTIFICATION_ID = 1010;
    private AdminSQLiteOpenHelper admin;
    private Cursor medicamento;
    private SQLiteDatabase bd;
    private String alarma,descripcion,titulo;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("MyTestService", "MyAlarmReceiver metodo onReceive");
        Intent i = new Intent(context, MyTestService.class);
        context.startService(i);
        Calendar calendario = Calendar.getInstance();
        Calendar calendarioCompleto = Calendar.getInstance();
        /*Variables para poder pasarlas a un Date y hacer la comparacion
            fechaSistema y fechaRegistro son de tipo incompleto
            fechaCompletaSistema y fechaCompletaRegistro son de tipo completo
        */
        String fechaSistema, fechaRegistro, fechaCompletaSistema, fechaCompletaRegistro;
        //Se manejan dos tipos de forma (incompleto y completo)
        SimpleDateFormat timeIncompletoFormat = new SimpleDateFormat("MM/dd/YYYY");
        SimpleDateFormat timeCompletoFormat = new SimpleDateFormat("MM/dd/YYYY HH:mm");

        //Se obtiene fechaSistema y fechaCompletaSistema ACTUALES
        fechaSistema = timeIncompletoFormat.format(calendario.getTime());
        fechaCompletaSistema = timeCompletoFormat.format(calendario.getTime());
        Log.i("MyTestService", "fechaSistema: -- " + fechaSistema);
        Log.i("MyTestService", "fechaCompletaSistema: -- " + fechaCompletaSistema);

        admin = new AdminSQLiteOpenHelper(context, "myDoctorBD2", null, 1);
        bd = admin.getReadableDatabase();
        medicamento = bd.rawQuery("select * from medicamento;",null);
        medicamento.move(-1);//Se checaran todos los medicamentos

        //Informacion a recuperar del medicamento para la creacion de la notificacion
        String nombreMedicamento = "";//1
        String numerodosisMedicamento = "";//9
        String dosisMedicamento = "";//10
        String fotoEnvaseMedicamento = "";//11
        //Informacion para hacer calculo
        String primerRecordatorio = "";//5 y 6
        String numeroPeriodo = "";//7
        String periodo = "";//8
        String todoJunto = "";//12
        String [] todoSeparado = null;//12

        //Por cada medicamento recuperar informacion y hacer calculo si es momento de notificar
        while(medicamento.moveToNext()){
            //Se convierte la fechaSistema a un tipo Date
            Date dateFechaSistema = new Date(fechaSistema);

            //-----Primero se calcula la última fechaIncompleta en que termina el tratamiento
            todoJunto = medicamento.getString(12);
            todoSeparado = todoJunto.split(",");
            fechaRegistro = todoSeparado[1];
            //Se modifica el formato del string fechaRegistrp "MM/DD/YYYY"
            String [] infoFecha = fechaRegistro.split("/");
            fechaRegistro = infoFecha[1] +"/"+ infoFecha[0] +"/"+ infoFecha[2];
            //Se convierte la fechaRegistro a un tipo Date
            Date dateFechaRegistro = new Date(fechaRegistro);
            calendario.setTime(dateFechaRegistro);

            numeroPeriodo = medicamento.getString(7);
            periodo = medicamento.getString(8);

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

            calendario.add(Calendar.DATE, factorPeriodo);//Se obtiene ultimo dia de periodo

            //-----BANDERAS
            boolean esHoy = false;
            boolean esHora = false;

            //La fechaSistema está dentro del rango que dura el tratamiento
            //Sabemos que es el dia, Ahora hay que checar si esHora
            if(dateFechaSistema.compareTo(calendario.getTime()) <= 0 ){
                esHoy = true;
                //Se convierte la fechaCompletaSistema a un tipo Date
                Date dateFechaCompletaSistema = new Date(fechaCompletaSistema);
                //---Comenzamos por checar si coincide con el primer recordatorio del dia
                int primerRecordatorioHora =  Integer.parseInt(medicamento.getString(5));
                int primerRecordatorioMinuto =  Integer.parseInt(medicamento.getString(6));
                primerRecordatorio = medicamento.getString(5) + ":" + medicamento.getString(6);
                fechaCompletaRegistro = fechaRegistro + " " + primerRecordatorio;
                //Se convierte la fechaCompletaRegistro a un tipo Date
                Date dateFechaCompletaRegistro = new Date(fechaCompletaRegistro);
                calendarioCompleto.setTime(dateFechaCompletaRegistro);
                //Para saber si el recordatorio ya se paso al dia siguiente
                int diaFechaCompletaRegistro = calendarioCompleto.getTime().getDate();

                int tomarCadaHora = Integer.parseInt(medicamento.getString(3));
                int tomarCadaMinuto = Integer.parseInt(medicamento.getString(4));
                int totalMinutos = tomarCadaMinuto + (tomarCadaHora*60);

                //Mientras que no haya cambiado de dia y no se haya encontrado un recordatorio
                while(diaFechaCompletaRegistro == calendarioCompleto.getTime().getDate() && esHora==false){
                    if(dateFechaCompletaSistema.compareTo(dateFechaCompletaRegistro) == 0){
                        esHora = true;
                        break;
                    }
                    else{
                        esHora = false;
                        //Se incrementa el numero de minutos
                        calendarioCompleto.add(Calendar.MINUTE, totalMinutos);
                    }
                }
            }
            else{
                esHoy = false;
            }

            //Si ambas banderas son true es momento de notificar
            if(esHoy && esHora){
                Log.i("MyTestService", "Es momento de notificar: -- ");
                //------------------INFO
                nombreMedicamento = medicamento.getString(1);
                numerodosisMedicamento = medicamento.getString(9);
                dosisMedicamento = medicamento.getString(10);
                fotoEnvaseMedicamento = medicamento.getString(11);

                alarma="ID1";
                titulo=nombreMedicamento;
                descripcion ="Tomar: " + numerodosisMedicamento + " " + dosisMedicamento + " " + fotoEnvaseMedicamento;
                triggerNotification(context,titulo+"\n"+descripcion);


            }
        }
        bd.close();
    }

    private void triggerNotification(Context contexto, String t) {
        Intent notificationIntent = new Intent(contexto, VerMedicamentosActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(contexto, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] pattern = new long[]{2000, 1000, 2000};

        NotificationCompat.Builder builder = new NotificationCompat.Builder(contexto);
        builder.setContentIntent(contentIntent)
                .setTicker("" )
                .setContentTitle("Recordatorio")
                .setContentText(t)
                .setLargeIcon(BitmapFactory.decodeResource(contexto.getResources(), R.drawable.ic_notificacion))
                .setSmallIcon(R.drawable.ic_notificacion)
                .setAutoCancel(true) //Cuando se pulsa la notificación ésta desaparece
                .setSound(defaultSound)
                .setVibrate(pattern);

        Notification notificacion = new NotificationCompat.BigTextStyle(builder.setContentIntent(contentIntent))
                .bigText(t)
                .setBigContentTitle("Recordatorio")
                .setSummaryText("Resumen de recordatorio")
                .build();

        notificationManager = (NotificationManager) contexto.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notificacion);
    }

}
