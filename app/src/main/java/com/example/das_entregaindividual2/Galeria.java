package com.example.das_entregaindividual2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Map;

public class Galeria extends AppCompatActivity {

    // Instanciamos los objetos para los campos y botones del formulario
    Button bajarFoto;
    ImageView foto;
    EditText nombreFotoB;

    // URL del .php para el bajar la foto de la base de datos remota
    String URL_BAJA_FOTO = "http://ec2-54-167-31-169.compute-1.amazonaws.com/aarsuaga010/WEB/bajarFoto.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.galeria);

        // Asignamos los id a las variables
        foto = (ImageView)findViewById(R.id.imageViewFotoB);
        bajarFoto = (Button) findViewById(R.id.buttonBajar);
        nombreFotoB = (EditText) findViewById(R.id.editTextNombreFotoB);

        // Bajar la foto y mostrarla al seleccionar el botón
        bajarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtenemos el nombre que ha introducido el usuario en el EditText
                String nombreFoto = nombreFotoB.getText().toString();
                // Si el nombre no es vacío
                if(!nombreFoto.equals("")){
                    // Bajar la foto con ese nombre (es un campo único en la BD)
                    bajaFoto(v, nombreFoto);
                }else{
                    // En caso contrario, se pide al usuario que introduzca un nombre
                    Toast.makeText(Galeria.this, "Introduzca un nombre, por favor.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**Basado en el código extraído de youtube.com
     Pregunta: https://www.youtube.com/watch?v=vXK3hsXpt2I
     Autor: El Estudio de ANDROFAST
     Modificado por Alba Arsuaga, se ha mantenido la estructura pero se
     ha modificado el contenido.
     **/
    // Método que se encarga de bajar la foto seleccionada
    private void bajaFoto(View view, String nombre){
        // Si se ha introducido un nombre
        if(!nombre.equals("")) {
            // Creamos la petición POST
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_BAJA_FOTO, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Si obtenemos una cadena vacía, no se ha recuperado la foto correctamente
                    if (response.equals("")) {
                        // Se informa al usuario del error.
                        Toast.makeText(Galeria.this, "No se ha podido recuperar la imagen.", Toast.LENGTH_LONG).show();
                    }else{
                        // En caso contrario, la imagen se ha recuperado correctamente y se puede mostrar

                        // Decodificamos la imagen
                        byte[] dcd = Base64.decode(response, Base64.DEFAULT);
                        Bitmap newBitmap = BitmapFactory.decodeByteArray(dcd, 0, dcd.length);
                        // Y la mostramos en el ImageView de la interfaz
                        foto.setImageBitmap(newBitmap);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(Galeria.this, error.toString().trim(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    // Creamos un map con el nombre de la imagen y lo devolvemos
                    Map<String, String> data = new HashMap<>();
                    data.put("nombre", nombre);

                    return data;
                }
            };
            // Creamos la requestQueue y agregar la solicitud a la cola
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }
}