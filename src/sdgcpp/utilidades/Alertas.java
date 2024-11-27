package sdgcpp.utilidades;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import javafx.scene.control.ButtonBar;

public class Alertas {

    public static final ButtonType BOTON_SI = new ButtonType("Sí");
    public static final ButtonType BOTON_NO = new ButtonType("No");
    
    public static void mostrarMensaje(AlertType tipoAlerta, String titulo, String contenido) {
        mostrarAlerta(tipoAlerta, titulo, contenido);
    }
    public static void mostrarMensaje(String titulo, String mensaje) {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle(titulo);
    alert.setHeaderText(null);
    alert.setContentText(mensaje);
    alert.showAndWait();
}
    public static boolean mostrarMensajeArchivoExistente() {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Confirmación");
    alert.setHeaderText("El archivo ya existe");
    alert.setContentText("El archivo ya existe en la carpeta. ¿Desea reemplazarlo?");
    
    Optional<ButtonType> result = alert.showAndWait();
    return result.isPresent() && result.get() == ButtonType.OK;
    }
    
    public static boolean mostrarMensajeInformeExistente() {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Confirmación");
    alert.setHeaderText("El archivo ya existe");
    alert.setContentText("El archivo ya existe en la carpeta. ¿Desea reemplazarlo?");
    
    Optional<ButtonType> result = alert.showAndWait();
    return result.isPresent() && result.get() == ButtonType.OK;
    }


    public static boolean mostrarConfirmacion(String titulo, String contenido) {
        return mostrarAlertaDeConfirmacion(titulo, contenido);
    }
    
    
    public static void mostrarMensajeNoHayEE() {
            mostrarAlerta(AlertType.INFORMATION, "AVISO", "Aun no tiene ninguna EE registrada");
    }
    
        public static void mostrarMensajeNoHayTR() {
            mostrarAlerta(AlertType.INFORMATION, "AVISO", "Aun no tiene ningun Trabajo recepcional registrado");
    }
        
        public static void mostrarMensajeNoHayPLADEA() {
            mostrarAlerta(AlertType.INFORMATION, "AVISO", "Aun no tiene ninguna PlaDEA registrada");
    }
      
        public static void mostrarMensajeNoHayPROYECTO() {
            mostrarAlerta(AlertType.INFORMATION, "AVISO", "Aun no tiene ningun Proyecto de campo registrado");
    }
    
    public static void mostrarMensajeColaboracionEnCurso() {
        String titulo = "Colaboración en Curso";
        String contenido = "No se puede abrir esta ventana mientras esté en curso una colaboración.";
        mostrarAlerta(AlertType.INFORMATION, titulo, contenido);
    }

    
    public static void mostrarMensajeNoHayColaboracionOfertda() {
            mostrarAlerta(AlertType.INFORMATION, "AVISO", "Solo podra ver las solicitudes cuando tenga una Propuesta de colaboración ofertada");
    }
    
    public static void mostrarMensajeNoCatalogosDisponibles() {
        mostrarAlerta(AlertType.INFORMATION, "AVISO", "Aún no se encuentran registrados los catálogos necesarios para realizar esta acción.");
    }

    
     public static void mostrarMensajeArchivoSinSeleccionar() {
            mostrarAlerta(AlertType.INFORMATION, "Seleccione un archivo", "No se ha seleccionado ningun archivo");
    }
    
    public static void mostrarMensajeProfesorNoSeleccionado() {
        mostrarAlerta(AlertType.INFORMATION, "AVISO", "Debe seleccionar un registro");
    }
    
    public static void mostrarMensajeColaboracionNoSeleccionado() {
        mostrarAlerta(AlertType.INFORMATION, "AVISO", "Debe seleccionar una colaboración");
    }
    
    public static void mostrarMensajeColaboracionEnCursoOFinalizada() {
        mostrarAlerta(AlertType.INFORMATION, "AVISO", "La colaboración que selecciono esta en curso o ya esta finalizada, por favor selecione una colaboracion cerrada");
    }
        
