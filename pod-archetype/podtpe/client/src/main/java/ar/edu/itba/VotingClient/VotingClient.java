package ar.edu.itba.VotingClient;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.edu.itba.utils.Party;
import ar.edu.itba.utils.Province;
import ar.edu.itba.Vote;
import ar.edu.itba.exceptions.InvalidVoteOperationException;
import ar.edu.itba.remoteinterfaces.VotingService;


public class VotingClient {
    private static Logger logger = LoggerFactory.getLogger(VotingClient.class);
    private static String serverAddressInput;
	private static String votesPathInput;
	private static List<Vote> voteList;
	    
    public static void main(String[] args) throws NotBoundException, NumberFormatException, IOException {
        logger.info("Voting client is starting ...");
        getSystemProperties();
        int number = readVotes();
        vote(number);
    }
    
    private static void getSystemProperties() {
    	serverAddressInput = System.getProperty("serverAddress");
    	votesPathInput = System.getProperty("votesPath");
    }
        
  	private static int readVotes() throws FileNotFoundException, IOException {
        int numberOfVotes = 0;
        
        voteList = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(votesPathInput))) {
            String line;
            while ((line = br.readLine()) != null) {
            	numberOfVotes++;
                String[] values = line.split(";");
                Province provinceVote = null;
                for(Province p: Province.values()) {
                	if(values[1].equals(p.name())) {
                		provinceVote = p;
                	}
                }
                
                String[] ranking = values[2].split(",");
                List<Party> votingRanking = new ArrayList<>();
                for(String s: ranking) {
                	Party partyVote = null;
                	for(Party p: Party.values()) {
                    	if(s.equals(p.name())) {
                    		partyVote = p;
                    	}
                    }
                	
                	votingRanking.add(partyVote);
                }
                
                Vote v = new Vote(Integer.parseInt(values[0]), provinceVote, votingRanking);
                voteList.add(v);
                
                System.out.println("Registered vote was: ");   
                System.out.println("Table: " + v.getTableId());
                System.out.println("Province: " + v.getProvince().name());
                int i = 0;
                while(i < v.getRanking().size()) {
                	System.out.println(i + ": " + v.getRanking().get(i).name());
                	i++;
                }
                System.out.println("---------------------");
            }
        }
        
        return numberOfVotes;
  	}
  	
  	private static void vote(int numberOfVotes) {
  		try {
        	String ip = "//" + serverAddressInput + "/" + "voting-service";
			final VotingService handle = (VotingService) Naming.lookup(ip);
			handle.vote(voteList);
		} catch (InvalidVoteOperationException | MalformedURLException | RemoteException | NotBoundException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}
       
        System.out.println(numberOfVotes + " votes registered.");
    }

}
