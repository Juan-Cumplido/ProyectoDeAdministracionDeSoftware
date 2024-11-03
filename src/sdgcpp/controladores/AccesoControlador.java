package sdgcpp.controladores;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import sdgcpp.logicaDeNegocio.clases.Profesor;
import sdgcpp.logicaDeNegocio.implementacionDAO.AccesoDAO;
import sdgcpp.utilidades.Alertas;
import sdgcpp.utilidades.AccesoSingleton;
import sdgcpp.utilidades.ColaboracionEnCursoSinglenton;

public class AccesoControlador implements Initializable {
    private static final Logger LOG = Logger.getLogger(AccesoControlador.class);
    @FXML
    private TextField textField_Usuario;
    @FXML
    private Button button_Acceso;
    @FXML
    private ImageView imageAccesoFondo;
    @FXML
    private PasswordField paswordField_Contrasenia;


    private boolean estaVacio() {
        return textField_Usuario.getText().isEmpty() || paswordField_Contrasenia.getText().isEmpty();
    }

    private int verificarAcceso() {
        if (!estaVacio()) {
            AccesoDAO accesoDao = new AccesoDAO();

            try {
                int existeAcceso = accesoDao.verificarExistenciaAcceso(textField_Usuario.getText(), paswordField_Contrasenia.getText());
                if (existeAcceso > 0) {
                    return existeAcceso;
                } else {
                    Alertas.mostrarMensajeInicioSesionFallido();
                }
            } catch (SQLException sqlException) {
                Alertas.mostrarMensajeErrorBaseDatos();
                LOG.fatal("No hay conexión con la base de datos :" +this.getClass().getName() + ", método " + Thread.currentThread().getStackTrace()[1].getMethodName() + ": " + sqlException.getMessage(), sqlException);
            }
        } else {
            Alertas.mostrarMensajeCamposVacios();
        }
        return 0;
    }
    
    private void abrirVentanaAdministrativoMenu() {
        Stage escenario = (Stage) button_Acceso.getScene().getWindow();
        SDGCOILVIC sdgcoilvic = new SDGCOILVIC();
        try {
            
        } catch (NullPointerException ex) {
            LOG.error("Error al abrir la ventana administrativa: " + ex.getMessage());
            Alertas.mostrarMensajeErrorCambioPantalla();
        }
    }

    private void abrirVentanaProfesorMenu() {
        
    }
    
    @FXML
    private void button_Acceso(ActionEvent event) {
        Node fuente = (Node) event.getSource();
        Stage escenario = (Stage) fuente.getScene().getWindow();

        if (verificarAcceso() != 0) {
            AccesoDAO accesoDAO = new AccesoDAO();
            int IdAcceso = accesoDAO.obtenerIdProfesor(textField_Usuario.getText(),paswordField_Contrasenia.getText());
            AccesoSingleton.getInstance().setAccesoId(IdAcceso);
            try {
                accesoDAO.ejecutarActualizacionBaseDatos();
                String tipoUsuario=accesoDAO.obtenerTipoUsuario(textField_Usuario.getText(), paswordField_Contrasenia.getText());
                switch (tipoUsuario) {
                    case "Administrativo" -> abrirVentanaAdministrativoMenu();
                }
            } catch (SQLException sqlException) {
                Alertas.mostrarMensajeErrorBaseDatos();
                LOG.fatal("No hay conexión con la base de datos :" +this.getClass().getName() + ", método " + Thread.currentThread().getStackTrace()[1].getMethodName() + ": " + sqlException.getMessage(), sqlException);
            }
        }
    }
    


    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
}