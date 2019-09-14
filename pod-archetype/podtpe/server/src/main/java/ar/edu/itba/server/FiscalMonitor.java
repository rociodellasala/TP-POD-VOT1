package ar.edu.itba.server;

import java.rmi.RemoteException;
import java.util.List;

import ar.edu.itba.Vote;

public interface FiscalMonitor {
	
	void notifyVote(Vote vote) throws RemoteException;
	
}
