package ar.edu.itba.remoteinterfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

import ar.edu.itba.Detector;
import ar.edu.itba.Vote;
import ar.edu.itba.exceptions.InvalidFiscalEnrollmentException;
import ar.edu.itba.utils.Party;

public interface FiscalService extends Remote {
	void register(Detector fiscal, int tableId, Party party) throws RemoteException, InvalidFiscalEnrollmentException;
	void fiscalize(Vote vote, Party party) throws RemoteException;
}
