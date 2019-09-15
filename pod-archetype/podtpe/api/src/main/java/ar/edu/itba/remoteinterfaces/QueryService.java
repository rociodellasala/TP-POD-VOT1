package ar.edu.itba.remoteinterfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

import ar.edu.itba.exceptions.InvalidQueryMomentException;
import ar.edu.itba.utils.Province;

public interface QueryService extends Remote {
	void percentageAtNationalLevel() throws RemoteException, InvalidQueryMomentException;
	void percentageAtProvincialLevel(Province province) throws RemoteException, InvalidQueryMomentException;
	void percentageAtTableLevel(Integer id) throws RemoteException, InvalidQueryMomentException;
}
