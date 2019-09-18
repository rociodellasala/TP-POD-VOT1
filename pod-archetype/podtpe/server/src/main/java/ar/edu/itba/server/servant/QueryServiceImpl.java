package ar.edu.itba.server.servant;

import java.rmi.RemoteException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.edu.itba.exceptions.InvalidQueryMomentException;
import ar.edu.itba.remoteinterfaces.QueryService;
import ar.edu.itba.server.ElectionCentral;
import ar.edu.itba.server.Server;
import ar.edu.itba.server.VotingSystems;
import ar.edu.itba.utils.ElectionState;
import ar.edu.itba.utils.Party;
import ar.edu.itba.utils.Province;


public class QueryServiceImpl implements QueryService {
	private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
    private ElectionCentral central;
    private VotingSystems vt;
    private Map<Party, Integer> mapResult;

    public QueryServiceImpl(ElectionCentral central) {
    	this.central = central;
    	this.vt = new VotingSystems();
    }

	@Override
	public String percentageAtNationalLevel() throws RemoteException, InvalidQueryMomentException {
		checkIfElectionsAreNotClosed();
		central.getLock().readLock().lock();
		try {
			double totalVotes = (double) central.getVotes().size();
			if (central.getState().equals(ElectionState.OPENED)) {
				LOGGER.info("Query solved using FPTP system (National level)");
				mapResult = vt.FPTP(VotingSystems.totalVotes(central.getVotes()));
				return vt.resultString(mapResult);
			} else {
				LOGGER.info("Query solved using AV system (National level)");
				mapResult = vt.AV(central.getVotes());
				return vt.resultStringAV(mapResult, totalVotes);
			}
		}finally {
			central.getLock().readLock().unlock();
		}
	}

	@Override
	public String percentageAtProvincialLevel(Province province) throws RemoteException, InvalidQueryMomentException {
		checkIfElectionsAreNotClosed();
		central.getLock().readLock().lock();
		try {
			if (central.getState().equals(ElectionState.OPENED)) {
				LOGGER.info("Query solved using FPTP system (Provinicial level)");
				mapResult = VotingSystems.provinceVotes(central.getVotes(), province);
				return vt.resultString(mapResult);
			} else {
				LOGGER.info("Query solved using STV system (Provinicial level)");
				Map<Party, Double> provinceMapResult = vt.STV(central.getVotes(), province);
				return vt.resultStringSTV(provinceMapResult);
			}
		}finally {
			central.getLock().readLock().unlock();
		}
	}

	@Override
	public String percentageAtTableLevel(Integer id) throws RemoteException, InvalidQueryMomentException {
		checkIfElectionsAreNotClosed();
		central.getLock().readLock().lock();
		try {
			LOGGER.info("Query solved using FPTP system (for a specific table)");
			mapResult = vt.FPTP(VotingSystems.tableVotes(central.getVotes(), id));
		}finally {
			central.getLock().readLock().unlock();
		}
		return vt.resultString(mapResult);
}
	
	public void checkIfElectionsAreNotClosed() throws InvalidQueryMomentException {
		ElectionState state = central.getState();
		if(state.equals(ElectionState.CLOSED)) {
			throw new InvalidQueryMomentException("You cannot query before elections open");
		}
	}
   
}
