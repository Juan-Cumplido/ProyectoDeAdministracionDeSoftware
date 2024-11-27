package sdgcpp.controladores;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import sdgcpp.logicaDeNegocio.clases.EE;
import sdgcpp.logicaDeNegocio.clases.PlaDEA;
import sdgcpp.logicaDeNegocio.clases.Profesor;
import sdgcpp.logicaDeNegocio.clases.TablaPC;
import sdgcpp.logicaDeNegocio.clases.TablaTR;
import sdgcpp.logicaDeNegocio.implementacionDAO.AccesoDAO;
import sdgcpp.logicaDeNegocio.implementacionDAO.EEDAO;
import sdgcpp.logicaDeNegocio.implementacionDAO.PeriodoDAO;
import sdgcpp.logicaDeNegocio.implementacionDAO.PlaDEADAO;
import sdgcpp.logicaDeNegocio.implementacionDAO.ProyectoCampoDAO;
import sdgcpp.logicaDeNegocio.implementacionDAO.TrabajoRecepcionalDAO;
import sdgcpp.utilidades.AccesoSingleton;
import sdgcpp.utilidades.Alertas;

public class ProfesorMenuControlador implements Initializable{
    private static final Logger LOG = Logger.getLogger(ProfesorMenuControlador.class);
    private AccesoSingleton accesoSingleton;
    ObservableList<EE> listaEE = FXCollections.observableArrayList();
    ObservableList<TablaTR> listaTR = FXCollections.observableArrayList();
    ObservableList<PlaDEA> listaPLADEA = FXCollections.observableArrayList();
    ObservableList<TablaPC> listaPC = FXCollections.observableArrayList();
    @FXML private Label label_Nombre;
    @FXML
    private ImageView imageView_Salir;
    @FXML private ComboBox<String> comboBox_Periodo;
    @FXML private ComboBox<String> comboBox_Constancia;
    @FXML private Button button_Buscar;
    @FXML private Button button_GenerarConstancia;
    @FXML private TableView<PlaDEA> tableView_PlaDEA;
    @FXML private TableColumn<PlaDEA, String> column_Eje;
    @FXML private TableColumn<PlaDEA, String> column_ProgramaEstrategico;
    @FXML private TableColumn<PlaDEA, String> column_Objetivos;
    @FXML private TableColumn<PlaDEA, String> column_Acciones;
    @FXML private TableColumn<PlaDEA, String> column_Metas;

    @FXML private TableView<EE> tableView_EE;
    @FXML private TableColumn<EE, String> column_ProgramaEducativo;
    @FXML private TableColumn<EE, String> column_Experiencia;
    @FXML private TableColumn<EE, String> column_Bloque;
    @FXML private TableColumn<EE, String> column_Seccion;
    @FXML private TableColumn<EE, String> column_Credito;
    @FXML private TableColumn<EE, String> column_H;
    @FXML private TableColumn<EE, String> column_S;
    @FXML private TableColumn<EE, String> column_M;
    
    @FXML private TableView<TablaPC> tableView_ProyectoCampo;
    @FXML private TableColumn<TablaPC, String> column_Proyecto;
    @FXML private TableColumn<TablaPC, String> column_FechaInicio;
    @FXML private TableColumn<TablaPC, String> column_FechaFin;
    @FXML private TableColumn<TablaPC, String> column_Lugar;
    @FXML private TableColumn<TablaPC, String> column_Impacto;
    @FXML private TableColumn<TablaPC, String> column_Alumnos;
    
