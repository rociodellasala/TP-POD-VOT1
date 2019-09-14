package ar.edu.itba;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Detector extends Remote {
	
	void detect(Vote vote) throws RemoteException;
}
