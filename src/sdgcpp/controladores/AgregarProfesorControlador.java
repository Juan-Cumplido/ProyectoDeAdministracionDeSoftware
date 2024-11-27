package sdgcpp.controladores;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import sdgcpp.logicaDeNegocio.clases.Acceso;
import sdgcpp.logicaDeNegocio.clases.Profesor;
import sdgcpp.logicaDeNegocio.enums.EnumTipoDeAcceso;
import sdgcpp.logicaDeNegocio.implementacionDAO.ProfesorDAO;
import sdgcpp.utilidades.Alertas;
import sdgcpp.utilidades.EnviosDeCorreoElectronico;
import sdgcpp.utilidades.GeneradorDeContrasenias;
public class AgregarProfesorControlador implements Initializable{
  private static final Logger LOG = Logger.getLogger(AgregarProfesorControlador.class);
    private Stage escenario;
    private Runnable onCloseCallback;
    @FXML private Button button_Cancelar;
    @FXML private Button button_Guardar;
    @FXML private TextField textField_Nombre;
    @FXML private TextField textField_ApellidoPaterno;
    @FXML private TextField textField_ApellidoMaterno;
    @FXML private TextField textField_Correo;
    @FXML private ComboBox<String> comboBox_CategoriaContratacion;
    @FXML private ComboBox<String> comboBox_TipoContratacion;
    
    
    public void setOnCloseCallback(Runnable onCloseCallback) {
        this.onCloseCallback = onCloseCallback;
    }
    private void aplicarValidacion(TextField textField, String expresionRegular) {
        UnaryOperator<TextFormatter.Change> filtro = cambio -> {
            String nuevoTexto = cambio.getControlNewText();
            return (nuevoTexto.matches(expresionRegular) || nuevoTexto.isEmpty()) ? cambio : null;
        };

        textField.setTextFormatter(new TextFormatter<>(filtro));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        aplicarValidacion(textField_Nombre, "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;\\-_:\\.]{1,60}$");
        aplicarValidacion(textField_ApellidoPaterno, "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;\\-_:\\.]{1,60}$");
        aplicarValidacion(textField_ApellidoMaterno, "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;\\-_:\\.]{1,60}$");
        llenarComboCategoriaContratacion(); 
        llenarComboTipoContratacion(); 
    }
    
    public void setStage(Stage escenario) {
        this.escenario = escenario;
    }
    
    private void llenarComboCategoriaContratacion() {
        try {
            List<List<String>> listaDeCategoria = obtenerListaDeCategoriaContratacion();
            ObservableList<String> articulos = FXCollections.observableArrayList(obtenerListaNombres(listaDeCategoria));
            comboBox_CategoriaContratacion.setItems(articulos);
        } catch (SQLException ex) {
            LOG.error(ex);
        }
    }
        
    private void llenarComboTipoContratacion() {
        try {
            List<List<String>> listaTipoContratacion = obtenerListaDeTipoContratacion();
            ObservableList<String> articulos = FXCollections.observableArrayList(obtenerListaNombres(listaTipoContratacion));
            comboBox_TipoContratacion.setItems(articulos);
        } catch (SQLException ex) {
            LOG.error(ex);
        }
    }
    
    private List<List<String>> obtenerListaDeCategoriaContratacion() throws SQLException {
        return new ProfesorDAO().obtenerListaDeCategoriaContratacion();
    }

    private List<List<String>> obtenerListaDeTipoContratacion() throws SQLException {
        return new ProfesorDAO().obtenerListaDeTipoContratacion();
    }
    
    private List<String> obtenerListaNombres(List<List<String>> lista) {
        List<String> nombres = new ArrayList<>();
        lista.forEach(item -> nombres.add(item.get(1)));
        return nombres;
    }
    

    @FXML
    void button_Regresar(ActionEvent event) {
        if (Alertas.mostrarMensajeCancelar()) {
            Stage escenario = (Stage) button_Cancelar.getScene().getWindow();
            escenario.close();
        }
    }
    
     private void asignarIdCategoriaContratacion(Profesor profesor) {
        int indiceCategoriaContratacionSeleccionado = comboBox_CategoriaContratacion.getSelectionModel().getSelectedIndex();
        if (indiceCategoriaContratacionSeleccionado >= 0) {
            try {
                List<List<String>> listaDeCategoriaContratacion = obtenerListaDeCategoriaContratacion();
                if (indiceCategoriaContratacionSeleccionado < listaDeCategoriaContratacion.size()) {
                    int IdCategoriaContratacion = Integer.parseInt(listaDeCategoriaContratacion.get(indiceCategoriaContratacionSeleccionado).get(0));
                    profesor.setIdCategoriaContratacionUV(IdCategoriaContratacion);
                }
            } catch (SQLException | NumberFormatException ex) {
                LOG.error(ex);
            }
        }
    }
    
    private void asignarIdTipoContratacion(Profesor profesor) {
        int indiceTipoContratacionSeleccionado = comboBox_TipoContratacion.getSelectionModel().getSelectedIndex();
        if (indiceTipoContratacionSeleccionado >= 0) {
            try {
                List<List<String>> listaDeTipoContratacion= obtenerListaDeTipoContratacion();
                if (indiceTipoContratacionSeleccionado < listaDeTipoContratacion.size()) {
                    int IdTipoContratacion = Integer.parseInt(listaDeTipoContratacion.get(indiceTipoContratacionSeleccionado).get(0));
                    profesor.setIdTipoContratacionUV(IdTipoContratacion);
                }
            } catch (SQLException | NumberFormatException ex) {
                LOG.error(ex);
            }
        }
    }
    
