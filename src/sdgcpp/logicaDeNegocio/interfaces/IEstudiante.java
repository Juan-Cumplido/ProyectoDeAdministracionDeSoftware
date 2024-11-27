package sdgcpp.logicaDeNegocio.interfaces;

import java.sql.SQLException;
import sdgcpp.logicaDeNegocio.clases.Estudiante;

public interface IEstudiante{

    public Estudiante obtenerEstudiantePorMatricula(String matricula)throws SQLException;

}
