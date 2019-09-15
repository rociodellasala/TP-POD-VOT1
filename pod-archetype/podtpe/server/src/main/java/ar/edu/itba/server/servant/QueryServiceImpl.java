package ar.edu.itba.server.servant;

import java.rmi.RemoteException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.edu.itba.exceptions.InvalidQueryMomentException;
import ar.edu.itba.remoteinterfaces.QueryService;
import ar.edu.itba.server.ElectionCentral;
import ar.edu.itba.server.Server;
import ar.edu.itba.server.VotingSystems;
import ar.edu.itba.utils.ElectionState;
import ar.edu.itba.utils.Province;


public class QueryServiceImpl implements QueryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
    private ElectionCentral central;
    private VotingSystems vt;

    public QueryServiceImpl(ElectionCentral central) {
    	this.central = central;
    	this.vt = new VotingSystems();
    }

	@Override
	public void percentageAtNationalLevel() throws RemoteException, InvalidQueryMomentException {
		checkIfElectionsAreNotClosed();
		if (central.getState().equals(ElectionState.OPENED)) {
			vt.FPTP(vt.totalVotes(central.getVotes()));
		} else {
			vt.AV(central.getVotes());
		}
	}

	@Override
	public void percentageAtProvincialLevel(Province province) throws RemoteException, InvalidQueryMomentException {
		checkIfElectionsAreNotClosed();
		if (central.getState().equals(ElectionState.OPENED)) {
			vt.provinceVotes(central.getVotes(), province);
		} else {
			//TODO:STV
		}
	}

	@Override
	public void percentageAtTableLevel(Integer id) throws RemoteException, InvalidQueryMomentException {
		checkIfElectionsAreNotClosed();
		vt.FPTP(vt.tableVotes(central.getVotes(), id));
}
	
	public void checkIfElectionsAreNotClosed() throws InvalidQueryMomentException {
		ElectionState state = central.getState();
		if(state.equals(ElectionState.CLOSED)) {
			throw new InvalidQueryMomentException("You cannot query before elections open");
		}
	}
   
}
