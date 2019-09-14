package ar.edu.itba.server;

import ar.edu.itba.AdministrationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdministrationServiceImpl implements AdministrationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
    private ElectionCentral central;

    public AdministrationServiceImpl(ElectionCentral central) {
    	this.central = central;
    }

    @Override
    synchronized public void open() throws IllegalStateException {
        switch(central.getState()){
            case NOT_STARTED:
                central.setState(ElectionState.STARTED); 
                LOGGER.info("Started election.");
                break;
            case STARTED:
                // TODO: que pasa en este caso?
                break;
            case FINISHED:
                throw new IllegalStateException("Tried to open a finished election.");
        }
    }

    @Override
    public ElectionState getCurrentSate() {
        return central.getState();
    }

    @Override
    synchronized public void close() throws IllegalStateException {
        switch(central.getState()){
            case NOT_STARTED:
                throw new IllegalStateException("Tried to close an election that has not started.");
            case STARTED:
            	central.setState(ElectionState.FINISHED); 
                LOGGER.info("Closed election.");
                break;
            case FINISHED:
                // TODO: que pasa en este caso?
                break;
        }
    }
}