package sdgcpp.logicaDeNegocio.implementacionDAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import sdgcpp.accesoADatos.ManejadorBaseDeDatos;
import sdgcpp.logicaDeNegocio.clases.Acceso;
import sdgcpp.logicaDeNegocio.clases.Profesor;
import sdgcpp.logicaDeNegocio.interfaces.IProfesor;


public class ProfesorDAO implements IProfesor {
    private static final Logger LOG = Logger.getLogger(ProfesorDAO.class);
    private static final String OBTENER_LISTA_DE_CATEGORIA_CONTRATACION = "SELECT idCategoriaContratacionUV, categoriaContratacion FROM categoria_contratacion_uv;";
    private static final String OBTENER_LISTA_DE_TIPO_CONSTRATACION = "SELECT idTipoContratacionUV, tipoContratacion FROM tipo_contratacion_uv;";
    private static final String AGREGAR_ACCESO = "{call insertarAcceso(?, ?, ?, ?)}";
    private static final String INSERTAR_PROFESOR = """
                                INSERT INTO profesor (nombre, apellidoPaterno, apellidoMaterno, correo, idAcceso, Categoria_Contratacion_UV_idCategoriaContratacionUV, Tipo_Contratacion_UV_idTipoContratacionUV)
                                VALUES ( ?, ?, ?, ?, ?, ?, ?);
                                """; 
    private static final String VERIFICAR_EXISTENCIA_NOMBRE_PROFESOR = "SELECT COUNT(*) AS number_of_matches FROM profesor WHERE (nombre = ?) AND (apellidoPaterno = ? AND apellidoMaterno = ?)";
    private static final String VERIFICAR_EXISTENCIA_CORREO = "SELECT COUNT(*) AS number_of_matches FROM profesor WHERE correo = ?";
    private static final String OBTENER_PROFESOR_CORREO = "SELECT * FROM profesor WHERE correo = ?;";
    private static final String OBTENER_PROFESOR_POR_ID = "SELECT * FROM profesor WHERE idProfesor = ?;";

    @Override
    public int registrarProfesorUV(Acceso acceso, Profesor profesor) throws SQLException {
        int numeroFilasAfectada = -1;
       int idAccesoGenerado = -1;
       try {
                Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
                conexion.setAutoCommit(false);
                CallableStatement accesoStatement = conexion.prepareCall(AGREGAR_ACCESO);
                accesoStatement.registerOutParameter(1, Types.INTEGER); 
                accesoStatement.setString(2, acceso.getContrasenia());
                accesoStatement.setString(3, acceso.getUsuario());
                accesoStatement.setString(4, acceso.getTipoUsuario());
                accesoStatement.execute();

                idAccesoGenerado = accesoStatement.getInt(1);
                
                PreparedStatement profesorStatement = conexion.prepareStatement(INSERTAR_PROFESOR, Statement.RETURN_GENERATED_KEYS);
                profesorStatement.setString(1, profesor.getNombre());
                profesorStatement.setString(2, profesor.getApellidoPaterno());
                profesorStatement.setString(3, profesor.getApellidoMaterno());
                profesorStatement.setString(4, profesor.getCorreo());
                profesorStatement.setInt(5, idAccesoGenerado);
                profesorStatement.setInt(6, profesor.getIdCategoriaContratacionUV());
                profesorStatement.setInt(7, profesor.getIdTipoContratacionUV());
                numeroFilasAfectada = profesorStatement.executeUpdate();
                conexion.commit();
                
       } catch (SQLException ex) {
           ManejadorBaseDeDatos.obtenerConexion().rollback();
       } finally {
           ManejadorBaseDeDatos.obtenerConexion().close();
       }
       return numeroFilasAfectada;
    }
    
        @Override
    public boolean verificarSiExisteElCorreo(String correo) throws SQLException {
        boolean existeCorreo = true;
        String consulta = VERIFICAR_EXISTENCIA_CORREO;
        Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
        PreparedStatement declaracion = conexion.prepareStatement(consulta);
        declaracion.setString(1, correo);
        ResultSet resultado = declaracion.executeQuery();
        
        if (resultado.next()) {
            int NO_COINCIDE = 0;
            
            if (resultado.getInt("number_of_matches") == NO_COINCIDE) {
                existeCorreo = false;
            }
        }
        ManejadorBaseDeDatos.obtenerConexion().close();
        return existeCorreo;
    }

