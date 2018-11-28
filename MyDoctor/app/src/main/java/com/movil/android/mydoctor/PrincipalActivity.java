package com.movil.android.mydoctor;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class PrincipalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        /*Se tiene que iniciar con el servicio de notificaciones*/
        servicio();
        /*Intent i = new Intent(PrincipalActivity.this, MyService.class);
        startService(i);*/
    }

    //Metodo del boton Agregar medicamento
    public void agregarMedicamento(View view){
        Intent intentAltaMedicamento = new Intent(this, AgregarMedicamentoActivity.class);
        startActivity(intentAltaMedicamento);
    }


    //Metodo del boton Ver medicamentos
    public void verMedicamentos(View view){
        Intent intentVerMedicamentos = new Intent(this, VerMedicamentosActivity.class);
        startActivity(intentVerMedicamentos);
    }

    //Metodo del boton Ver farmacias
    public void verFarmacias(View view){
        Intent intentVerFarmacias = new Intent(this, VerFarmaciasActivity.class);
        startActivity(intentVerFarmacias);
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

    //-----------------------------------------------------------------------------------------------------------------------------
    //Intent que cada 3 segundos se dispare servicio
    public void servicio() {
        System.out.println("Se inicia Servicio en Principal -- " );
        Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, MyAlarmReceiver.REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long firstMillis = System.currentTimeMillis(); //first run of alarm is immediate // arranca la aplicacion
        int intervalMillis = 1  * 3 * 1000; //3 segundos
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis, intervalMillis, pIntent);
        System.out.println("Se termina Servicio en Principal -- " );
    }


}
