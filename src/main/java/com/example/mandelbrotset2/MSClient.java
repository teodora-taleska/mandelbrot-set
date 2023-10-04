package com.example.mandelbrotset2;

import javafx.application.Application;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MSClient {
    /*
      The client remotely invokes any methods specified in the remote
      interface ( MSInterface ). To do so however, the client must
      first obtain a reference to the remote object from the RMI
      registry. Once a reference is obtained, methods can be invoked.
     */
    public static void main(String[] args) {
        try{
            // Locate the registry
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1099);

            // Get the reference of exported object from RMI Registry
            MSInterface msService = (MSInterface) registry.lookup("ms");

            Application.launch(MandelbrotDistributive.class);
            System.out.println("MSClient");
        }catch (Exception e){
            System.out.println("MSServer exception: " +e.getMessage());
            e.printStackTrace();
        }
    }
}
