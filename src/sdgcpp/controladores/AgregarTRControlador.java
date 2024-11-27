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
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import sdgcpp.logicaDeNegocio.clases.Estudiante;
import sdgcpp.logicaDeNegocio.clases.Profesor;
import sdgcpp.logicaDeNegocio.clases.TrabajoRecepcional;
import sdgcpp.logicaDeNegocio.implementacionDAO.EstudianteDAO;
import sdgcpp.logicaDeNegocio.implementacionDAO.PeriodoDAO;
import sdgcpp.logicaDeNegocio.implementacionDAO.ProfesorDAO;
import sdgcpp.logicaDeNegocio.implementacionDAO.TrabajoRecepcionalDAO;
import sdgcpp.utilidades.Alertas;

public class AgregarTRControlador implements Initializable{
    private static final Logger LOG = Logger.getLogger(AgregarTRControlador.class);
    private Stage escenario;
    private ObservableList<Profesor> listaProfesores;
    private ObservableList<Estudiante> listaEstudiantes;
    
    @FXML private TableView<Profesor> tableView_Profesores;
    @FXML private TableView<Estudiante> tableView_Estudiantes;
    @FXML private TableColumn<Profesor, String> column_Profesor;
    @FXML private TableColumn<Profesor, String> column_Rol;
    @FXML private TableColumn<Profesor, Void> column_QuitarProfesor;
    @FXML private TableColumn<Estudiante, String> column_Estudiante;
    @FXML private TableColumn<Estudiante, Void> column_QuitarEstudiante;
    @FXML private TextField textField_Correo;
    @FXML private TextField textField_MatriculaEstudiante;
    @FXML private TextField textField_Trabajo;
    @FXML private TextField textField_Modalidad;
    @FXML private DatePicker datePicker_FechaPresentación;
    @FXML private TextField textField_Resultado;
    @FXML private ComboBox<String> comboBox_Periodo;
    @FXML private ComboBox<String> comboBox_Rol;
    @FXML private ComboBox<String> comboBox_Licenciatura;
    
    @FXML private Button button_AgregarProfesor;
    @FXML private Button button_AgregarEstudiante;
    @FXML private Button button_Guardar;
    @FXML private Button button_Cancelar;
    
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
        aplicarValidacion(textField_Trabajo,  "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',;:\\-]{1,150}$");
        aplicarValidacion(textField_Modalidad, "^[\\p{L}áéíóúÁÉÍÓÚüÜ\\s',\\-]{1,50}$");
        aplicarValidacion(textField_Resultado,  "^(?:10|[0-9])$");
        
        
        column_Profesor.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        column_Rol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        column_Estudiante.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        listaProfesores = FXCollections.observableArrayList();
        tableView_Profesores.setItems(listaProfesores);
        listaEstudiantes = FXCollections.observableArrayList();
        tableView_Estudiantes.setItems(listaEstudiantes);

