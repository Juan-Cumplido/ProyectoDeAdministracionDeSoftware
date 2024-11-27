package manejadorBaseDeDatos;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Before;
import org.junit.Test;
import sdgcpp.accesoADatos.ManejadorBaseDeDatos;

public class PruebaManejadorBaseDeDatos {

    private Connection conexion;

    @Before
    public void establecerConexion() throws SQLException {
        conexion = ManejadorBaseDeDatos.obtenerConexion();
    }

    @Test
    public void pruebaConexionExitosa() {
        System.out.println("Obtener conexión");
        assertNotNull(conexion);
    }

    @Test
    public void pruebaConexionAbierta() throws SQLException {
        assertTrue(conexion.isValid(5));
    }

    @Test(expected = SQLException.class)
    public void pruebaErrorConexion() throws SQLException {
        ManejadorBaseDeDatos.obtenerConexion();
    }

    @Test
    public void pruebaCerrarConexion() throws SQLException {
        if (conexion != null && !conexion.isClosed()) {
            assertTrue(ManejadorBaseDeDatos.cerrarConexion());
        }

    }  
}

