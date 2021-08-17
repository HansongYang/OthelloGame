package othello;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPanel;


public class OthelloNetworkModalViewController extends JDialog{

    /** Whether the user pressed the Connect button. */
    private Boolean hasConnected = false;

    private Boolean connection = false;

    /** A reference to the private inner Controller class for use by the two buttons. */
    private Controller handler = new Controller();
    private JComboBox portInput;
    private JTextField addressInput;
    private JTextField nameInput;
    private JLabel status;
    private OthelloClient client;

public OthelloNetworkModalViewController (JFrame mainView){
        super(mainView,"Enter Network Address",true);

        setUndecorated(true);
        Container networkPanel = getContentPane();

        JPanel p = new JPanel();
        p.setBackground(Color.white);
        p.setBorder(BorderFactory.createLineBorder(Color.GRAY, 5));
        p.setLayout(new FlowLayout(FlowLayout.LEFT,5,10));
        p.setPreferredSize(new Dimension(330, 230));
        JLabel address = new JLabel("Address:");
        JLabel port = new JLabel("Port:");
        JLabel name = new JLabel("Name:");
        port.setPreferredSize(new Dimension(55, 30));
        status = new JLabel("");
        status.setPreferredSize(new Dimension(350, 30));
        addressInput = new JTextField(20);
        JButton connect = new JButton("Connect");
        connect.setActionCommand("C");
        connect.addActionListener(handler);
        connect.setPreferredSize(new Dimension(95, 40));
        JButton cancel = new JButton("Cancel");
        cancel.setPreferredSize(new Dimension(95, 40));
        cancel.setActionCommand("X");
        cancel.addActionListener(handler);
        String[] listData = new String[]{"", "31000","41000", "51000"};
        portInput = new JComboBox<String>(listData);
        portInput.setPreferredSize(new Dimension(220, 30));
        portInput.setEditable(true);
        portInput.setSelectedIndex(0);
        name.setPreferredSize(new Dimension(55, 30));
        nameInput = new JTextField(20);
        address.setDisplayedMnemonic('A');
        address.setLabelFor(addressInput);
        port.setDisplayedMnemonic('P');
        port.setLabelFor(portInput);
        name.setDisplayedMnemonic('N');
        name.setLabelFor(nameInput);
        JLabel empty = new JLabel();
        empty.setPreferredSize(new Dimension(100, 30));
        p.add(address);
        p.add(addressInput);
        p.add(port);
        p.add(portInput);
        p.add(name);
        p.add(nameInput);
        p.add(status);
        p.add(empty);
        p.add(connect);
        p.add(cancel);
        networkPanel.add(p);

        //This statement should be the last one.
        pack();
    }

    /** This method returns the value the user has entered.
        @return The actual value, unless there was an error or the user pressed the cancel button.
    */

    public String getAddress(){
        if (hasConnected)
        {
            return (addressInput.getText());
        }
        else
        {
            //You can return whatever error message you like.  This isn't cast in stone.
            return ("Error:  Invalid Address or attempt cancelled.");
        }
    }

    /** This method returns the port the user has selected OR ENTERED in the Combo Box.
    @return The port selected.  Returns -1 on invalid port or cancel pressed.
    */

    public int getPort(){
        int portnum;
        if (hasConnected)
        {
            //Ensure the user has entered digits.
            //You should probably also ensure the port numbers are in the correct range.
            //I did not.  That's from 0 to 65535, by the way.  Treat it like invalid input.
            try
            {
                portnum = Integer.parseInt((String)portInput.getSelectedItem());
            }
                catch(NumberFormatException nfe)
            {
                //I've been using a negative portnum as an error state in my main code.
                portnum = -1;
            }
            if(portnum >= 0 && portnum <= 65535){
                return portnum;
            }
        }
        return -1;
    }

    /** Responsible for final cleanup and hiding the modal. Does not do much at the moment.*/
    public void hideModal(){
        //Add any code that you may want to do after the user input has been processed
        //and you're figuratively closing up the shop.
        setVisible(false);

    }

    public String getName(){
        return nameInput.getText();
    }

    /** Returns whether or not the user had pressed connect.
    @return True if the user pressed Connect, false if the user backed out with cancel.
    */
    public boolean pressedConnect()
    {
        return hasConnected;
    }

    /** Returns whether or not the user had connected to the server.
    @return True if the user had connected to the server, false if the user does not connect to the server.
    */
    public boolean connected(){
        return connection;
    }

    //Tell the client to disconnet from the server
    public void disconnect(){
        client.disconnect();
    }

    //Tell the client to request a "who" message from the server
    public void who(){
        client.who();
    }

    //Tell the client to send a rename request to the server
    public void rename(String newname){
        client.rename(newname);
    }

    //Tell the client to request a broadcast message from the server
    public void message(String m){
        client.message(m);
    }

    private class Controller implements ActionListener{
        public void actionPerformed(ActionEvent evt){
            String s = evt.getActionCommand();
            if ("C".equals(s)){
                hasConnected = true;
                if(getAddress().length() == 0){
                    status.setText("Error: Address must not be blank.");
                    return;
                }

                if(getPort() == -1){
                    status.setText("Error: Valid port ranges are from 0 to 65535.");
                    return;
                }

                if(nameInput.getText().length() < 3){
                    status.setText("Error: Name must be at least three characters long.");
                    return;
                }

            		client = new OthelloClient(getAddress(), getPort(), nameInput.getText());
                if(client.connectToServer()){
                  connection = true;
                } else{
                  connection = false;
                }
            }
            else //My "Cancel" button has an action command of "X" and gets called here.
            {
                hasConnected = false;
            }
            //Hide the modal. For part 2, we may not want to hide the modal right away.
            hideModal();
        }
    }
}
