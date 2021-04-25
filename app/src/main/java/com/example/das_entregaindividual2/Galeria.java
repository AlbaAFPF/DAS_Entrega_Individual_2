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

    String URL_BAJA_FOTO = "http://192.168.0.112/DAS_Entrega2/bajarFoto.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.galeria);

        foto = (ImageView)findViewById(R.id.imageViewFotoB);
        bajarFoto = (Button) findViewById(R.id.buttonBajar);
        nombreFotoB = (EditText) findViewById(R.id.editTextNombreFotoB);


        // Bajar la foto y mostrarla al seleccionar el botón
        bajarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(Camara.this, "camara", Toast.LENGTH_LONG).show();
                String nombreFoto = nombreFotoB.getText().toString();
                if(!nombreFoto.equals("")){
                    bajaFoto(v, nombreFoto);
                }else{
                    Toast.makeText(Galeria.this, "Introduzca un nombre, por favor.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // Método que se encarga de bajar la foto seleccionada
    private void bajaFoto(View view, String nombre){
        if(!nombre.equals("")) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_BAJA_FOTO, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("")) {
                        Toast.makeText(Galeria.this, "No se ha podido recuperar la imagen", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(Galeria.this, "Hemos recuperado la imagen", Toast.LENGTH_LONG).show();

                        byte[] dcd = Base64.decode(response, Base64.DEFAULT);
                        Bitmap newBitmap = BitmapFactory.decodeByteArray(dcd, 0, dcd.length);

                        foto.setImageBitmap(newBitmap);

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Galeria.this, error.toString().trim(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("nombre", nombre);
                    return data;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }

}