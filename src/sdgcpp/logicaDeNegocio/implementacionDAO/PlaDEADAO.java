package sdgcpp.logicaDeNegocio.implementacionDAO;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import sdgcpp.accesoADatos.ManejadorBaseDeDatos;
import sdgcpp.logicaDeNegocio.clases.PlaDEA;
import sdgcpp.logicaDeNegocio.interfaces.IPlaDEA;

public class PlaDEADAO implements IPlaDEA {
    private static final Logger LOG = Logger.getLogger(ProfesorDAO.class);
    private static final String AGREGAR_PLADEA = "INSERT INTO PlaDEA (ejeEstrategico, programaEstrategico, objetivos, acciones, metas, Periodo_idPeriodo) " +
    "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String AGREGAR_RELACION_PLADEA_PROFESOR =
    "INSERT INTO PlaDEA_has_Profesor (PlaDEA_idPlaDEA, Profesor_idProfesor) " +
    "VALUES (?, ?)";
    @Override
    public int registrarPlaDEA(PlaDEA plaDEA, List<Integer> listaIdProfesores) throws SQLException {
        int resultado = 0;

        try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion()) {
             conexion.setAutoCommit(false);

            // Insertar en la tabla PlaDEA
            try (PreparedStatement psPlaDEA = conexion.prepareStatement(AGREGAR_PLADEA, Statement.RETURN_GENERATED_KEYS)) {
                psPlaDEA.setString(1, plaDEA.getEjeEstrategico());
                psPlaDEA.setString(2, plaDEA.getProgramaEstrategico());
                psPlaDEA.setString(3, plaDEA.getObjetivos());
                psPlaDEA.setString(4, plaDEA.getAcciones());
                psPlaDEA.setString(5, plaDEA.getMetas());
                psPlaDEA.setInt(6, plaDEA.getIdPeriodo());

                resultado = psPlaDEA.executeUpdate();

                if (resultado > 0) {
                    
                    try (ResultSet generatedKeys = psPlaDEA.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int idPlaDEA = generatedKeys.getInt(1);
                            plaDEA.setIdPlaDEA(idPlaDEA);
                            try (PreparedStatement psRelacion = conexion.prepareStatement(AGREGAR_RELACION_PLADEA_PROFESOR)) {
                                for (Integer idProfesor : listaIdProfesores) {
                                    psRelacion.setInt(1, idPlaDEA);
                                    psRelacion.setInt(2, idProfesor);
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
    
    @Override
        public List<PlaDEA> obtenerPlaDEAsPorProfesorYPeriodo(int idProfesor, int idPeriodo) throws SQLException {
        List<PlaDEA> listaPlaDEAs = new ArrayList<>();
        String CONSULTA_PLADEAS = 
            "SELECT " +
            "    p.idPlaDEA, p.ejeEstrategico, p.programaEstrategico, p.objetivos, p.acciones, p.metas, p.Periodo_idPeriodo " +
            "FROM " +
            "    PlaDEA p " +
            "JOIN " +
            "    PlaDEA_has_Profesor php ON p.idPlaDEA = php.PlaDEA_idPlaDEA " +
            "WHERE " +
            "    php.Profesor_idProfesor = ? AND p.Periodo_idPeriodo = ?";

        try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(CONSULTA_PLADEAS)) {

            ps.setInt(1, idProfesor);
            ps.setInt(2, idPeriodo);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PlaDEA plaDEA = new PlaDEA();
                    plaDEA.setIdPlaDEA(rs.getInt("idPlaDEA"));
                    plaDEA.setEjeEstrategico(rs.getString("ejeEstrategico"));
                    plaDEA.setProgramaEstrategico(rs.getString("programaEstrategico"));
                    plaDEA.setObjetivos(rs.getString("objetivos"));
                    plaDEA.setAcciones(rs.getString("acciones"));
                    plaDEA.setMetas(rs.getString("metas"));
                    plaDEA.setIdPeriodo(rs.getInt("Periodo_idPeriodo"));

                    listaPlaDEAs.add(plaDEA);
                }
            }
        }

        return listaPlaDEAs;
    }

        
}
