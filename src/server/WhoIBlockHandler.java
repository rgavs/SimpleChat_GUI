package server;

import ocsf.server.ConnectionToClient;

import java.io.IOException;
import java.util.HashSet;

/**
 * This class defines a message handler to simple request that a String be displayed.
 * It actually puts a block on the user specified by string
 */
public class WhoIBlockHandler extends ServerNonLoginHandler {

    public WhoIBlockHandler(String str, Chat4Server server, ConnectionToClient client) {
        super(str, server, client);
    }

    /**
     * This method puts a block on a user
     */
    public void handleMess() {
        HashSet<String> block = (HashSet<String>) getClient().getInfo("iblock");
        try {
            getClient().sendToClient("You block:");

            for (String blockee : block) {
                getClient().sendToClient(blockee);
            }
        } catch (IOException e) {
            getServer().getConsole().display(e + "\nError sending I-block list to client " + getClient().getInfo("id"));
        }
    }
}