     private Profesor crearProfesorUV() {
        Profesor profesorUV = new Profesor();
        profesorUV.setNombre(textField_Nombre.getText());
        profesorUV.setApellidoPaterno(textField_ApellidoPaterno.getText());
        profesorUV.setApellidoMaterno(textField_ApellidoMaterno.getText());
        profesorUV.setCorreo(textField_Correo.getText());
        
        asignarIdCategoriaContratacion(profesorUV);
        asignarIdTipoContratacion(profesorUV);
        return profesorUV;
    }


    private Acceso crearAcceso() {
        Acceso acceso = new Acceso();
        acceso.setUsuario(textField_Correo.getText());
        acceso.setContrasenia(GeneradorDeContrasenias.generarContraseña());
        acceso.setTipoUsuario(EnumTipoDeAcceso.Profesor.toString());
        return acceso;
    }
    
    @FXML
    void guardarRegistro(ActionEvent event){
        if (verificarInformacion()) {
            Profesor profesorUV = crearProfesorUV();
            Acceso acceso = crearAcceso();
            if (registrarProfesorUV( acceso, profesorUV) == true) {
                Alertas.mostrarMensajeExito();
                limpiarCampos();
            }
        }
    }
    
    private boolean registrarProfesorUV(Acceso acceso, Profesor profesorUV) {
        ProfesorDAO profesorDAO = new ProfesorDAO();
        boolean registroExitoso = false;              
            try {
                if (!profesorDAO.verificarSiExisteElCorreo(profesorUV.getCorreo())) {
                    if (!profesorDAO.verificarExistenciaProfesor(profesorUV.getNombre(), profesorUV.getApellidoPaterno(), profesorUV.getApellidoMaterno())) {
                            if (profesorDAO.registrarProfesorUV( acceso, profesorUV) == 1) {
                                registroExitoso = true;
                                if (enviarCorreo(profesorUV, acceso) == false) {
                                    Stage escenario = (Stage) button_Cancelar.getScene().getWindow();
                                    escenario.close();
                                    if (onCloseCallback != null) {
                                        onCloseCallback.run();
                                    }
                                     Alertas.mostrarMensajeElCorreoNoSePudoEnviar();
                                      registroExitoso = false;
                                }
                            } else {
                                Alertas.mostrarMensajeInformacionNoRegistrada();
                            }
                        
                    } else {
                        Alertas.mostrarMensajeProfesorYaExistente();
                    }
                } else {
                    Alertas.mostrarMensajeEmailYaRegistrado();
                }
            } catch (SQLException sqlException) {
                Alertas.mostrarMensajeErrorBaseDatos();
                LOG.fatal("Error en la base de datos en la clase " + this.getClass().getName() + ", método " + Thread.currentThread().getStackTrace()[1].getMethodName() + ": " + sqlException.getMessage(), sqlException);
            }
        return registroExitoso;
    }
    
        private boolean enviarCorreo(Profesor profesorUV, Acceso acceso) {
        String mensaje = "Estimado profesor " + profesorUV.getNombre() + ",\n\n" +
                "Lo hemos registrado exitosamente como profesor al sistema de generación de constancia. A continuación se muestran tus credenciales de acceso:\n\n" +
                "Usuario: " + acceso.getUsuario() + "\n" +
                "Contraseña: " + acceso.getContrasenia() + "\n\n" +
                "¡Gracias por su solicitud!\n" +
                "SDGDCPP";
        
        return EnviosDeCorreoElectronico.verificarEnvioCorreo(profesorUV.getCorreo(), "Credenciales de acceso", mensaje);
    
    }
    
    
    private boolean estaVacioTextField() {
        return textField_Nombre.getText().isEmpty() ||
               textField_ApellidoPaterno.getText().isEmpty() ||
               textField_Correo.getText().isEmpty();
    }

    private boolean estaVacioComboBox() {

        int indiceCategoriaContratacionSeleccionado = comboBox_CategoriaContratacion.getSelectionModel().getSelectedIndex();
        int indiceTipoContratacionSeleccionado = comboBox_TipoContratacion.getSelectionModel().getSelectedIndex();        
        
        return indiceCategoriaContratacionSeleccionado < 0 ||
               indiceTipoContratacionSeleccionado < 0;
    }

    private boolean verificarInformacion() {
        Profesor profesorUV = new Profesor();
        boolean validacion = true;

        boolean textFieldVacios = estaVacioTextField();
        boolean comboBoxVacios = estaVacioComboBox();

        if (textFieldVacios && comboBoxVacios) {
            Alertas.mostrarMensajeCamposVacios();
            validacion = false;
        } else if (textFieldVacios) {
            Alertas.mostrarMensajeCamposVacios();
            validacion = false;
        } else if (comboBoxVacios) {
            Alertas.mostrarMensajeComboBoxSinSeleccionar("Categoría de Contratación, Tipo de Contratación");
            validacion = false;
        } else {
            try {
                profesorUV.setNombre(textField_Nombre.getText());
                profesorUV.setApellidoPaterno(textField_ApellidoPaterno.getText());
                profesorUV.setApellidoMaterno(textField_ApellidoMaterno.getText());
            } catch (IllegalArgumentException ilegaLArgument) {
                Alertas.mostrarMensajeInformacionInvalida();
                validacion = false;
            }

            try {
                profesorUV.setCorreo(textField_Correo.getText());
            } catch (IllegalArgumentException correoException) {
                Alertas.mostrarMensajeCorreoConFormatoInvalido();
                validacion = false;
            }
        }

        return validacion;
    }
    
    private void limpiarCampos() {

        textField_Nombre.setText("");
        textField_ApellidoPaterno.setText("");
        textField_ApellidoMaterno.setText("");
        textField_Correo.setText("");
    }
}
