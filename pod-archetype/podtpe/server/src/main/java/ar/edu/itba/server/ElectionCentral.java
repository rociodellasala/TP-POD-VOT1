package ar.edu.itba.server;

import java.rmi.RemoteException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import ar.edu.itba.Vote;
import ar.edu.itba.exceptions.InvalidVoteOperationException;
import ar.edu.itba.utils.ElectionState;

public class ElectionCentral {
	private ElectionState currentState;
	private List<Vote> voteList;
	private final List<FiscalMonitor> monitors;
	private ReentrantReadWriteLock lock;
	
	public ElectionCentral() {
		currentState = ElectionState.CLOSED;
		voteList = new ArrayList<>();
		monitors = new ArrayList<>();
		lock = new ReentrantReadWriteLock();
	}
	
	public ElectionState getState() {
		return this.currentState;
	}
	
	public List<Vote> getVotes() {
		return this.voteList;
	}

	public List<FiscalMonitor> getMonitors() {
		return this.monitors;
	}
	
	public void setState(ElectionState newState) {
		this.currentState = newState;
	}
	
	public ReentrantReadWriteLock getLock() {
		return this.lock;
	}

	public void addVotes(List<Vote> newVotes) throws RemoteException, InvalidVoteOperationException {
		if (currentState.equals(ElectionState.OPENED)) {
			for (Vote v: newVotes) {
				voteList.add(v);
				for (FiscalMonitor m: monitors) {
					m.notifyVote(v);
				}
			}
		} else {
			throw new InvalidVoteOperationException("Tried to vote but election wasn't open");
		}
	}

	
	public void addFiscals(FiscalMonitor m) {
		synchronized (monitors) {
            monitors.add(m);
		}
		
	}
	
	
	

}
