package com.example.das_entregaindividual2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class RecAlarma extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Se ejecuta la alarma programada para las 21:00
        Toast.makeText(this, "No olvides subir tus tareas antes de las 22:00." , Toast.LENGTH_LONG).show();
        finish();

    }
}