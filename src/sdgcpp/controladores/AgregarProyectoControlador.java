package sdgcpp.controladores;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import sdgcpp.logicaDeNegocio.clases.Estudiante;
import sdgcpp.logicaDeNegocio.clases.Profesor;
import sdgcpp.logicaDeNegocio.clases.ProyectoCampo;
import sdgcpp.logicaDeNegocio.implementacionDAO.EstudianteDAO;
import sdgcpp.logicaDeNegocio.implementacionDAO.PeriodoDAO;
import sdgcpp.logicaDeNegocio.implementacionDAO.ProfesorDAO;
import sdgcpp.logicaDeNegocio.implementacionDAO.ProyectoCampoDAO;
import sdgcpp.utilidades.Alertas;


public class AgregarProyectoControlador implements Initializable {
    private static final Logger LOG = Logger.getLogger(AgregarProyectoControlador.class);
    private Stage escenario;
    private ObservableList<Profesor> listaProfesores;
    private ObservableList<Estudiante> listaEstudiantes;
    @FXML private TextField textField_Correo;
    @FXML private TextField textField_Proyecto;
    @FXML private DatePicker datePicker_FechaInicio;
    @FXML private DatePicker datePicker_FechaFin;
    @FXML private TextField textField_Lugar;
    @FXML private TextField textField_MatriculaEstudiante;
    @FXML private TextArea textArea_ImpactoObtenido;
    @FXML private ComboBox<String> comboBox_Periodo;
    
    @FXML private TableView<Profesor> tableView_Profesores;
    @FXML private TableColumn<Profesor, String> column_Profesor;
    @FXML private TableColumn<Profesor, Void > column_Quitar;
    @FXML private TableView<Estudiante> tableView_Estudiantes;
    @FXML private TableColumn<Estudiante, String> column_Estudiante;
    @FXML private TableColumn<Estudiante, Void> column_QuitarEstudiante;

    @FXML private Button button_AgregarProfesor;
    @FXML private Button button_AgregarEstudiante;
    @FXML private Button button_Cancelar;  
    @FXML private Button button_Guardar;

    
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
        aplicarValidacion(textField_Proyecto,  "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;:\\-_.0-9]{1,200}$");
        aplicarValidacion(textField_Lugar, "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',\\-]{1,200}$");
        aplicarValidacionTextArea(textArea_ImpactoObtenido, "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;:\\-_.0-9]{1,450}$");
        
        column_Profesor.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        column_Estudiante.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        listaProfesores = FXCollections.observableArrayList();
        tableView_Profesores.setItems(listaProfesores);
        listaEstudiantes = FXCollections.observableArrayList();
        tableView_Estudiantes.setItems(listaEstudiantes);

        asignarBotonQuitarProfesor();
        asignarBotonQuitarEstudiante();
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
    
    private void asignarIdPeriodo(ProyectoCampo proyectoCampo) {
        int indicePeriodoSeleccionada = comboBox_Periodo.getSelectionModel().getSelectedIndex();
        if (indicePeriodoSeleccionada >= 0) {
            try {
                List<List<String>> listaDePeriodos = obtenerListaDePeriodo();
                if (indicePeriodoSeleccionada < listaDePeriodos.size()) {
                    int periodosEscolares = Integer.parseInt(listaDePeriodos.get(indicePeriodoSeleccionada).get(0));
                    proyectoCampo.setIdPeriodo(periodosEscolares);
                }
            } catch (SQLException ex) {
                LOG.error(ex);
            }
        }
    }
    
    private ProyectoCampo crearProyectoCampo() {
        ProyectoCampo proyectoCampo = null;
        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaInicioActividad = datePicker_FechaInicio.getValue();
        LocalDate fechaCierreActividad = datePicker_FechaFin.getValue();

        if (fechaInicioActividad == null || fechaCierreActividad == null) {
            Alertas.mostrarMensaje(Alert.AlertType.INFORMATION, "AVISO", "Por favor selecciona ambas fechas.");
        } else if (!fechaInicioActividad.isBefore(fechaCierreActividad)) {
            Alertas.mostrarMensaje(Alert.AlertType.INFORMATION, "AVISO", "La fecha de inicio debe ser anterior a la fecha de cierre.");
           
        } else if (!fechaCierreActividad.isAfter(fechaActual)) {
            Alertas.mostrarMensaje(Alert.AlertType.INFORMATION, "AVISO", "La fecha de cierre debe ser posterior a la fecha actual.");
        } else {
            proyectoCampo = new ProyectoCampo();
            proyectoCampo.setNombre(textField_Proyecto.getText());
            proyectoCampo.setFechaInicio(fechaInicioActividad.toString());
            proyectoCampo.setFechaFin(fechaCierreActividad.toString());
            proyectoCampo.setLugar(textField_Lugar.getText());
            proyectoCampo.setImpactoObtenido(textArea_ImpactoObtenido.getText());
            asignarIdPeriodo(proyectoCampo);
        }

        return proyectoCampo;
    }

     
    private List<Integer> obtenerIdsDeProfesores() {
    return listaProfesores.stream()
                          .map(Profesor::getIdProfesor)
                          .collect(Collectors.toList());
    }
    
    private List<Integer> obtenerIdsDeEstudiantes() {
    return listaEstudiantes.stream()
                          .map(Estudiante::getIdEstudiante)
                          .collect(Collectors.toList());
    }
    
