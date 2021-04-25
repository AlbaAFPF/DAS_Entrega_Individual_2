package com.example.das_entregaindividual2;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.ActionBar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.ListenableWorker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Login extends AppCompatActivity {

    // Instanciamos los objetos para los campos y botones del formulario
    EditText editTextNombreUsuarioL, editTextContrasenaL;
    Button buttonIniciarSesionL;

    // URL del .php para el login en el servidor
    private String URL = "http://ec2-54-167-31-169.compute-1.amazonaws.com/aarsuaga010/WEB/login.php";

    String nombreUsuL, contraL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fijarIdioma();
        setContentView(R.layout.login);

        // Asignamos los id a las variables
        editTextNombreUsuarioL= (EditText)findViewById(R.id.editTextNombreUsuarioL);
        editTextContrasenaL= (EditText)findViewById(R.id.editTextContrasenaL);

        buttonIniciarSesionL= (Button)findViewById(R.id.buttonIniciarSesionL);


        // Botón de iniciar sesión
        buttonIniciarSesionL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Llamamos al método que hace el login con la BD remota
                login(v);
            }
        });
    }

    public void login(View view){
        // Guardamos los editText de la interfaz en variables
        nombreUsuL = editTextNombreUsuarioL.getText().toString();
        contraL = editTextContrasenaL.getText().toString();

        // Si todos los campos están completos
        if(!nombreUsuL.equals("") && !contraL.equals("")) {
            // Creamos la petición POST
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Si recibimos "success" como respuesta, la petición ha funcionado correctamente y hemos iniciado sesión
                    if (response.equals("success")) {
                        // Abrimos la pantalla del menú principal
                        Intent intent = new Intent(view.getContext(), MainActivity.class);
                        startActivityForResult(intent, 0);
                    }else{
                        // Si no, ha ocurrido un error.
                        Toast.makeText(Login.this, "Ha ocurrido un error.", Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(Login.this, error.toString().trim(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    // Creamos un map con el nombre de usuario y contraseña y lo devolvemos
                    Map<String, String> data = new HashMap<>();
                    data.put("nombre", nombreUsuL);
                    data.put("contrasena", contraL);

                    return data;
                }
            };
            // Creamos la requestQueue y agregar la solicitud a la cola
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }


    private void fijarIdioma() {
        // Usamos Locale para forzar la localización desde dentro de la aplicación
        Locale locale = new Locale(Param.locale);
        Locale.setDefault(locale);
        // Actualizamos la configuración de todos los recursos de la aplicación
        Configuration configuracion = new Configuration();
        configuracion.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(configuracion, getBaseContext().getResources().getDisplayMetrics());
    }
}