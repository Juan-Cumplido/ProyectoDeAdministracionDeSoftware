package sdgcpp.logicaDeNegocio.interfaces;

import java.sql.SQLException;
import java.util.List;
import sdgcpp.logicaDeNegocio.clases.Acceso;
import sdgcpp.logicaDeNegocio.clases.Profesor;

public interface IProfesor {
    
    int registrarProfesorUV( Acceso acceso,  Profesor profesor)throws SQLException;
    public List<List<String>> obtenerListaDeCategoriaContratacion() ;
    public List<List<String>> obtenerListaDeTipoContratacion() ;   
    public boolean verificarExistenciaProfesor(String nombre, String apellidoPaterno, String apellidoMaterno) throws SQLException;
    public boolean verificarSiExisteElCorreo(String correo) throws SQLException ;
    public Profesor obtenerProfesorPorCorreo(String correo)throws SQLException;
    public Profesor obtenerProfesorPorID(String idProfesor) throws SQLException;
    

}
