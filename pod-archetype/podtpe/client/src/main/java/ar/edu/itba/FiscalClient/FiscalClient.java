package ar.edu.itba.FiscalClient;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.server.UnicastRemoteObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.edu.itba.FiscalService;
import ar.edu.itba.Party;
import ar.edu.itba.VotingService;
import ar.edu.itba.VotingClient.VotingClient;

public class FiscalClient {

	private static Logger logger = LoggerFactory.getLogger(VotingClient.class);
	
	public static void main(String[] args) throws NotBoundException, NumberFormatException, IOException {
		
		logger.info("tppod fiscal client Starting ...");
		
//      String tableId = Integer.parseInt(System.getProperty(""));
//      String ip = System.getProperty("serverAddress");
//		String partyString = System.getProperty("");
		String partyString = "TIGER";
		Party fiscalParty = null;
		
		for(Party p: Party.values()) {
			if(p.name().equals(partyString)) {
				fiscalParty = p;
			}
		}
		
		int tableId = 1000;
		
		
		ElectionFiscal fiscal = new ElectionFiscal(fiscalParty, tableId);
		
        try {
            UnicastRemoteObject.exportObject(fiscal,0);
            final FiscalService handle = (FiscalService) Naming.lookup("//localhost:1099/fiscal-service"); // desps cambiarlo con el ip q recibimos
            handle.register(fiscal, tableId, fiscalParty);
        }catch (Exception e){
            System.err.println(e.getMessage());
            System.exit(-1);
        }


		
		System.out.println("Fiscal of " + fiscalParty.name() + " registered at polling place " + tableId +  ".");
		
	}
	
	
	
}
