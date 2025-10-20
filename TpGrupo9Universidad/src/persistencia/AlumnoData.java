package persistencia;

import entidades.Alumno;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class AlumnoData {

    private Connection conec = null;

    public AlumnoData(Conexion conexion) {
        this.conec = conexion.conectar();
    }

    public void ingresarAlumno(Alumno alumno) {
        try {
            String base = "INSERT INTO alumno (dni, apellido, nombre, fecha_nacimiento, estado) VALUES (?,?,?,?,?)";
            PreparedStatement ps = conec.prepareStatement(base, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, alumno.getDni());
            ps.setString(2, alumno.getApellido());
            ps.setString(3, alumno.getNombre());
            ps.setDate(4, Date.valueOf(alumno.getFechaNacimiento())); // LocalDate → Date SQL
            ps.setBoolean(5, alumno.isEstado());

            int filasAfectadas = ps.executeUpdate(); // Si hubo filas afectadas se muestra el cartel de exito.
            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(null, "Alumno cargado con exito", "INFORMATION_MESSAGE", JOptionPane.INFORMATION_MESSAGE);
            }

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                alumno.setIdAlumno(rs.getInt(1));
            }

            ps.close();
            rs.close(); // Cerramos el ResultSet

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al ingresar alumno " + ex.getMessage(), "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);

        }

    }

    public void actualizarAlumno(Alumno alumno) {
        try {
            String base = "UPDATE alumno SET dni=?,apellido=?,nombre=?,fecha_nacimiento=?,estado=? WHERE id_Alumno=?";
            PreparedStatement ps = conec.prepareStatement(base);
            ps.setInt(6, alumno.getIdAlumno());
            ps.setInt(1, alumno.getDni());
            ps.setString(2, alumno.getApellido());
            ps.setString(3, alumno.getNombre());
            ps.setDate(4, Date.valueOf(alumno.getFechaNacimiento())); // LocalDate → Date SQL
            ps.setBoolean(5, alumno.isEstado());

            int filasAfectadas = ps.executeUpdate(); // Si hubo filas afectadas se muestra el cartel de exito.
            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(null, "Alumno actualizado con exito", "INFORMATION_MESSAGE", JOptionPane.INFORMATION_MESSAGE);
            }

            ps.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al actualizar alumno " + ex.getMessage(), "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void bajaLogica(Alumno alumno) {
        try {
            String baja = "UPDATE alumno SET estado = 0 WHERE id_Alumno = ?";
            PreparedStatement ps = conec.prepareStatement(baja);
            ps.setInt(1, alumno.getIdAlumno());
            
            int filasAfectadas = ps.executeUpdate();
            
            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(null, "Alumno dado de baja con exito", "INFORMATION_MESSAGE", JOptionPane.INFORMATION_MESSAGE);
            }
            
            ps.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al dar de baja al alumno " + ex.getMessage(), "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void altaLogica(Alumno alumno) {
        try {
            String alta = "UPDATE alumno SET estado = 1 WHERE id_Alumno = ?";
            PreparedStatement ps = conec.prepareStatement(alta);
            ps.setInt(1, alumno.getIdAlumno());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(null, "Alumno dado de alta correctamente.");
            }

            ps.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al dar de alta al alumno." + e.getMessage(), "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
        }
     }

    public Alumno buscarAlumnoPorId(int id) {
        String sql = "SELECT dni,apellido,nombre,fecha_nacimiento,estado FROM alumno WHERE id_alumno = ? ";
        Alumno alumno = null;

        try {
            PreparedStatement ps = conec.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                alumno = new Alumno();
                alumno.setIdAlumno(id);
                alumno.setDni(rs.getInt("dni"));
                alumno.setApellido(rs.getString("apellido"));
                alumno.setNombre(rs.getString("nombre"));
                alumno.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
                alumno.setEstado(rs.getBoolean("estado"));
            } else {
                JOptionPane.showMessageDialog(null, "No existe alumno con id : " + id, "INFORMATION_MESSAGE", JOptionPane.INFORMATION_MESSAGE);
            }
            ps.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar alumno " + ex.getMessage(), "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
        }
        return alumno;
    }

    public Alumno buscarAlumnoPorDNI(int dni) {
        String sql = "SELECT id_alumno,apellido,nombre,fecha_nacimiento,estado FROM alumno WHERE dni = ? ";
        Alumno alumno = null;

        try {
            PreparedStatement ps = conec.prepareStatement(sql);
            ps.setInt(1, dni);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                alumno = new Alumno();
                alumno.setIdAlumno(rs.getInt("id_alumno"));
                alumno.setDni(dni);
                alumno.setApellido(rs.getString("apellido"));
                alumno.setNombre(rs.getString("nombre"));
                alumno.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
                alumno.setEstado(rs.getBoolean("estado"));
            } else {
                JOptionPane.showMessageDialog(null, "No existe alumno con id : " + dni, "INFORMATION_MESSAGE", JOptionPane.INFORMATION_MESSAGE);
            }
            ps.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al buscar alumno " + ex.getMessage(), "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
        }
        return alumno;
    }

    public List<Alumno> listarAlumnos() {
        String sql = "SELECT id_alumno,dni,apellido,nombre,fecha_nacimiento FROM alumno WHERE estado = 1";
        ArrayList<Alumno> alumnos = new ArrayList<>();

        try {
            PreparedStatement ps = conec.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Alumno alumno = new Alumno();
                alumno.setIdAlumno(rs.getInt("id_alumno"));
                alumno.setDni(rs.getInt("dni"));
                alumno.setApellido(rs.getString("apellido"));
                alumno.setNombre(rs.getString("nombre"));
                alumno.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
                alumno.setEstado(true);

                alumnos.add(alumno);
            }
            ps.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al listar alumno " + ex.getMessage(), "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
        }
        return alumnos;
    }

}
