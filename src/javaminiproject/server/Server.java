
package javaminiproject.server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Fuel Cost Calculator Server is used to retrieve the 
 * cost of fuel per litre from a file.
 * 
 * @author adite
 */
public class Server {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        System.out.println("----------Fuel Cost Calculator Server----------");

        try (ServerSocket server = new ServerSocket(2000)) {
            while (true) {
                
                try (Socket client = server.accept()) {

                    PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    BufferedReader fileReader = new BufferedReader(new FileReader("resources/fuelPrices.txt"));

                    switch (in.readLine()) {
                        case "98 Octane":
                            out.println(fileReader.readLine());
                            break;
                        case "Diesel":
                            fileReader.readLine();
                            out.println(fileReader.readLine());
                            break;
                    }
  
                } catch (IOException | NullPointerException e) {
                }
                
            }
        } catch (IOException e) {
        }

    }
}
