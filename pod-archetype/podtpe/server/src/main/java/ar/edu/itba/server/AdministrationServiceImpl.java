package ar.edu.itba.server;

import ar.edu.itba.AdministrationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdministrationServiceImpl implements AdministrationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
    private ElectionState state;

    public AdministrationServiceImpl() {
        state = ElectionState.NOT_STARTED;
    }

    @Override
    synchronized public void open() throws IllegalStateException {
        switch(state){
            case NOT_STARTED:
                state = ElectionState.STARTED;
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
        return state;
    }

    @Override
    synchronized public void close() throws IllegalStateException {
        switch(state){
            case NOT_STARTED:
                throw new IllegalStateException("Tried to close an election that has not started.");
            case STARTED:
                state = ElectionState.FINISHED;
                LOGGER.info("Closed election.");
                break;
            case FINISHED:
                // TODO: que pasa en este caso?
                break;
        }
    }
}