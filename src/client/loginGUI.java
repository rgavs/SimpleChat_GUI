package client;

import ocsf.client.ObservableClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

class loginGUI extends JPanel implements ActionListener, Observer {
    private final JTextField user;
    private final JPasswordField pass;
    private final JButton submit;
    private final ObservableClient oc;

    /**
     * The default port to connect on.
     */
    private final static int DEFAULT_PORT = 5555;

    /**
     * Should <code>loginGUI</code> be abstracted to allow choosing <code>host</code> and <code>port</code>?
     *
     * @param host String
     * @param port int
     */
    private loginGUI(String host, int port) {
        super(new GridBagLayout());
        oc = new ObservableClient(host, port);

        // initialize text box              @author Ryan
        user = new JTextField(20);
        user.getInputMap().put(KeyStroke.getKeyStroke("TAB"), "transferFocus");

        AbstractAction transferFocus = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                ((Component) e.getSource()).transferFocus();
            }
        };

        user.getActionMap().put("transferFocus", transferFocus);
        // initialize chat text area
        pass = new JPasswordField(20);
        // initialize submit button
        submit = new JButton();

        try { // some customization
            Font roboto = Font.createFont(Font.TRUETYPE_FONT, (new FileInputStream("src/res/Roboto-Regular.ttf"))).deriveFont(12.0f);
            System.out.printf("Font is: " + roboto.getFamily() + " and size is " + String.valueOf(roboto.getSize()));
            user.setFont(roboto.deriveFont(11.0f));
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createAndShowLoginGUI(String host, int port) {
        //Create and set up the window.
        JFrame frame = new JFrame("ChatLogin");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add contents to the window.
        frame.add(new loginGUI(host, port));

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void update(Observable observable, Object o) {
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        System.out.printf("Action performed. Command is: " + evt.getActionCommand());

    }

    public static void main(String[] args) {
        String hostStr, portStr;
        try {
            hostStr = args[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            hostStr = "localhost";
        }

        try {
            portStr = args[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            portStr = "" + DEFAULT_PORT;
        }

        final String host = hostStr;
        final int port = Integer.parseInt(portStr);

        javax.swing.SwingUtilities.invokeLater(() -> createAndShowLoginGUI(host, port));   // Ryan: language-level migration
    }
}
