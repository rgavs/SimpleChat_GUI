package client;

/* TextDemo.java requires no other files. */

import common.ChatIF;
import ocsf.client.ObservableClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Observable;
import java.util.Observer;

import static java.awt.Font.*;

public class Client4GUI extends JPanel implements ActionListener, ChatIF, Observer {

    private final JTextField textField;
    private final JTextArea textArea;
    private final JButton editChannel;
    private final JButton listChannels;
    private final JButton quit;
    private final JButton setport;
    private final JButton sethost;
    private final JButton users;
    private final JButton whisper; 
    private final static String newline = "\n";

    /**
     * The default port to connect on.
     */
    private final static int DEFAULT_PORT = 5555;

    /**
     * The instance of the client created by this ClientGUI.
     */
    private Chat4ClientCommandProcessor client;

    private Client4GUI(String host, int port, String id, String password) {
        super(new GridBagLayout());

        // initialize text box              @author Ryan
        textField = new JTextField(20);
        // initialize chat text area
        textArea = new JTextArea(5, 20);
        textArea.setEditable(false);
        // initialize channel button
        editChannel = new JButton();
        Image plus = new ImageIcon("src/res/plus_sign.png").getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH);
        editChannel.setIcon(new ImageIcon(plus));
        //initialize list channel button
         listChannels = new JButton("Channels");
         quit = new JButton("Quit");
         setport = new JButton("Setport");
         sethost = new JButton("Sethost");
         users = new JButton("Users");
         whisper = new JButton("Whisper");

        try { // some customization
            Font roboto = Font.createFont(TRUETYPE_FONT, (new FileInputStream("src/res/Roboto-Regular.ttf"))).deriveFont(12.0f);
            textField.setFont(roboto);
            textArea.setFont(roboto);
            System.out.printf("Font is: "+roboto.getFamily()+" and size is "+ String.valueOf(roboto.getSize()));
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(textArea);

        // Add Components to panel.
        editChannel.addActionListener(this);
        textField.addActionListener(this);
        listChannels.addActionListener(this);
        quit.addActionListener(this);
        setport.addActionListener(this);
        sethost.addActionListener(this);
        users.addActionListener(this);
        whisper.addActionListener(this);
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.NORTHWEST;
        add(editChannel);
        add(listChannels);
        add(quit);
        add(sethost);
        add(setport);
        add(users);
        add(whisper);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = GridBagConstraints.PAGE_END;
        add(textField, c);

        c.fill = GridBagConstraints.BOTH;
        c.gridy = 1;
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
       System.out.println("Action performed. Command is: "+evt.getActionCommand());
        if (evt.getSource() == editChannel) {
        	//System.out.println("hey");
        	JFrame frame = new JFrame();
        	Object channel = JOptionPane.showInputDialog(frame, "Enter Channel name: ");
        	if (channel != null){
        	client.handleMessageFromClientUI("#join " + channel);
        	}
        	
        }
        else if(evt.getSource() == listChannels) {
        	client.handleMessageFromClientUI("#list");
        }
        else if(evt.getSource() == quit) {
        	client.handleMessageFromClientUI("#quit");
        }
        else if(evt.getSource() == setport) {
        	JFrame frame = new JFrame();
        	Object port = JOptionPane.showInputDialog(frame, "Enter port number: ");
        	client.handleMessageFromClientUI("#setport "+ port);
        }
        else if(evt.getSource() == sethost) {
        	JFrame frame = new JFrame();
        	Object host = JOptionPane.showInputDialog(frame, "Enter host name: ");
        	client.handleMessageFromClientUI("#sethost "+ host);
        }
        else if(evt.getSource() == users) {
        	client.handleMessageFromClientUI("#users");
        }
        else if(evt.getSource() == whisper) {
            JFrame frame = new JFrame();
        	String receiver = JOptionPane.showInputDialog(frame, "Enter receiver's ID: ");
        	String msg = JOptionPane.showInputDialog(frame, "Message to "+ receiver+ " :");
        	client.handleMessageFromClientUI("#whisper");
        }
        else {
        String message = textField.getText();
        client.handleMessageFromClientUI(message);
        textField.setText("");
        }
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

        javax.swing.SwingUtilities.invokeLater(() -> createAndShowGUI(host, port, id, password));   // Ryan: language-level migration
    }
}

