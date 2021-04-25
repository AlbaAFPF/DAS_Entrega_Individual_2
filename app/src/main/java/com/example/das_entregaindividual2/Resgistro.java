package com.example.das_entregaindividual2;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Resgistro extends AppCompatActivity {



    // Instanciamos los objetos para los campos y botones
    EditText editTextNombreUsuario, editTextContrasena, editTextContrasena2;
    Button buttonRegistrarse, buttonIniciarSesion, buttonCambiarIdioma;

    // Variable con el ID del canal de notificaciones
    private static final String CHANNEL_ID = "101";

    // URL del .php para el registro en el servidor
    private String URL = "http://ec2-54-167-31-169.compute-1.amazonaws.com/aarsuaga010/WEB/registro.php";

    private String nombreUsu, contra, contra2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fijarIdioma();
        setContentView(R.layout.registro);

        // Crear canal de notificaciones, obtener el token y suscribirse
        createNotificationChannel();
        getToken();
        suscribirseFCM();

        // Asignamos los id a las variables
        editTextNombreUsuario= (EditText)findViewById(R.id.editTextNombreUsuario);
        editTextContrasena= (EditText)findViewById(R.id.editTextContrasena);
        editTextContrasena2= (EditText)findViewById(R.id.editTextContrasena2);

        buttonRegistrarse= (Button)findViewById(R.id.buttonRegistrarse);
        buttonIniciarSesion= (Button)findViewById(R.id.buttonIniciarSesion);

        buttonCambiarIdioma= (Button)findViewById(R.id.buttonCambiarIdioma);


        // Botón de registrarse
        buttonRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Método para guardar el usuario y la contraseña en la BD remota
                guardarUsuario(v);
            }
        });

        // Botón de cabiar el idioma
        buttonCambiarIdioma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Con alert dialog
                alertCambiarIdioma();
            }
        });

        // Botón de inicio de sesión
        buttonIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirigimos a la clase Login
                Intent intent = new Intent (v.getContext(), Login.class);
                startActivityForResult(intent, 0);
            }
        });
    }


    // Método para la obtención del token
    public void getToken(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        // Mostramos el token en consola
                        Log.e("token", instanceIdResult.getToken());
                    }
                });
    }

    // Creamos el canal de notificaciones
    private void createNotificationChannel() {
        // Creamos el NotificationChannel para API mayores o iguales que 26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Definimos el nombre y descripción del canal
            CharSequence name = "canalNotificaciones";
            String description = "Canal encargado de recibir las notificaciones de Firebase";
            // Definimos el nivel de importancia como por defecto
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            // Creamos el NotificationChannel con las especificaciones definidas y el ID
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Registrar el canal con el sistema
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // Método para suscribir al usuario al canal "recordatorios"
    private void suscribirseFCM (){
        FirebaseMessaging.getInstance().subscribeToTopic("recordatorios")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
    }

    // Método para guardar el usuario en la BD remota
    public void guardarUsuario(View view){
        // Guardamos los editText de la interfaz en variables
        nombreUsu = editTextNombreUsuario.getText().toString().trim();
        contra = editTextContrasena.getText().toString().trim();
        contra2 = editTextContrasena2.getText().toString().trim();

        // Si las contraseñas introducidas por el usuario son diferentes
        if(!contra.equals(contra2)){
            // Se le hace saber mediante un mensaje
            Toast.makeText(Resgistro.this, "Las contraseñas no coinciden.", Toast.LENGTH_LONG).show();

        // Si todos los campos están completos
        }else if(!nombreUsu.equals("") && !contra.equals("") && !contra2.equals("")){
            // Creamos la petición POST
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Si recibimos "success" como respuesta, la petición ha funcionado correctamente y nos hemos registrado
                    if (response.equals("success")) {
                        // Abrimos la pantalla de login
                        Intent intent = new Intent (view.getContext(), Login.class);
                        startActivityForResult(intent, 0);
                    }else{
                        // Si no, ha ocurrido un error
                        Toast.makeText(Resgistro.this, "Ha ocurrido un error.", Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(Resgistro.this, error.toString().trim(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    // Creamos un map con el nombre de usuario y contraseña y lo devolvemos
                    Map<String, String> data = new HashMap<>();
                    data.put("nombre", nombreUsu);
                    data.put("contrasena", contra);

                    return data;
                }
            };
            // Creamos la requestQueue y agregar la solicitud a la cola
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }


    // Método para el cambio de idioma
    private void alertCambiarIdioma() {
        // Array con los idiomas disponibles.
        final String[] listaIdiomas = {"Español", "Inglés"};

        // Creamos el AlertDialog con las opciones disponibles
        AlertDialog.Builder alert = new AlertDialog.Builder(Resgistro.this);
        alert.setSingleChoiceItems(listaIdiomas, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if(i==0){
                    // Si se selecciona el primer elemento, el idioma es Español
                    Param.locale = "es";
                    establecerIdioma("es");
                    recreate();
                }else if (i==1){
                    // Si se selecciona el primer elemento, el idioma es Inglés
                    Param.locale = "en";
                    establecerIdioma("en");
                    recreate();
                }
                // Cuando ya se ha selecionado
                dialog.dismiss();
            }
        });
        AlertDialog alertDia = alert.create();
        // Mostrar el alert
        alertDia.show();
    }

    // Método para establecer los idiomas
    private void establecerIdioma(String idioma) {
        // Usamos Locale para forzar la localización desde dentro de la aplicación
        Locale locale = new Locale(idioma);
        Locale.setDefault(locale);
        // Actualizamos la configuración de todos los recursos de la aplicación
        Configuration configuracion = new Configuration();
        configuracion.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(configuracion, getBaseContext().getResources().getDisplayMetrics());
        // Recargamos de nuevo la actividad
        finish();
        startActivity(getIntent());
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
