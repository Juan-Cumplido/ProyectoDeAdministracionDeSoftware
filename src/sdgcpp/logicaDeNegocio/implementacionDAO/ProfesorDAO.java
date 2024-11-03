package sdgcpp.logicaDeNegocio.implementacionDAO;

import java.sql.Statement;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Types;
import org.apache.log4j.Logger;
import sdgcpp.accesoADatos.ManejadorBaseDeDatos;
import sdgcpp.logicaDeNegocio.clases.Acceso;
import sdgcpp.logicaDeNegocio.clases.Profesor;
import sdgcpp.logicaDeNegocio.interfaces.IProfesor;


public class ProfesorDAO implements IProfesor {
    private static final Logger LOG = Logger.getLogger(ProfesorDAO.class);
    private static final String INSERTAR_PROFESOR = """
            INSERT INTO profesor (nombre, apellidoPaterno, apellidoMaterno, correo, idIdiomas, idAcceso, estadoProfesor, Institucion_claveInstitucional)
            VALUES ( ?, ?, ?, ?, ?, ?, ?, ?); """;
    private static final String AGREGAR_ACCESO = "{call insertarAcceso(?, ?, ?, ?)}";
    private static final String OBTENER_TODOS_LOS_PROFESORES = """
            SELECT p.idProfesor, p.nombre, p.apellidoPaterno, p.apellidoMaterno, p.correo, p.idIdiomas, p.idAcceso, p.estadoProfesor, i.nombreInstitucion 
            FROM profesor p JOIN institucion i ON p.Institucion_claveInstitucional = i.claveInstitucional;""";
    private static final String OBTENER_LISTA_DE_IDIOMA = "SELECT idIdiomas, nombreIdioma FROM idiomas;";
    private static final String OBTENER_LISTA_DE_INTITUCIONES = "SELECT claveInstitucional, nombreInstitucion FROM institucion;";
    private static final String OBTENER_LISTA_DE_INTITUCIONES_MENOS_LA_UV = """
                                                                                SELECT claveInstitucional, nombreInstitucion 
                                                                            FROM institucion
                                                                            WHERE claveInstitucional != '30MSU0940B';""";

