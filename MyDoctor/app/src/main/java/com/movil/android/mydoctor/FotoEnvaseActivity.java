package com.movil.android.mydoctor;

import android.content.Intent;
import android.graphics.Bitmap;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FotoEnvaseActivity extends AppCompatActivity {
    ImageView img;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto_envase);

        Button button = (Button) findViewById(R.id.buttonTomarFoto);
        img = (ImageView) findViewById(R.id.imageView);

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
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(intent, 0);
                        System.out.println("Entra1 - "+photoFile.getName());
                        System.out.println(""+photoFile.getAbsolutePath());
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
        //tomarFoto();
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
        img.setImageBitmap(imageBitmap);


    }


    /*Al presionar el boton Aceptar
    Muestra activity para tomar foto del medicamento*/
    public void continuar(View v) {
        Intent intentContinuar = new Intent(this, FotoMedicamentoActivity.class);
        startActivity(intentContinuar);
    }

    /*Al presionar el boton Cancelar
        Regresa al menú principal, no se crea un registro en la BD
    */
    public void cancelar(View v) {
        Intent intentCancelar = new Intent(this, PrincipalActivity.class);
        startActivity(intentCancelar);
    }


}