    public static void mostrarMensajeNoPuedesIniciarColaboracion() {
        mostrarAlerta(AlertType.INFORMATION, "AVISO", "Para iniciar la colaboracion debe de aceptar almenos una solicitud de colaboración");
    }
    
    public static void mostrarMensajeErrorColaboracionEnCurso() {
        mostrarAlerta(AlertType.INFORMATION, "AVISO", "Para iniciar una nueva colaboracion no debe de tener niguna colaboracion en curso");
    }
    
    public static void mostrarMensajeSolicitudNoSeleccionado() {
        mostrarAlerta(AlertType.INFORMATION, "AVISO", "Debe seleccionar una solicitud");
    }
    
    
    public static void mostrarMensajePropuestaNoSeleccionado() {
        mostrarAlerta(AlertType.INFORMATION, "AVISO", "Debe seleccionar una Propuesta");
    }
    
    public static void mostrarMensajeAccesoDenegado() {
        mostrarAlerta(AlertType.INFORMATION, "ACCESO DENEGADO", "Su cuenta se encuentra archivada, si desea ingresar contacte a los administrativos.");
    }
    
    public static void mostrarMensajeNoHayColaboraciones() {
        mostrarAlerta(AlertType.INFORMATION, "AVISO", "No hay Colaboraciones registradas en la base.");
    }
    
    public static void mostrarMensajeNoExisteActividades() {
        mostrarAlerta(AlertType.INFORMATION, "AVISO", "No hay actividades para mostrar.");
    }
    
    public static void mostrarMensajeInicioSesionFallido() {
        mostrarAlerta(AlertType.ERROR, "AVISO", "Usuario o contraseña incorrectos");
    }
    
    public static void mostrarMensajeErrorAlGuardarInforme() {
        mostrarAlerta(AlertType.ERROR, "Error en el guadado", "No se ha podido guardar el archivo");
    }
    
    public static void mostrarMensajeNoHayInstitucionesRegistradas() {
       mostrarAlerta(AlertType.INFORMATION, "Error", "No hay instituciones registradas en la base");
    }
    
    public static void mostrarMensajeNoExisteElArchivo() {
       mostrarAlerta(AlertType.ERROR, "ERROR", "El archivo que intenta visualizar no existe");
    }
    
    public static void mostrarMensajeErrorAlAbrirElArchivo() {
       mostrarAlerta(AlertType.ERROR, "ERROR", "No se pudo abrir el archivo. Por favor, intente de nuevo");
    }
    public static void mostrarMensajeSinResultados() {
        mostrarAlerta(AlertType.ERROR, "Sin resultados", "No se hallaron resultados");
    }
    
    public static void mostrarMensajeAlumnoYaREgistrado() {
        mostrarAlerta(AlertType.ERROR, "AVISO", "EL ESTUDIANTE YA TIENE UN TRABAJO RECEPCIONAL");
    }
    
    public static void mostrarMensajeCarpetaNoSeleccionada() {
        mostrarAlerta(AlertType.INFORMATION, "AVISO", "Debe seleccionar una carpeta");
    }
    
    public static void mostrarMensajeEvidenciaDescargado() {
        mostrarAlerta(AlertType.INFORMATION, "AVISO", "La evidencia fue descargada correctamente");
    }
    
    public static void mostrarMensajeCredencialesEnviadoExitoso() {
        mostrarAlerta(AlertType.INFORMATION, "AVISO", "Las credenciales fueron reenviados correctamente");
    }
    
    public static void mostrarMensajeInformacionNoRegistrada() {
        mostrarAlerta(AlertType.ERROR, "Error", "Hubo un error, no pudimos almacenar su informacion");
    }
    
    public static void mostrarMensajeErrorAlObtenerDatos() {
        mostrarAlerta(AlertType.ERROR, "Error", "Hubo un error, no pudimos obterner la informacion de las actividades");
    }
    
    public static void mostrarMensajeInstitucionNoEncontrada() {
        mostrarAlerta(AlertType.INFORMATION, "AVISO", "No existe coincidencias");
    }
    
