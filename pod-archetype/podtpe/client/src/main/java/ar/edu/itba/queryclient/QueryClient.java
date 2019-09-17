package ar.edu.itba.queryclient;

import java.io.FileWriter;
import java.io.IOException;
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
	private static Optional<String> optionalIdInput;
	private static Optional<String> optionalStateInput;
	private static String outPathInput = "./";
	private static Integer idInput;
	private static String stateInput;
	 private static String queryResults;
    
    public static void main(String[] args) throws RemoteException, 
    NotBoundException, MalformedURLException {
    	logger.info("Query client is starting");
        try {
			getSystemProperties();
			getResults();
		    writeOutputResults();
		} catch (InvalidQueryParametersException | IOException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}
    }
    
    private static void getSystemProperties() throws InvalidQueryParametersException {
    	serverAddressInput = System.getProperty("serverAddress");
    	optionalIdInput = Optional.ofNullable(System.getProperty("id"));
    	optionalStateInput = Optional.ofNullable(System.getProperty("state"));
    	StringBuilder str = new StringBuilder();
    	str.append(outPathInput);
    	str.append(System.getProperty("outPath"));
    	outPathInput = str.toString();
    	
    	if (optionalIdInput.isPresent() && optionalStateInput.isPresent()) {
    		throw new InvalidQueryParametersException("You cannot consult for id and state at the same time");
    	} else if (optionalIdInput.isPresent()) {
    		idInput = Integer.valueOf(optionalIdInput.get());
    	} else if (optionalStateInput.isPresent()) {
    		stateInput = optionalStateInput.get();
    	}
    }
    
    private static void getResults() {
    	try {
        	String ip = "//" + serverAddressInput + "/" + "query-service";
        	
			final QueryService handle = (QueryService) 
						Naming.lookup(ip);
			if (idInput == null && stateInput == null) {
				queryResults = handle.percentageAtNationalLevel();	
				System.out.println(queryResults);
				System.out.println(queryResults);
			} else if (stateInput != null) {
				Province province = Province.valueOf(stateInput);
				queryResults =  handle.percentageAtProvincialLevel(province);
				System.out.println(queryResults);
			} else {
				queryResults = handle.percentageAtTableLevel(idInput);
				System.out.println(queryResults);
			}
		} catch (MalformedURLException | NotBoundException | RemoteException | InvalidQueryMomentException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		} 
    }
    
    private static void writeOutputResults() throws IOException {
    	FileWriter fw = new FileWriter(outPathInput);
    	fw.write(queryResults);
    	fw.close();
    }
} 
