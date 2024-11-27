package sdgcpp.logicaDeNegocio.implementacionDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import sdgcpp.accesoADatos.ManejadorBaseDeDatos;
import sdgcpp.logicaDeNegocio.clases.Periodo;
import sdgcpp.logicaDeNegocio.interfaces.IPeriodo;

public class PeriodoDAO implements IPeriodo {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(PeriodoDAO.class);
    
    private static final String CONSULTA_AGREGAR_PERIODO = "INSERT INTO periodo (fechaInicio, fechaFin, nombrePeriodo) VALUES (?, ?, ?)";
    private static final String CONSULTA_CONSULTAR_TODOS_PERIODOS = "SELECT * FROM periodo";

    /**
    * Agrega un nuevo período académico a la base de datos.
    * 
    * @param periodo El período académico a agregar.
    * @return El número de filas afectadas en la base de datos.
    */
    @Override
    public int agregarPeriodo(Periodo periodo) {
        int resultado = 0;
        String consulta = CONSULTA_AGREGAR_PERIODO;
        try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
             PreparedStatement declaracion = conexion.prepareStatement(consulta)) {
            declaracion.setDate(1, periodo.getFechaInicio());
            declaracion.setDate(2, periodo.getFechaFin());
            declaracion.setString(3, periodo.getNombrePeriodo());
            resultado = declaracion.executeUpdate();
        } catch (SQLException ex) {
            LOG.error(ex);
        }
        return resultado;
    }
    
    /**
     * Obtiene una lista de todos los períodos académicos almacenados en la base de datos.
     * Cada elemento de la lista es una lista de cadenas que contiene el ID del período, el nombre del período,
     * la fecha de inicio y la fecha de fin.
     * 
     * @return Una lista de listas de cadenas que representan los períodos académicos.
     */
    @Override
    public List<List<String>> obtenerListaDePeriodos() {
        List<List<String>> listaDePeriodos = new ArrayList<>();
         String consulta = CONSULTA_CONSULTAR_TODOS_PERIODOS;
         try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion()){
            PreparedStatement declaracion = conexion.prepareStatement(consulta);
            ResultSet resultado = declaracion.executeQuery();   
            while (resultado.next()) {
                    List<String> periodo = new ArrayList<>();
                    periodo.add(resultado.getString("idPeriodo"));
                    periodo.add(resultado.getString("nombrePeriodo"));
                    periodo.add(resultado.getString("fechaInicio"));
                    periodo.add(resultado.getString("fechaFin"));
                    listaDePeriodos.add(periodo);
                }
        } catch (SQLException ex) {
            LOG.error(ex);
        }

        return listaDePeriodos;
    }
}