    public static void mostrarMensajeElCorreoNoSePudoEnviar() {
        mostrarAlerta(AlertType.ERROR, "Error al enviar correo", "El correo no se pudo enviar. Podra reenviar las creedenciales mas tarde");
    }
    
    public static void mostrarMensajeErrorAlGuardarArchivo() {
        mostrarAlerta(AlertType.ERROR, "ERROR", "Ocurrio un error al guardar el archivo, por favor intentelo de nuevo");
    }
    
    public static void mostrarMensajeCamposVacios() {
        mostrarMensaje(AlertType.WARNING, "No se puede dejar ningún campo vacío", 
                "Por favor, ingrese toda la información solicitada");
    }
    
    public static void mostrarMensajeCamposVaciosEvaluacion() {
        mostrarMensaje(AlertType.WARNING, "No se puede dejar Campos vacíos", 
                "Por favor, verifique que haya seleccionado una opción de la casilla Evaluar Propuesta de colaboración"
                        + "o que haya escrito una justificación");
    }
    
    public static void mostrarMensajeComboBoxSinSeleccionar(String comboBoxName) {
        mostrarMensaje(AlertType.WARNING, comboBoxName + " no seleccionada", 
                "Por favor, seleccione un elemento de la lista desplegable de " + comboBoxName + ".");
    }

    
    public static void mostrarMensajeColaboracionSinActividades() {
        mostrarMensaje(AlertType.WARNING, "Para cerrar la colaboración debe tener 3 actividades finalizadas", 
                "Por favor, verifique e intentelo de nuevo");
    }
    
    public static void mostrarMensajeErrorBaseDatos() {
        mostrarMensaje(AlertType.ERROR, "ERROR", 
                "No se pudo conectar con la base de datos.\nInténtelo de nuevo o hágalo más tarde");
    }
    
    public static void mostrarMensajeEmailYaRegistrado() {
        mostrarMensaje(AlertType.INFORMATION, "AVISO", 
                "El Email ingresado ya ha sido registrado previamente. \nPor favor, inténtelo nuevamente");
    }
    
    public static void mostrarMensajeArchivoNoSeleccionado() {
        mostrarMensaje(AlertType.INFORMATION, "AVISO", "Seleccione un archivo");
    }
    
    public static void mostrarMensajeExito() {
        mostrarMensaje(AlertType.INFORMATION, "Registro exitoso", 
                "La información se registró correctamente en el sistema");
    }
    
    public static void mostrarMensajeActualizacionExita() {
        mostrarMensaje(AlertType.INFORMATION, "Actualización exitosa", 
                "La información de la institucion se actualizo correctamente en el sistema");
    }
    
    
    public static void mostrarEvaluacionMensajeExito() {
        mostrarMensaje(AlertType.INFORMATION, "Evaluacion exitosa", 
                "La solicitud se a evaluado exitosamente");
    }
    
    public static void mostrarMensajeAgregarInstitucionExito() {
        mostrarMensaje(AlertType.INFORMATION, "Registro exitoso", 
                "Se ha registrado correctamente a la Universidad Veracruzana, ya puede registrar al profesor uv");
    }
    
    public static void mostrarMensajeArchivoCargado() {
        mostrarMensaje(AlertType.INFORMATION, "Datos correctos", 
                "La evidencia se registro correctamente");
    }
    
    public static void mostrarMensajeInformeGuardadoExitosamente() {
        mostrarMensaje(AlertType.INFORMATION, "Archivo guardado", "El informe ha sido guardado exitosamente.");
    }
   
    public static void mostrarMensajeEvaluacionPropuestaExito() {
        mostrarMensaje(AlertType.INFORMATION, "Evaluación exitosa", 
                "La evaluación se envio correctamente al profesor");
    }
    
    public static void mostrarMensajeRegistroActividadExito() {
        mostrarMensaje(AlertType.INFORMATION, "Registro exitoso", 
                "La actividad se registro correctamente en la colaboración");
    }
    
