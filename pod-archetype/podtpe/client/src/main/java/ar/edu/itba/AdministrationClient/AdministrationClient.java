package ar.edu.itba.AdministrationClient;

import ar.edu.itba.Action;
import ar.edu.itba.exceptions.InvalidStateException;
import ar.edu.itba.remoteinterfaces.AdministrationService;
import ar.edu.itba.utils.ElectionState;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class AdministrationClient {
    private static Logger logger = LoggerFactory.getLogger(AdministrationClient.class);
	private static String serverAddressInput;
	private static Action actionNameInput;
	    
    public static void main(String[] args) throws RemoteException, 
    NotBoundException, MalformedURLException, InvalidStateException {
    	logger.info("Administration client is starting");
        getSystemProperties();
        getActionDone();
    }
    
    private static void getSystemProperties() {
    	serverAddressInput = System.getProperty("serverAddress");
    	actionNameInput = Action.valueOf(System.getProperty("action"));
    }
    
    private static void getActionDone() throws InvalidStateException {
    	try {
        	String ip = "//" + serverAddressInput + "/" + "administration-service";
			final AdministrationService handle = (AdministrationService) 
						Naming.lookup(ip);
			switch(actionNameInput) {
				case open:
					handle.open();
					System.out.println("[OK]: \tThe elections are open");
					break;
				case state:
					ElectionState state = handle.getCurrentSate();
					System.out.printf("[OK]: \tThe election current state is: %s\n", state.name().toLowerCase());
					break;
				case close:
					handle.close();
					System.out.printf("[OK]: \tThe elections have finished\n");
					break;
				default:
					System.out.printf("[ERROR]: \tThe action is not valid. "
							+ "Try -Daction=open/state/close\n");
					break;
				}
		} catch (MalformedURLException | NotBoundException | RemoteException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		} 
    }
    
} 