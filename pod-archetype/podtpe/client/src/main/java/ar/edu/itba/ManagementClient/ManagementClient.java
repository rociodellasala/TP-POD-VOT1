package ar.edu.itba.ManagementClient;

import ar.edu.itba.AdministrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ManagementClient {
    private static Logger logger = LoggerFactory.getLogger(ManagementClient.class);

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        logger.info("tppod Client Starting ...");

        final AdministrationService handle = (AdministrationService) Naming.lookup("//localhost:1099/administration-service");

        handle.open();

        AdministrationService.ElectionState state = handle.getCurrentSate();
        String output = String.format("The election state is %s.", state);
        System.out.println(output);

        handle.close();
    }
}