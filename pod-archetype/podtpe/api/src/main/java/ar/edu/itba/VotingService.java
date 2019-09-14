package ar.edu.itba;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/* Servicio de VOTACIÓN para emitir votos */
public interface VotingService extends Remote{
	
	void vote(List<Vote> votes) throws RemoteException;

}
