package com.example.mandelbrotset2;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class MSServer {
    public static void main(String[] args) {
        /* 1. Create an instance of the remote object */
        /* 2. Register the object created with the RMI registry. */
       try {
           /*Set hostname for the server using javaProperty*/
           System.setProperty("java.rmi.server.hostname", "127.0.0.1");

           /* msi -- the ms object*/
            MSImplementation msi = new MSImplementation();

            /*Export the ms object using UnicastRemoteObject class*/
           MSInterface stub = (MSInterface) UnicastRemoteObject.exportObject(msi, 1099);

           /*Register the exported class in RMI registry with some name,
           * Client will use that name to get the reference of those exported object.*/
            Registry registry = LocateRegistry.getRegistry("127.0.0.1",1099);
            registry.bind("ms", stub);

            System.out.println("Server is ready.");

       } catch (RemoteException e) {
           System.out.println("ERROR: Could not create registry");
           throw new RuntimeException(e);
       } catch (AlreadyBoundException e) {
           throw new RuntimeException(e);
       }
    }
}
