package client;


/**
 * Implements client command to set the host.
 * Can only be executed if not connected.
 *
 * @author Chris Nevison
 * @version February 2012
 */
public class sethost extends NotConnectedClientCommand {
    public sethost(String str, Chat4ClientCommandProcessor client) {
        super(str, client);
    }

    public void doCmd() {
    	if (!getStr().equals("null")){
        getClient().OC().setHost(getStr());
        getClient().clientUI().display("Host is set to "+getStr());
    }
    }
}
