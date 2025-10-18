
package persistencia;

import entidades.Materia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class MateriaData {
    private Connection conec = null;
    
    public MateriaData(Conexion conexion){
       this.conec = conexion.conectar();
        
    }
    
    public void guardarMateria(Materia materia) {

        String sql = "INSERT INTO materia(nombre, semestre, estado)VALUES(?,?,?)";

        try {
            PreparedStatement ps = conec.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, materia.getNombre());
            ps.setInt(2, materia.getAnio());
            ps.setBoolean(3, materia.isEstado());
            
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                materia.setIdMateria(rs.getInt(1));
                JOptionPane.showMessageDialog(null, "Cargado exitoso");
            }else{
                JOptionPane.showMessageDialog( null,"Error al cargar ","ERROR_MESSAGE",JOptionPane.ERROR_MESSAGE );
            }
            ps.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog( null,"Error al acceder a la base de datos " + ex.getMessage(),"ERROR_MESSAGE",JOptionPane.ERROR_MESSAGE );
        }
    }

    public void modificarMateria(Materia materia) {

        String sql = "UPDATE materia SET nombre=?,semestre=?,estado=? WHERE id_Materia =?";

        try {
            PreparedStatement ps = conec.prepareStatement(sql);
            ps.setString(1, materia.getNombre());
            ps.setInt(2, materia.getAnio());
            ps.setBoolean(3, materia.isEstado());
            ps.setInt(4, materia.getIdMateria());

            int exito = ps.executeUpdate();
            if (exito == 1) {
                JOptionPane.showMessageDialog(null, "Materia modificada");
            }else{
                JOptionPane.showMessageDialog( null,"Error al medeficar la materia ","ERROR_MESSAGE",JOptionPane.ERROR_MESSAGE );
            }
            ps.close();  

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog( null,"Error al acceder a la base de datos " + ex.getMessage(),"ERROR_MESSAGE",JOptionPane.ERROR_MESSAGE );
        }

    }

    public void eliminarMateria(int id) {
        String sql = "UPDATE materia SET estado = 0 WHERE id_Materia = ?";

        
        try {
            PreparedStatement ps;
            ps = conec.prepareStatement(sql);
            ps.setInt(1, id);
            int exito = ps.executeUpdate();
            if (exito == 1) {
               JOptionPane.showMessageDialog( null,"Se elimino la maeria con id: "+id,"INFORMATION_MESSAGE", JOptionPane.INFORMATION_MESSAGE ); 
            }else{
                JOptionPane.showMessageDialog( null,"Error al borrar la materia ","ERROR_MESSAGE",JOptionPane.ERROR_MESSAGE );
            }
            ps.close();   

        } catch (SQLException ex) {
           JOptionPane.showMessageDialog( null,"Error al acceder a la base de datos " + ex.getMessage(),"ERROR_MESSAGE",JOptionPane.ERROR_MESSAGE );
        }

    }

    public Materia buscarMateriaPorId(int id) {
        String sql = "SELECT nombre, semestre, estado FROM materia WHERE id_Materia =?";
        Materia materia = null;

        try {
            PreparedStatement ps = conec.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                materia = new Materia();
                materia.setIdMateria(id);
                materia.setNombre(rs.getString("nombre"));
                materia.setAnio(rs.getInt("semestre"));
                materia.setEstado(rs.getBoolean("estado"));
            }else{
                JOptionPane.showMessageDialog( null,"No existe una materia con id:" + id,"ERROR_MESSAGE",JOptionPane.ERROR_MESSAGE );
            }
            ps.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog( null,"Error al aseder a la base de datos " + ex.getMessage(),"ERROR_MESSAGE",JOptionPane.ERROR_MESSAGE );
        }
        return materia;
    }

    public List<Materia> listarMaterias() {
        String sql = "SELECT * FROM materia WHERE estado = 1 order by nombre";
        ArrayList<Materia> materias = new ArrayList<>();

        try {
            PreparedStatement ps = conec.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Materia materia = new Materia();
                materia.setIdMateria(rs.getInt("id_Materia"));
                materia.setNombre(rs.getString("nombre"));
                materia.setAnio(rs.getInt("semestre"));
                materia.setEstado(true);
                
                materias.add(materia);
            }
            ps.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog( null,"Error al aseder a la base de datos " + ex.getMessage(),"ERROR_MESSAGE",JOptionPane.ERROR_MESSAGE );
        }
        
        return materias;
    }
}
