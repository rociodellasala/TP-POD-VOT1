package ar.edu.itba.VotingClient;

import java.io.BufferedReader;
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

import ar.edu.itba.Party;
import ar.edu.itba.Province;
import ar.edu.itba.Vote;
import ar.edu.itba.VotingService;


public class VotingClient {
	
    private static Logger logger = LoggerFactory.getLogger(VotingClient.class);

    public static void main(String[] args) throws NotBoundException, NumberFormatException, IOException {
        logger.info("tppod voting client Starting ...");

        /*
         * Falta hacer que reciba el path del CSV por parametro
         */
        final VotingService handle = (VotingService) 
        		Naming.lookup("//localhost:1099/administration-service");
        
        int numberOfVotes = 0;
        String pathToCsv = "";
        
        List<Vote> voteList = new ArrayList<>();
        
        /*
         * Parseo datos del CSV.
         */
        try (BufferedReader br = new BufferedReader(new FileReader(pathToCsv))) {
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
                int i = 0;
                while(v.getRanking().get(i) != null) {
                	System.out.println(i + ": " + v.getRanking().get(i).name());
                }
                System.out.println("---------------------");
            }
        }
        
        handle.vote(voteList);
        
        
        
        System.out.println(numberOfVotes + " votes registered.");
    }

}
