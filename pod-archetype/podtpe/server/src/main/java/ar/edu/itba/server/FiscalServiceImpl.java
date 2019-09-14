package ar.edu.itba.server;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.edu.itba.Detector;
import ar.edu.itba.FiscalService;
import ar.edu.itba.Party;
import ar.edu.itba.Vote;

public class FiscalServiceImpl implements FiscalService, FiscalMonitor {

	private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
	
	
	
	private ElectionCentral central;
	private Map<Party, Map<Integer, List<Detector>>> registeredFiscals;


	public FiscalServiceImpl(ElectionCentral central) {		
		this.central = central;
		registeredFiscals = new  HashMap<>();
		
		for(Party p: Party.values()) {
			registeredFiscals.put(p, new HashMap<>());
		}
		
		central.addFiscals(this);
	}


	@Override
	public void register(Detector fiscal, int tableId, Party party) throws RemoteException {
		
		LOGGER.info("ESTADO ES " + central.getState().name());
		
		switch (central.getState()) {
			
	        case NOT_STARTED:
	            LOGGER.info("Fiscal registered || Table: " + tableId + " || Party: " + party.name() + ".");
	            Map<Integer, List<Detector>> fiscalsForParty = registeredFiscals.get(party);
	            fiscalsForParty.putIfAbsent(tableId, new ArrayList<>());
	            fiscalsForParty.get(tableId).add(fiscal);
	            break;
	        case FINISHED:
	        	throw new IllegalStateException("Tried to fiscalize a finished election.");
	        case STARTED:
	            throw new IllegalStateException("Tried to fiscalize an ongoing election.");
			}
		
	}


	@Override
	public void notifyVote(Vote vote) throws RemoteException {
		for(Party p: vote.getRanking()) {
			fiscalize(vote, p);
		}
		
	}
	
	@Override
	public void fiscalize(Vote vote, Party party) throws RemoteException {

		Map<Integer, List<Detector>> fiscalsForParty = registeredFiscals.get(party);
		
		if(fiscalsForParty.containsKey(vote.getTableId())) {
			for(Detector d: fiscalsForParty.get(vote.getTableId())) {
				d.detect(vote);
			}
		}
		
	}
	

	
}
