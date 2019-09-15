package ar.edu.itba.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import ar.edu.itba.Vote;
import ar.edu.itba.utils.Party;
import ar.edu.itba.utils.Province;

public class VotingSystems {	
	public static final double AV_FLOOR = 45.0;
	public static final double STV_FLOOR = 45.0;

	// Falta hacer AV y STV!
	
	/*
	 * Llamar esto con lo q devuelve FPTP.
	 */
	public String resultString(Map<Party, Integer> sortedMap) {
		int total = 0;
		for (Party p: sortedMap.keySet()) {
			total = total + sortedMap.get(p);
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
		Map<Party, Integer> results = new HashMap<>();
		
		for (Vote v: votes) {
			Party p = v.getRanking().get(0);
			if (p != null) {
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
		
		while (finished != true && rankingPosition <= 5) {
			Party leastVoted = null;
			
			for (Party p: results.keySet()) {
				if ((results.get(p)/total) >= AV_FLOOR) {
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
			
			if (finished != true) {
				// tomo el candidato de menor cantidad de votos
				//actualizo results
				for (Vote v: votes) {
					if (v.getRanking().get(rankingPosition - 1).equals(leastVoted)) {
						results.put(leastVoted, results.get(leastVoted) - 1);
						Party newParty = v.getRanking().get(rankingPosition - 1);
						results.put(newParty, results.get(newParty) + 1);
					}
				}

			}
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
			if (p != null) {
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