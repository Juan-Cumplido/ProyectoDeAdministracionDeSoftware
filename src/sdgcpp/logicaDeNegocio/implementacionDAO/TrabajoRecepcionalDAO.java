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
import sdgcpp.logicaDeNegocio.clases.TablaTR;
import sdgcpp.logicaDeNegocio.clases.TrabajoRecepcional;
import sdgcpp.logicaDeNegocio.interfaces.ITrabajoRecepcional;


public class TrabajoRecepcionalDAO implements ITrabajoRecepcional {
    private static final Logger LOG = Logger.getLogger(TrabajoRecepcionalDAO.class);
    private static final String AGREGAR_TRABAJORECEPCIONAL = "INSERT INTO TrabajoRecepcional (titulo, fecha, licenciatura, modalidad, resultadoObtenidoDefensa, Periodo_idPeriodo) " +
    "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String AGREGAR_RELACION_TRABAJORECEPCIONAL_PROFESOR =
        "INSERT INTO TrabajoRecepcional_has_Profesor (TrabajoRecepcional_idTrabajoRecepcional, Profesor_idProfesor, rolAcademico) " +
        "VALUES (?, ?, ?)";

    private static final String AGREGAR_RELACION_ESTUDIANTE =
    "UPDATE estudiante SET TrabajoRecepcional_idTrabajoRecepcional = ? WHERE idEstudiante = ?"; // Actualiza el campo del estudiante con el ID del trabajo recepcional.

   @Override
   public List<TablaTR> obtenerTrabajosRecepcionales(int idProfesor, int idPeriodo) throws SQLException {
    List<TablaTR> trabajosRecepcionales = new ArrayList<>();
    String CONSULTA_TR = 
                """
                SELECT 
                    tr.titulo,
                    tr.modalidad,
                    tr.fecha,
                    tr.licenciatura,
                    tr.resultadoObtenidoDefensa,
                    trp.rolAcademico AS rol,
                    GROUP_CONCAT(CONCAT(e.nombre, ' ', e.apellidoPaterno, ' ', IFNULL(e.apellidoMaterno, '')) SEPARATOR ', ') AS alumnos
                FROM 
                    TrabajoRecepcional tr
                JOIN 
                    TrabajoRecepcional_has_Profesor trp 
                    ON tr.idTrabajoRecepcional = trp.TrabajoRecepcional_idTrabajoRecepcional
                JOIN 
                    estudiante e 
                    ON e.TrabajoRecepcional_idTrabajoRecepcional = tr.idTrabajoRecepcional
                WHERE 
                    trp.Profesor_idProfesor = ?
                    AND tr.Periodo_idPeriodo = ?
                GROUP BY 
                    tr.idTrabajoRecepcional, trp.rolAcademico;""";

    try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
         PreparedStatement ps = conexion.prepareStatement(CONSULTA_TR)) {
        ps.setInt(1, idProfesor);
        ps.setInt(2, idPeriodo);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                TablaTR tablaTR = new TablaTR();
                tablaTR.setTitulo(rs.getString("titulo"));
                tablaTR.setModalidad(rs.getString("modalidad"));
                tablaTR.setFecha(rs.getString("fecha"));
                tablaTR.setLicenciatura(rs.getString("licenciatura"));
                tablaTR.setDefensa(rs.getString("resultadoObtenidoDefensa"));
                tablaTR.setRol(rs.getString("rol"));
                tablaTR.setAlumnos(rs.getString("alumnos"));
                trabajosRecepcionales.add(tablaTR);
            }
        }
    } catch (SQLException ex) {
        LOG.error("Error al obtener los trabajos recepcionales", ex);
        throw ex;
    }

    return trabajosRecepcionales;
}

    @Override
    public int registrarTrabajoRecepcional(TrabajoRecepcional trabajoRecepcional, List<List<String>> profesores, List<Integer> listaIdEstudiantes) throws SQLException {
        int resultado = 0;

        try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion()) {
             conexion.setAutoCommit(false);

            try (PreparedStatement psTrabajoRecepcional = conexion.prepareStatement(AGREGAR_TRABAJORECEPCIONAL, Statement.RETURN_GENERATED_KEYS)) {
                psTrabajoRecepcional.setString(1, trabajoRecepcional.getTitulo());
                psTrabajoRecepcional.setDate(2, java.sql.Date.valueOf(trabajoRecepcional.getFecha()));
                psTrabajoRecepcional.setString(3, trabajoRecepcional.getLicenciatura());
                psTrabajoRecepcional.setString(4, trabajoRecepcional.getModalidad());
                psTrabajoRecepcional.setInt(5, trabajoRecepcional.getResultadoObtenidoDefensa());
                psTrabajoRecepcional.setInt(6, trabajoRecepcional.getIdPeriodo());

                resultado = psTrabajoRecepcional.executeUpdate();

                if (resultado > 0) {
                // Obtener el ID generado para el trabajo recepcional
                try (ResultSet generatedKeys = psTrabajoRecepcional.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int idTrabajoRecepcional = generatedKeys.getInt(1);

                        try (PreparedStatement psRelacionProfesor = conexion.prepareStatement(AGREGAR_RELACION_TRABAJORECEPCIONAL_PROFESOR)) {
                            for (List<String> profesor : profesores) {
                                int idProfesor = Integer.parseInt(profesor.get(0));  // Se asume que el ID está en la primera posición
                                String rolAcademico = profesor.get(1);  // El rol está en la segunda posición
                                
                                psRelacionProfesor.setInt(1, idTrabajoRecepcional);  // ID del trabajo recepcional
                                psRelacionProfesor.setInt(2, idProfesor);  // ID del profesor
                                psRelacionProfesor.setString(3, rolAcademico);  // Rol del profesor
                                psRelacionProfesor.addBatch();  // Agregar al batch
                            }
                            psRelacionProfesor.executeBatch();  // Ejecutar el batch
                        }

                        try (PreparedStatement psRelacionEstudiante = conexion.prepareStatement(AGREGAR_RELACION_ESTUDIANTE)) {
                            for (Integer idEstudiante : listaIdEstudiantes) {
                                psRelacionEstudiante.setInt(1, idTrabajoRecepcional);  // ID del trabajo recepcional
                                psRelacionEstudiante.setInt(2, idEstudiante);  // ID del estudiante
                                psRelacionEstudiante.addBatch();  // Agregar al batch
                            }
                            psRelacionEstudiante.executeBatch();  // Ejecutar el batch
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
