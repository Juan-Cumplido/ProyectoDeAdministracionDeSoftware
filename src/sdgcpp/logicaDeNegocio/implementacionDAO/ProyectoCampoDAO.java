package sdgcpp.logicaDeNegocio.implementacionDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import sdgcpp.accesoADatos.ManejadorBaseDeDatos;
import sdgcpp.logicaDeNegocio.clases.ProyectoCampo;
import sdgcpp.logicaDeNegocio.clases.TablaPC;
import sdgcpp.logicaDeNegocio.interfaces.IProyectoCampo;


public class ProyectoCampoDAO implements IProyectoCampo{
    private static final Logger LOG = Logger.getLogger(ProyectoCampoDAO.class);
    private static final String AGREGAR_PROYECTO = "INSERT INTO ProyectoCampo (nombre, fechaInicio, fechaFin, lugar, impactoObtenido, Periodo_idPeriodo) " +
    "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String AGREGAR_RELACION_PLADEA_PROFESOR =
    "INSERT INTO Profesor_has_ProyectoCampo (Profesor_idProfesor, ProyectoCampo_idProyectoCampo) " +
    "VALUES (?, ?)";
    
        private static final String AGREGAR_RELACION_PLADEA_ESTUDIANTE =
    "INSERT INTO Estudiante_has_ProyectoCampo (Estudiante_idEstudiante, ProyectoCampo_idProyectoCampo) " +
    "VALUES (?, ?)";

    @Override
    public List<TablaPC> obtenerProyectosPorProfesorYPeriodo(int idProfesor, int idPeriodo) throws SQLException {
    List<TablaPC> proyectos = new ArrayList<>();

    String sql = "SELECT " +
                 "pc.nombre AS nombreProyecto, " +
                 "pc.fechaInicio, " +
                 "pc.fechaFin, " +
                 "pc.lugar, " +
                 "pc.impactoObtenido, " +
                 "GROUP_CONCAT(DISTINCT CONCAT(e.nombre, ' ', e.apellidoPaterno, ' ', IFNULL(e.apellidoMaterno, '')) SEPARATOR ', ') AS alumnos " +
                 "FROM ProyectoCampo pc " +
                 "JOIN Profesor_has_ProyectoCampo phpc ON pc.idProyectoCampo = phpc.ProyectoCampo_idProyectoCampo " +
                 "LEFT JOIN Estudiante_has_ProyectoCampo ehpc ON pc.idProyectoCampo = ehpc.ProyectoCampo_idProyectoCampo " +
                 "LEFT JOIN estudiante e ON ehpc.Estudiante_idEstudiante = e.idEstudiante " +
                 "WHERE phpc.Profesor_idProfesor = ? " +
                 "AND pc.Periodo_idPeriodo = ? " +
                 "GROUP BY pc.idProyectoCampo";

    try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
         PreparedStatement ps = conexion.prepareStatement(sql)) {
        ps.setInt(1, idProfesor);
        ps.setInt(2, idPeriodo);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                TablaPC proyecto = new TablaPC();
                proyecto.setNombre(rs.getString("nombreProyecto"));
                proyecto.setFechaInicio(rs.getString("fechaInicio"));
                proyecto.setFechaFin(rs.getString("fechaFin"));
                proyecto.setLugar(rs.getString("lugar"));
                proyecto.setImpactoObtenido(rs.getString("impactoObtenido"));
                proyecto.setAlumnos(rs.getString("alumnos"));

                proyectos.add(proyecto);
            }
        }
    }

    return proyectos;
}

        
    @Override
    public int registrarProyectoCampo(ProyectoCampo proyectoCampo, List<Integer> listaIdProfesores, List<Integer> listaIdEstudiantes) throws SQLException {
                int resultado = 0;

        try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion()) {
             conexion.setAutoCommit(false);

            try (PreparedStatement psProyectoCampo = conexion.prepareStatement(AGREGAR_PROYECTO, Statement.RETURN_GENERATED_KEYS)) {
                psProyectoCampo.setString(1, proyectoCampo.getNombre());
                psProyectoCampo.setDate(2, java.sql.Date.valueOf(proyectoCampo.getFechaInicio()));
                psProyectoCampo.setDate(3, java.sql.Date.valueOf(proyectoCampo.getFechaFin()));
                psProyectoCampo.setString(4, proyectoCampo.getLugar());
                psProyectoCampo.setString(5, proyectoCampo.getImpactoObtenido());
                psProyectoCampo.setInt(6, proyectoCampo.getIdPeriodo());

                resultado = psProyectoCampo.executeUpdate();

                if (resultado > 0) {
                    
                    try (ResultSet generatedKeys = psProyectoCampo.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int idProyectoCampo = generatedKeys.getInt(1);
                            proyectoCampo.setIdProyectoCampo(idProyectoCampo);
                            try (PreparedStatement psRelacion = conexion.prepareStatement(AGREGAR_RELACION_PLADEA_PROFESOR)) {
                                for (Integer idProfesor : listaIdProfesores) {
                                    psRelacion.setInt(1, idProfesor);
                                    psRelacion.setInt(2, idProyectoCampo);
                                    
                                    psRelacion.addBatch();
                                }
                                psRelacion.executeBatch(); 
                            }
                        }
                    }
                    
                    try (ResultSet generatedKeys = psProyectoCampo.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int idProyectoCampo = generatedKeys.getInt(1);
                            proyectoCampo.setIdProyectoCampo(idProyectoCampo);
                            try (PreparedStatement psRelacion = conexion.prepareStatement(AGREGAR_RELACION_PLADEA_ESTUDIANTE)) {
                                for (Integer idEstudiante : listaIdEstudiantes) {
                                     psRelacion.setInt(1, idEstudiante);
                                    psRelacion.setInt(2, idProyectoCampo);
                                   
                                    psRelacion.addBatch();
                                }
                                psRelacion.executeBatch(); 
                            }
                        }
                    }
                }

               
                conexion.commit();
            } catch (SQLException ex) {
                conexion.rollback(); 
                throw ex;
            } finally {
                conexion.setAutoCommit(true); 
            }
        }

        return resultado;
    }
}