    private static final String VERIFICAR_EXISTENCIA_NOMBRE_PROFESOR = "SELECT COUNT(*) AS number_of_matches FROM profesor WHERE (nombre = ?) AND (apellidoPaterno = ? AND apellidoMaterno = ?)";
    private static final String VERIFICAR_EXISTENCIA_CORREO = "SELECT COUNT(*) AS number_of_matches FROM profesor WHERE correo = ?";
    private static final String OBTENER_TODOS_LOS_ESTADOPROFESOR= "SELECT estadoProfesor FROM profesor";
    private static final String ELIMINAR_PROFESOR = """
            DELETE profesor, acceso FROM profesor JOIN acceso ON profesor.idAcceso = acceso.idAcceso
            WHERE profesor.correo = ? ;""";
    private static final String OBTENER_PROFESOR_CORREO = "SELECT * FROM profesor WHERE correo = ?;";
    private static final String OBTENER_NOMBRES_DE_INSTITUCION = "SELECT nombreInstitucion FROM institucion;";
    private static final String OBTENER_PROFESOR_POR_ID = "SELECT * FROM profesor WHERE idProfesor = ?;";
    private static final String OBTENER_PROFESORES_POR_NOMBRE= """
            SELECT p.idProfesor, p.nombre, p.apellidoPaterno, p.apellidoMaterno, p.correo, p.idIdiomas,
                p.idAcceso, p.estadoProfesor, i.nombreInstitucion AS Institucion_claveInstitucional
            FROM profesor p
            JOIN institucion i ON p.Institucion_claveInstitucional = i.claveInstitucional
            WHERE p.nombre LIKE ? OR p.apellidoPaterno LIKE ? OR p.apellidoMaterno LIKE ? ;""";
    private static final String IDENTIFICAR_PROFESOR = 
            "SELECT IF(puv.noPersonal IS NOT NULL, puv.noPersonal, p.idProfesor) AS resultado FROM profesor p " +
            "LEFT JOIN profesor_uv puv ON p.idProfesor = puv.Profesor_idProfesor WHERE p.correo = ?";
    private static final String ACTUALIZAR_PROFESOR = "UPDATE profesor SET nombre = ?, apellidoPaterno = ?, apellidoMaterno = ?, correo = ?, " +
            "idIdiomas = ?, estadoProfesor = ?, Institucion_claveInstitucional = ? WHERE idProfesor = ?";
    private static final String VERIFICAR_QUE_EL_PROFESOR_NO_SE_REPITA = "SELECT COUNT(*) FROM profesor " +
            "WHERE nombre = ? AND apellidoPaterno = ? AND apellidoMaterno = ? AND correo = ? " +
            "AND idIdiomas = ? AND estadoProfesor = ? AND Institucion_claveInstitucional = ?";

  
    /**
    * Registra un nuevo profesor en la base de datos junto con su información de acceso.
    * 
    * @param profesor El objeto Profesor a registrar.
    * @param acceso El objeto Acceso asociado al profesor.
    * @return El número de filas afectadas en la base de datos.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
    */
    @Override
    public int registrarProfesor(Profesor profesor, Acceso acceso) throws SQLException{
       int numeroFilasAfectada = -1;
       int idAccesoGenerado = -1;
       int idProfesorGenerado = -1;

       try {
                Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
                conexion.setAutoCommit(false);
                CallableStatement declaraciónInvocable = conexion.prepareCall(AGREGAR_ACCESO);
                declaraciónInvocable.registerOutParameter(1, Types.INTEGER); 
                declaraciónInvocable.setString(2, acceso.getContrasenia());
                declaraciónInvocable.setString(3, acceso.getUsuario());
                declaraciónInvocable.setString(4, acceso.getTipoUsuario());
                declaraciónInvocable.execute();

                idAccesoGenerado = declaraciónInvocable.getInt(1);

                PreparedStatement consultaProfesor = conexion.prepareStatement(INSERTAR_PROFESOR, Statement.RETURN_GENERATED_KEYS);
                consultaProfesor.setString(1, profesor.getNombre());
                consultaProfesor.setString(2, profesor.getApellidoPaterno());
                consultaProfesor.setString(3, profesor.getApellidoMaterno());
                consultaProfesor.setString(4, profesor.getCorreo());
                consultaProfesor.setInt(5, profesor.getIdIdiomas());
                consultaProfesor.setInt(6, idAccesoGenerado);
                consultaProfesor.setString(7, profesor.getEstadoProfesor());
                consultaProfesor.setString(8, profesor.getClaveInstitucional());
                numeroFilasAfectada = consultaProfesor.executeUpdate();
                
                ResultSet generatedKeys = consultaProfesor.getGeneratedKeys();
                if (generatedKeys.next()) {
                    idProfesorGenerado = generatedKeys.getInt(1);
                    profesor.setIdProfesor(idProfesorGenerado);
                }

                conexion.commit();
       } catch (SQLException ex) {
           ManejadorBaseDeDatos.obtenerConexion().rollback();
       } finally {
           ManejadorBaseDeDatos.obtenerConexion().close();
       }
       return numeroFilasAfectada;
    }
    
    /**
    * Actualiza la información de un profesor en la base de datos.
    * 
    * @param profesor El objeto Profesor con la información actualizada.
    * @param idProfesor El ID del profesor a actualizar.
    * @return El número de filas afectadas en la base de datos.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
    */
    @Override
    public int actualizarInformacionDelProfesor(Profesor profesor, String idProfesor) throws SQLException{
        int resutado = -1;
        String consulta = ACTUALIZAR_PROFESOR;
        Connection connection = ManejadorBaseDeDatos.obtenerConexion();
        PreparedStatement declaracion = connection.prepareStatement(consulta);
        declaracion.setString(1, profesor.getNombre());
        declaracion.setString(2, profesor.getApellidoPaterno());
        declaracion.setString(3, profesor.getApellidoMaterno());
        declaracion.setString(4, profesor.getCorreo());
        declaracion.setInt(5, profesor.getIdIdiomas());
        declaracion.setString(6, profesor.getEstadoProfesor());
        declaracion.setString(7, profesor.getClaveInstitucional());
        declaracion.setString(8, idProfesor);
        resutado = declaracion.executeUpdate();
        ManejadorBaseDeDatos.cerrarConexion();
        return resutado; 
    }
    
