package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class Main {
    public static void main(String[] args) {
        JFrame mainFrame = new JFrame("Insomnia");
        mainFrame.setMinimumSize(new Dimension(1050, 450));
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);

        JMenuBar menuBarOfApplication = new JMenuBar();
        //---------------
        JMenu application = new JMenu("Application");
        application.setMnemonic(KeyEvent.VK_A);
        JMenu edit = new JMenu("Edit");
        edit.setMnemonic(KeyEvent.VK_E);
        JMenu view = new JMenu("View");
        view.setMnemonic(KeyEvent.VK_V);
        JMenu window = new JMenu("Window");
        window.setMnemonic(KeyEvent.VK_W);
        JMenu tools = new JMenu("Tools");
        tools.setMnemonic(KeyEvent.VK_T);
        JMenu help = new JMenu("Help");
        help.setMnemonic(KeyEvent.VK_H);
        //--------------
        /*
            menuItems of the application menu
         */
        JMenuItem preferences = new JMenuItem("Preferences");
        preferences.setMnemonic((KeyEvent.VK_P));
        preferences.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("choose preferences from the Application of the menubar");
            }
        });
        application.add(preferences);

        JMenuItem changelog = new JMenuItem("Changelog");
        changelog.setMnemonic(KeyEvent.VK_C);
        changelog.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("choose change log from the Application of the menubar");
            }
        });
        application.add(changelog);

        application.addSeparator();

        JMenuItem hideInsomnia = new JMenuItem("Hide Insomnia");
        hideInsomnia.setMnemonic(KeyEvent.VK_H);
        hideInsomnia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("choose hide insomnia from the Application of the menubar");
            }
        });
        application.add(hideInsomnia);

        JMenuItem hideOthers = new JMenuItem("HideOthers");
        hideOthers.setMnemonic(KeyEvent.VK_I);
        hideOthers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("choose hide others from the Application of the menubar");
            }
        });
        application.add(hideOthers);

        application.addSeparator();

        JMenuItem quit = new JMenuItem("Quit", new ImageIcon("C:\\Users\\venus\\Desktop\\uni\\barnameneVC pishrafte\\ProjeMid\\src\\GUI\\log-off-icon.png"));
        quit.setMnemonic(KeyEvent.VK_Q);
        quit.setToolTipText("quit the application");
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("choose quit from the Application of the menubar");
            }
        });
        application.add(quit);

        //--------------
        /*
            menuItems of the Edit menu
         */
        JMenuItem undo = new JMenuItem("Undo", new ImageIcon("C:\\Users\\venus\\Desktop\\uni\\barnameneVC pishrafte\\ProjeMid\\src\\GUI\\undo-icon.png"));
        undo.setMnemonic((KeyEvent.VK_U));
        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("choose undo from the Edit of the menubar");
            }
        });
        edit.add(undo);

        JMenuItem redo = new JMenuItem("redo", new ImageIcon("C:\\Users\\venus\\Desktop\\uni\\barnameneVC pishrafte\\ProjeMid\\src\\GUI\\redo-icon.png"));
        redo.setMnemonic(KeyEvent.VK_R);
        redo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("choose redo from the Edit of the menubar");
            }
        });
        edit.add(redo);

        edit.addSeparator();

        JMenuItem cut = new JMenuItem("Cut", new ImageIcon("C:\\Users\\venus\\Desktop\\uni\\barnameneVC pishrafte\\ProjeMid\\src\\GUI\\cut-icon.png"));
        cut.setMnemonic(KeyEvent.VK_C);
        cut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("choose cut insomnia from the edit menubar");
            }
        });
        edit.add(cut);

        JMenuItem copy = new JMenuItem("Copy", new ImageIcon("C:\\Users\\venus\\Desktop\\uni\\barnameneVC pishrafte\\ProjeMid\\src\\GUI\\copy-icon.png"));
        copy.setMnemonic(KeyEvent.VK_O);
        copy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("choose copy insomnia from the edit menubar");
            }
        });
        edit.add(copy);
        JMenuItem paste = new JMenuItem("Paste");
        paste.setMnemonic(KeyEvent.VK_P);
        paste.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("choose paste insomnia from the edit menubar");
            }
        });
        edit.add(paste);
        JMenuItem selectAll = new JMenuItem("Select All");
        selectAll.setMnemonic(KeyEvent.VK_S);
        selectAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("choose Select All insomnia from the edit menubar");
            }
        });
        edit.add(selectAll);

        /*
            menuItems of the View menu
         */
        JMenuItem toggleFullScreen = new JMenuItem("Toggle Full Screen");
        //source of the next line:
        //https://stackoverflow.com/questions/61576224/swing-full-screen-shortcut
        toggleFullScreen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F11, KeyEvent.SHIFT_MASK));
        toggleFullScreen.addActionListener(e -> mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH));
        view.add(toggleFullScreen);

        /*
            menuItems of the Help menu
         */
        JMenuItem keyBoardShortCuts = new JMenuItem("KeyBoard Short Cuts");
        keyBoardShortCuts.setMnemonic(KeyEvent.VK_K);
        help.add(keyBoardShortCuts);

        JMenuItem about = new JMenuItem("About");
        about.setMnemonic(KeyEvent.VK_O);
        help.add(about);

        //adding the menus created to the menu bar
        menuBarOfApplication.add(application);
        menuBarOfApplication.add(edit);
        menuBarOfApplication.add(view);
        menuBarOfApplication.add(window);
        menuBarOfApplication.add(tools);
        menuBarOfApplication.add(help);

        //adding the menu bar created to the frame of the application
        mainFrame.setJMenuBar(menuBarOfApplication);

        //working with the layout -> layout choosen was GridBagLayout
        mainFrame.setLayout(new GridBagLayout());



        GridBagConstraints constraintsInsomniaLabel = new GridBagConstraints();
        //preparing the Insomnia label to be add
