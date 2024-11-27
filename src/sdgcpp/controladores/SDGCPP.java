package sdgcpp.controladores;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class SDGCPP extends Application {
    @Override
    public void start(Stage escenario) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sdgcpp/interfazDeUsuario/Acceso.fxml"));
        Scene escena = new Scene(root);
        escenario.setScene(escena);  
        escenario.show();    
    } 
    
     public void mostrarVentanaAcceso (Stage escenario) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sdgcpp/interfazDeUsuario/Acceso.fxml"));
        Scene escena = new Scene (root);
        escenario.setScene(escena);
        escenario.show();
    }
     
    public void mostrarVentanaAdministradorMenu(Stage escenario) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sdgcpp/interfazDeUsuario/AdministradorMenu.fxml"));
        Scene escena = new Scene (root);
        escenario.setScene(escena);
        escenario.show();
    }
    
    public void mostrarVentanaProfesorMenu (Stage escenario) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sdgcpp/interfazDeUsuario/ProfesorMenu.fxml"));
        Scene escena = new Scene (root);
        escenario.setScene(escena);
        escenario.show();
    }
    
    public void mostrarVentanaAgregarPlaDEA(Stage escenario, Runnable onCloseCallback) throws IOException {
        Stage escenarioAgregar = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sdgcpp/interfazDeUsuario/AgregarPlaDEA.fxml"));
        Parent root = loader.load();
        Scene escena = new Scene(root);
        escenarioAgregar.setScene(escena);

        escenarioAgregar.initModality(Modality.APPLICATION_MODAL);
        AgregarPlaDEAControlador controller = loader.getController();
        controller.setOnCloseCallback(onCloseCallback);
        controller.setStage(escenario);

        escenarioAgregar.show();
    }
    
    public void mostrarVentanaAgregarEE(Stage escenario, Runnable onCloseCallback) throws IOException {
        Stage escenarioAgregar = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sdgcpp/interfazDeUsuario/AgregarImparticionEE.fxml"));
        Parent root = loader.load();
        Scene escena = new Scene(root);
        escenarioAgregar.setScene(escena);

        escenarioAgregar.initModality(Modality.APPLICATION_MODAL);
        AgregarEEControlador controller = loader.getController();
        controller.setOnCloseCallback(onCloseCallback);
        controller.setStage(escenario);

        escenarioAgregar.show();
    }
    
    public void mostrarVentanaAgregarProyecto(Stage escenario, Runnable onCloseCallback) throws IOException {
        Stage escenarioAgregar = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sdgcpp/interfazDeUsuario/AgregarProyecto.fxml"));
        Parent root = loader.load();
        Scene escena = new Scene(root);
        escenarioAgregar.setScene(escena);

        escenarioAgregar.initModality(Modality.APPLICATION_MODAL);
        AgregarProyectoControlador controller = loader.getController();
        controller.setOnCloseCallback(onCloseCallback);
        controller.setStage(escenario);

        escenarioAgregar.show();
    }
    
    public void mostrarVentanaAgregarTR(Stage escenario, Runnable onCloseCallback) throws IOException {
        Stage escenarioAgregar = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sdgcpp/interfazDeUsuario/AgregarTrabajoRecepcional.fxml"));
        Parent root = loader.load();
        Scene escena = new Scene(root);
        escenarioAgregar.setScene(escena);

        escenarioAgregar.initModality(Modality.APPLICATION_MODAL);
        AgregarTRControlador controller = loader.getController();
        controller.setOnCloseCallback(onCloseCallback);
        controller.setStage(escenario);

        escenarioAgregar.show();
    }
    
    public void mostrarVentanaAgregarProfesor(Stage escenario, Runnable onCloseCallback) throws IOException {
        Stage escenarioAgregar = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sdgcpp/interfazDeUsuario/AgregarProfesorUV.fxml"));
        Parent root = loader.load();
        Scene escena = new Scene(root);
        escenarioAgregar.setScene(escena);

        escenarioAgregar.initModality(Modality.APPLICATION_MODAL);
        AgregarProfesorControlador controller = loader.getController();
        controller.setOnCloseCallback(onCloseCallback);
        controller.setStage(escenario);

        escenarioAgregar.show();
    }
    
    public static void main(String[] args) {
           launch();
       }

}