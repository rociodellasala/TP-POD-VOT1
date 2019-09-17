package ar.edu.itba.remoteinterfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import ar.edu.itba.Vote;
import ar.edu.itba.exceptions.InvalidVoteOperationException;

public interface VotingService extends Remote {
	void vote(List<Vote> votes) throws RemoteException, InvalidVoteOperationException;
}
