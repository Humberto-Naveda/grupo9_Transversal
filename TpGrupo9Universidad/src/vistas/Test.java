package vistas;

import entidades.Alumno;
import java.time.LocalDate;
import persistencia.AlumnoData;
import persistencia.Conexion;


public class Test {

   
    public static void main(String[] args) {
        Conexion conn = new Conexion("grupo_9_universidad","jdbc:mariadb://localhost/","root","","org.mariadb.jdbc.Driver");
        AlumnoData alum = new AlumnoData(conn);
        
        Alumno alumno1 = new Alumno(29400899,"Simpson","Bart",LocalDate.of(1980,2,23),true);
        
        alum.ingresarAlumno(alumno1);
        
        alum.actualizarAlumno(alumno1);
        
        System.out.println(alumno1);
    }
    
}