    public static void mostrarMensajeEstudianteAgregadoExito() {
        mostrarMensaje(AlertType.INFORMATION, "Registro exitoso", 
                "El estudiante se agrego correctamente a la colaboración");
    }
    
    public static void mostrarMensajeErrorAlAccederAlaCarpeta() {
        mostrarMensaje(AlertType.INFORMATION, "Error en el guardador", 
                "No se pudo acceder a los elementos deseados");
    }
    
    public static void mostrarMensajeEstudiantesAgregadosExito() {
        mostrarMensaje(AlertType.INFORMATION, "Registro exitoso", 
                "Los estudiantes se agregaron correctamente a la colaboración");
    }
    
    public static void mostrarMensajeExitoInicioColanboracion() {
        mostrarMensaje(AlertType.INFORMATION, "Registro exitoso", 
                "La información se registró correctamente en el sistema. La Colaboracion se encuentra en curso");
    }
    
    public static void mostrarMensajeExitoIColaboracionCerrada() {
        mostrarMensaje(AlertType.INFORMATION, "Colaboración cerrada con exito", 
                "La colaboracion se cerro correctamente, felicidades por su colaboración");
    }
    
    public static void mostrarMensajeErrorRegistro() {
        mostrarMensaje(AlertType.INFORMATION, "AVISO", 
                "No se pudo registrar la información    .\nPor favor, inténtelo nuevamente");
    }
    
    public static void mostrarMensajeErrorCambioPantalla() {
        mostrarMensaje(AlertType.ERROR, "ERROR", 
                "Ha ocurrido un error inesperado al cambiar de pantalla.\nInténtelo de nuevo o hágalo más tarde");
    }
    
    public static void mostrarMensajeErrorIniciarColaboracion() {
        mostrarMensaje(AlertType.ERROR, "ERROR", 
                "Ha ocurrido un error inesperado al cambiar de pantalla.\nInténtelo de nuevo o hágalo más tarde");
    }
    
    public static void mostrarErrorEnLaCreacionDeInforme() {
        mostrarMensaje(AlertType.ERROR, "Error en la creación de informe", 
                "No se ha podido generar el informe, intentelo más tarde.");
    }
 
    public static void mostrarMensajeInformacionInvalida() {
        mostrarMensaje(AlertType.WARNING, "Los datos ingresados son inválidos", 
                "Por favor, compruebe la información e inténtelo nuevamente");
    }
    
    public static void mostrarMensajeNombreInvalido() {
        mostrarMensaje(AlertType.WARNING, "Debe de nombrar la evidencia", 
                "Por favor, intentelo de nuevo");
    }
    
    public static void mostrarMensajeNumeroEstudianteInvalido() {
        mostrarMensaje(AlertType.WARNING, "En la colaboración deben participar al menos 10 estudiantes y como máximo 150.", 
                "Por favor, compruebe la información e inténtelo nuevamente");
    }
    
    public static void mostrarMensajeFechaInvalida() {
        mostrarMensaje(AlertType.WARNING, "Fecha inválida", 
                "Verifique que la fecha ingresada sea correcta");
    }
        
    public static void mostrarMensajeDatosDuplicados() {
        mostrarMensaje(AlertType.WARNING, "Duplicado de datos", 
                "Los datos que desea ingresar ya han sido previamente insertados");
    }
    
    public static void mostrarMensajeNingunElementoSeleccionado () {
    mostrarMensaje(AlertType.WARNING, "Ningún elemento seleccionado", 
                "Por favor, seleccione un elemento de la tabla");    
    }
    
    public static void mostrarMensajeCorreoConFormatoInvalido () {
        mostrarMensaje(AlertType.WARNING, "Correo invalido", 
                "El correo electrónico es inválido. "
                                + "Por favor, asegúrate de que tenga el formato "
                                + "correcto, por ejemplo: usuario@dominio.com");    
    }
    
    public static void mostrarMensajeProfesorYaExistente() {
        mostrarMensaje(AlertType.INFORMATION, "AVISO", 
                "El Profesor ya ha sido registrado previamente. \nPor favor, inténtelo nuevamente");
    }
    
