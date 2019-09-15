package ar.edu.itba.VotingClient;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
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

    public static void main(String[] args) throws NotBoundException, NumberFormatException, IOException {
        logger.info("tppod voting client Starting ...");

        /*
         * Falta hacer que reciba el path del CSV por parametro
         */
        
//        String pathToCsv = System.getProperty("votesPath");
//        String ip = System.getProperty("serverAddress");
        
        
        final VotingService handle = (VotingService) Naming.lookup("//localhost:1099/voting-service"); // desps cambiarlo con el ip q recibimos
        
        
        	
        int numberOfVotes = 0;
        String pathToCsv = "test1.csv";
        
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
        
        try {
			handle.vote(voteList);
		} catch (InvalidVoteOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        
        System.out.println(numberOfVotes + " votes registered.");
    }

}
