package ar.edu.itba.server;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.edu.itba.Vote;
import ar.edu.itba.VotingService;

public class VotingServiceImpl implements VotingService{

	private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
	
	
	
	private ElectionCentral central;
	// tmbn falta agregarlo al server

	public VotingServiceImpl(ElectionCentral central) {
		
		this.central = central;
		
	}

	@Override
	public void vote(List<Vote> votes) throws RemoteException {
		
		central.addVotes(votes);
		LOGGER.info("Votes added.");
		
	}

	// desps probar leer de CSV posta y ver q funque. 
	
	
}