    /**
    * Verifica si ya existe un profesor con la misma información en la base de datos.
    * 
    * @param profesor El objeto Profesor a verificar.
    * @return true si ya existe un profesor con la misma información, false en caso contrario.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
    */
    @Override
    public boolean verificarProfesorDuplicado(Profesor profesor) throws SQLException {
        boolean existeProfesor = false;
            Connection connection = ManejadorBaseDeDatos.obtenerConexion();
            PreparedStatement declaracion = connection.prepareStatement(VERIFICAR_QUE_EL_PROFESOR_NO_SE_REPITA);
            declaracion.setString(1, profesor.getNombre());
            declaracion.setString(2, profesor.getApellidoPaterno());
            declaracion.setString(3, profesor.getApellidoMaterno());
            declaracion.setString(4, profesor.getCorreo());
            declaracion.setInt(5, profesor.getIdIdiomas());
            declaracion.setString(6, profesor.getEstadoProfesor());
            declaracion.setString(7, profesor.getClaveInstitucional());

            ResultSet resultado = declaracion.executeQuery();

            if (resultado.next()) {
                int count = resultado.getInt(1);
                existeProfesor = count > 0;
            }
        
        ManejadorBaseDeDatos.cerrarConexion();
        return existeProfesor;
    }
   
    /**
     * Obtiene una lista de profesores cuyo nombre coincida con el criterio de búsqueda.
     * 
     * @param criterioBusqueda El criterio de búsqueda para el nombre del profesor.
     * @return Una lista de profesores que coinciden con el criterio de búsqueda.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
     */
     @Override
     public List<Profesor> obtenerListaProfesoresPorNombre(String criterioBusqueda) throws SQLException {
        String consulta = OBTENER_PROFESORES_POR_NOMBRE;
            List<Profesor> listaProfesoresPorNombre = new ArrayList<>();
            Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
            PreparedStatement declaracion = conexion.prepareStatement(consulta);
                String criterioBusquedaLike = "%" + criterioBusqueda + "%";
                declaracion.setString(1, criterioBusquedaLike);
                declaracion.setString(2, criterioBusquedaLike);
                declaracion.setString(3, criterioBusquedaLike);
            ResultSet resultado = declaracion.executeQuery(); 
            listaProfesoresPorNombre = obtenerListaProfesores(resultado);
         
            ManejadorBaseDeDatos.cerrarConexion();
        return listaProfesoresPorNombre;
     }

