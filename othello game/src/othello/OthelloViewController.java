package othello;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

public class OthelloViewController extends JFrame {
    private static final long serialVersionUID = 1L;
    public OthelloModel model = new OthelloModel();
    public String picPath[]= {"../black_s.png","../white_s.png","../cat.png","../dog.png","../pumpkin.png","../bat.png", "../checkmark.png"};
    public JLabel[][] label = new JLabel[8][8];
    public JLabel piecesLabel = new JLabel();
    public JLabel bplayer = new JLabel();
    public JLabel wplayer = new JLabel();
    public static JTextArea pinkArea = new JTextArea();
    public JCheckBox svm = new JCheckBox();
    public JButton canMove;
    public int pieces = 0;
    public int currentPlayer = 1;
    public JButton letterButton[];
    public JButton numberButton[];
    public JMenuItem connection;
    public JMenuItem disconnect;
    public JButton submitButton;
    public JTextField userInput;
    OthelloNetworkModalViewController networkDialog = new OthelloNetworkModalViewController(this);

    public OthelloViewController() {
        Controller controller = new Controller();
        setBounds(150, 50, 990, 620);
        setTitle(Othello Client");
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(null);
        controlPanel.setBounds(0, 0, 990, 620);
        controlPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 5));

        /**create chess board**/
        JPanel chessBoard = new JPanel();
        chessBoard.setBounds(0, 0, 540, 540);
        chessBoard.setLayout(new GridLayout(9, 9));
        String buttonName[] = {"A","B","C","D","E","F","G","H"};
        letterButton = new JButton[buttonName.length];
        numberButton = new JButton[8];
        /**loop for rows**/
        for (int i = 0; i < 9; i++) {
          /**last row of letters**/
            if (i == 8) {
                for (int j = 0; j < 8; j++) {
                    letterButton[j] = createButton(buttonName[j], buttonName[j], Color.black, Color.GRAY, controller);
                    chessBoard.add(letterButton[j]);
                }
                canMove = createButton("move", "move", Color.black, Color.white, controller);
                canMove.setFont(new Font("", Font.PLAIN, 10));
                chessBoard.add(canMove);
            }
            /**chess squares and right side number buttons**/
            else{
              /**loop for columns**/
                for (int j = 0; j < 9; j++) {
                  /**the right most column of numbers**/
                    if (j == 8) {
                      numberButton[i] = createButton(String.valueOf(i + 1), String.valueOf(i + 1), Color.black, Color.GRAY, controller);
                      chessBoard.add(numberButton[i]);
                    }
                    /**chess squares**/
                    else {
                        label[i][j] = new JLabel("",JLabel.CENTER);
                        label[i][j].setOpaque(true);
                        if ((i + j) % 2 == 0) {
                            label[i][j].setBackground(Color.black);
                        } else {
                            label[i][j].setBackground(Color.white);
                        }
                        chessBoard.add(label[i][j]);
                    }
                }
            }
        }
        chessBoard.setBorder(BorderFactory.createLineBorder(Color.GRAY, 5));

