package vistas;

import entidades.Alumno;
import java.time.LocalDate;
import persistencia.AlumnoData;
import persistencia.Conexion;


public class Test {

   
    public static void main(String[] args) {
        Conexion conn = new Conexion("grupo_9_universidad","jdbc:mariadb://localhost/","root","","org.mariadb.jdbc.Driver");
        AlumnoData alum = new AlumnoData(conn);
        
        Alumno alumno3 = new Alumno(290089,"Simpson","at",LocalDate.of(1900,2,23),true);
        
        alum.ingresarAlumno(alumno3);
        
       
        
        System.out.println(alumno3);
    }
    
}
