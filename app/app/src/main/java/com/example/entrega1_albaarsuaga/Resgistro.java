package com.example.entrega1_albaarsuaga;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Locale;

public class Resgistro extends AppCompatActivity {

    // Instanciamos los objetos para los campos y botones
    EditText editTextNombreUsuario, editTextContrasena, editTextContrasena2;
    Button buttonRegistrarse, buttonIniciarSesion, buttonCambiarIdioma;

    // Instanciamos el gestorDB
    GestorBD bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fijarIdioma();
        setContentView(R.layout.registro);

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
                // Obtenemos lo insertado en los editText
                String nombreUsu = editTextNombreUsuario.getText().toString();
                String contra = editTextContrasena.getText().toString();
                String contra2 = editTextContrasena2.getText().toString();

                // Si el usuario no ha insertado datos
                if(nombreUsu.equals("") | contra.equals("") | contra2.equals("")){
                    // Se le pide que los inserte
                    Toast.makeText(Resgistro.this, "Introduzca sus datos, por favor.", Toast.LENGTH_LONG).show();
                }else{
                    // Si ha insertado, se compruba que estén bien
                    if(contra.equals(contra2)){
                        // Si las contraseñas introducidas son las mismas
                        // Comprobamos si existe el usuario introducido
                        Boolean comprobarUsu = bd.existeNombreUsuario(nombreUsu);

                        if (comprobarUsu==false){
                            // Insertamos los datos en la base de datos
                            bd.agregarLogin(nombreUsu, contra);

                            // Notificamos al usuario que se ha registrado correctamente

                            // Configuración del canal para notidicaciones
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                NotificationChannel elCanal = new NotificationChannel("IdCanal", "NombreCanal", NotificationManager.IMPORTANCE_DEFAULT);
                                NotificationManager elManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                                elManager.createNotificationChannel(elCanal);
                            }

                            // Crear el builder
                            NotificationCompat.Builder elBuilder = new NotificationCompat.Builder(getApplicationContext(), "IdCanal");
                            elBuilder.setContentTitle("Registro completo.");
                            elBuilder.setContentText("¡Te has registrado con éxito!");
                            elBuilder.setSmallIcon(R.drawable.ic_launcher_background);
                            elBuilder.setAutoCancel(true);

                            // Notificar al usuario
                            NotificationManagerCompat elManagerCompat = NotificationManagerCompat.from(getApplicationContext());
                            elManagerCompat.notify(1,elBuilder.build());

                            // Redirigimos a la página principal
                            Intent intent = new Intent (v.getContext(), Login.class);
                            startActivityForResult(intent, 0);
                        }else{
                            // Informamos al usuario de que el nombre que ha introducido ya pertecene a la BD
                            Toast.makeText(Resgistro.this, "Este nombre de usuario ya está registrado.", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        // Informamos al usuario de que las contraseñas introducidas no son las mismas
                        Toast.makeText(Resgistro.this, "Las contraseñas no coinciden.", Toast.LENGTH_LONG).show();
                    }
                }

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