        asignarBotonQuitarProfesor();
        asignarBotonQuitarEstudiante();
        llenarComboBoxPerdiodo();
        llenarComboBoxRol();
        llenarComboBoxLicenciatura();
    }
    
    private void llenarComboBoxLicenciatura () {
        ObservableList<String> items = FXCollections.observableArrayList("Lic. en Ingeniería en Ciencia de Datos", "Lic. en Ingeniería en Sistemas y Tecnologías de la Información", "Lic. en Ingeniería de Software","Lic. en Ingeniería de Ciberseguridad e Infraestructura de Cómputo",
                "Lic. en Estadística","Lic. en Redes y Servicios de Cómputo","Lic. Ciencias y Técnicas Estadísticas ","Lic. en Tecnologías Computacionales","Lic. Informática ");
        comboBox_Licenciatura.setItems(items);
    }
    
    private void llenarComboBoxRol() {
        ObservableList<String> items = FXCollections.observableArrayList("Jurado", "Director", "codirector");
        comboBox_Rol.setItems(items);
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
    
    private void asignarIdPeriodo(TrabajoRecepcional trabajoRecepcional) {
        int indicePeriodoSeleccionada = comboBox_Periodo.getSelectionModel().getSelectedIndex();
        if (indicePeriodoSeleccionada >= 0) {
            try {
                List<List<String>> listaDePeriodos = obtenerListaDePeriodo();
                if (indicePeriodoSeleccionada < listaDePeriodos.size()) {
                    int periodosEscolares = Integer.parseInt(listaDePeriodos.get(indicePeriodoSeleccionada).get(0));
                    trabajoRecepcional.setIdPeriodo(periodosEscolares);
                }
            } catch (SQLException ex) {
                LOG.error(ex);
            }
        }
    }
    
    private TrabajoRecepcional crearTrabajoRecepcional() {
        TrabajoRecepcional trabajoRecepcional = null;
        LocalDate fechaPresentación = datePicker_FechaPresentación.getValue();
        
        if (fechaPresentación == null) {
            Alertas.mostrarMensaje(Alert.AlertType.INFORMATION, "AVISO", "Por favor selecciona ambas fechas.");
        }else {
            trabajoRecepcional = new TrabajoRecepcional();
            trabajoRecepcional.setTitulo(textField_Trabajo.getText());
            trabajoRecepcional.setFecha(fechaPresentación.toString());
            String licenciatura= comboBox_Licenciatura.getValue();
            if (licenciatura != null) {
                trabajoRecepcional.setLicenciatura(licenciatura);
            } else {
                trabajoRecepcional.setLicenciatura(""); 
            }
            trabajoRecepcional.setModalidad(textField_Modalidad.getText());
            try {
            int resultaObtenido = Integer.parseInt(textField_Resultado.getText());
            trabajoRecepcional.setResultadoObtenidoDefensa(resultaObtenido);
            } catch (NumberFormatException nfe) {
               LOG.warn(nfe);
            }
            asignarIdPeriodo(trabajoRecepcional);
        }

        return trabajoRecepcional;
    }
    
    private List<List<String>> obtenerIdsYRolesDeProfesores() {
        return listaProfesores.stream()
                              .map(profesor -> List.of(
                                  String.valueOf(profesor.getIdProfesor()), // ID como cadena
                                  profesor.getRol()                          // Rol
                              ))
                              .collect(Collectors.toList());
    }


    
    private List<Integer> obtenerIdsDeEstudiantes() {
    return listaEstudiantes.stream()
                          .map(Estudiante::getIdEstudiante)
                          .collect(Collectors.toList());
    }
    
    
    @FXML
    void onAgregarProfesor(ActionEvent event) {
        String criterioBusqueda = textField_Correo.getText();
        int indiceLicenciaturaSeleccionado = comboBox_Rol.getSelectionModel().getSelectedIndex();
        
        if (criterioBusqueda.isEmpty() || indiceLicenciaturaSeleccionado < 0) {
            Alertas.mostrarMensaje(Alert.AlertType.INFORMATION, "AVISO", "Por favor ingresa un criterio de búsqueda o asigne el rol al académico ");
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
     profesorEncontrado.setRol(comboBox_Rol.getValue());
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
            
            if (estudianteEncontrado.getTrabajoRecepcional() >= 1) {
                Alertas.mostrarMensajeAlumnoYaREgistrado();
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
        column_QuitarProfesor.setCellFactory(param -> new TableCell<>() {
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
    void onGuardar(ActionEvent event) {
        if (verificarInformacion()) {
            TrabajoRecepcional trabajoRecepcional = crearTrabajoRecepcional();
            if (trabajoRecepcional != null) {
                if (registrarProyectoCampo( trabajoRecepcional) == true) {
                    Alertas.mostrarMensajeExito();
                    limpiarFormulario();
                }
            }
        }
    }
    
        private boolean registrarProyectoCampo(TrabajoRecepcional trabajoRecepcional) {
        TrabajoRecepcionalDAO trabajoRecepcionalDAO = new TrabajoRecepcionalDAO();
        List<List<String>> profesores = obtenerIdsYRolesDeProfesores();
        List<Integer> idsEstudiantes = obtenerIdsDeEstudiantes();
        boolean registroExitoso = false;

        try {
            if (trabajoRecepcionalDAO.registrarTrabajoRecepcional(trabajoRecepcional, profesores, idsEstudiantes)==1) {
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
        int indiceLicenciaturaSeleccionado = comboBox_Licenciatura.getSelectionModel().getSelectedIndex();
        
        return  textField_Trabajo.getText().isEmpty() ||
                textField_Modalidad.getText().isEmpty() ||
                textField_Resultado.getText().isEmpty() ||
                datePicker_FechaPresentación.getValue() == null ||
                indicePeriodoSeleccionado < 0 ||
                indiceLicenciaturaSeleccionado < 0;
    }   
    
        private boolean verificarInformacion() {
        TrabajoRecepcional trabajoRecepcional = new TrabajoRecepcional();
        boolean validacion = true;

        if (!estaVacio()) {
            if (listaProfesores.isEmpty() || listaEstudiantes.isEmpty()) {
                    Alertas.mostrarMensaje(Alert.AlertType.WARNING, "AVISO", "Debe agregar al menos un profesor y un estudiante");
                    return false;
            }else{
                try {
                    trabajoRecepcional.setTitulo(textField_Trabajo.getText());
                    trabajoRecepcional.setModalidad(textField_Modalidad.getText());
                 
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
    void onCancelar(ActionEvent event) {
        if (Alertas.mostrarMensajeCancelar()) {
            Stage escenario = (Stage) button_Cancelar.getScene().getWindow();
            escenario.close();
        }
    }
    
    private void limpiarFormulario() {
        textField_Correo.clear();
        textField_Trabajo.clear();
        datePicker_FechaPresentación.setValue(null);
        textField_Modalidad.clear();
        textField_Resultado.clear();
        textField_MatriculaEstudiante.clear();
        comboBox_Periodo.setValue(null);
        comboBox_Rol.setValue(null);
        comboBox_Licenciatura.setValue(null);
        tableView_Profesores.getItems().clear();
        tableView_Estudiantes.getItems().clear();
    }

}
