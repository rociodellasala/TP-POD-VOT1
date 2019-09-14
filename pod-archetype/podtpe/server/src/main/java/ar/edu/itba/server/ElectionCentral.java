package ar.edu.itba.server;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ar.edu.itba.Vote;
import ar.edu.itba.AdministrationService.ElectionState;
import ar.edu.itba.Detector;
import ar.edu.itba.Party;

public class ElectionCentral {
	
	private ElectionState state;
	private List<Vote> voteList;
	private final List<FiscalMonitor> monitors = new ArrayList<>();
	
	public ElectionCentral() {
		super();
		 voteList = new ArrayList<>();
		 state = ElectionState.NOT_STARTED;
	}
	
	public ElectionState getState() {
		return this.state;
	}
	
	public void setState(ElectionState newState) {
		this.state = newState;
	}

	public void addVotes(List<Vote> newVotes) throws RemoteException {
		
		if(state.equals(ElectionState.STARTED)) {
			for(Vote v: newVotes) {
				voteList.add(v);
				for(FiscalMonitor m: monitors) {
					m.notifyVote(v);
				}
			}
		} else {
			throw new IllegalStateException("Tried to vote but election wasn't open.");
		}
		
	}

	
	public void addFiscals(FiscalMonitor m) {
		synchronized (monitors) {
            monitors.add(m);
		}
		
	}
	
	
	

}
