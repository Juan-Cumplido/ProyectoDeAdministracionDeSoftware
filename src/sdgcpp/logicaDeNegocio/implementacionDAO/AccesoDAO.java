package sdgcpp.logicaDeNegocio.implementacionDAO;

import com.mysql.cj.jdbc.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import sdgcpp.accesoADatos.ManejadorBaseDeDatos;
import sdgcpp.logicaDeNegocio.clases.Acceso;
import sdgcpp.logicaDeNegocio.clases.Profesor;
import sdgcpp.logicaDeNegocio.interfaces.IAcceso;

public class AccesoDAO implements IAcceso {
    private static final Logger LOG = Logger.getLogger(AccesoDAO.class);
    private static final String AGREGAR_ACCESO = "{CALL agregarAcceso(?, ?, ?)}";
    private static final String ACCESO_EXISTENTE = "SELECT COUNT(*) FROM acceso WHERE usuario = ? AND contraseña = SHA2(?, 256)";
    private static final String OBTENER_ACCESO = "SELECT * FROM acceso WHERE usuario = ? AND contraseña = SHA2(?, 256)";
    private static final String OBTENER_IDPROFESOR = """
                                                     SELECT idProfesor
                                                     FROM profesor
                                                     WHERE idAcceso = (SELECT idAcceso FROM acceso WHERE usuario = ? AND contraseña = SHA2(?, 256));""";
    private static final String OBTENER_PROFESOR_POR_ID = "SELECT * FROM profesor WHERE idProfesor = ?;";
    private static final String ACTUALIZAR_BASEDEDATOS = "{CALL verificarActividadesColaborativas()}";
    private static final String ACTUALIZAR_ACCESO = """
                                                         UPDATE acceso
                                                         SET usuario = ?, contraseña = SHA2(?, 256)
                                                         WHERE idAcceso = (
                                                             SELECT idAcceso
                                                             FROM profesor
                                                             WHERE idProfesor = ?);""";
    
        

