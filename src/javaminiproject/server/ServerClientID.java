package javaminiproject.server;

/**
 * This class keeps a record of client ID and generates clientIDs.
 *
 * @author Aditeya Viju Govind
 * https://github.com/Aditeya/JavaMiniProject
 */
public class ServerClientID {

    private int client;

    /**
     * ServerClientID Constructor starts with client id at 0
     */
    public ServerClientID() {
        client = 0;
    }

    /**
     * Returns the clientID as an integer
     * 
     * @return client IDcas integer
     */
    public synchronized int generateClientID() {
        return client++;
    }
}