    public static void mostrarMensajeEstudianteYaExistente() {
        mostrarMensaje(AlertType.INFORMATION, "AVISO", 
                "El Estudiante ya ha sido registrado previamente. \nPor favor, inténtelo nuevamente");
    }
    
    public static void mostrarMensajeNumeroPersonalYaExistente() {
        mostrarMensaje(AlertType.INFORMATION, "AVISO", 
                "El Numero de personal ya ha sido registrado previamente. \nPor favor, inténtelo nuevamente");
    }
    
    public static void mostrarMensajeInstitucionYaExistente() {
        mostrarMensaje(AlertType.INFORMATION, "AVISO", 
                "El Nombre de la institucion ingresada ya ha sido registrado previamente. \nPor favor, inténtelo nuevamente");
    }
        
    public static void mostrarMensajeClaveInstitucionalYaRegistrada() {
        mostrarMensaje(AlertType.INFORMATION, "AVISO", 
                "La clave ingresada ya ha sido registrado previamente. \nPor favor, inténtelo nuevamente");
    }
    
    public static void mostrarMensajeDatosIguales() {
        mostrarMensaje(AlertType.INFORMATION, "AVISO", 
                "No hay cambios para actualizar. Por favor, modifique algún campo antes de intentar actualizar.");
    }

    public static boolean mostrarMensajeCancelar() {
        return mostrarConfirmacion("AVISO", "¿Estás seguro que deseas cancelar?");
    }

    public static boolean mostrarMensajeConfirmacionDesactivar() {
        return mostrarConfirmacion("AVISO", "¿Estás seguro que deseas desactivar?");
    }
    
    public static boolean mostrarMensajeConfirmacionArchivarProfesor() {
        return mostrarConfirmacion ("Confirmar archivado", "¿Está seguro de archivar al profesor?");
    }
    
    public static boolean mostrarMensajeConfirmacionActivarProfesor() {
        return mostrarConfirmacion ("Confirmar activación", "Una vez activado, el profesor estará disponible en el sistema.");
    }
    
    public static boolean mostrarMensajeAgregarEstudiantes() {
        return mostrarConfirmacion ("Agregar Estudiante", "¿No desar agruegar un estudiante a la colaboración?");
    }
    
    public static boolean mostrarMensajeConfirmacionCerrarColaboracion() {
        return mostrarConfirmacion ("Confirmar cerrar colaboración", "¿Está seguro de cerrar la colaboracion?");
    }
    
public static boolean mostrarMensajeConfirmacionAgregarInstitucionUV() {
    return mostrarConfirmacion(
        "Para agregar un profesor de la UV, primero debe añadir la Universidad Veracruzana.",
        "¿Desea agregar la Universidad Veracruzana ahora?"
    );
}

    
    public static boolean mostrarMensajeConfirmacionFinalizarColaboracion() {
        return mostrarConfirmacion ("Confirmar finalizar colaboración", "¿Está seguro de finalizar la colaboracion?");
    }
    
    public static boolean mostrarMensajeDescargaDeArchivo() {
        return mostrarConfirmacion ("Elaboración de archivo exitoso", "El archivo se creó correctamente.\n ¿Desea descargarlo?");
    }
    

    
    private static void mostrarAlerta(AlertType tipoAlerta, String titulo, String contenido) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setTitle(titulo);
        alerta.setHeaderText(titulo);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }

    private static boolean mostrarAlertaDeConfirmacion(String titulo, String contenido) {
        Alert alerta = new Alert(AlertType.CONFIRMATION, contenido, BOTON_SI, BOTON_NO);
        alerta.setTitle(titulo);
        alerta.setHeaderText(titulo);
        Optional<ButtonType> resultado = alerta.showAndWait();
        return resultado.isPresent() && resultado.get() == BOTON_SI;
    }
    
    public static boolean mostrarMensajeConfirmacion(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        ButtonType buttonTypeYes = new ButtonType("Sí");
        ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == buttonTypeYes;
    }
}