     @Override
    public boolean verificarExistenciaProfesor(String nombre, String apellidoPaterno, String apellidoMaterno) throws SQLException {
        boolean existenciaProfesor = true;
        String consulta = VERIFICAR_EXISTENCIA_NOMBRE_PROFESOR;
        Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
        PreparedStatement declaracion = conexion.prepareStatement(consulta);
        declaracion.setString(1, nombre);
        declaracion.setString(2, apellidoPaterno);
        declaracion.setString(3, apellidoMaterno);
        ResultSet resultado = declaracion.executeQuery();
        if (resultado.next()) {
            int NO_COINCIDE = 0;
            if (resultado.getInt("number_of_matches") == NO_COINCIDE) {
                existenciaProfesor = false;
            }
        }
        ManejadorBaseDeDatos.cerrarConexion();
        
        return existenciaProfesor;
    }

    @Override
    public List<List<String>> obtenerListaDeCategoriaContratacion() {
        List<List<String>> listaDeCategoriaContratacion = new ArrayList<>();
        String consulta = OBTENER_LISTA_DE_CATEGORIA_CONTRATACION;
        try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion()){
            PreparedStatement declaracion = conexion.prepareStatement(consulta);
            ResultSet resultado = declaracion.executeQuery(); 
            while (resultado.next()) {
                int idCategoriaContratacionUV = resultado.getInt("idCategoriaContratacionUV");
                String categoriaContratacion = resultado.getString("categoriaContratacion");
                List<String> categoria = new ArrayList<>();
                categoria.add(Integer.toString(idCategoriaContratacionUV));
                categoria.add(categoriaContratacion);
                listaDeCategoriaContratacion.add(categoria);
            }
        } catch (SQLException ex) {
            LOG.error(ex);
        }
        return listaDeCategoriaContratacion;
    }

    @Override
    public List<List<String>> obtenerListaDeTipoContratacion() {
        List<List<String>> listaDeTipoContratacion = new ArrayList<>();
        String consulta = OBTENER_LISTA_DE_TIPO_CONSTRATACION;
        try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion()){
            PreparedStatement declaracion = conexion.prepareStatement(consulta);
            ResultSet resultado = declaracion.executeQuery(); 
            while (resultado.next()) {
                int idTipoContratacionUV = resultado.getInt("idTipoContratacionUV");
                String tipoContratacion = resultado.getString("tipoContratacion");
                List<String> tipo = new ArrayList<>();
                tipo.add(Integer.toString(idTipoContratacionUV));
                tipo.add(tipoContratacion);
                listaDeTipoContratacion.add(tipo);
            }
            
        } catch (SQLException ex) {
            LOG.error(ex);
        }
        return listaDeTipoContratacion;
    }
    
     @Override
    public Profesor obtenerProfesorPorID(String idProfesor) throws SQLException {
        Profesor profesor = new Profesor();
        String consulta = OBTENER_PROFESOR_POR_ID;
        Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
        PreparedStatement declaracion = conexion.prepareStatement(consulta);
        declaracion.setString(1, idProfesor);
        ResultSet resultado = declaracion.executeQuery();
        if (resultado.next()) {
            profesor = obtenerProfesor(resultado);
        }
        ManejadorBaseDeDatos.cerrarConexion();
        return profesor;

    }

    @Override
    public Profesor obtenerProfesorPorCorreo(String correo) throws SQLException { 
     Profesor profesor = new Profesor();
     profesor = null;
        String consulta = OBTENER_PROFESOR_CORREO;
        Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
        PreparedStatement declaracion = conexion.prepareStatement(consulta);
        declaracion.setString(1, correo);
        ResultSet resultado = declaracion.executeQuery();
        if (resultado.next()) {
            profesor = obtenerProfesor(resultado);
        }
        ManejadorBaseDeDatos.cerrarConexion();
        return profesor;
    }
    
    private Profesor obtenerProfesor(ResultSet resultado) throws SQLException {
        Profesor profesor = new Profesor();
            profesor.setIdProfesor(resultado.getInt("idProfesor"));
            profesor.setNombre(resultado.getString("nombre"));
            profesor.setApellidoPaterno(resultado.getString("apellidoPaterno"));
            profesor.setApellidoMaterno(resultado.getString("apellidoMaterno"));
            profesor.setCorreo(resultado.getString("correo"));
            profesor.setIdAcceso(resultado.getInt("idAcceso"));
            
        return profesor;
    }

}