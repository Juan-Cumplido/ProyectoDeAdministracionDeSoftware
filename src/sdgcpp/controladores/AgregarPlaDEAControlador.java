package sdgcpp.controladores;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import sdgcpp.logicaDeNegocio.clases.PlaDEA;
import sdgcpp.logicaDeNegocio.clases.Profesor;
import sdgcpp.logicaDeNegocio.implementacionDAO.PeriodoDAO;
import sdgcpp.logicaDeNegocio.implementacionDAO.PlaDEADAO;
import sdgcpp.logicaDeNegocio.implementacionDAO.ProfesorDAO;
import sdgcpp.utilidades.Alertas;

public class AgregarPlaDEAControlador implements Initializable{
    private static final Logger LOG = Logger.getLogger(AgregarPlaDEAControlador.class);
    private Stage escenario;
    @FXML private TextField textField_Correo;
    @FXML private Button button_Buscar;
    @FXML private Button button_Cancelar;  
     @FXML private Button button_Guardar;
    @FXML private TextArea textArea_EjeEstrategico;
    @FXML private TextField textField_ProgramaEstrategico;
    @FXML private TextField textField_Objetivo;
    @FXML private TextField textField_Acciones;
    @FXML private TextField textField_Meta;
    @FXML private ComboBox<String> comboBox_Periodo;
    private ObservableList<Profesor> listaProfesores;
    @FXML private TableView<Profesor> tableView_Profesores;
    @FXML private TableColumn<Profesor, String> column_Profesor;
    @FXML private TableColumn<Profesor, Void> column_Quitar;   
    private Runnable onCloseCallback;

    public void setOnCloseCallback(Runnable onCloseCallback) {
        this.onCloseCallback = onCloseCallback;
    }
    
    public void setStage(Stage escenario) {
        this.escenario = escenario;
    }
    
    private void aplicarValidacion(TextField textField, String expresionRegular) {
        UnaryOperator<TextFormatter.Change> filtro = cambio -> {
            String nuevoTexto = cambio.getControlNewText();
            return (nuevoTexto.matches(expresionRegular) || nuevoTexto.isEmpty()) ? cambio : null;
        };

        textField.setTextFormatter(new TextFormatter<>(filtro));
    }
    
    private void aplicarValidacionTextArea(TextArea textArea, String expresionRegular) {
        UnaryOperator<TextFormatter.Change> filtro = cambio -> {
            String nuevoTexto = cambio.getControlNewText();
            return (nuevoTexto.matches(expresionRegular) || nuevoTexto.isEmpty()) ? cambio : null;
        };

        textArea.setTextFormatter(new TextFormatter<>(filtro));
    }
     @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        aplicarValidacionTextArea(textArea_EjeEstrategico,  "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;:\\-_.0-9]{1,100}$");
        aplicarValidacion(textField_ProgramaEstrategico, "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',\\-]{1,45}$");
        aplicarValidacion(textField_Objetivo, "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;:\\-_.0-9]{1,200}$");
        aplicarValidacion(textField_Acciones, "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;:\\-_.0-9]{1,100}$");
        aplicarValidacion(textField_Meta, "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;:\\-_.0-9]{1,200}$");
        listaProfesores = FXCollections.observableArrayList();
        tableView_Profesores.setItems(listaProfesores);

        column_Profesor.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        asignarBotonQuitar();

