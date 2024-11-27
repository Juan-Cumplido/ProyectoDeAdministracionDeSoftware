package sdgcpp.logicaDeNegocio.implementacionDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import sdgcpp.accesoADatos.ManejadorBaseDeDatos;
import sdgcpp.logicaDeNegocio.clases.Estudiante;
import sdgcpp.logicaDeNegocio.interfaces.IEstudiante;

public class EstudianteDAO implements IEstudiante {
    private static final String OBTENER_ESTUDIANTE_MATRICULA = "SELECT * FROM estudiante WHERE matricula = ?;";
    
    
    @Override
    public Estudiante obtenerEstudiantePorMatricula(String matricula) throws SQLException {
        Estudiante estudiante = new Estudiante();
        estudiante = null;
        String consulta = OBTENER_ESTUDIANTE_MATRICULA;
        Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
        PreparedStatement declaracion = conexion.prepareStatement(consulta);
        declaracion.setString(1, matricula);
        ResultSet resultado = declaracion.executeQuery();
        if (resultado.next()) {
            estudiante = obtenerProfesor(resultado);
        }
        ManejadorBaseDeDatos.cerrarConexion();
        return estudiante;
    }
    
        private Estudiante obtenerProfesor(ResultSet resultado) throws SQLException {
        Estudiante estudiante = new Estudiante();
            estudiante.setIdEstudiante(resultado.getInt("idEstudiante"));
            estudiante.setNombre(resultado.getString("nombre"));
            estudiante.setApellidoPaterno(resultado.getString("apellidoPaterno"));
            estudiante.setApellidoMaterno(resultado.getString("apellidoMaterno"));
            estudiante.setTrabajoRecepcional(resultado.getInt("TrabajoRecepcional_idTrabajoRecepcional"));
  
            
        return estudiante;
    }
     
}