//        constraintsInsomniaLabel.fill = GridBagConstraints.BOTH;
        //(0,0)
        constraintsInsomniaLabel.gridx = 0;
        constraintsInsomniaLabel.gridy = 0;
        constraintsInsomniaLabel.gridwidth = 1;
        constraintsInsomniaLabel.gridheight = 1;
        //growing constant
        constraintsInsomniaLabel.weightx = 0;
        constraintsInsomniaLabel.weighty = 1;
        //padding sides
        constraintsInsomniaLabel.ipady = 0;
        constraintsInsomniaLabel.ipadx = 0;
        constraintsInsomniaLabel.anchor = GridBagConstraints.FIRST_LINE_START;
        JLabel title = new JLabel("Insomnia",SwingConstants.CENTER);
        title.setBounds(new Rectangle(100,100));
        title.setMaximumSize(new Dimension(100,100));
        title.setForeground(Color.white);
        title.setFont(new Font("SansSerif", Font.BOLD, 23));
        title.setBackground(new java.awt.Color(123, 104, 238));
        title.setOpaque(true);
        mainFrame.add(title, constraintsInsomniaLabel);


        //preparing the second head part as a panel
        JPanel secondUpPart = new JPanel(new FlowLayout(FlowLayout.CENTER));
        secondUpPart.setMaximumSize(new Dimension(200,60));
        secondUpPart.setBorder(BorderFactory.createLineBorder(new java.awt.Color(218,218,218)));
        secondUpPart.setBounds(new Rectangle(200,60));
        GridBagConstraints constraintsCommand = new GridBagConstraints();
        constraintsCommand.gridx = 1;
        constraintsCommand.gridy = 0;
        //growing constant
        constraintsCommand.weightx = 1;
        constraintsCommand.weighty = 0;
        constraintsCommand.anchor = GridBagConstraints.FIRST_LINE_START;
//        constraintsInsomniaLabel.fill = GridBagConstraints.BOTH;

        //badan bayad commandesh chi bashe ro malum koni
        //creating components of the JPanel
        String commandTitle = "HEAD";
        JLabel command = new JLabel(commandTitle, SwingConstants.CENTER);
        command.setPreferredSize(new Dimension(200, 48));
        command.setForeground(Color.white);
        command.setFont(new Font("SansSerif", Font.BOLD, 23));
        command.setBackground(new java.awt.Color(166, 166, 166));
        command.setOpaque(true);
        secondUpPart.add(command);


        JTextField addressField = new JTextField("https://api.myproduct.com/v1/users");
        addressField.setForeground(new java.awt.Color(166, 166, 166));
        addressField.setFont(new Font("Serif", Font.PLAIN, 17));
        addressField.setPreferredSize( new Dimension(350, 48));
        secondUpPart.add(addressField);

        JButton sendButton = new JButton("Send");
        sendButton.setBackground(Color.white);
        sendButton.setPreferredSize(new Dimension(100 , 48));
        sendButton.setForeground(new java.awt.Color(166, 166, 166));
        secondUpPart.add(sendButton);

        mainFrame.add(secondUpPart, constraintsCommand);

        //preparing the third head part as a panel
        JPanel thirdUpPart = new JPanel(new FlowLayout(FlowLayout.LEFT, 10,10));