        llenarComboBoxPerdiodo();
    }
    
    private void llenarComboBoxPerdiodo() {
        try {
            List<List<String>> listaDePeriodos = obtenerListaDePeriodo();
            ObservableList<String> items = FXCollections.observableArrayList(obtenerNombresYFechasDeLasListas(listaDePeriodos));
            comboBox_Periodo.setItems(items);
        } catch (SQLException ex) {
            LOG.error(ex);
        }
    }
    private List<String> obtenerNombresYFechasDeLasListas(List<List<String>> lista) {
        List<String> nombresYFechas = new ArrayList<>();
        lista.forEach(item -> {
            String nombre = item.get(1);
            String fechaInicio = item.get(2);
            String fechaFin = item.get(3);
            nombresYFechas.add(nombre + " - " + fechaInicio + " a " + fechaFin);
        });
        return nombresYFechas;
    }
    private List<List<String>> obtenerListaDePeriodo() throws SQLException {
        return new PeriodoDAO().obtenerListaDePeriodos();
    }
    
    private void asignarIdPeriodo(PlaDEA plaDEA) {
        int indicePeriodoSeleccionada = comboBox_Periodo.getSelectionModel().getSelectedIndex();
        if (indicePeriodoSeleccionada >= 0) {
            try {
                List<List<String>> listaDePeriodos = obtenerListaDePeriodo();
                if (indicePeriodoSeleccionada < listaDePeriodos.size()) {
                    int periodosEscolares = Integer.parseInt(listaDePeriodos.get(indicePeriodoSeleccionada).get(0));
                    plaDEA.setIdPeriodo(periodosEscolares);
                }
            } catch (SQLException ex) {
                LOG.error(ex);
            }
        }
    }
    
    @FXML
    void button_Regresar(ActionEvent event) {
        if (Alertas.mostrarMensajeCancelar()) {
            Stage escenario = (Stage) button_Cancelar.getScene().getWindow();
            escenario.close();
        }
    }
    
    private PlaDEA crearPlaDEA() {
        PlaDEA plaDEA = new PlaDEA();
        plaDEA.setEjeEstrategico(textArea_EjeEstrategico.getText());
        plaDEA.setProgramaEstrategico(textField_ProgramaEstrategico.getText());
        plaDEA.setObjetivos(textField_Objetivo.getText());
        plaDEA.setAcciones(textField_Acciones.getText());
        plaDEA.setMetas(textField_Meta.getText());

        asignarIdPeriodo(plaDEA);
        return plaDEA;
    }
    private List<Integer> obtenerIdsDeProfesores() {
    return listaProfesores.stream()
                          .map(Profesor::getIdProfesor)
                          .collect(Collectors.toList());
    }

    @FXML
    void guardarRegistro(ActionEvent event){
        if (verificarInformacion()) {
            PlaDEA plaDEA = crearPlaDEA();
            if (registrarPlaDEA( plaDEA) == true) {
                Alertas.mostrarMensajeExito();
                limpiarCampos();
            }
        }
    }
    
    private boolean registrarPlaDEA(PlaDEA plaDEA) {
        PlaDEADAO plaDEADAO = new PlaDEADAO();
        List<Integer> idsProfesores = obtenerIdsDeProfesores();
        boolean registroExitoso = false;

        try {
            if (plaDEADAO.registrarPlaDEA(plaDEA, idsProfesores)==1) {
                registroExitoso = true;
                
            } else {
                Alertas.mostrarMensajeInformacionNoRegistrada();
            }
        } catch (SQLException ex) {
            LOG.error("Error al registrar el PlaDEA: ", ex);
            Alertas.mostrarMensajeErrorBaseDatos();
        }

        return registroExitoso;
    }

    private boolean estaVacio() {
        int indicePeriodoSeleccionado = comboBox_Periodo.getSelectionModel().getSelectedIndex();
        
        return  textArea_EjeEstrategico.getText().isEmpty() ||
                textField_ProgramaEstrategico.getText().isEmpty() ||
                textField_Objetivo.getText().isEmpty() ||
                textField_Acciones.getText().isEmpty() ||
                textField_Meta.getText().isEmpty() ||
                indicePeriodoSeleccionado < 0;
    }
    
    private boolean verificarInformacion() {
        PlaDEA plaDEA = new PlaDEA();
        boolean validacion = true;

        if (!estaVacio()) {
            if (listaProfesores.isEmpty()) {
                    Alertas.mostrarMensaje(Alert.AlertType.WARNING, "AVISO", "Debe agregar al menos un profesor.");
                    return false;
            }else{
                try {
                    plaDEA.setEjeEstrategico(textArea_EjeEstrategico.getText());
                    plaDEA.setProgramaEstrategico(textField_ProgramaEstrategico.getText());
                    plaDEA.setObjetivos(textField_Objetivo.getText());
                    plaDEA.setAcciones(textField_Acciones.getText());
                    plaDEA.setMetas(textField_Meta.getText());
                } catch (IllegalArgumentException illegalArgument) {
                    Alertas.mostrarMensajeInformacionInvalida();
                    validacion = false;
                }
            }
            
        } else {
            Alertas.mostrarMensajeCamposVacios();
            validacion = false;
        }
        return validacion;

    } 
    
    @FXML
    void button_Buscar(ActionEvent event) {
        String criterioBusqueda = textField_Correo.getText();
        if (criterioBusqueda.isEmpty()) {
            Alertas.mostrarMensaje(Alert.AlertType.INFORMATION, "AVISO", "Por favor ingresa un criterio de búsqueda.");
            return;
        }
        buscarProfesor(criterioBusqueda);
    
    }
    
    private void buscarProfesor(String criterioBusqueda) {
        ProfesorDAO profesorDAO = new ProfesorDAO();
        try {
            Profesor profesorEncontrado = profesorDAO.obtenerProfesorPorCorreo(criterioBusqueda);

            agregarATabla(profesorEncontrado);
        } catch (SQLException ex) {
            LOG.error("Error al buscar instituciones:", ex);
            Alertas.mostrarMensajeErrorBaseDatos();
        }
    }
   
   
    
    private void agregarATabla(Profesor profesorEncontrado) {
     if (profesorEncontrado == null) {
         Alertas.mostrarMensajeSinResultados();
         return;
     }

     boolean yaExiste = listaProfesores.stream()
                                       .anyMatch(profesor -> profesor.getIdProfesor() == profesorEncontrado.getIdProfesor());
     if (yaExiste) {
         Alertas.mostrarMensaje(Alert.AlertType.INFORMATION, "AVISO", "El profesor ya está en la lista.");
         return;
     }

     listaProfesores.add(profesorEncontrado);
    }
    
    private void asignarBotonQuitar() {
        column_Quitar.setCellFactory(param -> new TableCell<>() {
            private final Button button_Quitar = new Button("X");

            {
                button_Quitar.setOnAction(event -> {
                    Profesor profesorSeleccionado = getTableView().getItems().get(getIndex());
                    listaProfesores.remove(profesorSeleccionado);
                });
            }

            @Override
            public void updateItem(Void item, boolean vacio) {
                super.updateItem(item, vacio);
                setGraphic(vacio ? null : button_Quitar);
            }
        });
    }
    
    private void limpiarCampos() {
        textArea_EjeEstrategico.setText("");
        textField_ProgramaEstrategico.setText("");
        textField_Objetivo.setText("");
        textField_Acciones.setText("");
        textField_Meta.setText("");
        textField_Correo.setText("");
        tableView_Profesores.getItems().clear();
        comboBox_Periodo.getItems().clear();


    }
    
     
}
