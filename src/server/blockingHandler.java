package server;

import ocsf.server.ConnectionToClient;

import java.io.IOException;
import java.util.HashSet;

/**
 * This class defines a message handler to simple request that a String be displayed.
 * It actually puts a block on the user specified by string
 */
public class blockingHandler extends ServerNonLoginHandler {

    private static final long serialVersionUID = 1717928825890682607L;
    private final String whoToBlock;

    public blockingHandler(String str, Chat4Server server, ConnectionToClient client) {
        super(str, server, client);
        whoToBlock = str;
    }


    /**
     * This method puts a block on a user
     */
    public void handleMess() {
        if (getClient().getInfo("id").equals(whoToBlock)) {
            try {
                getClient().sendToClient("You cannot block yourself. No action taken.");
            } catch (IOException ex) {
                getServer().getConsole().display(ex + "\nUnable to send message to client.");
            }
            return;
        }

        ConnectionToClient clientToBlock = findClient(whoToBlock);
        if (clientToBlock == null) {
            try {
                getClient().sendToClient(whoToBlock + " is not connected. No action taken.");
            } catch (IOException ex) {
                getServer().getConsole().display(ex + "\n Failure to send to client " + getClient().getInfo("id"));
            }
            return;
        }

        ((HashSet<String>) clientToBlock.getInfo("blocksme")).add((String) getClient().getInfo("id"));
        ((HashSet<String>) getClient().getInfo("iblock")).add(whoToBlock);

        try {
            getClient().sendToClient(whoToBlock + " blocked");
            getServer().getConsole().display(getClient().getInfo("id") + " blocked " + whoToBlock);
        } catch (IOException ex) {
            try {
                getClient().sendToClient(ex + "\nError in block of " + whoToBlock);
            } catch (IOException e) {
                getServer().getConsole().display(e + "\nError in sending error message to client " + getClient().getInfo("id") + " in block");
            }
        }

    }

    private ConnectionToClient findClient(String who) {
        Thread[] clientList = getServer().getClientConnections();
        for (Thread aClientList : clientList) {
            if ((((ConnectionToClient) aClientList).getInfo("id")).equals(who)) {
                return (ConnectionToClient) aClientList;
            }
        }
        return null;
    }

}

