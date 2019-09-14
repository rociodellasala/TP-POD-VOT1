package ar.edu.itba.server;

import java.rmi.RemoteException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.edu.itba.FiscalService;
import ar.edu.itba.Party;
import ar.edu.itba.Vote;

public class FiscalServiceImpl implements FiscalService {

	private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
	
	
	
	private ElectionCentral central;


	public FiscalServiceImpl(ElectionCentral central) {
		
		this.central = central;
		
	}

	@Override
	public void register(int tableId, Party party) throws RemoteException {
		central.addFiscal(tableId, party);
		LOGGER.info("Fiscal registered for table ID " + tableId + " and party " + party + ".");
		
	}


	@Override
	public void fiscalize() throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	
	

	
}
