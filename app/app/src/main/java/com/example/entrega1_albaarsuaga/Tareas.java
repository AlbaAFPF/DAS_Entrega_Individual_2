package com.example.entrega1_albaarsuaga;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class Tareas extends AppCompatActivity {

    // Referencia a la listView del xml
    ListView listaTareas;
    ArrayList<Tarea> lista;

    ArrayAdapter adaptador;
    GestorBD bd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fijarIdioma();
        setContentView(R.layout.tareas);

        // Asignamos los id a las variables
        listaTareas = (ListView)findViewById(R.id.listViewDeberes);

        bd = new GestorBD(getApplicationContext());
        lista = bd.llenarLista();

        adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista);
        listaTareas.setAdapter(adaptador);

        // Al hacer click en la lista
        listaTareas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tarea tarea = (Tarea) parent.getItemAtPosition(position);
                // EL usuario ha hecho click en la lista en el elemento pos
                // Llamamos a la ventana con esos datos
                Intent intent = new Intent (parent.getContext(), NuevaTarea.class);
                intent.putExtra("tarea", tarea);
                intent.putExtra("pos", position);
                startActivityForResult(intent, 2);
            }
        });
        adaptador.notifyDataSetChanged();

        // Al pulsar el botón "botonElegir", vamos a la página crear nueva tarea
        Button botonElegir = (Button) findViewById(R.id.botonElegir);
        botonElegir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), NuevaTarea.class);
                intent.putExtra("lista", lista);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data==null) return;
        super.onActivityResult(requestCode, resultCode, data);

        Tarea tarea=null;
        // Si nos ha llegado una tarea
        if (data.hasExtra("tarea")){
            tarea=(Tarea) data.getSerializableExtra("tarea");
            switch (requestCode){
                case 0:
                    // Llega desde nueva tarea, añadimos la tarea
                    lista.add(tarea);
                    break;
                case 2:
                    // Llega desde Editar/Eliminar
                    if (data.hasExtra("accion") && data.hasExtra("pos")){
                        String accion = data.getStringExtra("accion");
                        int pos=data.getIntExtra("pos",-1);
                        //int idx=buscaEnLista(tarea);
                        if (pos>=0){
                            switch (accion){
                                // Llega desde editar
                                case "EDITAR":
                                    // Modificamos el elemento
                                    lista.set(pos,tarea);
                                    break;
                                // Llega desde eliminar
                                case "ELIMINAR":
                                    // Eliminamos el elemento
                                    lista.remove(pos);
                                    break;
                            }
                        }
                    }
                    break;
            }
            // Notificamos los cambios al adaptador
            adaptador.notifyDataSetChanged();
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