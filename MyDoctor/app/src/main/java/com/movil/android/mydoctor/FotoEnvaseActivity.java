package com.movil.android.mydoctor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FotoEnvaseActivity extends AppCompatActivity {
    ArrayList<String> datosS = new ArrayList<String>();
    ImageView img;
    Intent intent;
    Button imagen;
    String fotoEnvase;
    int banderaFotoCargada = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto_envase);

        Button button = (Button) findViewById(R.id.buttonTomarFoto);
        img = (ImageView) findViewById(R.id.imageView);
        imagen = button;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(null,
                                "com.example.android.fileprovider",
                                photoFile);
                        fotoEnvase = photoFile.getName();// checa esto porque guarda con otros nombres
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(intent, 1);
                    }
                }
            }
        });
    }




    //Metodo para crear un nombre unico de cada fotografia
    //se guarda en el dispositivo y un respaldo dentro del paaquete de instalacion de la app
    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Envase_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    //Para mostrar la vista previa de la foto tomada en un image view
    //static final int REQUEST_IMAGE_CAPTURE = 1;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        if (banderaFotoCargada==1) {
            Intent intentContinuar = new Intent(this, FotoMedicamentoActivity.class);
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
            String valor14 = fotoEnvase;
            datosS.add(valor14);
            System.out.println("**************************** VISTA DOSIS *************************");
            for (int i = 0; i < datosS.size(); i++) {
                System.out.println(i + "   ------   " + datosS.get(i));
            }
            //System.out.println("Esto es lo que buscaba" + numeroPeriodo);
            intentContinuar.putExtra("nombre", datosS.get(0));
            intentContinuar.putExtra("padecimiento", datosS.get(1));
            intentContinuar.putExtra("nombreDoctor", datosS.get(2));
            intentContinuar.putExtra("direccionDoctor", datosS.get(3));
            intentContinuar.putExtra("telDoctor", datosS.get(4));
            intentContinuar.putExtra("horas", datosS.get(5));
            intentContinuar.putExtra("minutos", datosS.get(6));
            intentContinuar.putExtra("horasRecordatorio", datosS.get(7));
            intentContinuar.putExtra("minutosRecordatorio", datosS.get(8));
            intentContinuar.putExtra("numeroPeriodo", datosS.get(9));
            intentContinuar.putExtra("periodo", datosS.get(10));
            intentContinuar.putExtra("numeroDosis", datosS.get(11));
            intentContinuar.putExtra("dosis", datosS.get(12));
            intentContinuar.putExtra("fotoEnvase", datosS.get(13));
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

    //-------------------------------------------------------------------------------------------------------------------------------
    private Context TheThis;

    public void SaveImage(Context context, Bitmap ImageToSave) throws IOException {

        TheThis = context;
        String CurrentDateAndTime = getCurrentDateAndTime();

        String imageFileName = "Envase_" + CurrentDateAndTime + "_";
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



}
