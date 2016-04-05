package server;

import ocsf.server.ConnectionToClient;

import java.io.IOException;
import java.util.HashSet;

/**
 * This class defines a message handler to simple request that a String be displayed.
 * It actually puts a block on the user specified by string
 */
public class WhoBlocksMeHandler extends ServerNonLoginHandler {

    public WhoBlocksMeHandler(String str, Chat4Server server, ConnectionToClient client) {
        super(str, server, client);
    }

    /**
     * This method puts a block on a user
     */
    public void handleMess() {
        HashSet<String> block = (HashSet<String>) getClient().getInfo("blocksme");

        try {
            getClient().sendToClient("You are blocked by:");

            for (String blocker : block) {
                getClient().sendToClient(blocker);
            }
        } catch (IOException e) {
            getServer().getConsole().display(e + "\nError sending blocked list to " + getClient().getInfo("id"));
        }
    }
}
