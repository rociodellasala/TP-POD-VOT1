package ar.edu.itba.server;

import ar.edu.itba.AdministrationService;
import ar.edu.itba.FiscalService;
import ar.edu.itba.VotingService;
import ar.edu.itba.server.AdministrationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {
    private static Logger LOGGER = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {
        LOGGER.info("tppod Server Starting ...");
        bindServices();
    }

    public static void bindServices(){
    	
    	ElectionCentral central = new ElectionCentral();
    	
        final AdministrationService adminService = new AdministrationServiceImpl(central);

        try {
            final Remote remoteAdmin = UnicastRemoteObject.exportObject(adminService,0);
            final Registry registry = LocateRegistry.getRegistry();
            registry.rebind("administration-service", remoteAdmin);
            LOGGER.info("Administration Service bound.");
        } catch(RemoteException e) {
            LOGGER.info("Remote exception.");
            e.printStackTrace();
        } 
        
        final VotingService votingService = new VotingServiceImpl(central);

        try {
            final Remote remoteVoter = UnicastRemoteObject.exportObject(votingService,0);
            final Registry registry = LocateRegistry.getRegistry();
            registry.rebind("voting-service", remoteVoter);
            LOGGER.info("Voting Service bound.");
        } catch(RemoteException e) {
            LOGGER.info("Remote exception.");
            e.printStackTrace();
        } 
        
        final FiscalService fiscalService = new FiscalServiceImpl(central);

        try {
            final Remote remoteFiscal = UnicastRemoteObject.exportObject(fiscalService,0);
            final Registry registry = LocateRegistry.getRegistry();
            registry.rebind("fiscal-service", remoteFiscal);
            LOGGER.info("Fiscal Service bound.");
        } catch(RemoteException e) {
            LOGGER.info("Remote exception.");
            e.printStackTrace();
        } 
    }
}