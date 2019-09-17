package ar.edu.itba.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.edu.itba.Vote;
import ar.edu.itba.utils.Party;
import ar.edu.itba.utils.Province;

public class VotingSystems {	
	public static final double AV_FLOOR = 50.0;
	public static final double STV_FLOOR = 45.0;
	private static Logger LOGGER = LoggerFactory.getLogger(Server.class);
	
	public String resultStringSTV(Map<Party, Double> sortedMap) {
		double total = 0.0;
		
		StringBuilder builder = new StringBuilder()
                .append("Porcentaje;Partido")
                .append("\r\n");
		
		Stream<Map.Entry<Party,Double>> sorted =
			    sortedMap.entrySet().stream()
			       .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()));
		
		Iterator<Entry<Party,Double>> it = sorted.iterator();
		
		while(it.hasNext()) {
			
			Entry<Party, Double> e = it.next();
			double percentage = e.getValue();
            builder.append(e.getKey())
            .append(";")
            .append(percentage)
            .append("%")
            .append("\r\n");
		}

		/*
		
		for (Party p: sortedMap.keySet()) {
    		double percentage = sortedMap.get(p);
            builder.append(p)
            .append(";")
            .append(percentage)
            .append("%")
            .append("\r\n");
		}
		*/
    
		return builder.toString();
	}
	
	/*
	 * Llamar esto con lo q devuelve FPTP.
	 */
	public String resultString(Map<Party, Integer> sortedMap) {
		double total = 0.0;
		
		for (Party p: sortedMap.keySet()) {
			total += sortedMap.get(p);
		}
		
		Stream<Map.Entry<Party,Integer>> sorted =
			    sortedMap.entrySet().stream()
			       .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()));
		
		Iterator<Entry<Party,Integer>> it = sorted.iterator();
		
		StringBuilder builder = new StringBuilder()
                .append("Porcentaje;Partido")
                .append("\r\n");
		
		while(it.hasNext()) {
			
			Entry<Party, Integer> e = it.next();
			double percentage = ((double) e.getValue()/(double) total) *100;
            builder.append(e.getKey())
            .append(";")
            .append(percentage)
            .append("%")
            .append("\r\n");
		}
		
		
		/*
		for (Party p: sortedMap.keySet()) {
    		double percentage = (sortedMap.get(p)/total) * 100;
            builder.append(p)
            .append(";")
            .append(percentage)
            .append("%")
            .append("\r\n");
		}*/
    
		return builder.toString();
	}

	/*
	 * Hay que pasarle el mapa que obtenemos de totalVotes, tableVotes o provinceVotes.
	 * Es el que usamos para comicios en curso en todos los ambitos.
	 * Tambien lo usamos para resultados finales por mesa. 
	 * Con el mapa que devuelve llamar a resultString.
	 */
	public Map<Party, Integer> FPTP (Map<Party, Integer> votes) {
		Map<Party, Integer> sortedMap = 
			     votes.entrySet().stream()
			    .sorted(Entry.comparingByValue())
			    .collect(Collectors.toMap(Entry::getKey, Entry::getValue,
			                              (e1, e2) -> e1, LinkedHashMap::new));
		
		return sortedMap;
	}
	
	
	
	
	private class VotingSet {
		
		List<Vote> firstChoiceVotes;
		List<Vote> secondChoiceVotes;
		List<Vote> thirdChoiceVotes;
		Party party;
		double percentage;
		int total;
		
		public VotingSet(Party party, int total) {
			super();
			this.firstChoiceVotes =  new ArrayList<>();
			this.secondChoiceVotes = new ArrayList<>();
			this.thirdChoiceVotes = new ArrayList<>();
			this.party = party;
			this.total = total;
		}
		
		public void setPercentage() {
			this.percentage = ((double) firstChoiceVotes.size() / (double) total)*100;
		}
		
		
	}
	
	/*
	 * Si estoy transfiriendo es porque tengo votos de mas. 
	 */
	public void transferVotes(Party from, VotingSet vs ,double percentageToTransfer, Map<Party, VotingSet> auxiMap, int total, boolean winner) {
		
		Map<Party, Integer> countMap = new HashMap<>();
		if(winner) {
			vs.percentage = 20.0;
		}	
		//int currentFromVotes = vs.firstChoiceVotes.size() + vs.secondChoiceVotes.size() + vs.thirdChoiceVotes.size();
		int counter = 0;
		for(Vote v: vs.firstChoiceVotes) {
			if(v.getRanking().size() > 1) {
				v.setCurrent(v.getCurrent()+1);
				Party newParty = v.getRanking().get(v.getCurrent());
				counter++;
				if(countMap.containsKey(newParty)) {
					countMap.put(newParty, countMap.get(newParty) + 1);
				} else {
					countMap.put(newParty, 1);
				}
				
				if(auxiMap.containsKey(newParty)) {
					auxiMap.get(newParty).secondChoiceVotes.add(v);
				} else {
					VotingSet newVs = new VotingSet(newParty, total);
					newVs.secondChoiceVotes.add(v);
					auxiMap.put(newParty, newVs);
				}
				
			}	
		}
		
		for(Vote v: vs.secondChoiceVotes) {
			if(v.getRanking().size() > 2) {
				v.setCurrent(v.getCurrent()+1);
				Party newParty = v.getRanking().get(v.getCurrent());
				if(countMap.containsKey(newParty)) {
					countMap.put(newParty, countMap.get(newParty) + 1);
				} else {
					countMap.put(newParty, 1);
				}
				counter++;
				//auxiMap.get(newParty).thirdChoiceVotes.add(v);
			}
		}
		
		
		for(Party p: countMap.keySet()) {
			LOGGER.info("---------------------------");
			LOGGER.info("Partido " + p.name());
			LOGGER.info("Porcentaje previo: " + auxiMap.get(p).percentage);
			LOGGER.info("esto es " + countMap.get(p));
			LOGGER.info("2esto es " + countMap.size());
			LOGGER.info("porc a transferir era  " + percentageToTransfer);
			auxiMap.get(p).percentage += ((double) countMap.get(p)/(double) counter)*percentageToTransfer;
			LOGGER.info("Porcentaje nuevo: " + auxiMap.get(p).percentage);
		}
		
		
	}
	
	public Map<Party, Double> STV(List<Vote> totalVotes, Province province) {
		
		/* 
		 * Total es mi cantidad total de votos
		 */
		int total = 0;
		List<Party> currentlyInRace = new ArrayList<>();
		Map<Party, VotingSet> auxiMap = new HashMap<>();
		
		
		for(Party p: Party.values()) {
			currentlyInRace.add(p);
		}
		
		/* Primero tomo los votos de mi provincia */
		List<Vote> votes = new ArrayList<>();
		for(Vote v: totalVotes) {
			if(v.getProvince().equals(province)) {
				v.setCurrent(0);
				votes.add(v);
				total++;
			}
		}
		
		
		
		Map<Party, Double> results = new HashMap<>();
		
		double STV_FLOOR = 100.0/5.0; //20%
		int totalWinners = 0;
		
		/*
		 * Resultados iniciales, en porcentaje
		 */
		for (Vote v: votes) {
			Party p = v.getRanking().get(0);
			if(!auxiMap.containsKey(p)) {
				VotingSet vs = new VotingSet(p, total);
				auxiMap.put(p, vs);
			}
			auxiMap.get(p).firstChoiceVotes.add(v);
			if (results.containsKey(p)) { 
				results.put(p, results.get(p) + ((double) 1/ (double) total)*100);
			} else {
				results.put(p, ((double) 1/ (double) total)*100);
			}
		}
		
		/*
		 * seteo los porcentajes iniciales en cada votingset
		 */
		for(Party p: auxiMap.keySet()) {
			auxiMap.get(p).setPercentage(); // lo llamo ahora pq ya pse todos los votos
		}
		
	
		
		//int votesToWin = (int)  ((STV_FLOOR*((double) total)) / 100.0);
		
		while(totalWinners != 5 && results.keySet().size() > 5) {
			
			boolean winnerFound = false;
			Party leastVoted = null;
			Party winner = null;
			
			for (Party p: results.keySet()) {
				if(currentlyInRace.contains(p)) {
					if (results.get(p) >= STV_FLOOR) {
						LOGGER.info("Encontrado un ganador es " + p.name());
						winnerFound = true;
						winner = p;
						totalWinners++;	
						currentlyInRace.remove(p); // lo saco de la carrera, pues ya gano
					}
					
					if (leastVoted == null) {
						leastVoted = p;
					} else {
						if (results.get(leastVoted) > results.get(p)) {
							leastVoted = p;
						}
					}
				}	
			}
			
			if(winnerFound) {
				VotingSet aux= auxiMap.get(winner);
				//if(aux.percentage - STV_FLOOR > 0.0) {
					LOGGER.info("Transfiriendo votos...");
					transferVotes(winner, aux, aux.percentage - STV_FLOOR, auxiMap, total, true);
					LOGGER.info("Votos transferidos!");
				//}
				results.put(winner, 20.0);
				/*
				 * Actualizo porcentajes
				 */
				
				for(Party p: auxiMap.keySet()) {
					if(currentlyInRace.contains(p)) {
						results.put(p, auxiMap.get(p).percentage);
						LOGGER.info("Porcentaje nuevo para " + p.name() + " es " + auxiMap.get(p).percentage);
					}				
				}
			}
			
			else {
				if(leastVoted != null) {
					//LOGGER.info("Menos votado fue " + leastVoted.name());
					LOGGER.info("LEAST VOTED ES " + leastVoted.name());
					VotingSet aux = auxiMap.get(leastVoted);
					transferVotes(leastVoted, aux, aux.percentage, auxiMap, total, false);
					results.remove(leastVoted);
					currentlyInRace.remove(leastVoted);
					for(Party p: auxiMap.keySet()) {
						if(currentlyInRace.contains(p)) {
							results.put(p, auxiMap.get(p).percentage);
						}				
					}

				}
			}
			LOGGER.info("Restuls size es " + results.keySet().size());
		}
		
		
		for(Vote v: votes) {
			v.setCurrent(0);
		}
		
		return results;
		
	}
	
	
	/*
	 * Aca no uso totalVotes ni nada de eso.
	 * Llamo directamente  a AV con todo el listado de votos.
	 * Desps con el mapa q devuelve llamar a resultString
	 */
	public Map<Party, Integer> AV(List<Vote> votes) {
		
		Map<Party, Integer> results = new HashMap<>();
		
		for (Vote v: votes) {
			v.setCurrent(0);
			Party p = v.getRanking().get(0);
			if (results.containsKey(p)) { 
				results.put(p, results.get(p) + 1);
			} else {
				results.put(p, 1);
			}
		}
		
		int total = 0;
		for (Party p: results.keySet()) {
			total = total + results.get(p);
		}
		
		
		boolean finished = false;
		int rankingPosition = 1; // empiezo en la 1 pq la 0 ya la tome antes
		
		while (finished != true /*&& rankingPosition <= 2*/) {
			Party leastVoted = null;
			
			for (Party p: results.keySet()) {
				if ((((double) results.get(p)/ (double) total)*100) >= AV_FLOOR) {
					LOGGER.info("Encontrado el ganador es " + p.name());
					double aux = ((results.get(p)/total)*100) ;
					finished = true;
				}
				
				if (leastVoted == null) {
					leastVoted = p;
				} else {
					if (results.get(leastVoted) > results.get(p)) {
						leastVoted = p;
					}
				}
				
			}
			
			LOGGER.info("MENOS VOTADO FUE :  " + leastVoted.name());

			if (finished != true) {
				// tomo el candidato de menor cantidad de votos
				//actualizo results
				for (Vote v: votes) {
					if(v.getRanking().get(v.getCurrent()).equals(leastVoted)) {
						
						v.setCurrent(v.getCurrent()+1);
						Party newParty = v.getRanking().get(v.getCurrent());
						if(results.containsKey(newParty)) {
							results.put(newParty, results.get(newParty) + 1);
						} else {
							results.put(newParty, 1);
						}
					}
				}
				results.remove(leastVoted);

			}
			
		}
		
		for(Vote v: votes) {
			v.setCurrent(0);
		}
		
		
		return results;
		
	}
	
	/*
	 * Llamar este metodo para comicios en curso a nivel nacional.
	 * Para comicios en curso a nivel provincial usar provinceVotes.
	 * Para resultados finales de mesa especifica usar el metodo tableVotes.
	 * Devuelve en un mapa la cantidad de votos para cada partido. 
	 */
	public static Map<Party, Integer> totalVotes (List<Vote> votes) {
		Map<Party, Integer> results = new HashMap<>();
		
		for (Vote v: votes) {
			Party p = v.getRanking().get(0);
			
			if (results.containsKey(p)) {
				results.put(p, results.get(p) + 1);
			} else {
				results.put(p, 1);
			}
		}
		
		return results;	
	}
	
	/*
	 * Recibe el listado de todos los votos. Se lo llama directo para cuando
	 * queremos resultados finales especificos de una mesa (tambien sirve para en curso).
	 * Para comicios en curso usar el metodo FPTP
	 * 
	 */
	public static Map<Party, Integer> tableVotes (List<Vote> votes, int tableId) {
		List<Vote> aux = new ArrayList<>();
		
		for (Vote v: votes) {
			if (v.getTableId().equals(tableId)) {
				aux.add(v);
			}
		}
		
		return totalVotes(aux);
	}
	
	/*
	 * Para comicios en curso a nivel provincial.
	 * 
	 */
	public static Map<Party, Integer> provinceVotes (List<Vote> votes, Province province) {
		List<Vote> aux = new ArrayList<>();
		
		for (Vote v: votes) {
			if (v.getProvince().equals(province)) {
				aux.add(v);
			}
		}
		
		return totalVotes(aux);		
	}
	
	
    
    
	
	
	
	
	
	
	
	
	
	
	
}