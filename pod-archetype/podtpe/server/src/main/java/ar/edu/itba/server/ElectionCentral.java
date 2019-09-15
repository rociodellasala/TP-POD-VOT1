package ar.edu.itba.server;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.Vote;
import ar.edu.itba.exceptions.InvalidVoteOperationException;
import ar.edu.itba.utils.ElectionState;

public class ElectionCentral {
	private ElectionState currentState;
	private List<Vote> voteList;
	private final List<FiscalMonitor> monitors;
	
	public ElectionCentral() {
		currentState = ElectionState.CLOSED;
		voteList = new ArrayList<>();
		monitors = new ArrayList<>();
	}
	
	public ElectionState getState() {
		return this.currentState;
	}
	
	public void setState(ElectionState newState) {
		this.currentState = newState;
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
