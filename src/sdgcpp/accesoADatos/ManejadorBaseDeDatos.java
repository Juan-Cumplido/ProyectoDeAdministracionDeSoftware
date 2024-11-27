package sdgcpp.accesoADatos;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.Properties;
import org.apache.log4j.Logger;

public class ManejadorBaseDeDatos {
    private static final Logger LOG = Logger.getLogger(ManejadorBaseDeDatos.class);
    private static Connection conexion;
/**
     * Obtiene la conexión a la base de datos. Si no existe o está cerrada, se crea una nueva.
     *
     * @return la conexión a la base de datos
     * @throws SQLException si ocurre un error al obtener la conexión
     *
*/
    public static Connection obtenerConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            conexion = conectar();
        }

        return conexion;
    }

    /**
     * Establece una nueva conexión a la base de datos utilizando las propiedades especificadas en el archivo de configuración.
     *
     * @return la nueva conexión a la base de datos
     * @throws SQLException si ocurre un error al conectar a la base de datos o si no se pueden obtener las credenciales
    */
    private static Connection conectar() throws SQLException {
        Connection nuevaConexion = null;
        Properties propiedades = new ManejadorBaseDeDatos().obtenerArchivoPropiedades();

        if (propiedades != null) {
            String direccion = propiedades.getProperty("direccion");
            String usuario = propiedades.getProperty("usuario");
            String contraseña = propiedades.getProperty("contrasenya");
            nuevaConexion = DriverManager.getConnection(direccion, usuario, contraseña);
        } else {
            throw new SQLDataException("No fue posible encontrar las credenciales de la base de datos");
        }
        return nuevaConexion;
    }

    /**
     * Obtiene las propiedades de configuración de la base de datos desde un archivo.
     *
     * @return las propiedades de configuración, o null si ocurre un error al cargar el archivo
    */
    private Properties obtenerArchivoPropiedades() {
        Properties propiedades = null;

        try (FileInputStream archivoConfiguracion = new FileInputStream(new File("src\\sdgcpp\\accesoADatos\\Logger.txt"))) {
            if (archivoConfiguracion != null) {
                propiedades = new Properties();
                propiedades.load(archivoConfiguracion);
            }
        } catch (FileNotFoundException excepcionArchivoNoEncontrado) {
            System.out.println(excepcionArchivoNoEncontrado.getMessage());
        } catch (IOException excepcionIO) {
            System.out.println(excepcionIO.getMessage());
        }
        return propiedades;
    }

    /**
     * Cierra la conexión a la base de datos si está abierta.
     *
     * @return true si la conexión fue cerrada correctamente, false en caso contrario
    */
    public static boolean cerrarConexion() {
        boolean estaCerrada = false;

        try {
            if (conexion != null) {
                conexion.close();
            }
            estaCerrada = true;
        } catch (SQLException excepcionSQL) {
            LOG.fatal(excepcionSQL);
        }
        return estaCerrada;
    }

    /**
     * Realiza un rollback en la conexión actual de la base de datos.
     *
     * @return true si el rollback se realizó correctamente, false en caso contrario
     * @throws SQLException si ocurre un error al realizar el rollback
     */
    public static boolean rollback() throws SQLException {
        boolean revertir = false;

        try {
            if (conexion != null) {
                conexion.rollback();
            }
            revertir = true;
        } catch (SQLException sQLException) {
            throw sQLException;
        }
        return revertir;
    }
}

