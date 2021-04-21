package com.example.das_entregaindividual2;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Locale;

public class Examenes extends AppCompatActivity {

    // Instanciamos los objetos para los campos y botones
    EditText editTextExamenes;
    Button buttonGuardar;

    // Creamos una variable con el nombre del fichero en el que se guardarán los datos
    public static final String NOMBRE_FICHERO = "fichero.txt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fijarIdioma();
        setContentView(R.layout.examenes);

        // Asignamos los id a las variables
        editTextExamenes = (EditText)findViewById(R.id.editTextExamenes);

        // Recuperamos los ficheros existentes a través de un array
        String ficheros[] = fileList();

        // Mostrar el texto cada vez que se abre la aplicación
        String nombreFichero = "fichero.txt";
        if(existeFichero(ficheros, nombreFichero)){
            // Para poder leer el fichero
            try {
                InputStreamReader inputStreamReader = new InputStreamReader(openFileInput(nombreFichero));
                // Leer el archivo que hemos abierto con InputStreamReader
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                // Para guardar el contenido del archivo línea por línea
                String linea = bufferedReader.readLine();

                // Para guardar el texto completo
                String textoCompleto = "";

                // Si línea no es "null", el texto no está vacío
                while(linea != null){
                    // El texto completo es lo acumulado más la línea actual
                    textoCompleto = textoCompleto + linea +"\n";
                    // Leer la línea
                    linea = bufferedReader.readLine();
                }
                // Parar de leer
                bufferedReader.close();
                // Cerrar fichero
                inputStreamReader.close();
                // Introducir el texto en el editText
                editTextExamenes.setText(textoCompleto);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Función para comprobar si el fichero existe
    private boolean existeFichero(String[] ficheros, String nombreFichero) {
        boolean var = false;
        for(int i=0; i<ficheros.length;i++){
            // Si encuentra nuestro fichero devuelve true
            if(nombreFichero.equals(ficheros[i])){
                var = true;
            }else{
                // Si no encuentra nuestro fichero devuelve false
                var = false;
            }
        }
        return var;
    }


    // Método para guardar en el fichero
    public void guardarTexto(View view){
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("fichero.txt", Activity.MODE_PRIVATE));
            // Escribir
            outputStreamWriter.write(editTextExamenes.getText().toString());
            // Limpiar el buffer
            outputStreamWriter.flush();
            // Cerrar fichero
            outputStreamWriter.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Mensaje de confirmación de guardado correcto
        Toast.makeText(this, "¡Guardado correctamente!", Toast.LENGTH_SHORT).show();
        // Cerrar pantalla al presionar el botón guardar
        finish();
    }

    private void leerFichero() throws IOException {
        // Para lectura
        FileInputStream fileInputStream = null;
        fileInputStream = openFileInput(NOMBRE_FICHERO);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        // Hacemos un bucle para leer todas las líneas
        String linea;
        StringBuilder stringBuilder = new StringBuilder();

        while((linea= bufferedReader.readLine()) != null){
            stringBuilder.append(linea).append("\n");
        }

        // Le pasamos al editTexto el contenido del fichero
        editTextExamenes.setText(stringBuilder);

        // Cerramos el fichero
        if(fileInputStream !=null){
            fileInputStream.close();
        }
    }

    private void guardarFichero() throws IOException {

        String ficheroGuardar = editTextExamenes.getText().toString();
        // Para escritura
        FileOutputStream fileOutputStream = null;

        // Abrimos el fichero, y si no existe, lo creamos
        fileOutputStream = openFileOutput(NOMBRE_FICHERO, MODE_PRIVATE);
        // Escribir en el fichero
        fileOutputStream.write(ficheroGuardar.getBytes());


        // Cerramos el fichero
        if(fileOutputStream !=null){
            fileOutputStream.close();
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
