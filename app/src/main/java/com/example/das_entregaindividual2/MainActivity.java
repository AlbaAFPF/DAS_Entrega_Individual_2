package com.example.das_entregaindividual2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Intanciamos la ListView
    ListView ListViewItem;
    List<ItemListViewPers> list;

    // Instanciamos el administrador de alarmas y la acción que se va a ejecutar
    private AlarmManager alarmMgr = null;
    private PendingIntent alarmIntent = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ejecutamos el método para la alarma
        setAlarm(this);

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
                }else if(item.nombre=="Cámara"){
                    // Abrimos la aplicación de calendario
                    Intent intent = new Intent (view.getContext(), Camara.class);
                    startActivityForResult(intent, 0);
                }else if(item.nombre=="Galería"){
                    // Abrimos la aplicación de calendario
                    Intent intent = new Intent (view.getContext(), Galeria.class);
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
        list.add(new ItemListViewPers(3, R.drawable.camera,"Cámara", "Realiza fotos de tus apuntes aquí."));
        list.add(new ItemListViewPers(3, R.drawable.gallery,"Galería", "Revisa las fotos de tus apuntes aquí."));

        // Devolvemos la lista
        return list;
    }


    /**Basado en el código extraído de developer.android.com
     Pregunta: https://developer.android.com/training/scheduling/alarms
     Autor: developer.android.com
     Modificado por Alba Arsuaga, se ha mantenido varias sentencias pero
     con modificaciones leves.
     **/
    // Método para la fijación de la alarma
    private void setAlarm(Context context){
        // Cargamos lo que se ejecutará cuando "salte" la alarma
        Intent intent = new Intent(context, RecAlarma.class);
        // Instanciamos un administrador de alarmas
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        // Fijamos que la alarma se dispare a las 9 de la noche
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 21);

        // Indicamos que la alarma se ejecute con el intent creado
        alarmIntent = PendingIntent.getActivity(context, 0, intent, 0);

        // Iniciamos la alarma repetitiva, pasándole el intent, y fijando que se ejecute cada día
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);

    }

    /**Extraído de developer.android.com
     Pregunta: https://developer.android.com/training/scheduling/alarms
     Autor: developer.android.com
     **/
    // Método para borrar la alarma
    private void unSetAlarma(){
        // Si existe el administrador y el intent
        if (alarmMgr!= null && alarmIntent!=null) {
            // Cancelar la alarma
            alarmMgr.cancel(alarmIntent);
        }
    }


}