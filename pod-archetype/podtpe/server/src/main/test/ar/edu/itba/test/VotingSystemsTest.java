package ar.edu.itba.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ar.edu.itba.Vote;
import ar.edu.itba.server.VotingSystems;
import ar.edu.itba.utils.Party;
import ar.edu.itba.utils.Province;

public class VotingSystemsTest {
		
	private VotingSystems vs;
	
	@Before
	public void before() {
		vs = new VotingSystems();
	}
	
	public List<Vote> createVotes() {
		List<Vote> votes = new ArrayList<Vote>();
		
		List<Party> l1 = new ArrayList<Party>();
		l1.add(Party.TIGER);
		List<Party> l2 = new ArrayList<Party>();
		l2.add(Party.TIGER);
		l2.add(Party.GORILLA);
		List<Party> l3 = new ArrayList<Party>();
		l3.add(Party.GORILLA);
		List<Party> l4 = new ArrayList<Party>();
		l4.add(Party.GORILLA);
		l4.add(Party.LYNX);
		List<Party> l5 = new ArrayList<Party>();
		l5.add(Party.LYNX);
		List<Party> l6 = new ArrayList<Party>();
		l6.add(Party.SNAKE);
		List<Party> l7 = new ArrayList<Party>();
		l7.add(Party.OWL);
		List<Party> l8 = new ArrayList<Party>();
		l8.add(Party.JACKALOPE);
		l8.add(Party.SNAKE);
		List<Party> l9 = new ArrayList<Party>();
		l9.add(Party.JACKALOPE);
		l9.add(Party.OWL);
		
		Vote v1 = new Vote(1400, Province.TUNDRA, l1);
		Vote v2 = new Vote(1400, Province.TUNDRA, l1);
		Vote v3 = new Vote(1400, Province.TUNDRA, l1);
		Vote v4 = new Vote(1400, Province.TUNDRA, l1);
		Vote v5 = new Vote(1400, Province.TUNDRA, l2);
		Vote v6 = new Vote(1400, Province.TUNDRA, l2);
		Vote v7 = new Vote(1400, Province.TUNDRA, l3);
		Vote v8 = new Vote(1400, Province.TUNDRA, l3);
		Vote v9 = new Vote(1400, Province.TUNDRA, l4);
		Vote v10 = new Vote(1400, Province.TUNDRA, l5);
		Vote v11 = new Vote(1400, Province.TUNDRA, l5);
		Vote v12 = new Vote(1400, Province.TUNDRA, l5);
		Vote v13 = new Vote(1400, Province.TUNDRA, l6);
		Vote v14 = new Vote(1400, Province.TUNDRA, l6);
		Vote v15 = new Vote(1400, Province.TUNDRA, l6);
		Vote v16 = new Vote(1400, Province.TUNDRA, l7);
		Vote v17 = new Vote(1400, Province.TUNDRA, l7);
		Vote v18 = new Vote(1400, Province.TUNDRA, l7);
		Vote v19 = new Vote(1400, Province.TUNDRA, l8);
		Vote v20 = new Vote(1400, Province.TUNDRA, l9);

		votes.add(v1);
		votes.add(v2);
		votes.add(v3);
		votes.add(v4);
		votes.add(v5);
		votes.add(v6);
		votes.add(v7);
		votes.add(v8);
		votes.add(v9);
		votes.add(v10);
		votes.add(v11);
		votes.add(v12);
		votes.add(v13);
		votes.add(v14);
		votes.add(v15);
		votes.add(v16);
		votes.add(v17);
		votes.add(v18);
		votes.add(v19);
		votes.add(v20);
		
		return votes;
	}
	
	public List<Vote> createVotesSTV() {
		List<Vote> votes = new ArrayList<Vote>();
		
		List<Party> l1 = new ArrayList<Party>();
		l1.add(Party.WHITE_TIGER);
		l1.add(Party.WHITE_GORILLA);
		List<Party> l2 = new ArrayList<Party>();
		l2.add(Party.WHITE_TIGER);
		List<Party> l3 = new ArrayList<Party>();
		l3.add(Party.OWL);
		l3.add(Party.WHITE_TIGER);
		List<Party> l4 = new ArrayList<Party>();
		l3.add(Party.WHITE_TIGER);
		l4.add(Party.LYNX);
		List<Party> l5 = new ArrayList<Party>();
		l5.add(Party.SNAKE);
		l5.add(Party.OWL);
		List<Party> l6 = new ArrayList<Party>();
		l6.add(Party.JACKALOPE);
		List<Party> l7 = new ArrayList<Party>();
		l7.add(Party.TIGER);
		l7.add(Party.WHITE_TIGER);
		List<Party> l8 = new ArrayList<Party>();
		l8.add(Party.LYNX);
		List<Party> l9 = new ArrayList<Party>();
		l9.add(Party.GORILLA);
		l9.add(Party.WHITE_GORILLA);
		
		Vote v1 = new Vote(1400, Province.TUNDRA, l1);
		Vote v2 = new Vote(1400, Province.TUNDRA, l2);
		Vote v3 = new Vote(1400, Province.TUNDRA, l1);
		Vote v4 = new Vote(1400, Province.TUNDRA, l2);
		Vote v5 = new Vote(1400, Province.TUNDRA, l3);
		Vote v6 = new Vote(1400, Province.TUNDRA, l2);
		Vote v7 = new Vote(1400, Province.TUNDRA, l4);
		Vote v8 = new Vote(1400, Province.TUNDRA, l5);
		Vote v9 = new Vote(1400, Province.TUNDRA, l6);
		Vote v10 = new Vote(1400, Province.TUNDRA, l7);
		Vote v11 = new Vote(1400, Province.TUNDRA, l8);
		Vote v12 = new Vote(1400, Province.TUNDRA, l9);

		votes.add(v1);
		votes.add(v2);
		votes.add(v3);
		votes.add(v4);
		votes.add(v5);
		votes.add(v6);
		votes.add(v7);
		votes.add(v8);
		votes.add(v9);
		votes.add(v10);
		votes.add(v11);
		votes.add(v12);

		
		return votes;
	}
	
