package com.movil.android.mydoctor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class FotoMedicamentoActivity extends AppCompatActivity {
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto_medicamento);

        Button button = (Button)findViewById(R.id.buttonTomarFoto);
        img = (ImageView)findViewById(R.id.imageView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });
    }

    //Para mostrar la vista previa de la foto tomada en un image view
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap imageBitmap = (Bitmap)data.getExtras().get("data");
        img.setImageBitmap(imageBitmap);
    }

    /*Al presionar el boton Aceptar
   Se regresa a pantalla principal y se muestra mensaje de resultado de insercion de medicamento*/
    public void continuar(View v) {
        Intent intentContinuar = new Intent(this, PrincipalActivity.class);
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
