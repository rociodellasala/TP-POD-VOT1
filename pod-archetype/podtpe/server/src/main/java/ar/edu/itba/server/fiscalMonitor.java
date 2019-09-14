package ar.edu.itba.server;

import java.util.List;

import ar.edu.itba.Vote;

public interface fiscalMonitor {
	
	void newVotes(List<Vote> vote);
	void newVote(Vote vote);
	
}
