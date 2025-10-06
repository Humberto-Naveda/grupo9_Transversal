
package persistencia;

import entidades.Alumno;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AlumnoData {
    private Connection conec =null;
    
    public AlumnoData(Conexion conexion){
        this.conec = conexion.conectar();
    }
    
    public void ingresarAlumno(Alumno alumno){
        try {
            String base = "INSERT INTO alumno (dni, apellido, nombre, fecha_Nacimiento, estado) VALUES (?,?,?,?,?)";
            PreparedStatement ps = conec.prepareStatement(base, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, alumno.getDni());
            ps.setString(2, alumno.getApellido());
            ps.setString(3, alumno.getNombre());
            ps.setDate(4, Date.valueOf(alumno.getFechaNacimiento())); // LocalDate → Date SQL
            ps.setBoolean(5, alumno.isEstado());
            
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                alumno.setIdAlumno(rs.getInt(1));
            }
            
            ps.close();
            System.out.println("Alumno agregado");
        } catch (SQLException ex) {
            System.out.println("Error al ingresar Alumno" + ex.getMessage());;
        }
        
    }
    
     public void actualizarAlumno(Alumno alumno){
        try {
            String base = "UPDATE alumno SET dni=?,apellido=?,nombre=?,fecha_Nacimiento=?,estado=? WHERE id_Alumno=?";
            PreparedStatement ps = conec.prepareStatement(base);
            ps.setInt(1,alumno.getIdAlumno());
            ps.setInt(2, alumno.getDni());
            ps.setString(3, alumno.getApellido());
            ps.setString(4, alumno.getNombre());
            ps.setDate(5, Date.valueOf(alumno.getFechaNacimiento())); // LocalDate → Date SQL
            ps.setBoolean(6, alumno.isEstado());
            
            ps.executeUpdate();
            
            ps.close();
            
             System.out.println("Alumno actualizado");
        } catch (SQLException ex) {
             System.out.println("Error al actualizar Alumno" + ex.getMessage());
        }
    }
    
}
