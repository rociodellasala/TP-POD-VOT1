package ar.edu.itba.QueryClient;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.edu.itba.exceptions.InvalidQueryMomentException;
import ar.edu.itba.exceptions.InvalidQueryParametersException;
import ar.edu.itba.remoteinterfaces.QueryService;
import ar.edu.itba.utils.Province;


public class QueryClient {
	private static Logger logger = LoggerFactory.getLogger(QueryClient.class);
	private static String serverAddressInput;
	private static Optional<Integer> optionalIdInput;
	private static Optional<String> optionalStateInput;
	private static String outPathInput;
	private static Integer idInput;
	private static String stateInput;
    
    public static void main(String[] args) throws RemoteException, 
    NotBoundException, MalformedURLException {
    	logger.info("Query client is starting");
        try {
			getSystemProperties();
		} catch (InvalidQueryParametersException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}
        getResults();
    }
    
    private static void getSystemProperties() throws InvalidQueryParametersException {
    	serverAddressInput = System.getProperty("serverAddress");
    	optionalIdInput = Optional.ofNullable(Integer.valueOf(System.getProperty("id")));
    	optionalStateInput = Optional.ofNullable(System.getProperty("state"));
    	outPathInput = System.getProperty("outPath");
    	
    	if (optionalIdInput.isPresent() && optionalStateInput.isPresent()) {
    		throw new InvalidQueryParametersException("You cannot consult for id and state at the same time");
    	} else if (optionalIdInput.isPresent()) {
    		idInput = optionalIdInput.get();
    	} else {
    		stateInput = optionalStateInput.get();
    	}
    }
    
    private static void getResults() {
    	try {
        	String ip = "//" + serverAddressInput + "/" + "query-service";
			final QueryService handle = (QueryService) 
						Naming.lookup(ip);
			if (idInput == null && stateInput == null) {
				handle.percentageAtNationalLevel();
			} else if (stateInput != null) {
				Province province = Province.valueOf(stateInput);
				handle.percentageAtProvincialLevel(province);
			} else {
				handle.percentageAtTableLevel(idInput);
			}
		} catch (MalformedURLException | NotBoundException | RemoteException | InvalidQueryMomentException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		} 
    }
    
} 