        @FXML
    void onGuardar(ActionEvent event) {
        if (verificarInformacion()) {
            ProyectoCampo proyectoCampo = crearProyectoCampo();
            if (proyectoCampo != null) {
                if (registrarProyectoCampo( proyectoCampo) == true) {
                    Alertas.mostrarMensajeExito();
                    limpiarFormulario();
                }
            }
        }
    }
    
    private boolean registrarProyectoCampo(ProyectoCampo proyectoCampo) {
        ProyectoCampoDAO proyectoCampoDAO = new ProyectoCampoDAO();
        List<Integer> idsProfesores = obtenerIdsDeProfesores();
        List<Integer> idsEstudiantes = obtenerIdsDeEstudiantes();
        boolean registroExitoso = false;

        try {
            if (proyectoCampoDAO.registrarProyectoCampo(proyectoCampo, idsProfesores, idsEstudiantes)==1) {
                registroExitoso = true;
                
            } else {
                Alertas.mostrarMensajeInformacionNoRegistrada();
            }
        } catch (SQLException ex) {
            LOG.error("Error al registrar el Proyecto: ", ex);
            Alertas.mostrarMensajeErrorBaseDatos();
        }

        return registroExitoso;
    }
        
    private boolean estaVacio() {
        int indicePeriodoSeleccionado = comboBox_Periodo.getSelectionModel().getSelectedIndex();
        
        return  textField_Proyecto.getText().isEmpty() ||
                textField_Lugar.getText().isEmpty() ||
                textArea_ImpactoObtenido.getText().isEmpty() ||
                datePicker_FechaInicio.getValue() == null ||
                datePicker_FechaFin.getValue() == null ||
                indicePeriodoSeleccionado < 0;
    }
    
    private boolean verificarInformacion() {
        ProyectoCampo proyectoCampo = new ProyectoCampo();
        boolean validacion = true;

        if (!estaVacio()) {
            if (listaProfesores.isEmpty() || listaEstudiantes.isEmpty()) {
                    Alertas.mostrarMensaje(Alert.AlertType.WARNING, "AVISO", "Debe agregar al menos un profesor o un estudiante.");
                    return false;
            }else{
                try {
                    proyectoCampo.setNombre(textField_Proyecto.getText());
                    proyectoCampo.setLugar(textField_Lugar.getText());
                    proyectoCampo.setImpactoObtenido(textArea_ImpactoObtenido.getText());

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
    void onAgregarProfesor(ActionEvent event) {
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

            agregarATablaProfesor(profesorEncontrado);
        } catch (SQLException ex) {
            LOG.error("Error al buscar instituciones:", ex);
            Alertas.mostrarMensajeErrorBaseDatos();
        }
    }
    
    private void agregarATablaProfesor(Profesor profesorEncontrado) {
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

    @FXML
    void onAgregarEstudiante(ActionEvent event) {
        String criterioBusqueda = textField_MatriculaEstudiante.getText();
        if (criterioBusqueda.isEmpty()) {
            Alertas.mostrarMensaje(Alert.AlertType.INFORMATION, "AVISO", "Por favor ingresa un criterio de búsqueda.");
            return;
        }
        buscarEstudiante(criterioBusqueda);
    }
    
    private void buscarEstudiante(String criterioBusqueda) {
        EstudianteDAO estudianteDAO = new EstudianteDAO();
        try {
            Estudiante estudianteEncontrado = estudianteDAO.obtenerEstudiantePorMatricula(criterioBusqueda);

            agregarATablaEstudiante(estudianteEncontrado);
        } catch (SQLException ex) {
            LOG.error("Error al buscar estudiantes:", ex);
            Alertas.mostrarMensajeErrorBaseDatos();
        }
    }
    
        private void agregarATablaEstudiante(Estudiante estudianteEncontrado) {
            if (estudianteEncontrado == null) {
                Alertas.mostrarMensajeSinResultados();
                return;
            }

            boolean yaExiste = listaEstudiantes.stream()
                                              .anyMatch(profesor -> profesor.getIdEstudiante() == estudianteEncontrado.getIdEstudiante());
            if (yaExiste) {
                Alertas.mostrarMensaje(Alert.AlertType.INFORMATION, "AVISO", "El estudiante ya está en la lista.");
                return;
            }

            listaEstudiantes.add(estudianteEncontrado);
           }
    
    private void asignarBotonQuitarProfesor() {
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
    
    private void asignarBotonQuitarEstudiante() {
        column_QuitarEstudiante.setCellFactory(param -> new TableCell<>() {
            private final Button button_QuitarEstudiante = new Button("X");

            {
                button_QuitarEstudiante.setOnAction(event -> {
                    Estudiante estudianteSeleccionado = getTableView().getItems().get(getIndex());
                    listaEstudiantes.remove(estudianteSeleccionado);
                });
            }

            @Override
            public void updateItem(Void item, boolean vacio) {
                super.updateItem(item, vacio);
                setGraphic(vacio ? null : button_QuitarEstudiante);
            }
        });
    }
    


    @FXML
    void onCancelar(ActionEvent event) {
        if (Alertas.mostrarMensajeCancelar()) {
            Stage escenario = (Stage) button_Cancelar.getScene().getWindow();
            escenario.close();
        }
    }

    private void limpiarFormulario() {
        textField_Correo.clear();
        textField_Proyecto.clear();
        datePicker_FechaInicio.setValue(null);
        datePicker_FechaFin.setValue(null);
        textField_Lugar.clear();
        textField_MatriculaEstudiante.clear();
        textArea_ImpactoObtenido.clear();
        comboBox_Periodo.setValue(null);
        tableView_Profesores.getItems().clear();
        tableView_Estudiantes.getItems().clear();
    }
}
