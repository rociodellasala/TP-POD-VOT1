package ar.edu.itba;

import java.rmi.Remote;
import java.rmi.RemoteException;

/* Servicio de ADMINISTRACIÓN, apertura y cierre de comicios */
public interface AdministrationService extends Remote {
	enum ElectionState {
        NOT_STARTED,
        STARTED,
        FINISHED
    }

    void open() throws RemoteException;
    ElectionState getCurrentSate() throws RemoteException;
    void close() throws RemoteException;
}