	@Test
	public void totalVotesTest() {
		List<Vote> votes = createVotes();
		Map<Party, Integer> votesMap = VotingSystems.totalVotes(votes);
		
		assertEquals(6, votesMap.get(Party.TIGER).intValue());
		assertEquals(3, votesMap.get(Party.GORILLA).intValue());
		assertEquals(3, votesMap.get(Party.LYNX).intValue());
		assertEquals(3, votesMap.get(Party.SNAKE).intValue());
		assertEquals(3, votesMap.get(Party.OWL).intValue());
		assertEquals(2, votesMap.get(Party.JACKALOPE).intValue());
	}
	
	@Test
	public void tableVotesTest() {
		List<Vote> votes = createVotes();
		Map<Party, Integer> votesMap = VotingSystems.tableVotes(votes,1400);
		Map<Party, Integer> votesMap2 = VotingSystems.tableVotes(votes,1500);
		
		assertEquals(6, votesMap.get(Party.TIGER).intValue());
		assertEquals(3, votesMap.get(Party.GORILLA).intValue());
		assertEquals(3, votesMap.get(Party.LYNX).intValue());
		assertEquals(3, votesMap.get(Party.SNAKE).intValue());
		assertEquals(3, votesMap.get(Party.OWL).intValue());
		assertEquals(2, votesMap.get(Party.JACKALOPE).intValue());
		assertNull(votesMap.get(Party.LEOPARD));
		
		assertEquals(0, votesMap2.size());
	}
	
	@Test
	public void provinceVotesTest() {
		List<Vote> votes = createVotes();
		Map<Party, Integer> votesMap = VotingSystems.provinceVotes(votes,Province.TUNDRA);
		Map<Party, Integer> votesMap2 = VotingSystems.provinceVotes(votes,Province.JUNGLE);
		
		assertEquals(6, votesMap.get(Party.TIGER).intValue());
		assertEquals(3, votesMap.get(Party.GORILLA).intValue());
		assertEquals(3, votesMap.get(Party.LYNX).intValue());
		assertEquals(3, votesMap.get(Party.SNAKE).intValue());
		assertEquals(3, votesMap.get(Party.OWL).intValue());
		assertEquals(2, votesMap.get(Party.JACKALOPE).intValue());
		assertNull(votesMap.get(Party.LEOPARD));
		
		assertEquals(0, votesMap2.size());
	}

	@Test
	public void FPTPTest() {
		Map<Party, Integer> votesMap = new HashMap<>();
		
		votesMap.put(Party.GORILLA, 3);
		votesMap.put(Party.JACKALOPE, 2);
		votesMap.put(Party.TIGER, 6);
		votesMap.put(Party.LYNX, 3);
		votesMap.put(Party.SNAKE, 3);
		votesMap.put(Party.OWL, 3);
		
		Map<Party, Integer> sortedMap = vs.FPTP(votesMap);

		//Siempre el próximo va a tener igual o más votos
		Integer curr = 0;
		for(Map.Entry<Party, Integer> e : sortedMap.entrySet()) {
			assertTrue(e.getValue() >= curr);
			curr = e.getValue();
		}
		
		//El último es el ganador
		Object array[] = sortedMap.keySet().toArray();
		assertEquals(array[sortedMap.size()-1],Party.TIGER);
	}
	
	@Test
	public void STVTest() {
		List<Vote> votes = createVotesSTV();
		Map<Party, Double> votesMap = vs.STV(votes, Province.TUNDRA);
		
		//El mapa devuelto solo contiene a los ganadores
		assertTrue(votesMap.containsKey(Party.WHITE_TIGER));
		assertTrue(votesMap.containsKey(Party.SNAKE));
		assertTrue(votesMap.containsKey(Party.LYNX));
		assertTrue(votesMap.containsKey(Party.JACKALOPE));
		assertTrue(votesMap.containsKey(Party.WHITE_GORILLA));
		assertFalse(votesMap.containsKey(Party.OWL));
		assertFalse(votesMap.containsKey(Party.TIGER));
			
		//Porcentajes de los ganadores
		assertEquals("20.0",votesMap.get(Party.WHITE_TIGER).toString().substring(0, 4));
		assertEquals("8.33",votesMap.get(Party.SNAKE).toString().substring(0, 4));
		assertEquals("16.66",votesMap.get(Party.LYNX).toString().substring(0, 5));
		assertEquals("8.33",votesMap.get(Party.JACKALOPE).toString().substring(0, 4));
		assertEquals("20.0",votesMap.get(Party.WHITE_GORILLA).toString().substring(0, 4));
		
	}
	

}
