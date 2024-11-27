package sdgcpp.logicaDeNegocio.interfaces;

import java.sql.SQLException;
import java.util.List;
import sdgcpp.logicaDeNegocio.clases.EE;


public interface IEE {
    int registrarEE(EE ee)throws SQLException;
    public List<EE> obtenerEEPorProfesorYPeriodo(int idProfesor, int idPeriodo) throws SQLException;

}
