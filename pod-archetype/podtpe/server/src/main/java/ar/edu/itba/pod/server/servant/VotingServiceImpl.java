package ar.edu.itba.server.servant;

import java.rmi.RemoteException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.edu.itba.Vote;
import ar.edu.itba.exceptions.InvalidVoteOperationException;
import ar.edu.itba.remoteinterfaces.VotingService;
import ar.edu.itba.server.ElectionCentral;
import ar.edu.itba.server.Server;

public class VotingServiceImpl implements VotingService {
	private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
	private ElectionCentral central;

	public VotingServiceImpl(ElectionCentral central) {
		this.central = central;
	}

	@Override
	public void vote(List<Vote> votes) throws RemoteException, InvalidVoteOperationException {
		central.getLock().writeLock().lock();
		try {
			central.addVotes(votes);
		}finally {
			central.getLock().writeLock().unlock();
		}
		
		LOGGER.info("Votes added");
	}
}

