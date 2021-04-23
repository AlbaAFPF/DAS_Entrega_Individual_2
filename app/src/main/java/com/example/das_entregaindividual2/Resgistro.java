package com.example.das_entregaindividual2;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

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

    private static final String CHANNEL_ID = "101";

    // Instanciamos los objetos para los campos y botones
    EditText editTextNombreUsuario, editTextContrasena, editTextContrasena2;
    Button buttonRegistrarse, buttonIniciarSesion, buttonCambiarIdioma;

    // Instanciamos el gestorDB
    GestorBD bd;

    private String URL = "http://192.168.0.112/DAS_Entrega2/registro.php";

    private String nombreUsu, contra, contra2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fijarIdioma();
        setContentView(R.layout.registro);

        // Crear canal de notificaciones y obtener el token cada vez que se crea el app
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

        bd = new GestorBD(getApplicationContext());







        // Botón de registrarse
        buttonRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(v);
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


    //----------------------
    // Obtener el token
    public void getToken(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        // El token se muestra en consola al ejecutar la aplicación
                        Log.e("token", instanceIdResult.getToken());
                    }
                });
    }

    // Creamos el canal de notificaciones
    // https://developer.android.com/training/notify-user/build-notification
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "canalNotificaciones";
            String description = "Canal encargado de recibir las notificaciones de Firebase";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void suscribirseFCM (){
        FirebaseMessaging.getInstance().subscribeToTopic("newsletter")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(Resgistro.this, "¡Suscrito!", Toast.LENGTH_SHORT).show();
                    }
                });


    }


    //----------------------


    public void save (View view){
        nombreUsu = editTextNombreUsuario.getText().toString().trim();
        contra = editTextContrasena.getText().toString().trim();
        contra2 = editTextContrasena2.getText().toString().trim();

        if(!contra.equals(contra2)){
            Toast.makeText(Resgistro.this, "Las contraseñas no coinciden.", Toast.LENGTH_LONG).show();

        }else if(!nombreUsu.equals("") && !contra.equals("") && !contra2.equals("")){

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(Resgistro.this, "AAAAAAAA"+response, Toast.LENGTH_LONG).show();

                    if (response.equals("success")) {
                        Intent intent = new Intent (view.getContext(), Login.class);
                        startActivityForResult(intent, 0);
                    }else{
                        Toast.makeText(Resgistro.this, "La respuesta ha sido: "+response, Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Resgistro.this, error.toString().trim(), Toast.LENGTH_LONG).show();

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("nombre", nombreUsu);
                    data.put("contrasena", contra);
                    return data;
                }
            };
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
