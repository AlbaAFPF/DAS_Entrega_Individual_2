package com.example.entrega1_albaarsuaga;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Intanciamos la ListView
    ListView ListViewItem;
    List<ItemListViewPers> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Asignamos los id a las variables
        ListViewItem = findViewById(R.id.ListViewItem);

        // Creamos un ApaptadorListView
        AdaptadorListView adaptador = new AdaptadorListView(this, getData());
        ListViewItem.setAdapter(adaptador);

        // Acciones cuando se presiona un elemento del ListViewItem
        ListViewItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                ItemListViewPers item = list.get(i);
                // Si el nombre del item es "Exámenes" abrir la pantalla de exámenes
                if(item.nombre=="Exámenes"){
                    Intent intent = new Intent (view.getContext(), Examenes.class);
                    startActivityForResult(intent, 0);
                // Si el nombre del item es "Tareas" abrir la pantalla de tareas
                }else if(item.nombre=="Tareas"){
                    Intent intent = new Intent (view.getContext(), Tareas.class);
                    startActivityForResult(intent, 0);
                // Si el nombre del item es "Calendario" abrir el calendario
                }else if(item.nombre=="Calendario"){
                    // Abrimos la aplicación de calendario
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("content://com.android.calendar/time/"));
                    startActivityForResult(intent, 0);
                }
            }
        });
    }

    // Método para añadir datos
    private List<ItemListViewPers> getData() {
        list = new ArrayList<>();

        // Añadimos los items a la lista
        list.add(new ItemListViewPers(1, R.drawable.exam,"Exámenes", "Escribe aquí las fechas de tus exámenes."));
        list.add(new ItemListViewPers(2, R.drawable.homework,"Tareas", "Escribe aquí tus tareas."));
        list.add(new ItemListViewPers(3, R.drawable.calendar,"Calendario", "Apunta recordatorios en el calendario de tu móvil."));
        // Devolvemos la lista
        return list;
    }
}