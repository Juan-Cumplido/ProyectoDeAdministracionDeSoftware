package sdgcpp.controladores;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class SDGCOILVIC extends Application {
    @Override
    public void start(Stage escenario) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sdgcoilvic/interfazDeUsuario/Acceso.fxml"));
        Scene escena = new Scene(root);
        escenario.setScene(escena);  
        escenario.show();    
    } 
    
     public void mostrarVentanaAcceso (Stage escenario) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sdgcoilvic/interfazDeUsuario/Acceso.fxml"));
        Scene escena = new Scene (root);
        escenario.setScene(escena);
        escenario.show();
    }
    
   
    
   
    
    public static void main(String[] args) {
           launch();
       }

}