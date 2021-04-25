package com.example.das_entregaindividual2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

public class Camara extends AppCompatActivity  {


    // Instanciamos los objetos para los campos y botones del formulario
    Button camara, subirFoto;
    ImageView foto;
    EditText nombreFoto;

    // Instanciamos el bitmap que utilizaremos para la imagen
    private Bitmap bitmap;

    // Elegimos un código
    private int PICK_IMAGE_REQUEST = 1;

    // Path del .php que se encarga de la subida de imágenes
    private String UPLOAD_URL ="http://ec2-54-167-31-169.compute-1.amazonaws.com/aarsuaga010/WEB/subirFoto.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camara);

        // Asignamos los id a las variables
        foto = (ImageView)findViewById(R.id.imageViewFotoB);
        camara = (Button)findViewById(R.id.buttonCamara);
        subirFoto = (Button) findViewById(R.id.buttonBajar);
        nombreFoto = (EditText) findViewById(R.id.editTextNombreFotoB);

        // Abrir cámara el selecionar el botón
        camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Llamamos a la función que se encarga de abrir la cámara
                abrirCamara();
            }
        });

        // Subir foto al seleccionar el botón
        subirFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Llamamos a la función que se encarga de subir la foto
                subirFoto();
            }
        });

    }

    // Método para convertir el bitmap en un string en Base64
    public String getStringImagen(Bitmap foto){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        foto.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] fototransformada = stream.toByteArray();
        String fotoen64 = Base64.encodeToString(fototransformada,Base64.DEFAULT);

        return fotoen64;
    }

    /**Basado en el código extraído de youtube.com
     Pregunta: https://www.youtube.com/watch?v=vXK3hsXpt2I
     Autor: El Estudio de ANDROFAST
     Modificado por Alba Arsuaga, se ha mantenido la estructura pero se
     ha modificado el contenido.
     **/
    // Método que se encarga de subir la foto sacada
    private void subirFoto(){
        // Creamos la petición POST
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        // Si recibimos "success" como respuesta, la petición ha funcionado correctamente y la foto se ha subido
                        if(s.equals("success")){
                            Toast.makeText(Camara.this, "La foto se ha subido correctamente." , Toast.LENGTH_LONG).show();
                        }else{
                            // Si no, ha ocurrido un error
                            Toast.makeText(Camara.this, "Ha ocurrido un error.", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Toast.makeText(Camara.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Convertimos el bitmap en un String
                String imagen = getStringImagen(bitmap);
                // Obtenemos el nombre que el usuario ha introducido en el EditText
                String nombre = nombreFoto.getText().toString().trim();

                // Creamos un map con la foto y el nombre de la foto y lo devolvemos
                Map<String,String> params = new Hashtable<String, String>();
                params.put("foto", imagen);
                params.put("nombre", nombre);

                return params;
            }
        };

        // Creamos la requestQueue y agregar la solicitud a la cola
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    // Recoger imagen y ponerla en una ImageView
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            foto.setImageBitmap(bitmap);
        }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                // Obtenemos el mapa de bits de la Galería
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                // Definimos la configuración del mapa de bits en ImageView
                foto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    // Método para abrir la cámara
    private void abrirCamara() {
        // Si los permisos oportunos no han sido concedidos, pedirlos
        if (ContextCompat.checkSelfPermission(Camara.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Camara.this,
                    new String[]{Manifest.permission.CAMERA},
                    0);
        }

        // Abrir cámara
        Intent elIntentFoto= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Código para distinguir la llamada 1
        startActivityForResult(elIntentFoto, 1);
    }
}