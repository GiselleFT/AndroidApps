package com.movil.android.mydoctor;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class FotoMedicamentoActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_WRITE_CONTACTS = 0;
    ArrayList<String> datosS = new ArrayList<String>();
    ImageView img;
    Intent intent;
    Button imagen;
    String fotoMedicamento;
    int banderaFotoCargada = 0;
    Context contexto = this;
    public int banderaCambio;
    public int tomoFoto;
    String idS;
    String valor3G,valor5G;
    //Los valores no deben repetirse
    private static final int PICK_CONTACT_REQUEST = 99;
    private static final int READ_CONTACTS_PERMISSION = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto_envase);

        Button button = (Button) findViewById(R.id.buttonTomarFoto);
        img = (ImageView) findViewById(R.id.imageView);
        imagen = button;
        idS = getIntent().getStringExtra("medicamentoId");
        System.out.println("IDS---------------------"+idS);
        if (idS!=null){
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"myDoctorBD2",null,1);
            SQLiteDatabase baseDeDatos = admin.getReadableDatabase();
            Cursor medicamento = baseDeDatos.rawQuery("select * from medicamento where idMedicamento = "+idS+";",null);
            if (medicamento.moveToFirst()){
                String todojunto = medicamento.getString(12);
                String[] todoseparado = todojunto.split(",");
                String nombreImg = todoseparado[0];
                System.out.println(nombreImg);
                Bitmap image = BitmapFactory.decodeFile((new File("/storage/emulated/0/Android/data/com.movil.android.mydoctor/files/Pictures/"+nombreImg)).getAbsolutePath());
                img.setImageBitmap(image);
            }
            banderaCambio=1;
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                tomoFoto = 1;
                //startActivityForResult(intent, 0);
                // Ensure that there's a camera activity to handle the intent
                if (intent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    System.out.println("//////////////////////////////////////////////////////////////////////////////");
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        System.out.println(ex);
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(contexto,
                                "com.example.android.fileprovider",
                                photoFile);
                        fotoMedicamento = photoFile.getName();// checa esto porque guarda con otros nombres
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        addImageToGallery(photoFile.getAbsolutePath(),contexto);
                        startActivityForResult(intent, 1);
                    }
                }
            }
        });
    }


    public static void addImageToGallery(final String filePath, final Context context) {

        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(filePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }


    //Metodo para crear un nombre unico de cada fotografia
    //se guarda en el dispositivo y un respaldo dentro del paaquete de instalacion de la app
    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Medicamento_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);///storage/emulated/0/Pictures/Mydoctor
        File f = new File(Environment.getExternalStorageDirectory()+"/Pictures/Mydoctor");
        System.out.println(f.getAbsolutePath()+" --------- DIRECTORIO");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        System.out.println(image.getAbsolutePath()); // /storage/emulated/0/Android/data/com.movil.android.mydoctor/files/Pictures/Envase_20181124_132750_3030207602995301149.jpg
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    //Para mostrar la vista previa de la foto tomada en un image view
    //static final int REQUEST_IMAGE_CAPTURE = 1;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //tomarFoto();
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
        img.setImageBitmap(imageBitmap);
        banderaFotoCargada=1;
        try {
            SaveImage(this,imageBitmap);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("muere");
        }


    }


    /*Al presionar el boton Aceptar
    Muestra activity para tomar foto del medicamento*/
    public void continuar(View v) {
        if (banderaFotoCargada==1  || idS!=null) {
            Intent intentContinuar = new Intent(this, PrincipalActivity.class);
            String valor1 = getIntent().getStringExtra("nombre");
            datosS.add(valor1);
            String valor2 = getIntent().getStringExtra("padecimiento");
            datosS.add(valor2);
            String valor3 = getIntent().getStringExtra("nombreDoctor");
            datosS.add(valor3);
            String valor4 = getIntent().getStringExtra("direccionDoctor");
            datosS.add(valor4);
            String valor5 = getIntent().getStringExtra("telDoctor");
            datosS.add(valor5);
            String valor6 = getIntent().getStringExtra("horas");
            datosS.add(valor6);
            String valor7 = getIntent().getStringExtra("minutos");
            datosS.add(valor7);
            String valor8 = getIntent().getStringExtra("horasRecordatorio");
            datosS.add(valor8);
            String valor9 = getIntent().getStringExtra("minutosRecordatorio");
            datosS.add(valor9);
            String valor10 = getIntent().getStringExtra("numeroPeriodo");
            datosS.add(valor10);
            String valor11 = getIntent().getStringExtra("periodo");
            datosS.add(valor11);
            String valor12 = getIntent().getStringExtra("numeroDosis");
            datosS.add(valor12);
            String valor13 = getIntent().getStringExtra("dosis");
            datosS.add(valor13);
            String valor14 = getIntent().getStringExtra("fotoEnvase");
            datosS.add(valor14);
            String valor15 = null;

            datosS.add(valor15);
            System.out.println("**************************** VISTA DOSIS *************************");
            int bandera=1;
            for (int i = 0; i < datosS.size()-1; i++) {
                if (!(datosS.get(i).length()>0)){
                    bandera=0;
                }
                System.out.println(i + "   ------   " + datosS.get(i));
            }
            if (banderaCambio==1){
                intentContinuar.putExtra("medicamentoId",idS);
                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"myDoctorBD2",null,1);
                SQLiteDatabase baseDeDatos = admin.getReadableDatabase();
                if (bandera==1){
                    ContentValues registro = new ContentValues();
                    //--------------------BUSCA DOCTOR------------
                    Cursor doctor = baseDeDatos.rawQuery("select iddoctor from doctor where nombre = '"+valor3+"' and direccion = '"+valor4+"' and telefono = '"+valor5+"';",null);
                    int idD;
                    if (doctor.moveToFirst()){
                        idD=Integer.valueOf(doctor.getString(0));
                        //Toast.makeText(this, "¡Existe doctor!", Toast.LENGTH_SHORT).show();
                    }else{
                        Cursor fila2 = baseDeDatos.rawQuery("select iddoctor from doctor;",null);
                        int id2;
                        if (fila2.moveToLast()){
                            id2=Integer.valueOf(fila2.getString(0))+1;
                        }else{
                            id2=1;
                        }
                        //-------------------------ADJUNTA DOCTOR ----------------------------------
                        registro.put("iddoctor", id2);//DOCTOR
                        registro.put("nombre", valor3);
                        registro.put("telefono", valor5);
                        registro.put("direccion", valor4);
                        baseDeDatos.insert("doctor", null, registro);
                        idD=id2;
                        //int cantidad = baseDeDatos.delete("medicamento","idmedicamento > 1",null);
                        //System.out.println("SE BORRARON ESTOS: "+cantidad);
                        //Toast.makeText(this, "¡No existe doctor y cree id!", Toast.LENGTH_SHORT).show();
                        System.out.println("PASA1");
                        accederAgendaContactos();
                        String DisplayName = valor3;
                        String MobileNumber = valor5;

                        ArrayList < ContentProviderOperation > ops = new ArrayList < ContentProviderOperation > ();

                        ops.add(ContentProviderOperation.newInsert(
                                ContactsContract.RawContacts.CONTENT_URI)
                                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                                .build());

                        //------------------------------------------------------ Names
                        if (DisplayName != null) {
                            ops.add(ContentProviderOperation.newInsert(
                                    ContactsContract.Data.CONTENT_URI)
                                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                    .withValue(ContactsContract.Data.MIMETYPE,
                                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                                    .withValue(
                                            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                                            DisplayName).build());
                        }

                        //------------------------------------------------------ Mobile Number
                        if (MobileNumber != null) {
                            ops.add(ContentProviderOperation.
                                    newInsert(ContactsContract.Data.CONTENT_URI)
                                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                    .withValue(ContactsContract.Data.MIMETYPE,
                                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, MobileNumber)
                                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                                    .build());
                        }


                        // Asking the Contact provider to create a new contact
                        try {
                            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                    //--------------CONSULTA NUEVO ID MEDICAMENTO---------------------
                    Cursor fila = baseDeDatos.rawQuery("select idMedicamento from medicamento where idMedicamento='"+idS+"';",null);
                    int id=Integer.valueOf(idS);
                    //-------------------------ADJUNTA MEDICAMENTO ----------------------------------
                    registro = new ContentValues();
                    registro.put("idMedicamento", id);
                    registro.put("nombre", valor1);
                    registro.put("padecimiento", valor2);
                    registro.put("horas", Integer.valueOf(valor6));
                    registro.put("minutos", Integer.valueOf(valor7));
                    registro.put("horasRecordatorio", Integer.valueOf(valor8));
                    registro.put("minutosRecordatorio", Integer.valueOf(valor9));
                    registro.put("numeroPeriodo", Integer.valueOf(valor10));
                    registro.put("periodo", valor11);
                    registro.put("numeroDosis", Integer.valueOf(valor12));
                    registro.put("dosis", valor13);
                    registro.put("fotoEnvase", valor14);
                    if (tomoFoto!=1){
                        admin = new AdminSQLiteOpenHelper(this,"myDoctorBD2",null,1);
                        baseDeDatos = admin.getReadableDatabase();
                        Cursor medicamento = baseDeDatos.rawQuery("select * from medicamento where idMedicamento = "+idS+";",null);
                        if (medicamento.moveToFirst()) {
                            String todojunto = medicamento.getString(12);
                            String[] todoseparado = todojunto.split(",");
                            Date date = new Date();
                            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            System.out.println("Fecha: "+dateFormat.format(date));
                            valor15 = todoseparado[0]+","+dateFormat.format(date);
                        }
                    }else{
                        Date date = new Date();
                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        System.out.println("Fecha: "+dateFormat.format(date));
                        valor15 = fotoMedicamento+","+dateFormat.format(date);
                    }
                    registro.put("fotoMedicamento", valor15);
                    registro.put("iddoctor", idD);
                    baseDeDatos.update("medicamento",registro,"idMedicamento="+id, null);
                    baseDeDatos.close();
                    Toast.makeText(this, "¡Modificacion exitosa!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "¡No completaste todos los campos, registro fallido!", Toast.LENGTH_SHORT).show();
                }
            }else{
                //------------ALTA-----------------------
                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"myDoctorBD2",null,1);
                SQLiteDatabase baseDeDatos = admin.getReadableDatabase();
                if (bandera==1){
                    ContentValues registro = new ContentValues();
                    //--------------------BUSCA DOCTOR------------
                    Cursor doctor = baseDeDatos.rawQuery("select iddoctor from doctor where nombre = '"+valor3+"' and direccion = '"+valor4+"' and telefono = '"+valor5+"';",null);
                    int idD;
                    if (doctor.moveToFirst()){
                        idD=Integer.valueOf(doctor.getString(0));
                        //Toast.makeText(this, "¡Existe doctor!", Toast.LENGTH_SHORT).show();
                    }else{
                        Cursor fila2 = baseDeDatos.rawQuery("select iddoctor from doctor;",null);
                        int id2;
                        if (fila2.moveToLast()){
                            id2=Integer.valueOf(fila2.getString(0))+1;
                        }else{
                            id2=1;
                        }
                        //-------------------------ADJUNTA DOCTOR ----------------------------------
                        registro.put("iddoctor", id2);//DOCTOR
                        registro.put("nombre", valor3);
                        registro.put("telefono", valor5);
                        registro.put("direccion", valor4);
                        baseDeDatos.insert("doctor", null, registro);
                        idD=id2;
                        //Toast.makeText(this, "¡No existe doctor y cree id!", Toast.LENGTH_SHORT).show();
                        System.out.println("PASA1");
                        accederAgendaContactos();
                        String DisplayName = valor3;
                        String MobileNumber = valor5;

                        ArrayList < ContentProviderOperation > ops = new ArrayList < ContentProviderOperation > ();

                        ops.add(ContentProviderOperation.newInsert(
                                ContactsContract.RawContacts.CONTENT_URI)
                                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                                .build());

                        //------------------------------------------------------ Names
                        if (DisplayName != null) {
                            ops.add(ContentProviderOperation.newInsert(
                                    ContactsContract.Data.CONTENT_URI)
                                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                    .withValue(ContactsContract.Data.MIMETYPE,
                                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                                    .withValue(
                                            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                                            DisplayName).build());
                        }

                        //------------------------------------------------------ Mobile Number
                        if (MobileNumber != null) {
                            ops.add(ContentProviderOperation.
                                    newInsert(ContactsContract.Data.CONTENT_URI)
                                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                    .withValue(ContactsContract.Data.MIMETYPE,
                                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, MobileNumber)
                                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                                    .build());
                        }


                        // Asking the Contact provider to create a new contact
                        try {
                            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                    //--------------CONSULTA NUEVO ID MEDICAMENTO---------------------
                    Cursor fila = baseDeDatos.rawQuery("select idMedicamento from medicamento;",null);
                    int id=0;
                    if (fila.moveToLast()){
                        id=Integer.valueOf(fila.getString(0))+1;
                    }else{
                        id=1;
                    }
                    //-------------------------ADJUNTA MEDICAMENTO ----------------------------------
                    registro = new ContentValues();
                    registro.put("idMedicamento", id);
                    registro.put("nombre", valor1);
                    registro.put("padecimiento", valor2);
                    registro.put("horas", Integer.valueOf(valor6));
                    registro.put("minutos", Integer.valueOf(valor7));
                    registro.put("horasRecordatorio", Integer.valueOf(valor8));
                    registro.put("minutosRecordatorio", Integer.valueOf(valor9));
                    registro.put("numeroPeriodo", Integer.valueOf(valor10));
                    registro.put("periodo", valor11);
                    registro.put("numeroDosis", Integer.valueOf(valor12));
                    registro.put("dosis", valor13);
                    registro.put("fotoEnvase", valor14);
                    Date date = new Date();
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    System.out.println("Fecha: "+dateFormat.format(date));
                    registro.put("fotoMedicamento", fotoMedicamento+","+dateFormat.format(date));
                    registro.put("iddoctor", idD);
                    baseDeDatos.insert("medicamento", null, registro);
                    baseDeDatos.close();
                    Toast.makeText(this, "¡Registro exitoso!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "¡No completaste todos los campos, registro fallido!", Toast.LENGTH_SHORT).show();
                }
            }
            startActivity(intentContinuar);
        }else{
            Toast.makeText(this, "¡No has cargado ninguna imagen!", Toast.LENGTH_SHORT).show();
        }
    }


    /*Al presionar el boton Cancelar
        Regresa al menú principal, no se crea un registro en la BD
    */
    public void cancelar(View v) {
        Intent intentCancelar = new Intent(this, PrincipalActivity.class);
        startActivity(intentCancelar);
    }


    //-----------------------------------------------------------------------------------------------------------------------------

    private Context TheThis;

    public void SaveImage(Context context, Bitmap ImageToSave) throws IOException {

        TheThis = context;
        String CurrentDateAndTime = getCurrentDateAndTime();

        String imageFileName = "Medicamento_" + CurrentDateAndTime + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        try {
            FileOutputStream fOut = new FileOutputStream(file);
            ImageToSave.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
            MakeSureFileWasCreatedThenMakeAvabile(file);
            AbleToSave();
        }

        catch(FileNotFoundException e) {
            System.out.println(e);
            UnableToSave();
        }
        catch(IOException e) {
            System.out.println(e);
            UnableToSave();
        }

    }

    private void MakeSureFileWasCreatedThenMakeAvabile(File file){
        MediaScannerConnection.scanFile(TheThis,
                new String[] { file.toString() } , null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
    }

    private String getCurrentDateAndTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-­ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    private void UnableToSave() {
        Toast.makeText(TheThis, "¡No se ha podido guardar la imagen!", Toast.LENGTH_SHORT).show();
    }

    private void AbleToSave() {
        Toast.makeText(TheThis, "Imagen guardada en la galería.", Toast.LENGTH_SHORT).show();
    }


    private void mostrarExplicacion() {
        System.out.println("INTENTAAAAA");
        new AlertDialog.Builder(this)
                .setTitle("Autorización")
                .setMessage("Necesito permiso para acceder a los contactos de tu dispositivo.")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS}, READ_CONTACTS_PERMISSION);
                        }

                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Mensaje acción cancelada
                        mensajeAccionCancelada();
                    }
                })
                .show();
    }

    public void mensajeAccionCancelada(){
        Toast.makeText(getApplicationContext(),
                "Haz rechazado la petición, por favor considere en aceptarla.",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        System.out.println("PASA----------");
        switch (requestCode) {
            case READ_CONTACTS_PERMISSION:
                //Si el permiso a sido concedido abrimos la agenda de contactos
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    accederAgendaContactos();
                } else {
                    mensajeAccionCancelada();
                }
                break;
        }
    }

    private void accederAgendaContactos(){
        //si la API 23 a mas
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //Habilitar permisos para la version de API 23 a mas
            System.out.println("PASA2");
            int verificarPermisoReadContacts = ContextCompat
                    .checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS);
            //Verificamos si el permiso no existe
            if(verificarPermisoReadContacts != PackageManager.PERMISSION_GRANTED){
                System.out.println("PASA3");
                //verifico si el usuario a rechazado el permiso anteriormente
                if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)){
                    System.out.println("PASA4");
                    //Si a rechazado el permiso anteriormente muestro un mensaje
                    mostrarExplicacion();
                }else{
                    System.out.println("PASA4B");
                    //De lo contrario carga la ventana para autorizar el permiso
                    ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.WRITE_CONTACTS}, READ_CONTACTS_PERMISSION);
                    System.out.println("SALE");
                }

            }else{
                System.out.println("PASA3B");
                //Si el permiso ya fue concedido abrimos en intent de contactos
                abrirIntentContactos();
            }

        }else{//Si la API es menor a 23 - abrimos en intent de contactos
            System.out.println("PASA2B");
            abrirIntentContactos();
        }
    }

    private void abrirIntentContactos(){
        System.out.println("Solo son pruebas, en teoria ya di permisos desde la configuracion");
    }

}
