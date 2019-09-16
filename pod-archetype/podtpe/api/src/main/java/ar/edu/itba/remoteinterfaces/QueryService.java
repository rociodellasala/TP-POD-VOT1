package ar.edu.itba.remoteinterfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

import ar.edu.itba.exceptions.InvalidQueryMomentException;
import ar.edu.itba.utils.Province;

public interface QueryService extends Remote {
	String percentageAtNationalLevel() throws RemoteException, InvalidQueryMomentException;
	String percentageAtProvincialLevel(Province province) throws RemoteException, InvalidQueryMomentException;
	String percentageAtTableLevel(Integer id) throws RemoteException, InvalidQueryMomentException;
}