    /**
     * Elimina un profesor de la base de datos dado su correo electrónico.
     * 
     * @param correo El correo electrónico del profesor a eliminar.
     * @return El número de filas afectadas en la base de datos.
     */
    @Override
    public int eliminarProfesor(String correo) {
        int columnaAfectada = -1;
        try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion()){
            PreparedStatement declaracion = conexion.prepareStatement(ELIMINAR_PROFESOR);
            declaracion.setString(1, correo);
            columnaAfectada = declaracion.executeUpdate();
        } catch (SQLException ex) {
            LOG.error(ex);
        }
        ManejadorBaseDeDatos.cerrarConexion();
        return columnaAfectada;
    }
    
    /**
     * Identifica si un profesor es de la Universidad Virtual del Estado de Michoacán (UVA) o un profesor externo.
     * 
     * @param correo El correo electrónico del profesor a identificar.
     * @return Una cadena que indica si el profesor es de la UVA o externo.
     */
    @Override
    public String identitificarProfesorUVOProfesorExterno(String correo) {
        String resultado = "";
        try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion()){
            PreparedStatement declaracion = conexion.prepareStatement(IDENTIFICAR_PROFESOR);
            declaracion.setString(1, correo);
            
            ResultSet resultadoDeclaracion = declaracion.executeQuery();
            if (resultadoDeclaracion.next()) {
                resultado = resultadoDeclaracion.getString("resultado");
            }
        
        } catch (SQLException ex) {
            LOG.error(ex);
        }
        ManejadorBaseDeDatos.cerrarConexion();
        return resultado;
    }

    /**
    * Verifica la existencia de un profesor en la base de datos dado su nombre completo.
    * 
    * @param nombre El nombre del profesor.
    * @param apellidoPaterno El apellido paterno del profesor.
    * @param apellidoMaterno El apellido materno del profesor.
    * @return true si el profesor existe en la base de datos, false en caso contrario.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
    */
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
   
    /**
     * Verifica si existe un profesor con el correo electrónico proporcionado.
     * 
     * @param correo El correo electrónico del profesor a verificar.
     * @return true si el correo electrónico ya está registrado en la base de datos, false en caso contrario.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
     */
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
    
    /**
     * Obtiene una lista de todos los idiomas almacenados en la base de datos.
     * 
     * @return Una lista de listas de strings, donde cada lista contiene el ID y el nombre del idioma.
     */
    @Override
    public List<List<String>> obtenerListaDeIdiomas() {
    List<List<String>> listaDeIdiomas = new ArrayList<>();
         String consulta = OBTENER_LISTA_DE_IDIOMA;
         try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion()){
         PreparedStatement declaracion = conexion.prepareStatement(consulta);
         ResultSet resultado = declaracion.executeQuery(); 
            while (resultado.next()) {
                int idIdioma = resultado.getInt("idIdiomas");
                String nombreIdioma = resultado.getString("nombreIdioma");
                List<String> idioma = new ArrayList<>();
                idioma.add(Integer.toString(idIdioma));
                idioma.add(nombreIdioma);
                listaDeIdiomas.add(idioma);
        }  
        } catch (SQLException ex) {
            LOG.error(ex);
        }  
        return listaDeIdiomas;
    }
    
    /**
    * Obtiene una lista de todos los estados de profesor almacenados en la base de datos.
    * 
    * @return Una lista de strings con los nombres de los estados de profesor.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
    */
    @Override
     public List<String>obtenerListaDeTodosLosEstadoProfesor() throws SQLException{
         List<String> listaEstadoProfesor = new ArrayList<>();
         String consulta = OBTENER_TODOS_LOS_ESTADOPROFESOR;
         Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
         PreparedStatement statement = conexion.prepareStatement(consulta);
         ResultSet resultado = statement.executeQuery(); 
            while (resultado.next()) {
                String estadoProfesor = resultado.getString("estadoProfesor");
                listaEstadoProfesor.add(estadoProfesor);
            }
        ManejadorBaseDeDatos.cerrarConexion();
        return listaEstadoProfesor;
     }
     
     /**
    * Obtiene una lista de todos los nombres de instituciones almacenadas en la base de datos.
    * 
    * @return Una lista de strings con los nombres de las instituciones.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
    */
    @Override
     public List<String>obtenerListaDeNombreInstitucion() throws SQLException{
         List<String> listaEstadoProfesor = new ArrayList<>();
         String consulta = OBTENER_NOMBRES_DE_INSTITUCION;
         Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
         PreparedStatement declaracion = conexion.prepareStatement(consulta);
         ResultSet resultado = declaracion.executeQuery(); 
            while (resultado.next()) {
                String estadoProfesor = resultado.getString("nombreInstitucion");
                listaEstadoProfesor.add(estadoProfesor);
            }
        ManejadorBaseDeDatos.cerrarConexion();
        return listaEstadoProfesor;
     }
    
     /**
    * Obtiene una lista de todas las instituciones almacenadas en la base de datos.
    * 
    * @return Una lista de listas de strings, donde cada lista contiene la clave y el nombre de la institución.
    */
    @Override
    public List<List<String>> obtenerListaDeInstituciones() {
    List<List<String>> listaDeInstituciones = new ArrayList<>();
         String consulta = OBTENER_LISTA_DE_INTITUCIONES;
         try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion()){
         PreparedStatement declaracion = conexion.prepareStatement(consulta);
         ResultSet resultado = declaracion.executeQuery(); 
            while (resultado.next()) {
            String clave = resultado.getString("claveInstitucional");
            String nombreInstitucion = resultado.getString("nombreInstitucion");
            List<String> institucion = new ArrayList<>();
            institucion.add(clave);
            institucion.add(nombreInstitucion);
            listaDeInstituciones.add(institucion);
        }
        } catch (SQLException ex) {
            LOG.error(ex);
        }  
        return listaDeInstituciones;
     }

    /**
     * Obtiene un profesor por su correo electrónico.
     * 
     * @param correo El correo electrónico del profesor a buscar.
     * @return El objeto Profesor correspondiente al correo proporcionado, o un objeto Profesor vacío si no se encuentra.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
     */
    @Override
    public Profesor obtenerProfesorPorCorreo (String correo) throws SQLException {
        Profesor profesor = new Profesor();
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

    /**
     * Obtiene una lista de todos los profesores almacenados en la base de datos.
     * 
     * @return Una lista de objetos Profesor que representan a todos los profesores almacenados.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
     */
    @Override
    public List<Profesor> obtenerListaTodosLosProfesores() throws SQLException {
    List<Profesor> listaProfesores = new ArrayList<>();
    Connection conexion = ManejadorBaseDeDatos.obtenerConexion();
    PreparedStatement declaracion = conexion.prepareStatement(OBTENER_TODOS_LOS_PROFESORES);
    ResultSet resultado = declaracion.executeQuery();

    while (resultado.next()) {
        Profesor profesor = new Profesor();
        profesor.setIdProfesor(resultado.getInt("idProfesor"));
        profesor.setNombre(resultado.getString("nombre"));
        profesor.setApellidoPaterno(resultado.getString("apellidoPaterno"));
        profesor.setApellidoMaterno(resultado.getString("apellidoMaterno"));
        profesor.setCorreo(resultado.getString("correo"));
        profesor.setIdIdiomas(resultado.getInt("idIdiomas"));
        profesor.setIdAcceso(resultado.getInt("idAcceso"));
        profesor.setEstadoProfesor(resultado.getString("estadoProfesor"));
        profesor.setClaveInstitucional(resultado.getString("nombreInstitucion"));

        listaProfesores.add(profesor);
    }

    ManejadorBaseDeDatos.cerrarConexion();
    return listaProfesores;
    }
    
    /**
     * Obtiene un profesor por su ID.
     * 
     * @param idProfesor El ID del profesor a buscar.
     * @return El objeto Profesor correspondiente al ID proporcionado, o un objeto Profesor vacío si no se encuentra.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
     */
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

    /**
     * Método auxiliar para obtener una lista de profesores a partir de un ResultSet.
     * 
     * @param resultado El ResultSet que contiene los datos de los profesores.
     * @return Una lista de objetos Profesor extraídos del ResultSet.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
     */
    private List<Profesor> obtenerListaProfesores(ResultSet resultado) throws SQLException {
        List<Profesor> listaProfesores = new ArrayList<>();
        
        while (resultado.next()) {
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
            listaProfesores.add(profesor);
        }
        return listaProfesores;
    }
    
    /**
    * Método auxiliar para obtener un objeto Profesor a partir de un ResultSet.
    * 
    * @param resultado El ResultSet que contiene los datos de un profesor.
    * @return Un objeto Profesor con los datos extraídos del ResultSet.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos, por ejemplo:
     *                      - Cuando no se puede establecer conexión con la base de datos.
     *                      - Si los parámetros proporcionados son inválidos o nulos.
     *                      - Si la consulta SQL no se puede ejecutar correctamente.
     *                      - Si hay un problema al cerrar la conexión con la base de datos.
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
    
    @Override
    public List<List<String>> obtenerListaDeInstitucionesSinInstitucion() {
    List<List<String>> listaDeInstituciones = new ArrayList<>();
         String consulta = OBTENER_LISTA_DE_INTITUCIONES_MENOS_LA_UV;
         try (Connection conexion = ManejadorBaseDeDatos.obtenerConexion()){
         PreparedStatement declaracion = conexion.prepareStatement(consulta);
         ResultSet resultado = declaracion.executeQuery(); 
            while (resultado.next()) {
            String clave = resultado.getString("claveInstitucional");
            String nombreInstitucion = resultado.getString("nombreInstitucion");
            List<String> institucion = new ArrayList<>();
            institucion.add(clave);
            institucion.add(nombreInstitucion);
            listaDeInstituciones.add(institucion);
        }
        } catch (SQLException ex) {
            LOG.error(ex);
        }  
        return listaDeInstituciones;
     }
}