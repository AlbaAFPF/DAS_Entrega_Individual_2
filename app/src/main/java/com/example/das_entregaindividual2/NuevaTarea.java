package com.example.das_entregaindividual2;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Locale;

public class NuevaTarea extends AppCompatActivity  {

    // Instanciamos los objetos para los campos y botones
    EditText edtCodigo, edtAsignatura, edtDocente;
    Button btnAgregar, buttonEditar, buttonEliminar, buttonMostrar;
    //Instancimos la lista de Tareas
    ArrayList<Tarea> lista;
    // Intanciamos la tarea y posición
    Tarea tarea;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fijarIdioma();
        setContentView(R.layout.nueva_tarea);

        // Asignamos los id a las variables
        edtCodigo= (EditText)findViewById(R.id.editTextCodigoT);
        edtAsignatura= (EditText)findViewById(R.id.editTextNombreAsig);
        edtDocente= (EditText)findViewById(R.id.editTextNombreDocente);

        btnAgregar= (Button)findViewById(R.id.buttonAnadirT);
        buttonEditar= (Button)findViewById(R.id.buttonEditarT);
        buttonEliminar= (Button)findViewById(R.id.buttonEliminarT);


        // Cargamos una posible tarea y una posible pos
        if (getIntent().hasExtra("tarea")){
            // Estoy en editar o eliminar
            tarea = (Tarea) getIntent().getSerializableExtra("tarea");
            edtCodigo.setText(tarea.getCodigo());
            // Marcamos que no se pueda editar el campo del código
            edtCodigo.setEnabled(false);
            edtAsignatura.setText(tarea.getAsignatura());
            edtDocente.setText(tarea.getTarea());
            // Invisibilizamos el botón de añadir
            btnAgregar.setVisibility(View.INVISIBLE);
        }else{
            // Estoy en añadir, invisibilizamos los botones para modificar y eliminar
            buttonEditar.setVisibility(View.INVISIBLE);
            buttonEliminar.setVisibility(View.INVISIBLE);
            tarea=null;
        }
        if (getIntent().hasExtra("pos")){
            // Asignamos la posición
            pos=getIntent().getIntExtra("pos",-1);
        }else{
            pos=-1;
        }

        // Instanciamos gestorBD para usar sus métodos
        final GestorBD gestorBD = new GestorBD(getApplicationContext());

        // Al pulsar el botón "btnAgregar", vamos a la página para crear nueva tarea
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Añadimos la tarea
                gestorBD.anadirTarea(edtCodigo.getText().toString(), edtAsignatura.getText().toString(), edtDocente.getText().toString());
                // Informamos al usuario de que la tarea se ha añadido correctamente
                Toast.makeText(getApplicationContext(), "La tarea se ha añadido correctamente.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                Tarea tarea=new Tarea(edtCodigo.getText().toString(),edtAsignatura.getText().toString(),edtDocente.getText().toString());
                intent.putExtra("tarea", tarea);
                setResult(RESULT_OK,intent);
                finish();

            }
        });

        buttonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Llamamos al método que se encarga de la modificación de valores
                AlertDialogModificar();
            }
        });

        buttonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Llamamos al método que se encarga de la eliminación de valores
                AlertDialogEliminar();
            }
        });


    }

    // Método para el botón de eliminación de tareas
    public void AlertDialogEliminar(){
        final GestorBD gestorBD = new GestorBD(getApplicationContext());
        // Creamos un alert en el que solamente si el usuario responde "Sí" se ejecuta la eliminación.
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("¡Cuidado!")
                .setMessage("¿Estás segur@ de que quieres eliminar la tarea?")
                .setPositiveButton("Sí.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Eliminamos la tarea
                        gestorBD.borrarTarea(edtCodigo.getText().toString());
                        // Informamos al usuario de que la tarea se ha eliminado con éxito
                        Toast.makeText(getApplicationContext(), "La tarea se ha eliminado correctamente.", Toast.LENGTH_SHORT).show();

                        Tarea tarea=new Tarea(edtCodigo.getText().toString(),edtAsignatura.getText().toString(),edtDocente.getText().toString());
                        Intent intent = new Intent();
                        intent.putExtra("tarea", tarea);
                        intent.putExtra("pos", pos);
                        intent.putExtra("accion", "ELIMINAR");
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                })
                .setNegativeButton("No.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }

    // Método para el botón de modificación de tareas
    public void AlertDialogModificar(){
        final GestorBD gestorBD = new GestorBD(getApplicationContext());
        // Creamos un alert en el que solamente si el usuario responde "Sí" se ejecuta la modificación.
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("¡Cuidado!")
                .setMessage("¿Estás segur@ de que quieres modificar la tarea?")
                .setPositiveButton("Sí.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Modificamos la tarea
                        gestorBD.modificaraTarea(edtCodigo.getText().toString(), edtAsignatura.getText().toString(), edtDocente.getText().toString());
                        // Informamos al usuario de que la tarea se ha modificado con éxito
                        Toast.makeText(getApplicationContext(), "La tarea se ha modificado correctamente.", Toast.LENGTH_SHORT).show();

                        Tarea tarea=new Tarea(edtCodigo.getText().toString(),edtAsignatura.getText().toString(),edtDocente.getText().toString());
                        Intent intent = new Intent();
                        intent.putExtra("tarea", tarea);
                        intent.putExtra("pos", pos);
                        intent.putExtra("accion", "EDITAR");
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                })
                .setNegativeButton("No.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
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