package com.movil.android.mydoctor;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.dropbox.core.v2.users.FullAccount;
import com.movil.android.mydoctor.OneDrive.DropboxClient;
import com.movil.android.mydoctor.OneDrive.LoginActivity;
import com.movil.android.mydoctor.OneDrive.URI_to_Path;
import com.movil.android.mydoctor.OneDrive.UploadTask;
import com.movil.android.mydoctor.OneDrive.UserAccountTask;
import com.movil.android.mydoctor.VerMedicamento.MedicamentoModel;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class PrincipalActivity extends AppCompatActivity {

    private static final int IMAGE_REQUEST_CODE = 101;
    private String ACCESS_TOKEN;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        //GIS DESCOMENTA ESTO
        /*AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"myDoctorBD2",null,1);
        SQLiteDatabase baseDeDatos = admin.getReadableDatabase();
        ContentValues registro = new ContentValues();
        registro.put("iddoctor", 100);//DOCTOR
        registro.put("nombre", "Gis");
        registro.put("telefono", "5512345678");
        registro.put("direccion", "Escom");
        baseDeDatos.insert("doctor", null, registro);
        registro = new ContentValues();
        registro.put("idMedicamento", 100);
        registro.put("nombre", "vitacilina");
        registro.put("padecimiento", "dolor");
        registro.put("horas", 0);
        registro.put("minutos", 2);//minutos del recordatorrio
        registro.put("horasRecordatorio", 20);
        registro.put("minutosRecordatorio", 0);
        registro.put("numeroPeriodo", 2);
        registro.put("periodo", "Día(s)");
        registro.put("numeroDosis", 2);
        registro.put("dosis", "miligramos");
        registro.put("fotoEnvase", "Envase_20181128_183608_4785902127191106960.jpg");
        registro.put("fotoMedicamento", "Medicamento_20181128_183618_7979042185458162380.jpg,28/11/2018");
        registro.put("iddoctor", 100);
        baseDeDatos.update("medicamento",registro,"idMedicamento="+id, null);
        baseDeDatos.close();*/

        if (!tokenExists()) {
            //No token
            //Back to LoginActivity
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        ACCESS_TOKEN = retrieveAccessToken();
        getUserAccount();

        Button fab = (Button) findViewById(R.id.buttonOneDrive);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //--------------RECUPERAR DATOS DE LA BASE---------------------------------

                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(context,"myDoctorBD2",null,1);
                //admin.onUpgrade(admin.getReadableDatabase(),1,1);
                //ESTA LINEA DE CÓDIGO SIRVE PARA RESETEAR LA BASE DE DATOS
                SQLiteDatabase baseDeDatos = admin.getReadableDatabase();
                Cursor medicamentos = baseDeDatos.rawQuery("select * from medicamento ;",null);
                String [][] medicamentosList = new String[medicamentos.getCount()][14];
                int i = 0;
                if (medicamentos.moveToFirst()) {
                    while (!medicamentos.isAfterLast()) {
                        medicamentosList [i][0] = medicamentos.getString(0);
                        medicamentosList [i][1] = medicamentos.getString(1);
                        medicamentosList [i][2] = medicamentos.getString(2);
                        medicamentosList [i][3] = medicamentos.getString(3);
                        medicamentosList [i][4] = medicamentos.getString(4);
                        medicamentosList [i][5] = medicamentos.getString(5);
                        medicamentosList [i][6] = medicamentos.getString(6);
                        medicamentosList [i][7] = medicamentos.getString(7);
                        medicamentosList [i][8] = medicamentos.getString(8);
                        medicamentosList [i][9] = medicamentos.getString(9);
                        medicamentosList [i][10] = medicamentos.getString(10);
                        medicamentosList [i][11] = medicamentos.getString(11);
                        medicamentosList [i][12] = medicamentos.getString(12);
                        medicamentosList [i][13] = medicamentos.getString(13);
                        //-------------------------------
                        i++;
                        medicamentos.moveToNext();
                    }
                }
                Cursor doctores = baseDeDatos.rawQuery("select * from doctor ;",null);
                String [][] doctoresList = new String[doctores.getCount()][14];
                i = 0;
                if (doctores.moveToFirst()) {
                    while (!doctores.isAfterLast()) {
                        doctoresList [i][0] = doctores.getString(0);
                        doctoresList [i][1] = doctores.getString(1);
                        doctoresList [i][2] = doctores.getString(2);
                        doctoresList [i][3] = doctores.getString(3);
                        //-------------------------------
                        i++;
                        doctores.moveToNext();
                    }
                }
                //--------------RECUPERAR DATOS DE LA BASE---------------------------------

                //-------------------EXCEL----------------------------

                //-------------------EXCEL----------------------------
                File file = saveExcelFile(context, "RespaldoMyDoctor.xls", doctoresList, medicamentosList);
                if (file != null) {
                    //Initialize UploadTask
                    new UploadTask(DropboxClient.getClient(ACCESS_TOKEN), file, getApplicationContext()).execute();
                }
            }
        });

        /*Se tiene que iniciar con el servicio de notificaciones*/
        servicio();
        /*Intent i = new Intent(PrincipalActivity.this, MyService.class);
        startService(i);*/

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



    private static File saveExcelFile(Context context, String fileName, String [][] doctores, String [][] medicamentos) {

        // check if available and not read only
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.e(null, "Storage not available or read only");
            return null;
        }

        boolean success = false;

        //New Workbook
        Workbook wb = new HSSFWorkbook();

        Cell c = null;

        //Cell style for header row
        CellStyle cs = wb.createCellStyle();
        cs.setFillForegroundColor(HSSFColor.LIME.index);
        cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        //New Sheet
        Sheet sheet1 = null;
        sheet1 = wb.createSheet("Doctores");

        // Generate column headings
        Row row = sheet1.createRow(0);

        c = row.createCell(0);
        c.setCellValue("Id");
        c.setCellStyle(cs);

        c = row.createCell(1);
        c.setCellValue("Nombre");
        c.setCellStyle(cs);

        c = row.createCell(2);
        c.setCellValue("Telefono");
        c.setCellStyle(cs);

        c = row.createCell(3);
        c.setCellValue("Direccion");
        c.setCellStyle(cs);

        sheet1.setColumnWidth(0, (15 * 250));
        sheet1.setColumnWidth(1, (15 * 250));
        sheet1.setColumnWidth(2, (15 * 250));
        sheet1.setColumnWidth(3, (15 * 250));

        int a=0;
        for (int x=0; x<doctores.length;x++){
            row = sheet1.createRow(x+1);
            a=0;
            for (int y=0;y<4;y++){
                c = row.createCell(y);
                c.setCellValue(doctores[x][y]);
            }
        }

        //---------------------Hoja de Medicamentos--------------------------
        /*"idMedicamento int primary key, " +
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
                "iddoctor int," +*/

        Sheet sheet2 = null;
        sheet2 = wb.createSheet("Medicamentos");

        // Generate column headings
        row = sheet2.createRow(0);
        c = row.createCell(0);
        c.setCellValue("Nombre");
        c.setCellStyle(cs);
        c = row.createCell(1);
        c.setCellValue("Padecimiento");
        c.setCellStyle(cs);
        c = row.createCell(2);
        c.setCellValue("Horas");
        c.setCellStyle(cs);
        c = row.createCell(3);
        c.setCellValue("Minutos");
        c.setCellStyle(cs);
        c = row.createCell(4);
        c.setCellValue("Minutos");
        c.setCellStyle(cs);
        c = row.createCell(5);
        c.setCellValue("Horas Recordatorio");
        c.setCellStyle(cs);
        c = row.createCell(6);
        c.setCellValue("Minutos Recordatorio");
        c.setCellStyle(cs);
        c = row.createCell(7);
        c.setCellValue("Numero de periodo");
        c.setCellStyle(cs);
        c = row.createCell(8);
        c.setCellValue("Periodo");
        c.setCellStyle(cs);
        c = row.createCell(9);
        c.setCellValue("Numero de dosis");
        c.setCellStyle(cs);
        c = row.createCell(10);
        c.setCellValue("Dosis");
        c.setCellStyle(cs);
        c = row.createCell(11);
        c.setCellValue("Nombre foto envase");
        c.setCellStyle(cs);
        c = row.createCell(12);
        c.setCellValue("Nombre foto medicamento");
        c.setCellStyle(cs);
        c = row.createCell(13);
        c.setCellValue("fechaRegistro");
        c.setCellStyle(cs);
        c = row.createCell(14);
        c.setCellValue("iddoctor");
        c.setCellStyle(cs);

        for (int i=0; i<15; i++){
            if (i==11||i==12){
                sheet2.setColumnWidth(i,(15 * 900));
            }else{
                sheet2.setColumnWidth(i,(15 * 400));
            }
        }

        for (int x=0; x<medicamentos.length;x++){
            row = sheet2.createRow(x+1);
            a=0;
            for (int y=0;y<15;y++){
                c = row.createCell(y);
                if (y==12){
                    String todojunto = medicamentos[x][y];
                    System.out.println("ESTO ES TODOJUNTO: "+todojunto);
                    String [] todoseparado = todojunto.split(",");
                    c.setCellValue(todoseparado[0]);
                    y++;
                    c = row.createCell(y);
                    c.setCellValue(todoseparado[1]);
                }else if (y==14){
                    c.setCellValue(medicamentos[x][13]);
                }else{
                    c.setCellValue(medicamentos[x][y]);
                }

            }
        }


        // Create a path where we will place our List of objects on external storage
        File file = new File(context.getExternalFilesDir(null), fileName);
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(file);
            wb.write(os);
            Log.w("FileUtils", "Writing file" + file);
            success = true;

        } catch (IOException e) {
            Log.w("FileUtils", "Error writing " + file, e);
        } catch (Exception e) {
            Log.w("FileUtils", "Failed to save file", e);
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception ex) {
            }
        }
        return file;
    }


    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }


    private boolean tokenExists() {
        SharedPreferences prefs = getSharedPreferences("com.example.valdio.dropboxintegration", Context.MODE_PRIVATE);
        String accessToken = prefs.getString("access-token", null);
        return accessToken != null;
    }

    private String retrieveAccessToken() {
        //check if ACCESS_TOKEN is stored on previous app launches
        SharedPreferences prefs = getSharedPreferences("com.example.valdio.dropboxintegration", Context.MODE_PRIVATE);
        String accessToken = prefs.getString("access-token", null);
        if (accessToken == null) {
            Log.d("AccessToken Status", "No token found");
            return null;
        } else {
            //accessToken already exists
            Log.d("AccessToken Status", "Token exists");
            return accessToken;
        }
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

    protected void getUserAccount() {
        if (ACCESS_TOKEN == null)return;
        new UserAccountTask(DropboxClient.getClient(ACCESS_TOKEN), new UserAccountTask.TaskDelegate() {
            @Override
            public void onAccountReceived(FullAccount account) {
                //Print account's info
                Log.d("User", account.getEmail());
                Log.d("User", account.getName().getDisplayName());
                Log.d("User", account.getAccountType().name());
                System.out.println("Esto es lo que obtengo: "+account.getEmail()+ "  -  "+account.getName().getDisplayName()+"  -  "+account.getAccountType().name());
            }
            @Override
            public void onError(Exception error) {
                Log.d("User", "Error receiving account details.");
            }
        }).execute();
    }

    private void upload() {
        if (ACCESS_TOKEN == null)return;
        //Select image to upload
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent,
                "Upload to Dropbox"), IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) return;
        // Check which request we're responding to
        if (requestCode == IMAGE_REQUEST_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                //Image URI received
                File file = new File(URI_to_Path.getPath(getApplication(), data.getData()));
                if (file != null) {
                    //Initialize UploadTask
                    new UploadTask(DropboxClient.getClient(ACCESS_TOKEN), file, getApplicationContext()).execute();
                }
            }
        }
    }

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) return;
        // Check which request we're responding to
        if (requestCode == IMAGE_REQUEST_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                //Image URI received
                File file = new File(URI_to_Path.getPath(getApplication(), data.getData()));
                if (file != null) {
                    //Initialize UploadTask
                    new UploadTask(DropboxClient.getClient(ACCESS_TOKEN), file, getApplicationContext()).execute();
                }
            }
        }
    }
     */

}
