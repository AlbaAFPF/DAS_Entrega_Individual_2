package com.example.das_entregaindividual2;

import java.io.Serializable;

public class Tarea implements Serializable {

    // Instanciamos las variables que vamos a usar en el ListView
    private String codigo;
    private String asignatura;
    private String tarea;

    public Tarea(String codigo, String asignatura, String tarea) {
        this.codigo = codigo;
        this.asignatura = asignatura;
        this.tarea = tarea;
    }

    // -- Getters y Setters --
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }

    public String getTarea() {
        return tarea;
    }

    public void setTarea(String tarea) {
        this.tarea = tarea;
    }

    // Sobreescribimos el método toString para que las tareas se muestren con el formato deseado
    public String toString(){
        return getCodigo()+" \n"+getAsignatura()+" \n"+ getTarea();
    }

    // Sobreescribimos el método equals para la tarea
    public boolean equals(Tarea tarea){
        if(tarea==null){
            return false;
        }else{
            return this.getCodigo()==tarea.getCodigo();
        }
    }
}
