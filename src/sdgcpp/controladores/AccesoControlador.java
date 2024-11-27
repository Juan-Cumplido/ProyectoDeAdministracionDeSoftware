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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import sdgcpp.logicaDeNegocio.implementacionDAO.AccesoDAO;
import sdgcpp.utilidades.Alertas;
import sdgcpp.utilidades.AccesoSingleton;

public class AccesoControlador implements Initializable {
private static final Logger LOG = Logger.getLogger(AccesoControlador.class);
    @FXML
    private TextField textField_Usuario;
    @FXML
    private Button button_Acceso;
    @FXML
    private PasswordField paswordField_Contrasenia;
    @FXML
    private Label lbl_FaltaAmbos;
    @FXML
    private Label lbl_FaltaUsuario;
    @FXML
    private Label lbl_FaltaContrasenia;

    private int estaVacio() {
        int resultadoVerificacion = 0;
        if(textField_Usuario.getText().isEmpty() & paswordField_Contrasenia.getText().isEmpty()){
            resultadoVerificacion = 1;
        }else if(textField_Usuario.getText().isEmpty()){
            resultadoVerificacion = 2;
        }else if (paswordField_Contrasenia.getText().isEmpty()) {
            resultadoVerificacion = 3;
        }
        return resultadoVerificacion;
    }

    private int verificarAcceso() {
        int existeAcceso = 0;
        if (estaVacio()==0) {
            AccesoDAO accesoDao = new AccesoDAO();

            try {
                existeAcceso = accesoDao.verificarExistenciaAcceso(textField_Usuario.getText(), paswordField_Contrasenia.getText());
                if (existeAcceso > 0) {
                    return existeAcceso;
                } else {
                    Alertas.mostrarMensajeInicioSesionFallido();
                }
            } catch (SQLException sqlException) {
                Alertas.mostrarMensajeErrorBaseDatos();
                LOG.fatal("No hay conexión con la base de datos :" +this.getClass().getName() + ", método " + Thread.currentThread().getStackTrace()[1].getMethodName() + ": " + sqlException.getMessage(), sqlException);
            }
        } else if (estaVacio()==1){
            lbl_FaltaAmbos.setVisible(true);
            lbl_FaltaUsuario.setVisible(false);
            lbl_FaltaContrasenia.setVisible(false);
        }
         else if (estaVacio()==2){
            lbl_FaltaUsuario.setVisible(true);
            lbl_FaltaAmbos.setVisible(false);
            lbl_FaltaContrasenia.setVisible(false);
        }
        
        else if (estaVacio()==3){
            lbl_FaltaContrasenia.setVisible(true);
            lbl_FaltaUsuario.setVisible(false);
            lbl_FaltaAmbos.setVisible(false);
        }
        
        return existeAcceso;
    }
    
    private void abrirVentanaAdministradorMenu() {
        Stage escenario = (Stage) button_Acceso.getScene().getWindow();
        SDGCPP sdgcpp = new SDGCPP();
        try {
            sdgcpp.mostrarVentanaAdministradorMenu(escenario);
        } catch (IOException | NullPointerException ex) {
            LOG.error("Error al abrir la ventana administrativa: " + ex.getMessage());
            Alertas.mostrarMensajeErrorCambioPantalla();
        }
    }

    private void abrirVentanaProfesorMenu() {
        Stage ventana = (Stage) button_Acceso.getScene().getWindow();
        SDGCPP sdgcpp = new SDGCPP();
        try {
            sdgcpp.mostrarVentanaProfesorMenu(ventana);
        }catch (IOException | NullPointerException ex) {
            LOG.error("Error al abrir la ventana de profesor: " + ex.getMessage());
            Alertas.mostrarMensajeErrorCambioPantalla();
        }
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
                String tipoUsuario=accesoDAO.obtenerTipoUsuario(textField_Usuario.getText(), paswordField_Contrasenia.getText());
                switch (tipoUsuario) {
                    case "Administrador" -> abrirVentanaAdministradorMenu();
                    case "Profesor" -> abrirVentanaProfesorMenu();
                }
            } catch (SQLException sqlException) {
                Alertas.mostrarMensajeErrorBaseDatos();
                LOG.fatal("No hay conexión con la base de datos :" +this.getClass().getName() + ", método " + Thread.currentThread().getStackTrace()[1].getMethodName() + ": " + sqlException.getMessage(), sqlException);
            }
        }     
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lbl_FaltaAmbos.setVisible(false);
        lbl_FaltaUsuario.setVisible(false);
        lbl_FaltaContrasenia.setVisible(false);
    }
}