    @FXML private TableView<TablaTR> tableView_TrabajoRecepcional;
    @FXML private TableColumn<TablaTR, String> column_Rol;
    @FXML private TableColumn<TablaTR, String> column_AlumnosTR;
    @FXML private TableColumn<TablaTR, String> column_Titulo;
    @FXML private TableColumn<TablaTR, String> column_Modalidad;
    @FXML private TableColumn<TablaTR, String> column_Fecha;
    @FXML private TableColumn<TablaTR, String> column_Licenciatura;
    @FXML private TableColumn<TablaTR, String> column_Defensa;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       accesoSingleton = AccesoSingleton.getInstance();
       tableView_PlaDEA.setVisible(false);
       tableView_EE.setVisible(false);
       tableView_ProyectoCampo.setVisible(false);
       tableView_TrabajoRecepcional.setVisible(false);
        llenarComboBoxPerdiodo();
        llenarComboBoxTipoConstancia();
        mostrarNombreProfesor();
    }
    
    private void mostrarNombreProfesor(){
        AccesoDAO accesoDAO = new AccesoDAO();
        int idProfesor = AccesoSingleton.getInstance().getAccesoId();
        try {
            Profesor profesor = accesoDAO.obtenerProfesorPorID(idProfesor);
             label_Nombre.setText(profesor.getNombre() +" "+ profesor.getApellidoPaterno() +" "+ profesor.getApellidoMaterno());
        }catch (SQLException sqlException ) {
            Alertas.mostrarMensajeErrorBaseDatos();
            LOG.fatal("No hay conexión con la base de datos :" +this.getClass().getName() + ", método " + Thread.currentThread().getStackTrace()[1].getMethodName() + ": " + sqlException.getMessage(), sqlException);
            
        }
    }
    
    private void llenarComboBoxTipoConstancia() {
        ObservableList<String> items = FXCollections.observableArrayList("Jurado", "PlaDEA", "Proyecto de campo", "Impartición EE");
        comboBox_Constancia.setItems(items);
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
    
    
    @FXML
    private void button_Buscar(ActionEvent  event) {
       
        int indiceTipoConstancia = comboBox_Constancia.getSelectionModel().getSelectedIndex();
        int indiceLicenciaturaSeleccionado = comboBox_Periodo.getSelectionModel().getSelectedIndex();
        
        if (indiceTipoConstancia >= 0 && indiceLicenciaturaSeleccionado >= 0) {
            String tipoConstancia = comboBox_Constancia.getValue();
            int idPeriodo = asignarIdPeriodo();
            buscarConstancias(tipoConstancia,idPeriodo);    
        }else{
            Alertas.mostrarMensaje(Alert.AlertType.INFORMATION, "AVISO", "Por favor seleccione el tipo de constancia y el periodo");
        }
        
 
    }
    

    
    private void buscarConstancias(String tipoConstancia, int idPeriodo) {
        switch (tipoConstancia){
            case "Jurado" -> {
                tableView_PlaDEA.setVisible(false);
                tableView_EE.setVisible(false);
                tableView_ProyectoCampo.setVisible(false);
                tableView_TrabajoRecepcional.setVisible(true); 
                llenarTablaJurado(idPeriodo);
                
                
            }
            case "PlaDEA" ->{
                tableView_PlaDEA.setVisible(true);
                tableView_EE.setVisible(false);
                tableView_ProyectoCampo.setVisible(false);
                tableView_TrabajoRecepcional.setVisible(false);
                llenarTablaPladea(idPeriodo);
            }
            case "Proyecto de campo" ->{
                tableView_PlaDEA.setVisible(false);
                tableView_EE.setVisible(false);
                tableView_ProyectoCampo.setVisible(true);
                tableView_TrabajoRecepcional.setVisible(false);
                llenarTablaPC(idPeriodo);
            }
            default -> {
                tableView_PlaDEA.setVisible(false);
                tableView_EE.setVisible(true);
                tableView_ProyectoCampo.setVisible(false);
                tableView_TrabajoRecepcional.setVisible(false);
                llenarTablaEE(idPeriodo);
            }
                
            
        }
    }

    private void llenarTablaPC(int idPeriodo) {
        int idAcceso = accesoSingleton.getAccesoId();
        ProyectoCampoDAO proyectoCampoDAO = new ProyectoCampoDAO();
        List<TablaPC> ProyectoCampoLista = null;
        try {
            ProyectoCampoLista = proyectoCampoDAO.obtenerProyectosPorProfesorYPeriodo(idAcceso, idPeriodo);
            if (ProyectoCampoLista == null || ProyectoCampoLista.isEmpty()) {
               listaPC.clear();
               Alertas.mostrarMensajeNoHayPROYECTO();
            } else {
                listaPC.clear();
                for (int i = 0; i < ProyectoCampoLista.size(); i++) {
                    TablaPC tablaPC = ProyectoCampoLista.get(i);
                    listaPC.add(new TablaPC(
                            tablaPC.getNombre(),
                            tablaPC.getFechaInicio(), 
                            tablaPC.getFechaFin(), 
                            tablaPC.getLugar(),
                            tablaPC.getImpactoObtenido(),
                            tablaPC.getAlumnos()));

                }
                tableView_ProyectoCampo.setItems(listaPC);
                column_Proyecto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
                column_FechaInicio.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
                column_FechaFin.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
                column_Lugar.setCellValueFactory(new PropertyValueFactory<>("lugar"));
                column_Impacto.setCellValueFactory(new PropertyValueFactory<>("impactoObtenido"));
                column_Alumnos.setCellValueFactory(new PropertyValueFactory<>("alumnos"));
            }
        } catch (SQLException ex) {
            Alertas.mostrarMensajeErrorBaseDatos();
            LOG.error(ex);
        }
 
    }
    
    
        private void llenarTablaPladea(int idPeriodo) {
        int idAcceso = accesoSingleton.getAccesoId();
        PlaDEADAO plaDEADAO = new PlaDEADAO();
        List<PlaDEA> PlaDEALista = null;
        try {
            PlaDEALista = plaDEADAO.obtenerPlaDEAsPorProfesorYPeriodo(idAcceso, idPeriodo);
            if (PlaDEALista == null || PlaDEALista.isEmpty()) {
               listaPLADEA.clear();
               Alertas.mostrarMensajeNoHayPLADEA();
            } else {
                listaPLADEA.clear();
                for (int i = 0; i < PlaDEALista.size(); i++) {
                    PlaDEA pladea = PlaDEALista.get(i);
                    listaPLADEA.add(new PlaDEA(
                            pladea.getEjeEstrategico(),
                            pladea.getProgramaEstrategico(), 
                            pladea.getObjetivos(), 
                            pladea.getAcciones(),
                            pladea.getMetas()));

                }
                tableView_PlaDEA.setItems(listaPLADEA);
                column_Eje.setCellValueFactory(new PropertyValueFactory<>("ejeEstrategico"));
                column_ProgramaEstrategico.setCellValueFactory(new PropertyValueFactory<>("programaEstrategico"));
                column_Objetivos.setCellValueFactory(new PropertyValueFactory<>("objetivos"));
                column_Acciones.setCellValueFactory(new PropertyValueFactory<>("acciones"));
                column_Metas.setCellValueFactory(new PropertyValueFactory<>("metas"));
            }
        } catch (SQLException ex) {
            Alertas.mostrarMensajeErrorBaseDatos();
            LOG.error(ex);
        }
 
    }
    
    
    private void llenarTablaJurado(int idPeriodo) {
        int idAcceso = accesoSingleton.getAccesoId();
        TrabajoRecepcionalDAO trabajoRecepcionalDAO = new TrabajoRecepcionalDAO();
        List<TablaTR> TRLista = null;
        try {
            TRLista = trabajoRecepcionalDAO.obtenerTrabajosRecepcionales(idAcceso, idPeriodo);
            if (TRLista == null || TRLista.isEmpty()) {
               listaTR.clear();
               Alertas.mostrarMensajeNoHayTR();
            } else {
                listaTR.clear();
                for (int i = 0; i < TRLista.size(); i++) {
                    TablaTR tr = TRLista.get(i);
                    listaTR.add(new TablaTR(
                            tr.getRol(),
                            tr.getAlumnos(), 
                            tr.getTitulo(), 
                            tr.getModalidad(),
                            tr.getFecha(),
                            tr.getLicenciatura(),
                            tr.getDefensa()));

                }
                tableView_TrabajoRecepcional.setItems(listaTR);

                column_Rol.setCellValueFactory(new PropertyValueFactory<>("rol"));
                column_AlumnosTR.setCellValueFactory(new PropertyValueFactory<>("alumnos"));
                column_Titulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
                column_Modalidad.setCellValueFactory(new PropertyValueFactory<>("modalidad"));
                column_Fecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
                column_Licenciatura.setCellValueFactory(new PropertyValueFactory<>("licenciatura"));
                column_Defensa.setCellValueFactory(new PropertyValueFactory<>("defensa"));
               
            }
        } catch (SQLException ex) {
            Alertas.mostrarMensajeErrorBaseDatos();
            LOG.error(ex);
        }
 
    }
    
    private void llenarTablaEE(int idPeriodo) {
        int idAcceso = accesoSingleton.getAccesoId();
        EEDAO eeDAO = new EEDAO();
        List<EE> EELista = null;
        try {
            EELista = eeDAO.obtenerEEPorProfesorYPeriodo(idAcceso, idPeriodo);
            if (EELista == null || EELista.isEmpty()) {
               listaEE.clear();
               Alertas.mostrarMensajeNoHayEE();
            } else {
                listaEE.clear();
                for (int i = 0; i < EELista.size(); i++) {
                    EE ee = EELista.get(i);
                    listaEE.add(new EE(
                            ee.getProgramaEducativo(),
                            ee.getExperienciaEducativa(), 
                            ee.getBloque(), 
                            ee.getSesion(),
                            ee.getCreditos(),
                            ee.getHoras(),
                            ee.getSemanas(),
                            ee.getMes()));

                }
                tableView_EE.setItems(listaEE);
                column_ProgramaEducativo.setCellValueFactory(new PropertyValueFactory<>("programaEducativo"));
                column_Experiencia.setCellValueFactory(new PropertyValueFactory<>("experienciaEducativa"));
                column_Bloque.setCellValueFactory(new PropertyValueFactory<>("bloque"));
                column_Seccion.setCellValueFactory(new PropertyValueFactory<>("sesion"));
                column_Credito.setCellValueFactory(new PropertyValueFactory<>("creditos"));
                column_H.setCellValueFactory(new PropertyValueFactory<>("horas"));
                column_S.setCellValueFactory(new PropertyValueFactory<>("semanas"));
                column_M.setCellValueFactory(new PropertyValueFactory<>("mes"));
            }
        } catch (SQLException ex) {
            Alertas.mostrarMensajeErrorBaseDatos();
            LOG.error(ex);
        }
 
    }
    
    private int asignarIdPeriodo() { 
        int idPeriodo = -1; 
        int indicePeriodoSeleccionada = comboBox_Periodo.getSelectionModel().getSelectedIndex();
        if (indicePeriodoSeleccionada >= 0) {
            try {
                List<List<String>> listaDePeriodos = obtenerListaDePeriodo();
                if (indicePeriodoSeleccionada < listaDePeriodos.size()) {
                    idPeriodo = Integer.parseInt(listaDePeriodos.get(indicePeriodoSeleccionada).get(0));
                }
            } catch (SQLException ex) {
                LOG.error(ex);
            }
        }
        return idPeriodo; 
    }

    @FXML
    private void button_GenerarConstancia(ActionEvent  event) {
        Object seleccion = null;
        String tipoConstancia = null;

        if (!tableView_PlaDEA.getSelectionModel().isEmpty()) {
            seleccion = tableView_PlaDEA.getSelectionModel().getSelectedItem();
            tipoConstancia = "PlaDEA";
        } else if (!tableView_EE.getSelectionModel().isEmpty()) {
            seleccion = tableView_EE.getSelectionModel().getSelectedItem();
             tipoConstancia = "Impartición EE";
        } else if (!tableView_TrabajoRecepcional.getSelectionModel().isEmpty()) {
            seleccion = tableView_TrabajoRecepcional.getSelectionModel().getSelectedItem();
            tipoConstancia = "Jurado";
        } else if (!tableView_ProyectoCampo.getSelectionModel().isEmpty()) {
            seleccion = tableView_ProyectoCampo.getSelectionModel().getSelectedItem();
            tipoConstancia = "Proyecto de campo";
        }

        if (seleccion == null || tipoConstancia == null) {
            Alertas.mostrarMensaje(Alert.AlertType.INFORMATION, "AVISO", "Por favor seleccione un elemento.");
            return;
        }

        guardarConstanciaEnArchivo(seleccion, tipoConstancia);
      

    }
        
    private void guardarConstanciaEnArchivo(Object seleccion, String tipoConstancia) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Guardar Constancia");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf"));
            File archivo = fileChooser.showSaveDialog(null);

            if (archivo != null) {
                try {
                   Document document = new Document();
                    PdfWriter.getInstance(document, new FileOutputStream(archivo));
                    document.open();

                   switch (tipoConstancia) {
                        case "PlaDEA":
                            agregarContenidoPlaDEA(document, (PlaDEA) seleccion);
                            break;
                        case "Proyecto de campo":
                            agregarContenidoProyectoCampo(document, (TablaPC) seleccion);
                            break;
                        case "Impartición EE":
                            agregarContenidoImparticionEE(document, (EE) seleccion);
                            break;
                        case "Jurado":
                            agregarContenidoJurado(document, (TablaTR) seleccion);
                            break;
                        default:
                            throw new IllegalArgumentException("Tipo de constancia no soportado");
                    }

                    document.close();

                    Alertas.mostrarMensaje(Alert.AlertType.INFORMATION, "Éxito", "Constancia guardada correctamente.");
                } catch (Exception e) {
                    Alertas.mostrarMensaje(Alert.AlertType.ERROR, "Error", "No se pudo guardar la constancia.");
                    LOG.error("Error al guardar la constancia en PDF: " + e.getMessage(), e);
                }
            } else {
                Alertas.mostrarMensaje(Alert.AlertType.WARNING, "Cancelado", "La operación de guardado fue cancelada.");
            }
        }

    private void agregarContenidoPlaDEA(Document document, PlaDEA pladea) throws DocumentException {
        LocalDate fechaActual = LocalDate.now();
        document.add(new Paragraph("Facultad de Estadística e Informática\nRegión Xalapa\n\n", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
        document.add(new Paragraph("A quien corresponda:\n\nPresente\n\n"));
        document.add(new Paragraph("El que suscribe, Director de la Facultad de Estadística e Informática de la Universidad Veracruzana\n\nHACE CONSTAR\n\n", FontFactory.getFont(FontFactory.HELVETICA, 12)));
        document.add(new Paragraph("Que la Mtra. " + label_Nombre.getText()+", Profesora adscrita a esta Facultad contribuyó a la consecución de las siguientes metas del Plan de Desarrollo de la Entidad Académica (PlaDEA) 2017-2021:\n\n"));
        document.add(new Paragraph("Eje estratégico: " + pladea.getEjeEstrategico()));
        document.add(new Paragraph("Programa estratégico: " + pladea.getProgramaEstrategico()));
        document.add(new Paragraph("Objetivos generales: " + pladea.getObjetivos()));
        document.add(new Paragraph("Acciones: " + pladea.getAcciones()));
        document.add(new Paragraph("Metas: " + pladea.getMetas()));
        document.add(new Paragraph("\nA petición de la interesada y para los usos legales que a la misma convengan, se extiende la presente en la ciudad de Xalapa de Enriquez, Veracruz a los "+fechaActual+ ".\n"));
        document.add(new Paragraph("A t e n t a m e n t e\n\n“Lis de Veracruz: Arte, Ciencia, Luz”\n\n Dr. Luis Gerardo Montané Jiménez \nDirector"));
    }
    
    private void agregarContenidoProyectoCampo(Document document, TablaPC proyecto) throws DocumentException {
        LocalDate fechaActual = LocalDate.now();
        document.add(new Paragraph("A quien corresponda:\n\n"));
        document.add(new Paragraph("El que suscribe, Dr. Luis Gerardo Montané Jiménez Director de la Facultad de Estadística e Informática de la Universidad Veracruzana\n\nHACE CONSTAR\n\n", FontFactory.getFont(FontFactory.HELVETICA, 12)));
        document.add(new Paragraph("Que el/la "+label_Nombre.getText()+" participó en un proyecto de campo con las siguientes características:\n\n"));
        document.add(new Paragraph("Proyecto realizado: " + proyecto.getNombre()));
        document.add(new Paragraph("Duración: " + proyecto.getFechaInicio() + " - " + proyecto.getFechaFin()));
        document.add(new Paragraph("Lugar donde se desarrolló: " + proyecto.getLugar()));
        document.add(new Paragraph("Nombre de las y los alumnos involucrados: " + proyecto.getAlumnos()));
        document.add(new Paragraph("Impacto obtenido: " + proyecto.getImpactoObtenido()));
        document.add(new Paragraph("\nA t e n t a m e n t e\n\n“Lis de Veracruz: Arte, Ciencia, Luz”\n\nDr. Luis Gerardo Montané Jiménez\nDirector"));
    }

    private void agregarContenidoImparticionEE(Document document, EE ee) throws DocumentException {
        LocalDate fechaActual = LocalDate.now();
        document.add(new Paragraph("Facultad de Estadística e Informática\nRegión Xalapa\n\n", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
        document.add(new Paragraph("A quien corresponda:\n\n"));
        document.add(new Paragraph("El que suscribe, Director de la Facultad de Estadística e Informática de la Universidad Veracruzana\n\nHACE CONSTAR\n\n", FontFactory.getFont(FontFactory.HELVETICA, 12)));
        document.add(new Paragraph("Que el Mtro. "+label_Nombre.getText()+" impartió la siguiente experiencia educativa en el periodo "+comboBox_Periodo.getValue()+":\n\n"));
        document.add(new Paragraph("Programa educativo: " + ee.getProgramaEducativo()));
        document.add(new Paragraph("Experiencia educativa: " + ee.getExperienciaEducativa()));
        document.add(new Paragraph("Bloque: " + ee.getBloque()));
        document.add(new Paragraph("Sección: " + ee.getSesion()));
        document.add(new Paragraph("Créditos: " + ee.getCreditos()));
        document.add(new Paragraph("H: " + ee.getHoras()));
        document.add(new Paragraph("S: " + ee.getSemanas()));
        document.add(new Paragraph("M: " + ee.getMes()));
        document.add(new Paragraph("\nA petición de la interesada y para los fines legales que a la misma convengan, se extiende la presente en la ciudad de Xalapa de Enriquez, Veracruz a los "+fechaActual+ ".\n"));
        document.add(new Paragraph("A t e n t a m e n t e\n\n“Lis de Veracruz: Arte, Ciencia, Luz”\n\nDr. Luis Gerardo Montané Jiménez\nDirector"));
    }
    
    private void agregarContenidoJurado(Document document, TablaTR tr) throws DocumentException {
        LocalDate fechaActual = LocalDate.now();
        document.add(new Paragraph("Facultad de Estadística e Informática\nRegión Xalapa\n\n", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
        document.add(new Paragraph("A quien corresponda:\n\n"));
        document.add(new Paragraph("El que suscribe, Director de la Facultad de Estadística e Informática de la Universidad Veracruzana\n\nHACE CONSTAR\n\n", FontFactory.getFont(FontFactory.HELVETICA, 12)));
        document.add(new Paragraph("Que el Mtro. "+label_Nombre.getText()+" fungió como "+tr.getRol()+" en los siguientes trabajos recepcionales de la licenciatura "+tr.getLicenciatura()+".\n\n"));
        document.add(new Paragraph("Nombre(s) del (los) alumno(s): " + tr.getAlumnos()));
        document.add(new Paragraph("Título del trabajo: " + tr.getTitulo()));
        document.add(new Paragraph("Modalidad: " + tr.getModalidad()));
        document.add(new Paragraph("Fecha de presentación: " + tr.getFecha()));
        document.add(new Paragraph("Resultado obtenido en la defensa: " + tr.getDefensa()));
        document.add(new Paragraph("\nA petición de la interesada y para los fines legales que a la misma convengan, se extiende la presente en la ciudad de Xalapa de Enriquez, Veracruz a los "+fechaActual+ ".\n"));
        document.add(new Paragraph("A t e n t a m e n t e\n\n“Lis de Veracruz: Arte, Ciencia, Luz”\n\nDr. Luis Gerardo Montané Jiménez\nDirector"));
    }


    
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


    
}
