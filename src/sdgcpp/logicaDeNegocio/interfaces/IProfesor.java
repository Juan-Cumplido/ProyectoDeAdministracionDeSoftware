package sdgcpp.logicaDeNegocio.interfaces;

import java.sql.SQLException;
import java.util.List;
import sdgcpp.logicaDeNegocio.clases.Acceso;
import sdgcpp.logicaDeNegocio.clases.Profesor;

public interface IProfesor {
    public int registrarProfesor(Profesor profesor, Acceso accesor) throws SQLException;
    public boolean verificarExistenciaProfesor(String nombre, String apellidoPaterno, String apellidoMaterno) throws SQLException;
    public boolean verificarSiExisteElCorreo(String correo) throws SQLException ;
    public Profesor obtenerProfesorPorID(String idProfesor) throws SQLException;
    public int eliminarProfesor(String correo) ;
    public List<List<String>> obtenerListaDeIdiomas() ;
    public List<List<String>> obtenerListaDeInstituciones() ;
    public Profesor obtenerProfesorPorCorreo(String correo)throws SQLException;
    public List<Profesor> obtenerListaTodosLosProfesores()throws SQLException ;
    public List<String>obtenerListaDeTodosLosEstadoProfesor() throws SQLException;
    public List<Profesor> obtenerListaProfesoresPorNombre(String criterioBusqueda) throws SQLException ;
    public String identitificarProfesorUVOProfesorExterno(String correo);
    public List<String>obtenerListaDeNombreInstitucion() throws SQLException;
    public int actualizarInformacionDelProfesor(Profesor profesor, String idProfesor) throws SQLException;
    public boolean verificarProfesorDuplicado(Profesor profesor) throws SQLException ;
    public List<List<String>> obtenerListaDeInstitucionesSinInstitucion();
}
