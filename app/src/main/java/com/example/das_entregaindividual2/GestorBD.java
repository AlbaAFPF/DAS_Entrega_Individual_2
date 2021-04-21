package com.example.das_entregaindividual2;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.ArrayMap;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class GestorBD extends SQLiteOpenHelper {

    // Asignamos un nombre a la BD
    private static final String NOMBRE_BD="basedatos.bd";
    // Versión de la BD, que se modifica al hacer cambios
    private static final int  VERSION_BD=8;
    // Tabla para la lista de tareas
    private static final String TABLA_ASIG="CREATE TABLE Tareas(Codigo VARCHAR(255), Asignatura VARCHAR(255), Tarea VARCHAR(255))";
    // Tabla para el Login y Registro
    private static final String TABLA_LOGIN="CREATE TABLE Usuarios(NombreUsuario VARCHAR(255), Contrasena VARCHAR(255))";



    public GestorBD(@Nullable Context context) {
        super(context, NOMBRE_BD, null, VERSION_BD);
    }

    // Para crear las tablas de la base de datos
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Tabla para la lista de tareas
        sqLiteDatabase.execSQL(TABLA_ASIG);
        // Tabla para el Login y Registro
        sqLiteDatabase.execSQL(TABLA_LOGIN);

        // Usuario permanente
        sqLiteDatabase.execSQL("INSERT INTO Usuarios (NombreUsuario, Contrasena) VALUES ('user','pass')");
    }

    // Para actualizar al cambiar la versión
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE Tareas");
        db.execSQL("DROP TABLE Usuarios");
        onCreate(db);
    }

    // Método para añadir Login
    public void agregarLogin(String nombreUsuario, String contrasena){
        // Para poder trabajar con la BD en modo escritura y lectura.
        SQLiteDatabase bd = getWritableDatabase();

        // Comprobamos que la BD se ha abierto correctamente
        if(bd!=null){
            // Ejecutamos la consulta
            bd.execSQL("INSERT INTO Usuarios VALUES('"+nombreUsuario+"','"+contrasena+"')");
            // Cerramos la conexción con la BD
            bd.close();
        }

    }

    // Método para comprobar si el nombre de usuario introducido ya está en la BD
    public boolean existeNombreUsuario(String nombreUsuario){
        boolean coincidencia = false;

        SQLiteDatabase database = this.getReadableDatabase();
        String consulta = "SELECT * FROM Usuarios WHERE NombreUsuario ='"+nombreUsuario+"'";

        Cursor nombresUsuarios = database.rawQuery(consulta, null);

        // Si hemos encontrado alguna coincidencia
        if(nombresUsuarios.getCount()>0){
            coincidencia = true;
        }else{
            // Si no hemos encontrado ninguna coincidencia
            coincidencia = false;
        }
        // Devolvemos el resultado
        return coincidencia;
    }

    // Método para comprobar si el nombre de usuario y contraseña son correctos
    public boolean comprobarUsuarioContrasema(String nombreUsuario, String contrasena){
        boolean coincidencia= false;

        SQLiteDatabase database = this.getReadableDatabase();
        String consulta = "SELECT * FROM Usuarios WHERE NombreUsuario ='"+nombreUsuario+"' AND Contrasena ='"+contrasena+"'";

        Cursor nombresContras = database.rawQuery(consulta, null);

        // Si hemos encontrado alguna coincidencia
        if(nombresContras.getCount()>0){
            coincidencia = true;
        }else{
            // Si no hemos encontrado ninguna coincidencia
            coincidencia = false;
        }
        // Devolvemos el resultado
        return coincidencia;
    }


    // Método para añadir tareas
    public void anadirTarea(String codigo, String asignatura, String tarea){
        // Para poder trabajar con la BD en modo escritura y lectura.
        SQLiteDatabase bd = getWritableDatabase();

        // Comprobamos que la BD se ha abierto correctamente
        if(bd!=null){
            // Ejecutamos la consulta
            bd.execSQL("INSERT INTO Tareas VALUES('"+codigo+"','"+asignatura+"','"+tarea+"')");
            // Cerramos la conexción con la BD
            bd.close();
        }

    }

    // Método para modificar tareas
    public void modificaraTarea(String codigo, String asignatura, String tarea){
        // Para poder trabajar con la BD en modo escritura y lectura.
        SQLiteDatabase bd = getWritableDatabase();

        // Comprobamos que la BD se ha abierto correctamente
        if(bd!=null){
            // Ejecutamos la consulta
            bd.execSQL("UPDATE Tareas SET Asignatura='"+asignatura+"', Tarea='"+tarea+"' WHERE Codigo='"+codigo+"'");
            // Cerramos la conexción con la BD
            bd.close();
        }
    }

    // Método para borrar tareas
    public void borrarTarea(String codigo){
        // Para poder trabajar con la BD en modo escritura y lectura.
        SQLiteDatabase bd = getWritableDatabase();

        // Comprobamos que la BD se ha abierto correctamente
        if(bd!=null){
            // Ejecutamos la consulta
            bd.execSQL("DELETE FROM Tareas WHERE Codigo='"+codigo+"'");
            // Cerramos la conexción con la BD
            bd.close();
        }
    }

    // Métodos que llena la ListView que se mostrará en la interfaz de "tareas".
    public void llenarLista(ArrayList<Tarea> lista) {
        // Limpamos la lista
        lista.clear();
        // Para poder leer sólo
        SQLiteDatabase database = this.getReadableDatabase();
        String consulta = "SELECT * FROM Tareas";

        Cursor registros = database.rawQuery(consulta, null);

        if(registros.moveToFirst()){
            do{
                // Añadimos los datos a la lista
                lista.add(new Tarea(registros.getString(0),registros.getString(1), registros.getString(2)));
                // Mientras haya más datos
            }while (registros.moveToNext());
        }
    }
    public ArrayList<Tarea> llenarLista() {
        ArrayList<Tarea> lista = new ArrayList<>();
        llenarLista(lista);
        return lista;
    }

}
