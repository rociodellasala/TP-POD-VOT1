package ar.edu.itba.server.servant;

import java.rmi.RemoteException;
import java.util.Map;

import ar.edu.itba.exceptions.InvalidQueryMomentException;
import ar.edu.itba.remoteinterfaces.QueryService;
import ar.edu.itba.server.ElectionCentral;
import ar.edu.itba.server.VotingSystems;
import ar.edu.itba.utils.ElectionState;
import ar.edu.itba.utils.Party;
import ar.edu.itba.utils.Province;


public class QueryServiceImpl implements QueryService {
    private ElectionCentral central;
    private VotingSystems vt;
    private Map<Party, Integer> mapResult;

    public QueryServiceImpl(ElectionCentral central) {
    	this.central = central;
    	this.vt = new VotingSystems();
    }

	@Override
	public String percentageAtNationalLevel() throws RemoteException, InvalidQueryMomentException {
		double totalVotes = (double) central.getVotes().size();
		checkIfElectionsAreNotClosed();
		if (central.getState().equals(ElectionState.OPENED)) {
			mapResult = vt.FPTP(VotingSystems.totalVotes(central.getVotes()));
			return vt.resultString(mapResult);
		} else {
			mapResult = vt.AV(central.getVotes());
			return vt.resultStringAV(mapResult, totalVotes);
		}
	}

	@Override
	public String percentageAtProvincialLevel(Province province) throws RemoteException, InvalidQueryMomentException {
		checkIfElectionsAreNotClosed();
		if (central.getState().equals(ElectionState.OPENED)) {
			mapResult = VotingSystems.provinceVotes(central.getVotes(), province);
			return vt.resultString(mapResult);
		} else {
			Map<Party, Double> provinceMapResult = vt.STV(central.getVotes(), province);
			return vt.resultStringSTV(provinceMapResult);
		}
	}

	@Override
	public String percentageAtTableLevel(Integer id) throws RemoteException, InvalidQueryMomentException {
		checkIfElectionsAreNotClosed();
		mapResult = vt.FPTP(VotingSystems.tableVotes(central.getVotes(), id));
		return vt.resultString(mapResult);
}
	
	public void checkIfElectionsAreNotClosed() throws InvalidQueryMomentException {
		ElectionState state = central.getState();
		if(state.equals(ElectionState.CLOSED)) {
			throw new InvalidQueryMomentException("You cannot query before elections open");
		}
	}
   
}
