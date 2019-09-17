package ar.edu.itba.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ar.edu.itba.Vote;
import ar.edu.itba.exceptions.InvalidVoteOperationException;
import ar.edu.itba.server.ElectionCentral;
import ar.edu.itba.server.FiscalMonitor;
import ar.edu.itba.utils.ElectionState;
import ar.edu.itba.utils.Party;
import ar.edu.itba.utils.Province;



public class ElectionCentralTest {
	
	private ElectionCentral central;
	
	@BeforeEach
	public void createCentral() {
		central = new ElectionCentral();
	}
	
	public List<Vote> createVotes() {
		List<Vote> votes = new ArrayList<Vote>();
		List<Party> list = new ArrayList<Party>();
		list.add(Party.MONKEY);
		Vote v1 = new Vote(12, Province.TUNDRA, list);
		votes.add(v1);
		
		return votes;
		
	} 
	
	@Test
	public void initialConditionsTest() {
		assertEquals(central.getState(), ElectionState.CLOSED);
		assertEquals(central.getVotes().size(),0);
		assertEquals(central.getMonitors().size(),0);
	}
	
	@Test
	public void addVotesElectionsClosedTest() {
		List<Vote> votes = createVotes();
		assertThrows(InvalidVoteOperationException.class, () -> central.addVotes(votes));
	}
	
	@Test
	public void addVotesElectionsOpenTest() {
		central.setState(ElectionState.OPENED);
		List<Vote> votes = createVotes();
		try {
			central.addVotes(votes);
		} catch(Exception e) {
			e.printStackTrace();
		}
		assertNotEquals(central.getVotes().size(),0);
		assertEquals(central.getVotes().size(),1);
		List<Vote> moreVotes = createVotes();
		try {
			central.addVotes(moreVotes);
		} catch(Exception e) {
			e.printStackTrace();
		}
		assertEquals(central.getVotes().size(),2);
	}
	
	@Test
	public void numberOfFiscalsTest() {
		assertEquals(central.getMonitors().size(),0);
		central.addFiscals(new FiscalMonitor() {
			@Override
			public void notifyVote(Vote vote) throws RemoteException {
				
			}
		});
		assertEquals(central.getMonitors().size(),1);
		central.addFiscals(new FiscalMonitor() {		
			@Override
			public void notifyVote(Vote vote) throws RemoteException {
				
			}
		});
		assertEquals(central.getMonitors().size(),2);
	}
	
}