package ar.edu.itba.FiscalClient;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.edu.itba.remoteinterfaces.FiscalService;
import ar.edu.itba.utils.Party;
import ar.edu.itba.exceptions.InvalidFiscalEnrollmentException;

public class FiscalClient {
	private static Logger logger = LoggerFactory.getLogger(FiscalClient.class);
	private static String serverAddressInput;
	private static Integer idInput;
	private static String partyInput;
	private static Party fiscalParty;
	private static ElectionFiscal fiscal;
	private static int port = 0;
	
	public static void main(String[] args) throws NotBoundException, NumberFormatException, IOException {
		logger.info("Fiscal client is starting");
		getSystemProperties();
		getParty();
		registerFiscal();
	}
	
	private static void getSystemProperties() {
    	serverAddressInput = System.getProperty("serverAddress");
    	idInput = Integer.valueOf(System.getProperty("id"));
    	partyInput = System.getProperty("party");
    }
	
	private static void getParty() {
		for(Party p: Party.values()) {
			if(p.name().equals(partyInput)) {
				fiscalParty = p;
			}
		}
	}
		
	public static void registerFiscal() {
		fiscal = new ElectionFiscal(fiscalParty, idInput);
		
        try {
            UnicastRemoteObject.exportObject(fiscal, port);
            String ip = "//" + serverAddressInput + "/" + "fiscal-service";
			final FiscalService handle = (FiscalService) 
						Naming.lookup(ip);
            handle.register(fiscal, idInput, fiscalParty);
        } catch (InvalidFiscalEnrollmentException
        		| RemoteException | MalformedURLException | NotBoundException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }

		System.out.println("Fiscal of " + fiscalParty.name() + " registered at polling place " + idInput +  ".");
	}
	
}
