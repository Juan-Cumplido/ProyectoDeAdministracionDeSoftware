package sdgcpp.controladores;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import sdgcpp.utilidades.AccesoSingleton;
import sdgcpp.utilidades.Alertas;


public class AdministradorMenuControlador implements Initializable {
    private static final Logger LOG = Logger.getLogger(AdministradorMenuControlador.class);
    @FXML
    private ImageView imageView_Salir;
    @FXML
    private ImageView imageView_PlaDEA;
    @FXML
    private ImageView imageView_Proyecto;
    @FXML
    private ImageView imageView_EE;
    @FXML
    private ImageView imageView_Recepcional;
    @FXML
    private ImageView imageView_Profesor;
    
    
    @FXML
    private void cerrarSesion(MouseEvent event) {
        if (Alertas.mostrarConfirmacion("Cerrar Sesión", "¿Seguro que desea cerrar sesión?")) {
            AccesoSingleton.getInstance().borrarInstancia();
            Stage escenario = (Stage) imageView_Salir.getScene().getWindow();
            SDGCPP sdgcpp = new SDGCPP();

            try {
                sdgcpp.mostrarVentanaAcceso(escenario);
            } catch (IOException ex) {
                LOG.error( ex);
            }
        }
    }
    
    private void actualizarDatos() {
       
    }
    
    @FXML
    private void abrirVentanaPlaDEA(MouseEvent event) {
        Stage escenario = (Stage) imageView_PlaDEA.getScene().getWindow();
        SDGCPP sdgcpp = new SDGCPP();

        try {
            sdgcpp.mostrarVentanaAgregarPlaDEA(escenario, this::actualizarDatos);
        } catch (IOException ex) {
            LOG.error( ex);
        }
    }
    
    @FXML
    private void abrirVentanaProyecto(MouseEvent event) {
        Stage escenario = (Stage) imageView_Proyecto.getScene().getWindow();
        SDGCPP sdgcpp = new SDGCPP();

        try {
            sdgcpp.mostrarVentanaAgregarProyecto(escenario, this::actualizarDatos);
        } catch (IOException ex) {
            LOG.error( ex);
        }
    }

    
    @FXML
    private void abrirVentanaEE(MouseEvent event) {
        Stage escenario = (Stage) imageView_EE.getScene().getWindow();
        SDGCPP sdgcpp = new SDGCPP();

        try {
            sdgcpp.mostrarVentanaAgregarEE(escenario, this::actualizarDatos);
        } catch (IOException ex) {
            LOG.error( ex);
        }
    }

    
    @FXML
    private void abrirVentanaTR(MouseEvent event) {
        Stage escenario = (Stage) imageView_Recepcional.getScene().getWindow();
        SDGCPP sdgcpp = new SDGCPP();

        try {
            sdgcpp.mostrarVentanaAgregarTR(escenario, this::actualizarDatos);
        } catch (IOException ex) {
            LOG.error( ex);
        }
    }
    
     @FXML
    private void abrirVentanaProfesor(MouseEvent event) {
        Stage escenario = (Stage) imageView_Profesor.getScene().getWindow();
        SDGCPP sdgcpp = new SDGCPP();

        try {
            sdgcpp.mostrarVentanaAgregarProfesor(escenario, this::actualizarDatos);
        } catch (IOException ex) {
            LOG.error( ex);
        }
    }


    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
}
