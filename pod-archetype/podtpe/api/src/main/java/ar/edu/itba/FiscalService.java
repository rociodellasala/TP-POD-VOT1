package ar.edu.itba;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface FiscalService extends Remote{
	
	void register(Detector fiscal, int tableId, Party party) throws RemoteException;
	void fiscalize() throws RemoteException;

}
