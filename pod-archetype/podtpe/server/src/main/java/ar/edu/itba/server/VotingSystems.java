package ar.edu.itba.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.edu.itba.Vote;
import ar.edu.itba.utils.Party;
import ar.edu.itba.utils.Province;

public class VotingSystems {	
	public static final double AV_FLOOR = 50.0;
	public static final double STV_FLOOR = 45.0;
	private static Logger LOGGER = LoggerFactory.getLogger(Server.class);
	
	// Falta hacer AV y STV!
	
	/*
	 * Llamar esto con lo q devuelve FPTP.
	 */
	public String resultString(Map<Party, Integer> sortedMap) {
		double total = 0.0;
		
		for (Party p: sortedMap.keySet()) {
			total += sortedMap.get(p);
		}
		
		StringBuilder builder = new StringBuilder()
                .append("Porcentaje;Partido")
                .append("\r\n");
		
		for (Party p: sortedMap.keySet()) {
    		double percentage = (sortedMap.get(p)/total) * 100;
            builder.append(p)
            .append(";")
            .append(percentage)
            .append("%")
            .append("\r\n");
		}
    
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
	
	/*
	 * Aca no uso totalVotes ni nada de eso.
	 * Llamo directamente  a AV con todo el listado de votos.
	 * Desps con el mapa q devuelve llamar a resultString
	 */
	public Map<Party, Integer> AV(List<Vote> votes) {
		
		List<Party> removedParties = new ArrayList<>();
		Map<Party, Integer> results = new HashMap<>();
		
		for (Vote v: votes) {
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
					//LOGGER.info("Porcentaje fue " + aux);
					finished = true;
				}
				
				if (leastVoted == null) {
					leastVoted = p;
				} else {
					if (results.get(leastVoted) > results.get(p)) {
						leastVoted = p;
					}
				}
				
				LOGGER.info("No ganador:  " + p.name());
				double aux = (((double) results.get(p)/ (double) total)*100) ;
				LOGGER.info("Porcentaje fue " + aux);
				LOGGER.info("-------------------");
			}
			
			LOGGER.info("MENOS VOTADO FUE :  " + leastVoted.name());
			removedParties.add(leastVoted);

			if (finished != true) {
				// tomo el candidato de menor cantidad de votos
				//actualizo results
				/*
				 * Hay un problema con la logica.
				 * Cuando voy por el segundo menos votad o (leopard)
				 * Tengo que tomar desde la segunda opcion (1) de ese y eso no estaria ahceindolo
				 * tnego q hcer tmbn q cuando eliminan un voto q ya le elminaron la primea opicon, que tome la tercera
				 */
				for (Vote v: votes) {
					int k = 0;
					boolean cut = false;
					while(k < v.getRanking().size() && cut != true) {
						LOGGER.info("2CUT ES " + cut);;
						boolean found = false;
						boolean doNotSearch = false;
						int auxi = 0;
						while(auxi < rankingPosition) {
							if(v.getRanking().size() > auxi && v.getRanking().get(auxi).equals(leastVoted)) {
								found = true;
							}
							if(v.getRanking().size() > auxi && !removedParties.contains(v.getRanking().get(auxi))) {
								doNotSearch = true;
							}
							auxi++;
						}
						
						if(doNotSearch && found && !removedParties.contains(v.getRanking().get(k))) {
							Party newParty = v.getRanking().get(k);
							if(results.containsKey(newParty)) {
								results.put(newParty, results.get(newParty) + 1);
								LOGGER.info("Sumando voto a " + newParty.name());
							} else {
								results.put(newParty, 1);
								LOGGER.info("Sumando voto a " + newParty.name());
							}
							cut = true;
							LOGGER.info("CUT ES " + cut);;
						}
						k++;
					}
					/*if (v.getRanking().size() > rankingPosition &&  v.getRanking().get(rankingPosition - 1).equals(leastVoted)) {
						results.put(leastVoted, results.get(leastVoted) - 1);
						
						Party newParty = v.getRanking().get(rankingPosition);
						if(results.containsKey(newParty)) {
							results.put(newParty, results.get(newParty) + 1);
						} else {
							results.put(newParty, 1);
						}
						
					}*/
				}
				LOGGER.info("******************");
				results.remove(leastVoted);

			}
			
			
			rankingPosition++;
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