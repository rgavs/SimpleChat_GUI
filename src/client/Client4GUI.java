package client;

/* TextDemo.java requires no other files. */

import common.ChatIF;
import ocsf.client.ObservableClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class Client4GUI extends JPanel implements ActionListener, ChatIF, Observer {

    protected JTextField textField;
    protected JTextArea textArea;
    private final static String newline = "\n";

    /**
     * The default port to connect on.
     */
    final public static int DEFAULT_PORT = 5555;

    /**
     * The instance of the client created by this ClientGUI.
     */
    Chat4ClientCommandProcessor client;

    public Client4GUI(String host, int port, String id, String password) {
        super(new GridBagLayout());

        textField = new JTextField(20);
        textField.addActionListener(this);

        textArea = new JTextArea(5, 20);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        //Add Components to this panel.
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;

        c.fill = GridBagConstraints.HORIZONTAL;
        add(textField, c);

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(scrollPane, c);

        try {
            client = new Chat4ClientCommandProcessor(id, password, new ObservableClient(host, port), this);

        } catch (IOException ex) {
            System.out.println("IOException " + ex + "when connecting, shutting down.");
        }
        display("connected to " + host + "-" + port);
        client.OC().addObserver(this);
    }

    public void actionPerformed(ActionEvent evt) {
        String message = textField.getText();
        client.handleMessageFromClientUI(message);
        textField.setText("");
    }

    public void update(Observable OC, Object msg) {
        if (msg instanceof String)
            display((String) msg);
        else if (msg instanceof Exception)
            display("Connection exception " + msg);
    }

    public void display(String message) {
        textArea.append(message + newline);
        //Make sure the new text is visible, even if there
        //was a selection in the text area.
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI(String host, int port, String id, String password) {
        //Create and set up the window.
        JFrame frame = new JFrame("Chat");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add contents to the window.
        frame.add(new Client4GUI(host, port, id, password));


        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        String hostStr = "";
        String idStr = "";
        String pswdStr = "";
        String portStr = "";  //The port number

        try {
            idStr = args[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("No id provided, can't login.");
            System.exit(-1);
        }

        try {
            pswdStr = args[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            pswdStr = "";
        }

        try {
            hostStr = args[2];
        } catch (ArrayIndexOutOfBoundsException e) {
            hostStr = "localhost";
        }

        try {
            portStr = args[3];
        } catch (ArrayIndexOutOfBoundsException e) {
            portStr = "" + DEFAULT_PORT;
        }

        final String host = hostStr;
        final String id = idStr;
        final String password = pswdStr;
        final int port = Integer.parseInt(portStr);

        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(host, port, id, password);
            }
        });
    }
}

