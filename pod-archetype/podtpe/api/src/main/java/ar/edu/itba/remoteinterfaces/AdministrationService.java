package ar.edu.itba.remoteinterfaces;

import ar.edu.itba.exceptions.InvalidStateException;
import ar.edu.itba.utils.ElectionState;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AdministrationService extends Remote {
    void open() throws RemoteException, InvalidStateException;
    ElectionState getCurrentSate() throws RemoteException;
    void close() throws RemoteException, InvalidStateException;
}
