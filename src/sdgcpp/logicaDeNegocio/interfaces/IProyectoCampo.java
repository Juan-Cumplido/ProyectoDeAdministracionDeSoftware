package sdgcpp.logicaDeNegocio.interfaces;

import java.sql.SQLException;
import java.util.List;
import sdgcpp.logicaDeNegocio.clases.ProyectoCampo;
import sdgcpp.logicaDeNegocio.clases.TablaPC;

public interface IProyectoCampo {
    int registrarProyectoCampo(ProyectoCampo proyectoCampo, List<Integer> listaIdProfesores, List<Integer> listaIdEstudiantes)throws SQLException;
 public List<TablaPC> obtenerProyectosPorProfesorYPeriodo(int idProfesor, int idPeriodo) throws SQLException;
}
