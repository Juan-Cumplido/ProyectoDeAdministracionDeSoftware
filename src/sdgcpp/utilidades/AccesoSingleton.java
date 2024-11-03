package sdgcpp.utilidades;


public final class AccesoSingleton {
    private static AccesoSingleton instance;
    private int accesoId;

    private AccesoSingleton() {
        
    }

    public static AccesoSingleton getInstance() {
        if (instance == null) {
            instance = new AccesoSingleton();
        }
        return instance;
    }
    
    public void borrarInstancia() {
        accesoId = -1;
    }

    public int getAccesoId() {
        return accesoId;
    }

    public void setAccesoId(int accesoId) {
        this.accesoId = accesoId;
    }
}
