package ar.edu.itba.server.servant;

import ar.edu.itba.remoteinterfaces.AdministrationService;
import ar.edu.itba.server.ElectionCentral;
import ar.edu.itba.exceptions.InvalidStateException;
import ar.edu.itba.server.Server;
import ar.edu.itba.utils.ElectionState;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdministrationServiceImpl implements AdministrationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
    private ElectionCentral central;

    public AdministrationServiceImpl(ElectionCentral central) {
    	this.central = central;
    }

    @Override
    synchronized public void open() throws InvalidStateException {
        switch(central.getState()){
            case CLOSED:
                central.setState(ElectionState.OPENED); 
                LOGGER.info("The elections have started");
                break;
            case OPENED:
                throw new InvalidStateException("Could not open: The elections are already open");
            case FINISHED:
                throw new InvalidStateException("Could not open: The elections have finished");
                
        }
    }

    @Override
    public ElectionState getCurrentSate() {
        return central.getState();
    }

    @Override
    synchronized public void close() throws InvalidStateException {
        switch(central.getState()){
            case CLOSED:
                throw new InvalidStateException("Could not close: The elections have not started");
            case OPENED:
            	central.setState(ElectionState.FINISHED); 
                LOGGER.info("Closed election");
                break;
            case FINISHED:
            	throw new InvalidStateException("Could not close: The elections have finished");
        }
    }
}