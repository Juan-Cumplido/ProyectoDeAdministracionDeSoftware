
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import sdgcpp.logicaDeNegocio.clases.EE;
import sdgcpp.logicaDeNegocio.clases.Profesor;
import sdgcpp.logicaDeNegocio.implementacionDAO.EEDAO;
import sdgcpp.logicaDeNegocio.implementacionDAO.PeriodoDAO;
import sdgcpp.logicaDeNegocio.implementacionDAO.ProfesorDAO;
import sdgcpp.utilidades.Alertas;

public class AgregarEEControlador implements Initializable{
    private static final Logger LOG = Logger.getLogger(AgregarEEControlador.class);
    private Stage escenario;
    @FXML private TextField textField_Correo;
    @FXML private Button button_Buscar;
    @FXML private Button button_Cancelar;  
    @FXML private Button button_Guardar;
     
     
    @FXML private TextField textField_ProgramaEducativo;
    @FXML private TextField textField_EE;
    @FXML private TextField textField_Bloque;
    
    @FXML private TextField textField_Sesion;
    @FXML private TextField textField_Creditos;
    
    @FXML private TextField textField_Horas;
    @FXML private TextField textField_Semanas;
    @FXML private TextField textField_Meses;
    
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        aplicarValidacion(textField_ProgramaEducativo, "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',\\-]{1,50}$");
        aplicarValidacion(textField_EE, "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;:\\-_.0-9]{1,50}$");
        aplicarValidacion(textField_Bloque, "^(?:100|[1-9]?[0-9])$");
        aplicarValidacion(textField_Sesion, "^(?:10|[0-9])$");
        aplicarValidacion(textField_Creditos, "^(?:100|[1-9]?[0-9])$");
        aplicarValidacion(textField_Horas, "^(?:100|[1-9]?[0-9])$");
        aplicarValidacion(textField_Semanas, "^(?:100|[1-9]?[0-9])$");
        aplicarValidacion(textField_Meses, "^(?:10|[0-9])$");              
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
    
    private void asignarIdPeriodo(EE ee) {
        int indicePeriodoSeleccionada = comboBox_Periodo.getSelectionModel().getSelectedIndex();
        if (indicePeriodoSeleccionada >= 0) {
            try {
                List<List<String>> listaDePeriodos = obtenerListaDePeriodo();
                if (indicePeriodoSeleccionada < listaDePeriodos.size()) {
                    int periodosEscolares = Integer.parseInt(listaDePeriodos.get(indicePeriodoSeleccionada).get(0));
                    ee.setIdPeriodo(periodosEscolares);
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
    
    private EE crearEE() {
        EE ee = new EE();
        ee.setProgramaEducativo(textField_ProgramaEducativo.getText());
        ee.setExperienciaEducativa(textField_EE.getText());
        ee.setBloque(textField_Bloque.getText());
        ee.setSesion(textField_Sesion.getText());
        ee.setSemanas(textField_Semanas.getText());
        ee.setMes(textField_Meses.getText());
        try {
            int creditosNum = Integer.parseInt(textField_Creditos.getText());
            ee.setCreditos(creditosNum);
        } catch (NumberFormatException nfe) {
           LOG.warn(nfe);
        }
        try {
            int horasNum = Integer.parseInt(textField_Horas.getText());
            ee.setHoras(horasNum);
        } catch (NumberFormatException nfe) {
           LOG.warn(nfe);
        }
        if (!listaProfesores.isEmpty()) {
                ee.setIdProfesor(listaProfesores.get(0).getIdProfesor()); 
            }

        asignarIdPeriodo(ee);
        return ee;
    }
    
        @FXML
    void guardarRegistro(ActionEvent event){
        if (verificarInformacion()) {
            EE ee = crearEE();
            if (registrarEE( ee) == true) {
                Alertas.mostrarMensajeExito();
                limpiarCampos();
            }
        }
    }
    
    private boolean registrarEE(EE ee) {
        EEDAO eeDAO = new EEDAO();
        boolean registroExitoso = false;

        try {
            if (eeDAO.registrarEE(ee)==1) {
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
        
        return  textField_ProgramaEducativo.getText().isEmpty() ||
                textField_EE.getText().isEmpty() ||
                textField_Bloque.getText().isEmpty() ||
                textField_Sesion.getText().isEmpty() ||
                textField_Creditos.getText().isEmpty() ||
                textField_Horas.getText().isEmpty() ||
                textField_Semanas.getText().isEmpty() ||
                textField_Meses.getText().isEmpty() ||
                indicePeriodoSeleccionado < 0;
    }
    
    private boolean verificarInformacion() {
        EE ee = new EE();
        boolean validacion = true;

        if (!estaVacio()) {
            if (listaProfesores.isEmpty()) {
                    Alertas.mostrarMensaje(Alert.AlertType.WARNING, "AVISO", "Debe agregar un profesor.");
                    return false;
            }else{
                try {
                    ee.setProgramaEducativo(textField_ProgramaEducativo.getText());
                    ee.setExperienciaEducativa(textField_EE.getText());
                    ee.setBloque(textField_Bloque.getText());
                    ee.setSesion(textField_Sesion.getText());
                    ee.setSemanas(textField_Semanas.getText());
                    ee.setMes(textField_Meses.getText());
                    int creditosNum = Integer.parseInt(textField_Creditos.getText());
                        ee.setCreditos(creditosNum);
                    int horasNum = Integer.parseInt(textField_Horas.getText());
                        ee.setHoras(horasNum);
                        ee.setIdProfesor(listaProfesores.get(0).getIdProfesor()); 
                        
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
        if (listaProfesores.size() >= 1) {
            Alertas.mostrarMensaje(Alert.AlertType.INFORMATION, "AVISO", "Ya hay un profesor en la lista. Solo se puede agregar uno.");
            return;
        }
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
        textField_ProgramaEducativo.setText("");
        textField_EE.setText("");
        textField_Bloque.setText("");
        textField_Sesion.setText("");
        textField_Creditos.setText("");
        textField_Horas.setText("");
        textField_Semanas.setText("");
        textField_Meses.setText("");
        textField_Correo.setText("");
        tableView_Profesores.getItems().clear();
        comboBox_Periodo.getItems().clear();


    }
    
}