//        thirdUpPart.setPreferredSize(new Dimension(200,60));
        thirdUpPart.setMaximumSize(new Dimension(200,60));
        thirdUpPart.setBounds(new Rectangle(200,60));
        thirdUpPart.setBorder(BorderFactory.createLineBorder(new java.awt.Color(218,218,218)));
        GridBagConstraints infoCommand = new GridBagConstraints();
        infoCommand.gridx = 2;
        infoCommand.gridy = 0;
        //growing constant
        infoCommand.weightx = 1;
        infoCommand.weighty = 0;
        infoCommand.anchor = GridBagConstraints.FIRST_LINE_START;
        infoCommand.fill = GridBagConstraints.BOTH;

        //badan bayad time va hajmesh ro bedast biyari
        //creating components of the JPanel
        JLabel error = new JLabel("Error", SwingConstants.CENTER);
        error.setPreferredSize(new Dimension(50,35));
        error.setBorder(BorderFactory.createLineBorder(new java.awt.Color(218,218,218)));
        error.setForeground(Color.white);
        error.setFont(new Font("Serif", Font.BOLD, 15));
        error.setBackground(new java.awt.Color(205, 71, 78));
        error.setOpaque(true);
        thirdUpPart.add(error);

        String time = "0ms";
        JLabel timeTaken = new JLabel(time, SwingConstants.CENTER);
        timeTaken.setPreferredSize(new Dimension(40,30));
        timeTaken.setBorder(BorderFactory.createLineBorder(new java.awt.Color(218,218,218)));
        timeTaken.setForeground(new java.awt.Color(128, 128,128));
        timeTaken.setFont(new Font("Serif", Font.BOLD, 15));
        timeTaken.setBackground(new java.awt.Color(166, 166, 166));
        timeTaken.setOpaque(true);
        thirdUpPart.add(timeTaken);

        String dataUsed = "0B";
        JLabel netTaken = new JLabel(dataUsed, SwingConstants.CENTER);
        netTaken.setPreferredSize(new Dimension(40,30));
        netTaken.setBorder(BorderFactory.createLineBorder(new java.awt.Color(218,218,218)));
        netTaken.setForeground(new java.awt.Color(128,128,128));
        netTaken.setFont(new Font("Serif", Font.BOLD, 15));
        netTaken.setBackground(new java.awt.Color(166, 166, 166));
        netTaken.setOpaque(true);
        thirdUpPart.add(netTaken);
        mainFrame.add(thirdUpPart, infoCommand);


        //the first panel down the "Insomnia" label containig the history of requests
        JPanel historialOfRequest = new JPanel();
        historialOfRequest.setPreferredSize(new Dimension(112, 500));
        historialOfRequest.setMaximumSize(new Dimension(112, 500));
        historialOfRequest.setSize(new Dimension(112, 500));
        historialOfRequest.setBorder(BorderFactory.createLineBorder(new java.awt.Color(128, 128, 128)));
        historialOfRequest.setBackground(new java.awt.Color(38, 38, 38));
        GridBagConstraints historialConstraints = new GridBagConstraints();
        historialConstraints.gridx = 0;
        historialConstraints.gridy = 1;
        //growing constant
        historialConstraints.weightx = 1;
        historialConstraints.weighty = 1;
        historialConstraints.anchor = GridBagConstraints.LINE_START;
        constraintsInsomniaLabel.fill = GridBagConstraints.BOTH;
        mainFrame.add(historialOfRequest, historialConstraints);


        //the second panel down the "Insomnia" label containig the history of requests
        JTabbedPane setRequest = new JTabbedPane();
        setRequest.setPreferredSize(new Dimension(600, 500));
        setRequest.setBorder(BorderFactory.createLineBorder(new java.awt.Color(128, 128, 128)));
        setRequest.setBackground(new java.awt.Color(38, 38, 38));
        GridBagConstraints settingRequestConstraints = new GridBagConstraints();
        settingRequestConstraints.gridx = 1;
        settingRequestConstraints.gridy = 1;
        //growing constant
        settingRequestConstraints.weightx = 1;
        settingRequestConstraints.weighty = 1;
        settingRequestConstraints.anchor = GridBagConstraints.LINE_START;
        settingRequestConstraints.fill = GridBagConstraints.BOTH;
        mainFrame.add(setRequest, settingRequestConstraints);

        mainFrame.pack();

    }
}