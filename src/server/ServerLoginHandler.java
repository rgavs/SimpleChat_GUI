package server;

import ocsf.server.ConnectionToClient;

import java.io.IOException;
import java.util.HashSet;

/**
 * This class handles a request from a client to login to the server.
 *
 * @author Chris Nevison
 * @version February 2012
 */
public class ServerLoginHandler extends ServerMessageHandler {
    private static final long serialVersionUID = -7411069810184198297L;

    public ServerLoginHandler(String str, Chat4Server server, ConnectionToClient client) {
        super(str, server, client);
    }

    /**
     * This method logs the client in by saving its id String and
     * sends a message to all clients that the new client has logged in
     * If already logged in (id String has been set) a message is sent to the
     * client and no other action is taken.
     */
    public void handleMessage() {
        if (getClient().getInfo("id") != null) {
            try {
                getClient().sendToClient(getMessage() + " is already logged on. \nNo new connection made.");
            } catch (IOException ex) {
                getServer().getConsole().display(ex + "\n in ServerLoginHandler.handleMessage.");
            }
        } else {
            try {
                String[] idPassword = getMessage().split(" ");
                if (idPassword.length != 2) {
                    getClient().sendToClient("Invalid input, sorry.  Please log in with valid input.");
                    getClient().close();
                } else if (getServer().getPasswordManager().validID(idPassword[0])) {
                    if (getServer().getPasswordManager().validatePassword(idPassword[0], idPassword[1])) {
                        setup(idPassword[0]);
                    } else {
                        getClient().sendToClient("Invalid password.  Please try to login again.");
                        getClient().close();
                    }
                } else { // not logged in, add id-password pair
                    getServer().getPasswordManager().addIDPasswordPair(idPassword[0], idPassword[1]);
                    getClient().sendToClient("Account successfully created, congratulations!\n");
                    setup(idPassword[0]);
                }
            } catch (Exception e) {
                System.out.println(e + "\nAn error occurred during login.");
            }
        }
    }

    private void setup(String id) {
        getClient().setInfo("id", id);
        getServer().getChannelManager().joinChannel("global", getClient());
        getClient().setInfo("channel", "global");
        getClient().setInfo("blocksme", new HashSet<String>());
        getClient().setInfo("iblock", new HashSet<String>());
        getServer().getConsole().display(id + " has logged on");
        getServer().sendToAllClients("SERVER MSG> " + id + " has joined");
    }
}