        /**create check box**/
        svm.setText("Show Valid Moves");
        svm.setBounds(540, 10, 400, 20);
		    svm.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
              if(e.getStateChange() == ItemEvent.SELECTED){
                for(int i = 0; i < 8; i++){
                   for(int j = 0; j < 8; j++){
                       if(model.isValid(i,j,currentPlayer)){
                         ImageIcon icon = new ImageIcon(picPath[6]);
                         icon.setImage(icon.getImage().getScaledInstance(30, 30,Image.SCALE_DEFAULT));
                         label[i][j].setIcon(icon);
                       }
                   }
                }
              } else {
                for(int i = 0; i < 8; i++){
                   for(int j = 0; j < 8; j++){
                       if(model.isValid(i,j,currentPlayer)){
                         label[i][j].setIcon(null);
                       }
                   }
                }
              }
            }
        });

        /**menu bar **/
        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu game = new JMenu("Game");
        JMenu network = new JMenu("Network");
        JMenu help = new JMenu("Help");
        JMenuItem newGame = new JMenuItem("New game");
        JMenuItem load = new JMenuItem("Load");
        JMenuItem save = new JMenuItem("Save");
        JMenuItem exit = new JMenuItem("Exit");
        load.setEnabled(false);
        save.setEnabled(false);
        newGame.setActionCommand("new game");
        newGame.addActionListener(controller);
        exit.addActionListener((event) -> System.exit(0));
        file.add(newGame);
        file.add(load);
        file.add(save);
        file.add(exit);

        JMenu reskin = new JMenu("Resking Pieces");
        JMenu debug = new JMenu("Debug Scenarios");
        game.add(reskin);
        game.add(debug);
        ButtonGroup reskinGroup = new ButtonGroup();
        JRadioButtonMenuItem normal = new JRadioButtonMenuItem("Normal Pieces (black and white)");
        JRadioButtonMenuItem cd = new JRadioButtonMenuItem("Cats VS. Dogs");
        JRadioButtonMenuItem pb = new JRadioButtonMenuItem("Pumpkins VS. Bats");
        normal.setSelected(true);
        pieces = 0;
        reskin.add(normal);
        reskin.add(cd);
        reskin.add(pb);
        normal.setActionCommand("Normal Pieces");
        normal.addActionListener(controller);
        cd.setActionCommand("CD");
        cd.addActionListener(controller);
        pb.setActionCommand("PB");
        pb.addActionListener(controller);
        reskinGroup.add(normal);
        reskinGroup.add(cd);
        reskinGroup.add(pb);

        ButtonGroup debugGroup = new ButtonGroup();
        JRadioButtonMenuItem normalGame = new JRadioButtonMenuItem("Normal Game");
        JRadioButtonMenuItem corner = new JRadioButtonMenuItem("Corner Test");
        JRadioButtonMenuItem side = new JRadioButtonMenuItem("Side Tests");
        JRadioButtonMenuItem capture1 = new JRadioButtonMenuItem("1x Capture Test");
        JRadioButtonMenuItem capture2 = new JRadioButtonMenuItem("2x Capture Test");
        JRadioButtonMenuItem empty = new JRadioButtonMenuItem("Empty Board");
        JRadioButtonMenuItem inner = new JRadioButtonMenuItem("Inner Square Test");
        normalGame.setSelected(true);
        model.initialize(0);
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(model.getBoard(i,j) == 1){
                    ImageIcon icon = new ImageIcon(picPath[0]);
                    icon.setImage(icon.getImage().getScaledInstance(30, 30,Image.SCALE_DEFAULT));
                    label[i][j].setIcon(icon);
                } else if(model.getBoard(i,j) == 2){
                    ImageIcon icon = new ImageIcon(picPath[1]);
                    icon.setImage(icon.getImage().getScaledInstance(30, 30,Image.SCALE_DEFAULT));
                    label[i][j].setIcon(icon);
                }
            }
        }

        debug.add(normalGame);
        debug.add(corner);
        debug.add(side);
        debug.add(capture1);
        debug.add(capture2);
        debug.add(empty);
        debug.add(inner);
        normalGame.setActionCommand("Normal Game");
        normalGame.addActionListener(controller);
        corner.setActionCommand("corner");
        corner.addActionListener(controller);
        side.setActionCommand("side");
        side.addActionListener(controller);
        capture1.setActionCommand("capture1");
        capture1.addActionListener(controller);
        capture2.setActionCommand("capture2");
        capture2.addActionListener(controller);
        empty.setActionCommand("empty");
        empty.addActionListener(controller);
        inner.setActionCommand("inner");
        inner.addActionListener(controller);

        debugGroup.add(normalGame);
        debugGroup.add(corner);
        debugGroup.add(side);
        debugGroup.add(capture1);
        debugGroup.add(capture2);
        debugGroup.add(empty);
        debugGroup.add(inner);

        connection = new JMenuItem("New Connection");
        disconnect = new JMenuItem("Disconnect");
        connection.setActionCommand("connection");
        connection.addActionListener(controller);
        disconnect.setActionCommand("disconnect");
        disconnect.addActionListener(controller);
        disconnect.setEnabled(false);
        network.add(connection);
        network.add(disconnect);

        JMenuItem about = new JMenuItem("About");
        about.setActionCommand("About");
        about.addActionListener(controller);
        help.add(about);

        menuBar.add(file);
        menuBar.add(game);
        menuBar.add(network);
        menuBar.add(help);
        setJMenuBar(menuBar);

        /**right pink area**/
        setupTextArea();

		    /**the bottom area of pink part**/
        piecesLabel.setBounds(545, 480, 440, 50);
        piecesLabel.setText("<html>Player 1 pieces:<br><br>Player 2 pieces:</html>");
        piecesLabel.setLayout(null);
        ImageIcon bicon;
        ImageIcon wicon;
        if(normal.isSelected()){
            bicon = new ImageIcon(picPath[0]);
            wicon = new ImageIcon(picPath[1]);
        } else if(cd.isSelected()){
            bicon = new ImageIcon(picPath[2]);
            wicon = new ImageIcon(picPath[3]);
        } else {
            bicon = new ImageIcon(picPath[4]);
            wicon = new ImageIcon(picPath[5]);
        }
        bicon.setImage(bicon.getImage().getScaledInstance(20, 20,Image.SCALE_DEFAULT));
        bplayer = new JLabel(String.valueOf(model.getChips(1)),bicon,SwingConstants.RIGHT);
        bplayer.setBounds(380, 3, 40, 20);
        piecesLabel.add(bplayer);
        wicon.setImage(wicon.getImage().getScaledInstance(20, 20,Image.SCALE_DEFAULT));
        wplayer = new JLabel(String.valueOf(model.getChips(2)),wicon,SwingConstants.RIGHT);
        wplayer.setBounds(380, 28, 40, 20);
        piecesLabel.add(wplayer);

        /**the bottom part of textarea and submit button**/
        JPanel submitPanel = new JPanel();
        submitPanel.setBackground(Color.white);
        submitPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 10));
        submitPanel.setBounds(0, 535, 985, 51);
        userInput = new JTextField();
        userInput.setBounds(10, 10, 890, 35);
        submitButton = new JButton();
        submitButton.setText("Submit");
        submitButton.setEnabled(false);
        submitButton.setBounds(892, 10, 90, 37);
        submitButton.setForeground(Color.red);
        submitButton.setBackground(Color.black);
        submitButton.setOpaque(true);
        submitButton.setActionCommand("submit");
        submitButton.addActionListener(controller);
        submitButton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        submitPanel.setLayout(null);
        submitPanel.add(userInput);
        submitPanel.add(submitButton);

        /**store all small panels to the whole panel**/
        controlPanel.add(chessBoard);
        controlPanel.add(submitPanel);
        controlPanel.add(svm);
        controlPanel.add(piecesLabel);
        JScrollPane scroll = new JScrollPane (pinkArea);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setBounds(535, 30, 450, 450);

        add(scroll);
        add(controlPanel);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

	/**
	 * The method will create Button with a few attributes strains
	 *
	 * @param String text -  the title of the button
	 * @param String ac - the action command
	 * @param Color fgc - the foreground color
	 * @param Color bgc - the background color
	 * @param ActionListener handler - registered as the event manager
	 *
	 * @return Button object
	 */
    private JButton createButton(String text, String ac, Color fgc, Color bgc, ActionListener handler) {
    	  JButton bt = new JButton(text);
    	  bt.setFont(new Font("", Font.PLAIN+Font.BOLD, 15));
		    bt.setForeground(fgc);
		    bt.setBackground(bgc);
		    bt.setActionCommand(ac);
		    bt.addActionListener(handler);
        bt.setBorder(new LineBorder(Color.BLACK));
        bt.setOpaque(true);
        bt.setBorderPainted(true);
		    return bt;
    }

    //This method will refresh the board for new input.
    private void refreshBoard(){
      int x = 0, y = 1;
      if(pieces == 1){
        x = 2;
        y = 3;
      } else if(pieces == 2){
        x = 4;
        y = 5;
      }

      for(int i = 0; i < 8; i++){
          for(int j = 0; j < 8; j++){
              if(model.getBoard(i,j) == 1){
                ImageIcon icon = new ImageIcon(picPath[x]);
                icon.setImage(icon.getImage().getScaledInstance(30, 30,Image.SCALE_DEFAULT));
                label[i][j].setIcon(icon);
              } else if(model.getBoard(i,j) == 2){
                ImageIcon icon = new ImageIcon(picPath[y]);
                icon.setImage(icon.getImage().getScaledInstance(30, 30,Image.SCALE_DEFAULT));
                label[i][j].setIcon(icon);
              } else {
                label[i][j].setIcon(null);
              }
          }
        }
        if(!model.canMove(currentPlayer)){
          canMove.setText("Skip");
          canMove.setActionCommand("skip");
          pinkArea.append("Player " + currentPlayer + " has no valid moves. Press skip.\n");
        } else {
          canMove.setText("move");
          canMove.setActionCommand("move");
        }
        canMove.setEnabled(true);
        svm.setSelected(false);
        setupScore(x,y);
    }

    //This method will create a dialog for about menu
    private void dialog(){
      JDialog d = new JDialog(this, "About");
      JLabel l = new JLabel("<html>Othello Game<br>By Hansong Yang<br>and Invisible Partner<br>October 2020</html>");
      d.add(l);
      d.setSize(200, 100);
      d.setVisible(true);
    }

    //This method will setup the score panel
    private void setupScore(int i, int j){
        piecesLabel.remove(bplayer);
        piecesLabel.remove(wplayer);
        ImageIcon bicon = new ImageIcon(picPath[i]);
        ImageIcon wicon = new ImageIcon(picPath[j]);
        bicon.setImage(bicon.getImage().getScaledInstance(20, 20,Image.SCALE_DEFAULT));
        bplayer = new JLabel(String.valueOf(model.getChips(1)),bicon,SwingConstants.RIGHT);
        bplayer.setBounds(380, 3, 40, 20);
        piecesLabel.add(bplayer);
        wicon.setImage(wicon.getImage().getScaledInstance(20, 20,Image.SCALE_DEFAULT));
        wplayer = new JLabel(String.valueOf(model.getChips(2)),wicon,SwingConstants.RIGHT);
        wplayer.setBounds(380, 28, 40, 20);
        piecesLabel.add(wplayer);
        piecesLabel.paintImmediately(piecesLabel.getVisibleRect());
    }

    //This method will setup the text area
    private void setupTextArea(){
        pinkArea.setBounds(535, 30, 450, 450);
        pinkArea.setText("Player 1 initialized with " + model.getChips(1) + " piece(s). \nPlayer 2 initialized with " + model.getChips(2) + " piece(s).\n");
        pinkArea.setOpaque(true);
        pinkArea.setLineWrap(true);
        pinkArea.setWrapStyleWord(true);
        pinkArea.setBackground(Color.pink);
        pinkArea.setEditable(false);
        pinkArea.setBorder(BorderFactory.createLineBorder(Color.GRAY, 5));
    }

    //This method will enable or disable some buttons depends on the connection with the server
    private void connected(boolean connected){
        if(connected){
            connection.setEnabled(false);
            submitButton.setEnabled(true);
            disconnect.setEnabled(true);
        } else {
            connection.setEnabled(true);
            submitButton.setEnabled(false);
            disconnect.setEnabled(false);
        }
    }

    //This method will display incoming messages from the server
    public static void displayMessage(String result){
        if(result.startsWith("who")){
          String s = result.substring(3, result.length());
          String[] arr = s.split(" ");
          pinkArea.append("Currently on the server : \n");
          for(int i = 0; i < arr.length; i++){
              pinkArea.append(arr[i] + "\n");
          }
        } else if(result.startsWith("name")){
            String s = result.substring(4, result.length());
            pinkArea.append(s+"\n");
        } else if(result.startsWith("broadcast")){
            String s = result.substring(9, result.length());
            pinkArea.append(s+"\n");
        }
    }

	/**
	 * This class is a private inner class for controller
	 *
	 * @author
	 * @version 1
	 * @date
	 *
	 */
    private class Controller implements ActionListener{
      int row = -1;
      int col = -1;
      int mode = 0;
		@Override
		public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("new game")){
            model.initialize(mode);
            refreshBoard();
            setupTextArea();
        } else if(e.getActionCommand().equals("Normal Pieces")){
            for(int i = 0; i < 8; i++){
                for(int j = 0; j < 8; j++){
                    if(model.getBoard(i,j) == 1){
                      ImageIcon icon = new ImageIcon(picPath[0]);
                      icon.setImage(icon.getImage().getScaledInstance(30, 30,Image.SCALE_DEFAULT));
                      label[i][j].setIcon(icon);
                    } else if(model.getBoard(i,j) == 2){
                      ImageIcon icon = new ImageIcon(picPath[1]);
                      icon.setImage(icon.getImage().getScaledInstance(30, 30,Image.SCALE_DEFAULT));
                      label[i][j].setIcon(icon);
                    }
                }
            }
            pieces = 0;
            setupScore(0,1);
        } else if(e.getActionCommand().equals("CD")){
            for(int i = 0; i < 8; i++){
                for(int j = 0; j < 8; j++){
                    if(model.getBoard(i,j) == 1){
                        ImageIcon icon = new ImageIcon(picPath[2]);
                        icon.setImage(icon.getImage().getScaledInstance(30, 30,Image.SCALE_DEFAULT));
                        label[i][j].setIcon(icon);
                    } else if(model.getBoard(i,j) == 2){
                        ImageIcon icon = new ImageIcon(picPath[3]);
                        icon.setImage(icon.getImage().getScaledInstance(30, 30,Image.SCALE_DEFAULT));
                        label[i][j].setIcon(icon);
                    }
                }
            }
            pieces = 1;
            setupScore(2,3);
          } else if(e.getActionCommand().equals("PB")){
              for(int i = 0; i < 8; i++){
                for(int j = 0; j < 8; j++){
                    if(model.getBoard(i,j) == 1){
                      ImageIcon icon = new ImageIcon(picPath[4]);
                      icon.setImage(icon.getImage().getScaledInstance(30, 30,Image.SCALE_DEFAULT));
                      label[i][j].setIcon(icon);
                    } else if(model.getBoard(i,j) == 2){
                      ImageIcon icon = new ImageIcon(picPath[5]);
                      icon.setImage(icon.getImage().getScaledInstance(30, 30,Image.SCALE_DEFAULT));
                      label[i][j].setIcon(icon);
                    }
                  }
              }
              pieces = 2;
              setupScore(4,5);
         } else if (e.getActionCommand().equals("About")) {
            dialog();
         } else if(e.getActionCommand().equals("Normal Game")){
            model.initialize(0);
            mode = 0;
            currentPlayer = 1;
         } else if(e.getActionCommand().equals("corner")){
            model.initialize(1);
            mode = 1;
            currentPlayer = 1;
         } else if(e.getActionCommand().equals("side")){
            model.initialize(2);
            currentPlayer = 1;
            mode = 2;
         } else if(e.getActionCommand().equals("capture1")){
            model.initialize(3);
            mode = 3;
            currentPlayer = 1;
         } else if(e.getActionCommand().equals("capture2")){
            model.initialize(4);
            mode = 4;
            currentPlayer = 1;
         } else if(e.getActionCommand().equals("empty")){
            model.initialize(5);
            mode = 5;
            currentPlayer = 1;
         } else if(e.getActionCommand().equals("inner")){
            model.initialize(6);
            mode = 6;
            currentPlayer = 1;
         } else if(e.getActionCommand().length() == 1){
            if(e.getActionCommand().matches("[-+]?\\d*\\.?\\d+")){
                numberButton[Integer.valueOf(e.getActionCommand())-1].setBackground(Color.WHITE);
                row = Integer.valueOf(e.getActionCommand())-1;
                for(int i = 0; i < numberButton.length; i++){
                  if(i != Integer.valueOf(e.getActionCommand())-1){
                      numberButton[i].setBackground(Color.GRAY);
                  }
                }
            } else {
                letterButton[e.getActionCommand().charAt(0) - 'A'].setBackground(Color.WHITE);
                col = e.getActionCommand().charAt(0) - 'A';
                for(int i = 0; i < letterButton.length; i++){
                  if(i != e.getActionCommand().charAt(0) - 'A'){
                      letterButton[i].setBackground(Color.GRAY);
                  }
                }
            }
         } else if(e.getActionCommand().equals("move")){
            if(row == -1 || col == -1){
              return;
            }
            int captured = model.move(row,col,currentPlayer);
            if(captured > 0){
                numberButton[row].setBackground(Color.GRAY);
                letterButton[col].setBackground(Color.GRAY);
                if(currentPlayer == 2){
                    pinkArea.append("Player 2 has captured " + captured + (captured > 1 ? " pieces.\n" : " piece. \n"));
                    currentPlayer = 1;
                } else {
                    pinkArea.append("Player 1 has captured " + captured + (captured > 1 ? " pieces.\n" : " piece. \n"));
                    currentPlayer = 2;
                }
                refreshBoard();
            }
         } else if(e.getActionCommand().equals("skip")){
             if(currentPlayer == 2){
                  currentPlayer = 1;
             } else {
                  currentPlayer = 2;
             }
             if(!model.canMove(currentPlayer)){
                pinkArea.append("END OF Game\n");
                int bChips = model.getChips(1);
                int wChips = model.getChips(2);
                pinkArea.append("Player 1 finishes with " + bChips + " pieces.\n");
                pinkArea.append("Player 2 finishes with " + wChips + " pieces.\n");
                if(bChips > wChips){
                    pinkArea.append("Player 1 wins! \n\n");
                } else if(bChips < wChips){
                    pinkArea.append("Player 2 wins! \n\n");
                } else {
                    pinkArea.append("Tie Game! \n\n");
                }
                pinkArea.append("Play Again? (Select \'New Game\' from the File menu.)\n");
                canMove.setEnabled(false);
                currentPlayer = 1;
             } else{
                refreshBoard();
             }
         } else if(e.getActionCommand().equals("disconnect")){
              pinkArea.append("Disconnecting from network connection.\n");
              connected(false);
              networkDialog.disconnect();
         } else if(e.getActionCommand().equals("connection")){
              Point thisLocation = getLocation();
              Dimension parentSize = getSize();
              Dimension dialogSize = networkDialog.getSize();
              int offsetX = (parentSize.width-dialogSize.width)/2+thisLocation.x; int offsetY = (parentSize.height-dialogSize.height)/2+thisLocation.y;
              networkDialog.setLocation(offsetX,offsetY);
              networkDialog.setVisible(true);
              if(networkDialog.pressedConnect()){
                  pinkArea.append("Negotiating Connection to " + networkDialog.getAddress() + " on Port " + networkDialog.getPort() + "\n");
              }
              if(networkDialog.connected()){
                  pinkArea.append("Connection Successful.\n");
                  pinkArea.append("Welcome to Hansong Yang's Othello Server.\n");
                  pinkArea.append("Use \'/help\' for commands.\n");
                  connected(true);
              } else {
                  pinkArea.append("Error: Connection refused. Server is not available. Check port or restart server.\n");
                  connected(false);
              }
         } else if(e.getActionCommand().equals("submit")){
              if(userInput.getText().length() < 0){
                  return;
              }

              if(userInput.getText().equals("/help")){
                  pinkArea.append("HELP: \n");
                  pinkArea.append("/help: this message. \n");
                  pinkArea.append("/bye: disconnect.\n");
                  pinkArea.append("/who: shows the name of all connected players.\n");
                  pinkArea.append("/name (name): Rename yourself.\n");
              } else if(userInput.getText().equals("/bye")){
                  networkDialog.disconnect();
                  pinkArea.append("SERVER: Disconnecting.\n");
                  pinkArea.append("Disconnected from server.\n");
                  connected(false);
              } else if(userInput.getText().equals("/who")){
                  networkDialog.who();
              } else if(userInput.getText().startsWith("/name")){
                  networkDialog.rename(userInput.getText().substring(6,userInput.getText().length()));
              } else {
                  networkDialog.message(userInput.getText());
                  pinkArea.append(networkDialog.getName() + ": " + userInput.getText()+"\n");
              }
         }
      }
	 }
}
