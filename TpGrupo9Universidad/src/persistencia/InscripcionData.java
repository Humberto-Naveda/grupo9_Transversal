
package persistencia;
import entidades.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


public class InscripcionData {
    private Connection conec = null;
    private MateriaData matData;
    private AlumnoData aluData;
    
    public InscripcionData(Conexion conexion){
        this.conec = conexion.conectar();
        matData = new MateriaData(conexion);
        aluData = new AlumnoData(conexion); 
    }
    
    public void guardarInscripcion(Inscripcion insc){
        String verificarSql = "SELECT id_inscripcion FROM inscripcion "
            + "WHERE id_alumno = ? AND id_materia = ? AND año = ?";

        String insertarSql = "INSERT INTO inscripcion(nota, año, id_alumno, id_materia, estado) VALUES (?, ?, ?, ?, ?)";
        String reactivarSql = "UPDATE inscripcion SET estado = 1 WHERE id_inscripcion = ?";

        try {
        // Verificamos si ya existe una inscripción (aunque esté inactiva)
           PreparedStatement psVerificar = conec.prepareStatement(verificarSql);
           psVerificar.setInt(1, insc.getAlumno().getIdAlumno());
           psVerificar.setInt(2, insc.getMateria().getIdMateria());
           psVerificar.setInt(3, insc.getAnio());
           ResultSet rs = psVerificar.executeQuery();

           if (rs.next()) {
               //  Ya existe → la reactivamos
               int idExistente = rs.getInt("id_inscripcion");
               PreparedStatement psReactivar = conec.prepareStatement(reactivarSql);
               psReactivar.setInt(1, idExistente);
               psReactivar.executeUpdate();
               psReactivar.close();
               JOptionPane.showMessageDialog(null, "Inscripción reactivada correctamente.");
           } else {
              // No existe → insertamos una nueva
              PreparedStatement psInsertar = conec.prepareStatement(insertarSql, Statement.RETURN_GENERATED_KEYS);
              
              psInsertar.setDouble(1, insc.getNota());
              
              psInsertar.setInt(2, insc.getAnio());
              
              psInsertar.setInt(3, insc.getAlumno().getIdAlumno());
              
              psInsertar.setInt(4, insc.getMateria().getIdMateria());
              
              psInsertar.setBoolean(5, insc.isEstado());
              
              psInsertar.executeUpdate();

              ResultSet rsInsert = psInsertar.getGeneratedKeys();
              if (rsInsert.next()) {
                  insc.setIdInscripcion(rsInsert.getInt(1));
                  JOptionPane.showMessageDialog(null, "Inscripción cargada correctamente.");
              }

                  psInsertar.close();
            }

            psVerificar.close();

            } catch (SQLException ex) {
                  JOptionPane.showMessageDialog(null, "Error al guardar inscripción: " + ex.getMessage(), "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
            }
        
    }
    
    public List<Materia> obtenerMateriasCursadas(int id, int anio){
        String sql = "SELECT inscripcion.id_materia, materia.nombre, materia.semestre "
           + "FROM inscripcion "
           + "JOIN materia ON inscripcion.id_materia = materia.id_materia "
           + "WHERE inscripcion.id_alumno = ? "
           + "AND inscripcion.estado = 1 "
           + "AND inscripcion.año = ?";

        ArrayList<Materia> materias = new ArrayList<Materia>();
        
        try {
            PreparedStatement ps = conec.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setInt(2, anio);
            ResultSet rs = ps.executeQuery();
            Materia materia;
            while (rs.next()) {
                materia = new Materia();
                materia.setIdMateria(rs.getInt("inscripcion.id_materia"));
                materia.setNombre(rs.getString("nombre"));
                materia.setAnio(rs.getInt("semestre"));

                materias.add(materia);
            }
            ps.close();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al obtener inscripciones " + ex.getMessage());
        }
        return materias;
        
    }
    
    public void borrarInscripcionMateriaAlumno(int idAlumno, int idMateria){
        String sql = "UPDATE inscripcion SET estado = 0"
                +" WHERE estado = 1 AND id_Alumno = ? AND id_materia = ?";
         try {
        PreparedStatement ps = conec.prepareStatement(sql);
        ps.setInt(1, idAlumno);
        ps.setInt(2, idMateria);

        int exito = ps.executeUpdate();

        if (exito > 0) {
            JOptionPane.showMessageDialog(null, "Inscripción eliminada correctamente","INFORMATION_MESSAGE", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró una inscripción activa para eliminar","INFORMATION_MESSAGE", JOptionPane.INFORMATION_MESSAGE);
        }

        ps.close();

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, " Error al acceder a la tabla inscripciones: " + ex.getMessage(),"ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
    }
    }   
    
    public void actualizarNota(int idAlumno, int idMateria, int anio, double nota) {
    String sql = "UPDATE inscripcion SET nota=? WHERE id_alumno=? AND id_materia=? AND año=?";
    try (
        PreparedStatement ps = conec.prepareStatement(sql)) {
        ps.setDouble(1, nota);
        ps.setInt(2, idAlumno);
        ps.setInt(3, idMateria);
        ps.setInt(4, anio);
        ps.executeUpdate();
    } catch (SQLException ex) {
       JOptionPane.showMessageDialog(null, " Error al conectarce con la base de datos " + ex.getMessage(),"ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
    }
}
    
    public List<Materia> obtenerMateriasNoCursadas(int idAlumno ,int anio) {

   String sql = "SELECT * "
           + "FROM materia "
           + "WHERE estado = 1 "
           + "AND id_materia NOT IN ("
           + "    SELECT id_materia FROM inscripcion "
           + "    WHERE id_alumno = ? AND estado = 1 AND año = ?"
           + ")";


    ArrayList<Materia> materias = new ArrayList<>();

    try {
        PreparedStatement ps = conec.prepareStatement(sql);
        ps.setInt(1, idAlumno);
        ps.setInt(2, anio);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Materia materia = new Materia();
            materia.setIdMateria(rs.getInt("id_materia"));
            materia.setNombre(rs.getString("nombre")); 
            materia.setAnio(rs.getInt("semestre"));
            materias.add(materia);
        }

        ps.close();

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Error al obtener materias no cursadas: " + ex.getMessage(),"ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
    }

    return materias;
}
    
    public List<Alumno> obtenerAlumnosPorMateriaYAnio(int idMateria, int anio) {
    List<Alumno> alumnos = new ArrayList<>();

    String sql = "SELECT alumno.id_alumno, alumno.dni, alumno.apellido, alumno.nombre "
               + "FROM inscripcion  "
               + "JOIN alumno  ON inscripcion.id_alumno = alumno.id_alumno "
               + "WHERE inscripcion.id_materia = ? AND inscripcion.año = ? AND inscripcion.estado = 1";

    try {
        PreparedStatement ps = conec.prepareStatement(sql);
        ps.setInt(1, idMateria);
        ps.setInt(2, anio);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Alumno a = new Alumno();
            a.setIdAlumno(rs.getInt("id_Alumno"));
            a.setDni(rs.getInt("dni"));
            a.setApellido(rs.getString("apellido"));
            a.setNombre(rs.getString("nombre"));
            alumnos.add(a);
        }

        ps.close();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, " Error al conectarce con la base de datos " + ex.getMessage(),"ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
    }

    return alumnos;
}
    
    public List<Inscripcion> obtenerInscripcionesPorAlumno(int idAlumno) {
    List<Inscripcion> inscripciones = new ArrayList<>();
    
   String sql = "SELECT inscripcion.id_inscripcion, inscripcion.nota, inscripcion.`año`, "
           + "materia.id_materia, materia.nombre, materia.semestre, materia.estado "
           + "FROM inscripcion "
           + "JOIN materia ON inscripcion.id_materia = materia.id_materia "
           + "WHERE inscripcion.id_alumno = ? AND inscripcion.estado = 1";

  

    try {
        PreparedStatement ps = conec.prepareStatement(sql);
        ps.setInt(1, idAlumno);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Materia materia = new Materia();
            materia.setIdMateria(rs.getInt("id_materia"));
            materia.setNombre(rs.getString("nombre"));
            materia.setAnio(rs.getInt("semestre"));
            materia.setEstado(rs.getBoolean("estado"));

            Inscripcion insc = new Inscripcion();
            insc.setIdInscripcion(rs.getInt("id_inscripcion"));
            insc.setNota(rs.getDouble("nota"));
            insc.setAnio(rs.getInt("año"));
            insc.setMateria(materia);

            inscripciones.add(insc);
        }

        ps.close();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, " Error al conectarce con la base de datos " + ex.getMessage(),"ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
    }

    return inscripciones;
}
    
} 
    
