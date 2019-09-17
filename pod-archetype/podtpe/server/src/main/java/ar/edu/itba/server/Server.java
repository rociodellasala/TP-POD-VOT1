package ar.edu.itba.server;

import ar.edu.itba.remoteinterfaces.*;
import ar.edu.itba.server.servant.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {
    private static Logger LOGGER = LoggerFactory.getLogger(Server.class);
    private static ElectionCentral central;
    private static int port = 0;
    
    public static void main(String[] args) {
        LOGGER.info("The server is starting");
        bindServices();
    }

    public static void bindServices(){
    	central = new ElectionCentral();
    	bindAdministrationService(central);
    	bindFiscalService(central);
    	bindVotingService(central);
    	bindQueryService(central);
    }
   
    public static void bindAdministrationService(ElectionCentral central) {
        final ManagementService adminService = new ManagementServiceImpl(central);

        try {
            final Remote remoteAdmin = UnicastRemoteObject.exportObject(adminService, port);
            final Registry registry = LocateRegistry.getRegistry();
            registry.rebind("administration-service", remoteAdmin);
            LOGGER.info("Administration service bound");
        } catch(RemoteException e) {
            LOGGER.info("Remote exception");
            e.printStackTrace();
        } 
    }
    
    public static void bindFiscalService(ElectionCentral central) {
        final FiscalService fiscalService = new FiscalServiceImpl(central);

        try {
            final Remote remoteFiscal = UnicastRemoteObject.exportObject(fiscalService, port);
            final Registry registry = LocateRegistry.getRegistry();
            registry.rebind("fiscal-service", remoteFiscal);
            LOGGER.info("Fiscal service bound");
        } catch(RemoteException e) {
            LOGGER.info("Remote exception");
            e.printStackTrace();
        } 
    }
    
    public static void bindVotingService(ElectionCentral central) {
        final VotingService votingService = new VotingServiceImpl(central);

        try {
            final Remote remoteVoter = UnicastRemoteObject.exportObject(votingService, port);
            final Registry registry = LocateRegistry.getRegistry();
            registry.rebind("voting-service", remoteVoter);
            LOGGER.info("Voting service bound");
        } catch(RemoteException e) {
            LOGGER.info("Remote exception");
            e.printStackTrace();
        } 
    }
    
    public static void bindQueryService(ElectionCentral central) {
        final QueryService queryService = new QueryServiceImpl(central);

        try {
            final Remote remoteQuery = UnicastRemoteObject.exportObject(queryService, port);
            final Registry registry = LocateRegistry.getRegistry();
            registry.rebind("query-service", remoteQuery);
            LOGGER.info("Query service bound");
        } catch(RemoteException e) {
            LOGGER.info("Remote exception");
            e.printStackTrace();
        } 
    }
    
  
}