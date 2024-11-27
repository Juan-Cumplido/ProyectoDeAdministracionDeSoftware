package sdgcpp.logicaDeNegocio.implementacionDAO;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import sdgcpp.accesoADatos.ManejadorBaseDeDatos;
import sdgcpp.logicaDeNegocio.clases.EE;
import sdgcpp.logicaDeNegocio.interfaces.IEE;

public class EEDAO implements IEE {
    private static final Logger LOG = Logger.getLogger(ProfesorDAO.class);
    private static final String AGREGAR_EE = "INSERT INTO EE (programaEducativo, experienciaEducativa, bloque, "
            + "sesion, creditos, horas,semanas , mes,Periodo_idPeriodo , Profesor_idProfesor ) " +
    "VALUES (?, ?, ?, ?, ?, ?,?,?,?,?)";

    private static final String OBTENER_EE_POR_PROFESOR_Y_PERIODO = 
    "SELECT * FROM EE WHERE Profesor_idProfesor = ? AND Periodo_idPeriodo = ?";

    @Override
    public List<EE> obtenerEEPorProfesorYPeriodo(int idProfesor, int idPeriodo) throws SQLException {
        List<EE> listaEE = new ArrayList<>();

        try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
             PreparedStatement preparedStatement = conexion.prepareStatement(OBTENER_EE_POR_PROFESOR_Y_PERIODO)) {

            preparedStatement.setInt(1, idProfesor);
            preparedStatement.setInt(2, idPeriodo);

            try (ResultSet resultado = preparedStatement.executeQuery()) {
                while (resultado.next()) {
                    EE ee = new EE();
                    ee.setIdEE(resultado.getInt("idEE"));
                    ee.setProgramaEducativo(resultado.getString("programaEducativo"));
                    ee.setExperienciaEducativa(resultado.getString("experienciaEducativa"));
                    ee.setBloque(resultado.getString("bloque"));
                    ee.setSesion(resultado.getString("sesion"));
                    ee.setCreditos(resultado.getInt("creditos"));
                    ee.setHoras(resultado.getInt("horas"));
                    ee.setSemanas(resultado.getString("semanas"));
                    ee.setMes(resultado.getString("mes"));
                    ee.setIdPeriodo(resultado.getInt("Periodo_idPeriodo"));
                    ee.setIdProfesor(resultado.getInt("Profesor_idProfesor"));

                    listaEE.add(ee);
                }
            }
        } catch (SQLException ex) {
            LOG.error("Error al obtener las EE por profesor y periodo: ", ex);
            throw ex;
        }

        return listaEE;
    }

    @Override
    public int registrarEE(EE ee) throws SQLException {
        int numeroFilasAfectada = -1;
       try {
                Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
                conexion.setAutoCommit(false);
                PreparedStatement eEStatement = conexion.prepareStatement(AGREGAR_EE, Statement.RETURN_GENERATED_KEYS);
                eEStatement.setString(1, ee.getProgramaEducativo());
                eEStatement.setString(2, ee.getExperienciaEducativa());
                eEStatement.setString(3, ee.getBloque());
                eEStatement.setString(4, ee.getSesion());
                eEStatement.setInt(5, ee.getCreditos());
                eEStatement.setInt(6, ee.getHoras());
                eEStatement.setString(7, ee.getSemanas());
                eEStatement.setString(8, ee.getMes());
                eEStatement.setInt(9, ee.getIdPeriodo());
                eEStatement.setInt(10, ee.getIdProfesor());
                numeroFilasAfectada = eEStatement.executeUpdate();
                conexion.commit();
                
       } catch (SQLException ex) {
           ManejadorBaseDeDatos.obtenerConexion().rollback();
       } finally {
           ManejadorBaseDeDatos.obtenerConexion().close();
       }
       return numeroFilasAfectada;
    }

}
