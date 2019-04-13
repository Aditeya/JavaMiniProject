package javaminiproject.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Fuel Cost Calculator Server is used to calculate total fuel cost, send back
 * the given parameters/answer and write it to a file, using multi-threading.
 *
 * @author Aditeya Viju Govind
 */
public class Server {

    /**
     * Starts Server on port 2000 and serves each user in a thread
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        System.out.println("----------Fuel Cost Calculator Server----------");

        try (ServerSocket server = new ServerSocket(2000)) {

            while (true) {
                try {
                    Socket client = server.accept();
                    Thread thread = new Thread(new ServerWorker(client));
                    thread.start();
                } catch (IOException | NullPointerException e) {
                }
            }
        } catch (IOException e) {
        }

    }

}
