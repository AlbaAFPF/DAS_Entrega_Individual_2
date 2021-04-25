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

    private Bitmap bitmap;

    private int PICK_IMAGE_REQUEST = 1;

    // Path del .php que se encarga de la subida de imágenes
    private String UPLOAD_URL ="http://192.168.0.112/DAS_Entrega2/subirFoto.php";

    private String KEY_IMAGEN = "foto";
    private String KEY_NOMBRE = "nombre";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camara);

        foto = (ImageView)findViewById(R.id.imageViewFotoB);
        camara = (Button)findViewById(R.id.buttonCamara);
        subirFoto = (Button) findViewById(R.id.buttonBajar);
        nombreFoto = (EditText) findViewById(R.id.editTextNombreFotoB);


        // Abrir cámara el selecionar el botón
        camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(Camara.this, "camara", Toast.LENGTH_LONG).show();
                abrirCamara();
            }
        });

        // Subir foto al seleccionar el botón
        subirFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

    }

    public String getStringImagen(Bitmap foto){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        foto.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] fototransformada = stream.toByteArray();
        String fotoen64 = Base64.encodeToString(fototransformada,Base64.DEFAULT);
        /*
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
         */
        return fotoen64;
    }


    private void uploadImage(){
        //Mostrar el diálogo de progreso
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Mostrando el mensaje de la respuesta
                        if(s.equals("success")){
                            Toast.makeText(Camara.this, "La foto se ha subido correctamente." , Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(Camara.this, s , Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Showing toast
                        Toast.makeText(Camara.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Convertir bits a cadena
                String imagen = getStringImagen(bitmap);

                //Obtener el nombre de la imagen
                String nombre = nombreFoto.getText().toString().trim();

                //Creación de parámetros
                Map<String,String> params = new Hashtable<String, String>();

                //Agregando de parámetros
                params.put(KEY_IMAGEN, imagen);
                params.put(KEY_NOMBRE, nombre);

                //Parámetros de retorno
                return params;
            }
        };

        //Creación de una cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Agregar solicitud a la cola
        requestQueue.add(stringRequest);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Imagen"), PICK_IMAGE_REQUEST);
    }


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
                //Cómo obtener el mapa de bits de la Galería
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Configuración del mapa de bits en ImageView
                foto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}