    /**
     * Ejecuta una actualización en la base de datos mediante un procedimiento almacenado.
     *
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
     */
    @Override
    public void ejecutarActualizacionBaseDatos() throws SQLException {
        try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
             CallableStatement declaraciónInvocable = (CallableStatement) conexion.prepareCall(ACTUALIZAR_BASEDEDATOS)){
            declaraciónInvocable.execute();
            System.out.println("Procedimiento almacenado ejecutado correctamente.");
        } catch (SQLException ex) {
            System.err.println("Error al ejecutar el procedimiento almacenado: " + ex.getMessage());
        } 
    }

    /**
     * Agrega un nuevo acceso a la base de datos.
     *
     * @param acceso El objeto Acceso que contiene la información del acceso a agregar.
     * @return Un entero que indica el número de filas afectadas por la operación.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
     */
    @Override
    public int agregarAcceso(Acceso acceso) throws SQLException {
        int resultado;
        String consulta = AGREGAR_ACCESO;
        try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
             CallableStatement declaraciónInvocable = (CallableStatement) conexion.prepareCall(consulta)){
            declaraciónInvocable.setString(1, acceso.getContrasenia());
            declaraciónInvocable.setString(2, acceso.getUsuario());
            declaraciónInvocable.setString(3, acceso.getTipoUsuario());
            resultado = declaraciónInvocable.executeUpdate();
        }
        return resultado;    
    }

    /**
     * Actualiza un acceso existente en la base de datos.
     *
     * @param acceso El objeto Acceso que contiene la nueva información del acceso.
     * @param idProfesor El ID del profesor asociado al acceso.
     * @return Un entero que indica el número de filas afectadas por la operación.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
     */
    @Override
    public int actualizarAcceso(Acceso acceso, int idProfesor) throws SQLException {
        int resultado;
        String consulta = ACTUALIZAR_ACCESO;
        try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
             PreparedStatement declaracion = conexion.prepareStatement(consulta)){
            declaracion.setString(1, acceso.getUsuario());
            declaracion.setString(2, acceso.getContrasenia());
            declaracion.setInt(3, idProfesor);
            resultado = declaracion.executeUpdate();
        }
        return resultado;    
    }

    /**
     * Verifica la existencia de un acceso en la base de datos.
     *
     * @param usuario El nombre de usuario del acceso.
     * @param contrasenia La contraseña del acceso.
     * @return Un entero que indica si el acceso existe (1) o no (0).
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
     */
    @Override
    public int verificarExistenciaAcceso(String usuario, String contrasenia) throws SQLException {
        int existeAcceso = 0;
        String consulta = ACCESO_EXISTENTE;
        try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
             PreparedStatement declaracion = conexion.prepareStatement(consulta)){
            declaracion.setString(1, usuario);
            declaracion.setString(2, contrasenia);
            ResultSet resultado = declaracion.executeQuery();
            resultado.next();
            existeAcceso = resultado.getInt(1);
        }
        return existeAcceso;
    }

    /**
     * Obtiene el tipo de usuario para un acceso dado.
     *
     * @param usuario El nombre de usuario del acceso.
     * @param contrasenia La contraseña del acceso.
     * @return Una cadena que indica el tipo de usuario, o "NoExiste" si el acceso no se encuentra.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
     */
    @Override
    public String obtenerTipoUsuario(String usuario, String contrasenia) throws SQLException {
        String consulta = OBTENER_ACCESO;
        String tipoUsuario = "NoExiste";
        try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
             PreparedStatement declaracion = conexion.prepareStatement(consulta)){
            declaracion.setString(1, usuario);
            declaracion.setString(2, contrasenia);
            ResultSet resultado = declaracion.executeQuery();
            if (resultado.next()) {
                tipoUsuario = resultado.getString("tipoUsuario");
            }
        }
        return tipoUsuario;
    }

    /**
     * Obtiene el ID del profesor asociado a un acceso dado.
     *
     * @param usuario El nombre de usuario del acceso.
     * @param contrasenia La contraseña del acceso.
     * @return El ID del profesor, o -1 si no se encuentra.
     */
    @Override
    public int obtenerIdProfesor(String usuario, String contrasenia) {
        String consulta = OBTENER_IDPROFESOR;
        int idProfesor = -1;
        try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion()){
            PreparedStatement declaracion = conexion.prepareStatement(consulta);
            declaracion.setString(1, usuario);
            declaracion.setString(2, contrasenia);
            ResultSet resultado = declaracion.executeQuery();
            if (resultado.next()) {
                idProfesor = resultado.getInt("idProfesor");
            }
        } catch (SQLException sqlException){
            LOG.fatal(sqlException.getMessage());
        }
        return idProfesor;
    }

    /**
     * Obtiene un profesor por su ID.
     *
     * @param idProfesor El ID del profesor a obtener.
     * @return Un objeto Profesor con la información del profesor.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
     */
    @Override
    public Profesor obtenerProfesorPorID(int idProfesor) throws SQLException {
        Profesor profesor = new Profesor();
        String consulta = OBTENER_PROFESOR_POR_ID;
        Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
        PreparedStatement declaracion = conexion.prepareStatement(consulta);
        declaracion.setInt(1, idProfesor);
        ResultSet resultado = declaracion.executeQuery();
        if (resultado.next()) {
            profesor = obtenerProfesor(resultado);
        }
        ManejadorBaseDeDatos.cerrarConexion();
        return profesor;
    }

    /**
     * Convierte un ResultSet en un objeto Profesor.
     *
     * @param resultado El ResultSet con la información del profesor.
     * @return Un objeto Profesor con la información obtenida del ResultSet.
     * @throws SQLException Si ocurre un error al interactuar con el ResultSet.
     */
    private Profesor obtenerProfesor(ResultSet resultado) throws SQLException {
        Profesor profesor = new Profesor();
        profesor.setIdProfesor(resultado.getInt("idProfesor"));
        profesor.setNombre(resultado.getString("nombre"));
        profesor.setApellidoPaterno(resultado.getString("apellidoPaterno"));
        profesor.setApellidoMaterno(resultado.getString("apellidoMaterno"));
        profesor.setCorreo(resultado.getString("correo"));
        profesor.setIdIdiomas(resultado.getInt("idIdiomas"));
        profesor.setEstadoProfesor(resultado.getString("estadoProfesor"));
        profesor.setIdAcceso(resultado.getInt("idAcceso"));
        profesor.setClaveInstitucional(resultado.getString("Institucion_claveInstitucional"));
        return profesor;
    }

   
}