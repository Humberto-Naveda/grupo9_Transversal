/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

/**
 *
 * @author Usuario
 */
public class Materia {
    private int id_Materia;
    private String nombre;
    private int semestre;
    private boolean estado;

    public Materia() {
    }

    public Materia(int id_Materia, String nombre, int semestre, boolean estado) {
        this.id_Materia = id_Materia;
        this.nombre = nombre;
        this.semestre = semestre;
        this.estado = estado;
    }

    public Materia(String nombre, int semestre, boolean estado) {
        this.nombre = nombre;
        this.semestre = semestre;
        this.estado = estado;
    }

    public int getId_Materia() {
        return id_Materia;
    }

    public void setId_Materia(int id_Materia) {
        this.id_Materia = id_Materia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Materia{" + "id_Materia=" + id_Materia + ", nombre=" + nombre + ", semestre=" + semestre + ", estado=" + estado + '}';
    }
    
}
