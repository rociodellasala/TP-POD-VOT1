package ar.edu.itba.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.Before;
import org.junit.Test;

import ar.edu.itba.exceptions.InvalidStateException;
import ar.edu.itba.server.ElectionCentral;
import ar.edu.itba.server.servant.ManagementServiceImpl;
import ar.edu.itba.utils.ElectionState;


public class ManagementServiceImplTest {

	private ElectionCentral central;
	private ManagementServiceImpl ms;

	@Before
	public void createFiscalServiceImpl() {
		central = new ElectionCentral();
		ms = new ManagementServiceImpl(central);
	}
	
	@Test
	public void openElectionsPreviouslyClosed() {
		central.setState(ElectionState.CLOSED);
		try {
			ms.open();
		} catch (InvalidStateException e) {
			e.printStackTrace();
		}
		assertEquals(ElectionState.OPENED, ms.getCurrentSate());
	}
	
	@Test
	public void openElectionsPreviouslyOpen() {
		central.setState(ElectionState.OPENED);
		assertThrows(InvalidStateException.class, () -> {ms.open();});
	}
	
	@Test
	public void openElectionsPreviouslyFinished() {
		central.setState(ElectionState.FINISHED);
		assertThrows(InvalidStateException.class, () -> {ms.open();});
	}
	
	@Test
	public void closeElectionsPreviouslyClosed() {
		central.setState(ElectionState.CLOSED);
		assertThrows(InvalidStateException.class, () -> {ms.close();});
	}
	
	@Test
	public void closeElectionsPreviouslyOpen() {
		central.setState(ElectionState.OPENED);
		try {
			ms.close();
		} catch (InvalidStateException e) {
			e.printStackTrace();
		}
		assertEquals(ElectionState.FINISHED, ms.getCurrentSate());
	}
	
	@Test
	public void closeElectionsPreviouslyFinished() {
		central.setState(ElectionState.FINISHED);
		assertThrows(InvalidStateException.class, () -> {ms.close();});
	}
}
