package ar.edu.itba.server;

import java.rmi.RemoteException;

import ar.edu.itba.Vote;

public interface FiscalMonitor {
	void notifyVote(Vote vote) throws RemoteException;
}
