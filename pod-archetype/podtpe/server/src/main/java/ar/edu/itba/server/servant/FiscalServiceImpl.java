package ar.edu.itba.server.servant;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.edu.itba.Detector;
import ar.edu.itba.remoteinterfaces.FiscalService;
import ar.edu.itba.utils.Party;
import ar.edu.itba.Vote;
import ar.edu.itba.exceptions.InvalidFiscalEnrollmentException;
import ar.edu.itba.server.ElectionCentral;
import ar.edu.itba.server.FiscalMonitor;
import ar.edu.itba.server.Server;

public class FiscalServiceImpl implements FiscalService, FiscalMonitor {
	private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
	private ElectionCentral central;
	private Map<Party, Map<Integer, Detector>> registeredFiscals;

	public FiscalServiceImpl(ElectionCentral central) {		
		this.central 		= central;
		registeredFiscals 	= new ConcurrentHashMap<>();
		
		for (Party p: Party.values()) {
			registeredFiscals.put(p, new HashMap<>());
		}
		
		central.addFiscals(this);
	}


	@Override
	public void register(Detector fiscal, int tableId, Party party) throws RemoteException,
	InvalidFiscalEnrollmentException {
		switch (central.getState()) {
	        case CLOSED:
	            LOGGER.info("Fiscal registered || Table: " + tableId + " || Party: " + party.name());
	            Map<Integer, Detector> fiscalsForParty = registeredFiscals.get(party);
	            if (fiscalsForParty.putIfAbsent(tableId, fiscal) != null) {
	            	throw new InvalidFiscalEnrollmentException("Tried to register the same fiscal twice");
	            }
	            break;
	        case FINISHED:
	        	throw new InvalidFiscalEnrollmentException("Tried to fiscalize a finished election");
	        case OPENED:
	            throw new InvalidFiscalEnrollmentException("Tried to fiscalize an ongoing election");
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
		Map<Integer, Detector> fiscalsForParty = registeredFiscals.get(party);
		
		if (fiscalsForParty.containsKey(vote.getTableId())) {
			fiscalsForParty.get(vote.getTableId()).detect(vote);
		}
	}
	
}
