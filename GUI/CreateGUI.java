package GUI;

import GUI.CellRendererForBodyComboBox;
import com.sun.xml.internal.ws.resources.DispatchMessages;
import javafx.scene.control.Tooltip;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.stream.Location;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.File;

public class CreateGUI {
    private JFrame mainFrame;
    private static int timesBodyTypeComboBoxChanged = 0;
    private static int timesAuthTypeComboBoxChanged = 0;


    //the photos color
    private final static String purple = "\\purple";
    private final static String blue = "\\blue";
    private static String colorOfThemeForground = purple;
    //the BackGround color
    private final static Color light1 = new java.awt.Color(118,118,118);
    private final static Color light2 = new java.awt.Color(229,229,229);
    private final static Color dark1 = new java.awt.Color(128,128,128);
    private final static Color dark2 = new java.awt.Color(38, 38, 38);
    private static Color colorOfThemeBackground1 = light1;
    private static Color colorOfThemeBackground2 = light2;

    private JLabel insomnia;
    private GridBagConstraints insomniaConstraints;
    private JScrollPane downInsomnia;
    private GridBagConstraints downInsomniaConstraints;
    private boolean sideBar = true;
    private ImageIcon[] icons = new ImageIcon[26];


    public CreateGUI() {
        mainFrame = new JFrame("Insomnia");
        Image taskbarIcon = Toolkit.getDefaultToolkit().getImage((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\my-app-icon1.png");

        mainFrame.setIconImage(taskbarIcon);

        mainFrame.setMinimumSize(new Dimension(1500, 450));
        mainFrame.setMaximumSize(new Dimension(1520, 1080));
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
        mainFrame.setBackground(new java.awt.Color(128,128, 128));
        createMenuBar();

        //working with the layout -> layout choosen was GridBagLayout
        mainFrame.setLayout(new GridBagLayout());

        createInsomniaDisplayArea();
        createRequestClasifier();
        createRequestInfoPanel();
        createHistorialRequest();
        createRequestInfo();
        createRequestHistoryPanel();



        //preparing "Hide to system Tray" mode
        TrayIcon trayIcon;
        SystemTray tray;
        tray=SystemTray.getSystemTray();

        Image image = Toolkit.getDefaultToolkit().getImage((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\my-app-icon1.png");

        //creating the pop up menu to be shown when clicked on the tray icon
        PopupMenu trayPopupMenu=new PopupMenu();
        MenuItem exitOption=new MenuItem("Exit");
        exitOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //when clicked in this item the frame will close and the program will end
                System.out.println("Thanks for using my app!  \n:)");
                mainFrame.dispose();
                System.exit(0);
            }
        });
        trayPopupMenu.add(exitOption);
        MenuItem openOption = new MenuItem("Open");
        openOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.setVisible(true);
                mainFrame.setExtendedState(JFrame.NORMAL);
            }
        });
        trayPopupMenu.add(openOption);
        //preparing the tray icon
        trayIcon=new TrayIcon(image, ":)", trayPopupMenu);
        trayIcon.setImageAutoSize(true);

        //syncing the main frame
        mainFrame.addWindowStateListener(new WindowStateListener() {
            //sorce of the next method code:
            //https://stackoverflow.com/questions/7461477/how-to-hide-a-jframe-in-system-tray-of-taskbar
            @Override
            public void windowStateChanged(WindowEvent e) {
                if(e.getNewState()==JFrame.ICONIFIED){
                    try {
                        tray.add(trayIcon);
                        mainFrame.setVisible(false);
                        System.out.println("added to SystemTray");
                    } catch (AWTException ex) {
                        System.out.println("unable to add to tray");
                    }
                }
                //the code above is for fixing another little bug, but its basically the same as before
                if(e.getNewState()==7){
                    try{
                        tray.add(trayIcon);
                        mainFrame.setVisible(false);
                        System.out.println("added to SystemTray");
                    }catch(AWTException ex){
                        System.out.println("unable to add to system tray");
                    }
                }
                if(e.getNewState()==JFrame.MAXIMIZED_BOTH){
                    tray.remove(trayIcon);
                    mainFrame.setVisible(true);
                    System.out.println("Tray icon removed");
                }
                if(e.getNewState()==JFrame.NORMAL){
                    tray.remove(trayIcon);
                    mainFrame.setVisible(true);
                    System.out.println("Tray icon removed");
                }
            }
        });
        //the icon in the top-left of the mainFrame window
        mainFrame.setIconImage(Toolkit.getDefaultToolkit().getImage((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\my-app-icon1.png"));

        mainFrame.pack();

    }

    private void createMenuBar() {

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

        JMenuItem quit = new JMenuItem("Quit", new ImageIcon((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\log-off-icon.png"));
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
        JMenuItem undo = new JMenuItem("Undo", new ImageIcon((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\undo-icon.png"));
        undo.setMnemonic((KeyEvent.VK_U));
        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("choose undo from the Edit of the menubar");
            }
        });
        edit.add(undo);

        JMenuItem redo = new JMenuItem("redo", new ImageIcon((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\redo-icon.png"));
        redo.setMnemonic(KeyEvent.VK_R);
        redo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("choose redo from the Edit of the menubar");
            }
        });
        edit.add(redo);

        edit.addSeparator();

        JMenuItem cut = new JMenuItem("Cut", new ImageIcon((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\cut-icon.png"));
        cut.setMnemonic(KeyEvent.VK_C);
        cut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("choose cut insomnia from the edit menubar");
            }
        });
        edit.add(cut);

        JMenuItem copy = new JMenuItem("Copy", new ImageIcon((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\copy-icon.png"));
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

        //Toggle Side Bar
        JMenuItem toggleSideBar = new JMenuItem("Toggle Side Bar");
        toggleSideBar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.SHIFT_MASK));
        toggleSideBar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Dimension sizeOfNow = mainFrame.getSize();
                Point locationOfNow = mainFrame.getLocationOnScreen();
                if(sideBar){
                //now its false and we have to remove it from the main frame
                    mainFrame.remove(insomnia);
                    mainFrame.remove(downInsomnia);
                }else{
                    mainFrame.add(insomnia, insomniaConstraints);
                    mainFrame.add(downInsomnia, downInsomniaConstraints);
                }
                mainFrame.setPreferredSize(sizeOfNow);
                mainFrame.setLocation(locationOfNow);
                mainFrame.revalidate();
                mainFrame.repaint();
                mainFrame.pack();
                sideBar=!sideBar;
            }

        });
        view.add(toggleSideBar);

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

    }

    private void createInsomniaDisplayArea() {

        GridBagConstraints constraintsInsomniaLabel = new GridBagConstraints();
        //preparing the Insomnia label to be add
        constraintsInsomniaLabel.fill = GridBagConstraints.BOTH;
        //(0,0)
        constraintsInsomniaLabel.gridx = 0;
        constraintsInsomniaLabel.gridy = 0;
        constraintsInsomniaLabel.gridwidth = 1;
        constraintsInsomniaLabel.gridheight = 1;
        //growing constant
        constraintsInsomniaLabel.weightx = 1;
        constraintsInsomniaLabel.weighty = -1;
        //padding sides
        constraintsInsomniaLabel.ipady = 0;
        constraintsInsomniaLabel.ipadx = 0;
        constraintsInsomniaLabel.anchor = GridBagConstraints.FIRST_LINE_START;
//        JLabel title = new JLabel("Insomnia", new ImageIcon((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\my-app-icon111.png"), SwingConstants.CENTER);
        JLabel title = new JLabel("Insomnia", new ImageIcon((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\my-app-icon111.png"), SwingConstants.CENTER);
        title.setBounds(new Rectangle(100, 100));
        title.setMaximumSize(new Dimension(100, 100));
        title.setForeground(Color.white);
        title.setFont(new Font("SansSerif", Font.BOLD, 23));
        if(colorOfThemeForground.equals(purple)) {
            title.setBackground(new java.awt.Color(123, 104, 238));
        }else{
            title.setBackground(new java.awt.Color(61, 189, 238));
        }
        title.setOpaque(true);
        mainFrame.add(title, constraintsInsomniaLabel);
        insomnia=title;
        insomniaConstraints=constraintsInsomniaLabel;
    }

    private void createRequestInfoPanel() {

        //preparing the second head part as a panel
        JPanel secondUpPart = new JPanel(new FlowLayout(FlowLayout.CENTER));
        secondUpPart.setBorder(BorderFactory.createLineBorder(new java.awt.Color(218, 218, 218)));
//        secondUpPart.setBounds(new Rectangle(200,60));
        secondUpPart.setSize(new Dimension(200, 60));
        GridBagConstraints constraintsCommand = new GridBagConstraints();
        constraintsCommand.gridx = 1;
        constraintsCommand.gridy = 0;
        //growing constant
        constraintsCommand.weightx = 1;
        constraintsCommand.weighty = -1;
        constraintsCommand.gridheight = 1;
        constraintsCommand.gridwidth = 1;
        constraintsCommand.anchor = GridBagConstraints.FIRST_LINE_START;
        constraintsCommand.fill = GridBagConstraints.BOTH;

        //badan bayad commandesh chi bashe ro malum koni
        //creating components of the JPanel
        Object methods[][] = {
                {new Font("Serif", Font.BOLD, 15), new java.awt.Color(47, 198, 102), "POST"},
                {new Font("Serif", Font.BOLD, 15), new java.awt.Color(239, 255, 20), "PATCH"},
                {new Font("Serif", Font.BOLD, 15), new java.awt.Color(255, 161, 20), "PUT"},
                {new Font("Serif", Font.BOLD, 15), new java.awt.Color(255, 20, 20), "DELETE"},
                {new Font("Serif", Font.BOLD, 15), new java.awt.Color(123, 104, 238), "GET"},
                {new Font("Serif", Font.BOLD, 15), new java.awt.Color(82, 218, 233), "HEAD"},
                {new Font("Serif", Font.BOLD, 15), new java.awt.Color(69, 162, 255), "OPTION"},
                {new Font("Serif", Font.BOLD, 15), Color.BLACK, "SEPARATOR"},
                {new Font("Serif", Font.BOLD, 15), Color.BLACK, "costume method"}

        };
        //the costume renderer for the JcomboBox containing the methods for sending the data
        ListCellRenderer renderer = new CellRendererForComboBox();
        JComboBox method = new JComboBox(methods);
        if(colorOfThemeForground.equals(purple)) {
            method.setForeground(new java.awt.Color(127, 113, 255));
        }else{
            method.setForeground(new java.awt.Color(54, 247, 255));
        }

        method.setPreferredSize(new Dimension(200, 48));
        if(colorOfThemeForground.equals(blue)){
            method.setBackground(new java.awt.Color(67, 154, 166));
        }else if(constraintsCommand.equals(purple)) {
            method.setBackground(new java.awt.Color(122, 111, 166));
        }
        method.setRenderer(renderer);
//        method.add(new JToolBar.Separator(),7);
        secondUpPart.add(method);


        JTextField addressField = new JTextField("https://api.myproduct.com/v1/users");
        addressField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                addressField.setText("");
            }
        });
        addressField.setForeground(new java.awt.Color(166, 166, 166));
        addressField.setFont(new Font("Serif", Font.PLAIN, 17));
        addressField.setPreferredSize(new Dimension(350, 48));
        secondUpPart.add(addressField);

        JButton sendButton = new JButton("Send");
        sendButton.setBackground(Color.white);
        sendButton.setPreferredSize(new Dimension(100, 48));
        sendButton.setForeground(new java.awt.Color(166, 166, 166));
        secondUpPart.add(sendButton);

        mainFrame.add(secondUpPart, constraintsCommand);
    }

    private void createHistorialRequest() {

        //preparing the third head part as a panel
        JPanel thirdUpPart = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
//        thirdUpPart.setPreferredSize(new Dimension(200,60));
        thirdUpPart.setMaximumSize(new Dimension(200, 60));
        thirdUpPart.setBounds(new Rectangle(200, 60));
        thirdUpPart.setBorder(BorderFactory.createLineBorder(new java.awt.Color(218, 218, 218)));
        GridBagConstraints infoCommand = new GridBagConstraints();
        infoCommand.gridx = 2;
        infoCommand.gridy = 0;
        //growing constant
        infoCommand.weightx = 1;
        infoCommand.weighty = -1;
        infoCommand.anchor = GridBagConstraints.FIRST_LINE_START;
        infoCommand.fill = GridBagConstraints.BOTH;

        //badan bayad time va hajmesh ro bedast biyari
        //creating components of the JPanel
        JLabel error = new JLabel("Error", SwingConstants.CENTER);
        error.setPreferredSize(new Dimension(50, 35));
        error.setBorder(BorderFactory.createLineBorder(new java.awt.Color(218, 218, 218)));
        error.setForeground(Color.white);
        error.setFont(new Font("Serif", Font.BOLD, 15));
        error.setBackground(new java.awt.Color(205, 71, 78));
        error.setOpaque(true);
        thirdUpPart.add(error);

        String time = "0ms";
        JLabel timeTaken = new JLabel(time, SwingConstants.CENTER);
        timeTaken.setPreferredSize(new Dimension(40, 30));
        timeTaken.setBorder(BorderFactory.createLineBorder(new java.awt.Color(218, 218, 218)));
        timeTaken.setForeground(colorOfThemeBackground1);
        timeTaken.setFont(new Font("Serif", Font.BOLD, 15));
        timeTaken.setBackground(new java.awt.Color(166, 166, 166));
        timeTaken.setOpaque(true);
        thirdUpPart.add(timeTaken);

        String dataUsed = "0B";
        JLabel netTaken = new JLabel(dataUsed, SwingConstants.CENTER);
        netTaken.setPreferredSize(new Dimension(40, 30));
        netTaken.setBorder(BorderFactory.createLineBorder(new java.awt.Color(218, 218, 218)));
        netTaken.setForeground(colorOfThemeBackground1);
        netTaken.setFont(new Font("Serif", Font.BOLD, 15));
        netTaken.setBackground(new java.awt.Color(166, 166, 166));
        netTaken.setOpaque(true);
        thirdUpPart.add(netTaken);

        //badan bayad barash pup up menu ham bezani ke bere historial ghabli haro ham bebine
        //and the historial of this request
        JButton historialThisRequest = new JButton("Last Time");
        historialThisRequest.setBackground(Color.white);
        historialThisRequest.setPreferredSize(new Dimension(100, 48));
        historialThisRequest.setForeground(new java.awt.Color(166, 166, 166));
        thirdUpPart.add(historialThisRequest);

        mainFrame.add(thirdUpPart, infoCommand);
    }

    private void createRequestClasifier() {
        //the first panel down the "Insomnia" label containig the history of requests
        JPanel historialOfRequest = new JPanel();
        int numberOfButtons = 1;
        historialOfRequest.setPreferredSize(new Dimension(112, 100 + numberOfButtons * 25));
//        historialOfRequest.setSize(new Dimension(112, 500));
        historialOfRequest.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground1));
        if(colorOfThemeForground.equals(purple)) {
            historialOfRequest.setBackground(new java.awt.Color(65, 65, 104));
        }else{
            historialOfRequest.setBackground(new java.awt.Color(42, 168, 181));
        }
        GridBagLayout gBL = new GridBagLayout();
        historialOfRequest.setLayout(gBL);
        //creating pop up menu for the left panel containing:
        // JMenuItems "New Request" and "New Folder"
        JPopupMenu leftPanelPopUpMenu = new JPopupMenu();
        //badan bayad barash ye action tarif koni
        JMenuItem newRequestItem = new JMenuItem("New Request", new ImageIcon((new File(".").getAbsolutePath())+"src\\GUI\\resource"+colorOfThemeForground+"\\newRequest-icon.png"));
        newRequestItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        JMenuItem newFolderItem = new JMenuItem("New Folder", new ImageIcon((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\newFolder-icon.png"));

        newFolderItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK + InputEvent.SHIFT_MASK));
        leftPanelPopUpMenu.add(newRequestItem);
        leftPanelPopUpMenu.add(newFolderItem);
        //with right click in this component you will see the JPopUpMenu poping up
        historialOfRequest.setComponentPopupMenu(leftPanelPopUpMenu);

        //ading the panel created
        GridBagConstraints historialConstraints = gBL.getConstraints(historialOfRequest);
        historialConstraints.gridx = 0;
        historialConstraints.gridy = 1;
        //growing constant
        historialConstraints.weightx = 1;
        historialConstraints.weighty = 1;
        historialConstraints.anchor = GridBagConstraints.PAGE_START;
        historialConstraints.fill = GridBagConstraints.BOTH;

        JScrollPane js = new JScrollPane(historialOfRequest,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        js.setPreferredSize(new Dimension(112, 500));
//        js.setMaximumSize(new Dimension(112, 500));
//        js.setMinimumSize(new Dimension(112, 500));
//        js.setSize(new Dimension(112, 500));
        mainFrame.add(js, historialConstraints);
        downInsomnia=js;
        downInsomniaConstraints=(GridBagConstraints)historialConstraints.clone();

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        //growing constant
        constraints.weightx = 0;
        constraints.weighty = 0;
        constraints.ipadx = 0;
        constraints.ipady = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.insets = new Insets(1, 1, 1, 1);

        //search bar : we have a small upper panel with a text area for search and then aside it a plus button opening th popup menu already created
        JPanel upperPart = new JPanel();
        upperPart.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground1));
        upperPart.setBackground(colorOfThemeBackground2);
        upperPart.setLayout(new FlowLayout(FlowLayout.CENTER));
        //the textField part for search
        JTextField searchField = new JTextField("Filter");
        searchField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                searchField.setText("");
            }
        });
        searchField.setPreferredSize(new Dimension(100, 30));
        searchField.setFont(new Font("SansSerif", Font.BOLD, 13));
        searchField.setBackground(colorOfThemeBackground2);
        searchField.setForeground(colorOfThemeBackground1);
        searchField.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground1));
        upperPart.add(searchField);

        //creating the "Create New Request Button"
        JButton plusButton = new JButton(new ImageIcon((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\plus-icon.png"));
        plusButton.setPreferredSize(new Dimension(25, 25));
        plusButton.setBackground(colorOfThemeBackground2);
        plusButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //this pop up menu is to be shown when the plus button is pressed
                leftPanelPopUpMenu.show(historialOfRequest, historialOfRequest.getX()+40 , historialOfRequest.getY()+20 );
            }
        });
        upperPart.add(plusButton);

        historialOfRequest.add(upperPart, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.ipadx = 0;
        constraints.ipady = 0;
        constraints.weightx = 0;
        constraints.weighty = 0;
        constraints.insets = new Insets(1, 1, 1, 1);
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        //then the requests and the folders containing them

        //now we have to add the new scrollable Jpanel down the search bar and the "new request" button
//        JPanel downSidePart = new JPanel();
//        historialOfRequest.add(downSidePart, gridConstraints);
//        downSidePart.setLayout(new BoxLayout(downSidePart, BoxLayout.Y_AXIS));


        //here is the part we get the information of the already done requests
        //and we make a JButton list of them and then we show them
        //button proto-type
        for (int i = 0; i < 5; i++) {
            if (i == 4) {
                constraints.weightx = 1;
                constraints.weighty = 1;
            }
            historialOfRequest.setPreferredSize(new Dimension(112, 100 + numberOfButtons * 25));
            JButton protoTypeButton = new JButton("prototype-request" + i, new ImageIcon((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\GET2-icon.png"));
            protoTypeButton.setMaximumSize(new Dimension(protoTypeButton.getPreferredSize().width, plusButton.getPreferredSize().height));
            protoTypeButton.setMinimumSize(new Dimension(protoTypeButton.getPreferredSize().width, plusButton.getPreferredSize().height));
            protoTypeButton.setPreferredSize(new Dimension(protoTypeButton.getPreferredSize().width, plusButton.getPreferredSize().height));
            protoTypeButton.setSize(new Dimension(protoTypeButton.getPreferredSize().width, plusButton.getPreferredSize().height));
//            protoTypeButton.setFont(new Font("SansSerif", Font.BOLD, 15));
            protoTypeButton.setBackground(colorOfThemeBackground2);
            protoTypeButton.setOpaque(true);
            protoTypeButton.setForeground(colorOfThemeBackground1);
            protoTypeButton.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground2));
            historialOfRequest.add(protoTypeButton, constraints);
            constraints.gridy += 1.0;
            numberOfButtons++;

        }
//        historialOfRequest.add(downSidePart, gridBagConstraints2);

    }

    private void createRequestInfo() {


        //the second panel down the "Insomnia" label containig the command info of requests
        JTabbedPane setRequestTabedPane = new JTabbedPane();
        setRequestTabedPane.setPreferredSize(new Dimension(600, 500));
        setRequestTabedPane.setFont(new Font("SansSerif", Font.BOLD, 15));
        setRequestTabedPane.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground1));
        setRequestTabedPane.setBackground(colorOfThemeBackground2);
        setRequestTabedPane.setForeground(colorOfThemeBackground1);
        //now the panels
        JPanel body = new JPanel();
        body.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground1));
        body.setBackground(colorOfThemeBackground2);
        setRequestTabedPane.add("", body);


        Object dataTypes[][] = {

        {new Font("Serif", Font.BOLD, 15), colorOfThemeBackground1, new ImageIcon((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\url-icon.png"), "Form Url Encoded"},
                {new Font("Serif", Font.BOLD, 15), new java.awt.Color(255, 161, 20), new ImageIcon((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\json-icon-1.png"), "JSON"},
                {new Font("Serif", Font.BOLD, 15), Color.BLACK, null, "SEPARATOR"},
                {new Font("Serif", Font.BOLD, 15), colorOfThemeBackground1, new ImageIcon((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\binary-file-icon1.png"), "Binary File"}

        };
        //the costume renderer for the JcomboBox containing the methods for sending the data
        ListCellRenderer renderer = new CellRendererForBodyComboBox();
        JComboBox dataType = new JComboBox(dataTypes);
        dataType.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (setRequestTabedPane.getSelectedIndex() != 0) {
                        setRequestTabedPane.setSelectedIndex(0);
                    } else {
                        dataType.showPopup();
                    }
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    dataType.showPopup();
                }
            }
        });

        dataType.setPreferredSize(new Dimension(200, 48));
        dataType.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground2));
        if(colorOfThemeBackground1.equals(dark1)) {
            dataType.setBackground(colorOfThemeBackground2);
        }else{
            dataType.setBackground(new java.awt.Color(93, 93, 86));
        }
        if(colorOfThemeForground.equals(purple)) {
            dataType.setForeground(new java.awt.Color(127, 113, 255));
        }else{
            dataType.setForeground(new java.awt.Color(59, 192, 255));
        }
        dataType.setForeground(Color.WHITE);
        dataType.setRenderer(renderer);
        setRequestTabedPane.setTabComponentAt(0, dataType);
        dataType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createBodyTab(body, dataType.getSelectedIndex(), timesBodyTypeComboBoxChanged);
                timesBodyTypeComboBoxChanged++;
                mainFrame.revalidate();
                mainFrame.repaint();
                mainFrame.pack();
            }
        });

        JPanel auth = new JPanel();
        auth.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground1));
        auth.setBackground(colorOfThemeBackground2);
        setRequestTabedPane.add("", auth);

        //the type of body chosen in the popup menu down the Body word of the tab reference
        Object auths[][] = {

        {new Font("Serif", Font.BOLD, 15), colorOfThemeBackground1, new ImageIcon((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\bearer-token-icon1.png"), "Bearer Token"},
                {new Font("Serif", Font.BOLD, 15), Color.BLACK, null, "SEPARATOR"},
                {new Font("Serif", Font.BOLD, 15), colorOfThemeBackground1, new ImageIcon((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\no-authentication-icon.png"), "No Authentication"}
        };

        //the costume renderer for the JcomboBox containing the methods for sending the data
        ListCellRenderer authRenderer = new CellRendererForBodyComboBox();
        JComboBox authType = new JComboBox(auths);
        authType.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (setRequestTabedPane.getSelectedIndex() != 1) {
                        setRequestTabedPane.setSelectedIndex(1);
                    } else {
                        authType.showPopup();
                    }
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    authType.showPopup();
                }
            }
        });

        authType.setPreferredSize(new Dimension(200, 48));
        authType.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground2));


        if(colorOfThemeBackground1.equals(dark1)) {
            authType.setBackground(colorOfThemeBackground2);
        }else{
            authType.setBackground(new java.awt.Color(93, 93, 86));
        }

        if(colorOfThemeBackground1.equals(dark1)) {
            authType.setForeground(colorOfThemeBackground1);
        }else{
            authType.setForeground(new java.awt.Color(1,1,1));
        }
        authType.setForeground(Color.WHITE);
        authType.setRenderer(authRenderer);
        authType.setSelectedIndex(2);
        setRequestTabedPane.setTabComponentAt(1, authType);
        authType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAuthTab(auth, authType.getSelectedIndex(), timesAuthTypeComboBoxChanged);
                timesAuthTypeComboBoxChanged++;
                mainFrame.revalidate();
                mainFrame.repaint();
                mainFrame.pack();
            }
        });

        JPanel query = new JPanel();
        query.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground1));
        query.setBackground(colorOfThemeBackground2);
        setRequestTabedPane.add("Query", query);
        //query tab its basically the same as the header tab

        GridBagConstraints queryTabConstraints = new GridBagConstraints();
        queryTabConstraints.gridx = 0;
        queryTabConstraints.gridy = 0;
        queryTabConstraints.weightx = 0;
        queryTabConstraints.weighty = 0;
        queryTabConstraints.fill = GridBagConstraints.HORIZONTAL;
        queryTabConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        createQueryTab(query, queryTabConstraints);

        JPanel header = new JPanel();
        header.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground1));
        header.setBackground(colorOfThemeBackground2);
        header.setLayout(new GridBagLayout());
        setRequestTabedPane.add("Header", header);
        GridBagConstraints headerConstraints = new GridBagConstraints();
        headerConstraints.gridx = 0;
        headerConstraints.gridy = 0;
        headerConstraints.weightx = 0;
        headerConstraints.weighty = 0;
//        headerConstraints.ipadx = 0;
//        headerConstraints.ipady = 0;
//        headerConstraints.insets = new Insets(0, 0, 0, 0);
        headerConstraints.fill = GridBagConstraints.HORIZONTAL;
        headerConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        createHeaderTab(header, headerConstraints);

        JPanel docs = new JPanel();
        docs.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground1));
        docs.setBackground(colorOfThemeBackground2);
        setRequestTabedPane.add("Docs", docs);

        GridBagConstraints settingRequestConstraints = new GridBagConstraints();
        settingRequestConstraints.gridx = 1;
        settingRequestConstraints.gridy = 1;
        //growing constant
        settingRequestConstraints.weightx = 1;
        settingRequestConstraints.weighty = 1;
        settingRequestConstraints.anchor = GridBagConstraints.LINE_START;
        settingRequestConstraints.fill = GridBagConstraints.BOTH;
        mainFrame.add(setRequestTabedPane, settingRequestConstraints);

    }

    private void createBodyTab(JPanel body, int bodyType, int timesChanged) {
        body.setLayout(new GridBagLayout());
        if (timesChanged > 0) {
            try {
//                Component toRemove = body.getComponent(timesChanged-1);
//                toRemove.setVisible(false);
//                toRemove.setMaximumSize(new Dimension(0,0));
//                toRemove.setPreferredSize(new Dimension(0,0));
//                toRemove.setMinimumSize(new Dimension(0,0));
                body.removeAll();
//            GridBagLayout layout = (GridBagLayout) body.getLayout();
//            GridBagConstraints gbc = layout.getConstraints(toRemove);
//                body.remove(0);
//                body.remove(timesChanged-1);
                body.setLayout(new GridBagLayout());
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println(e.getStackTrace());
            }
        }
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.fill = GridBagConstraints.BOTH;
        Dimension sizeOfNow = mainFrame.getSize();
        Point locationOfNow = mainFrame.getLocationOnScreen();

        if (bodyType == 0) {
            //its "From URL Encoded
            JLabel hello = new JLabel(bodyType + "");
            hello.setBackground(Color.BLUE);
            body.add(hello, constraints);

        } else if (bodyType == 1) {
            //its "JSON"
            JTextArea bodyReqJSON = new JTextArea();
            bodyReqJSON.setSize(new Dimension(600, 900));
            bodyReqJSON.setPreferredSize(new Dimension(600, 900));
            bodyReqJSON.setFont(new Font("DialogInput", Font.PLAIN, 15));
            bodyReqJSON.setForeground(colorOfThemeBackground1);
            bodyReqJSON.setBackground(colorOfThemeBackground2);
            bodyReqJSON.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground1));
            body.add(bodyReqJSON, constraints);
        } else if (bodyType == 3) {
            //its "Binary File"
            JLabel description = new JLabel("SELECTED FILE");
            description.setForeground(colorOfThemeBackground1);
            description.setBackground(colorOfThemeBackground2);
            description.setOpaque(false);
            description.setFont(new Font("Serif", Font.BOLD, 15));

            constraints.ipady = 0;
            constraints.ipadx = 0;
            constraints.insets = new Insets(10, 10, 10, 10);
            constraints.anchor = GridBagConstraints.LAST_LINE_START;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            body.add(description, constraints);

            JTextArea selectedFile = new JTextArea("No file selected");
            selectedFile.setOpaque(true);
            selectedFile.setForeground(colorOfThemeBackground1);
            selectedFile.setBackground(new java.awt.Color(73, 73, 73));
            selectedFile.setBorder(BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
            selectedFile.setPreferredSize(new Dimension(150, 30));
            selectedFile.setFont(new Font("Serif", Font.BOLD, 20));
            selectedFile.setEditable(false);
            JScrollPane fileScrollPane = new JScrollPane(selectedFile);
            constraints.gridy = 1;
            constraints.gridwidth = 2;
            constraints.anchor = GridBagConstraints.CENTER;
            body.add(fileScrollPane, constraints);


//            the reset file only enabled if a file is chosen
            constraints.gridwidth = 1;
            constraints.gridx = 0;
            constraints.gridy = 2;
            constraints.anchor = GridBagConstraints.FIRST_LINE_START;
            JButton resetFile = new JButton("Resest File", new ImageIcon((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\reset-icon.png"));
            resetFile.setBackground(colorOfThemeBackground2);
            resetFile.setForeground(colorOfThemeBackground1);
            resetFile.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground1));
            resetFile.setEnabled(false);
            resetFile.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("file reseeted");
                    resetFile.setEnabled(false);
                    resetFile.setForeground(new java.awt.Color(94, 94, 94));
                    resetFile.setFont(new Font("Serif", Font.BOLD, 20));
                    resetFile.setBackground(colorOfThemeBackground2);
                    selectedFile.setText("No file selected");
                }
            });
            body.add(resetFile, constraints);


            //first i will add the JFileChooser and then the Reset File JButton
            constraints.gridx = 1;
            constraints.gridwidth = 1;
            constraints.anchor = GridBagConstraints.FIRST_LINE_START;
            //the JFileChooser and its JButton

            JFileChooser fileChooser = new JFileChooser();
            JButton fileChooserButton = new JButton("Choose File ", new ImageIcon((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\choose-file-icon1.png"));
            fileChooserButton.setBackground(colorOfThemeBackground2);
            fileChooserButton.setForeground(colorOfThemeBackground1);
            fileChooserButton.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground1));
//            fileChooserButton
            fileChooserButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    int returnVal = fileChooser.showOpenDialog(mainFrame);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        System.out.println("You chose to open this file: " + fileChooser.getSelectedFile().getName());
                        selectedFile.setText(fileChooser.getSelectedFile().getName());
                    } else {
                        System.out.println("didnt want to open the file");
                    }

                    if (selectedFile.getText().equals("No file selected")) {
                        resetFile.setEnabled(false);
                        resetFile.setForeground(new java.awt.Color(94, 94, 94));
                        resetFile.setFont(new Font("Serif", Font.BOLD, 20));
                        resetFile.setBackground(colorOfThemeBackground2);
                        resetFile.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground2));
                    } else {
                        resetFile.setEnabled(true);
                        resetFile.setForeground(new java.awt.Color(138, 138, 138));
                        resetFile.setFont(new Font("DialogInput", Font.PLAIN, 18));
                        resetFile.setBackground(new java.awt.Color(94, 94, 94));
                        resetFile.setBorder(BorderFactory.createLineBorder(new java.awt.Color(164, 164, 164)));
                    }
                    mainFrame.revalidate();
                    mainFrame.repaint();
                    mainFrame.pack();

                }
            });
            body.add(fileChooserButton, constraints);


        }
        mainFrame.revalidate();
        mainFrame.repaint();
        mainFrame.pack();
        //a small bug here that i dont know how to fix// never mind now i have an idea but dont have the time
        mainFrame.setPreferredSize(sizeOfNow);
        mainFrame.setLocation(locationOfNow);
    }

    private void createAuthTab(JPanel auth, int authType, int timesChanged) {

        auth.setLayout(new GridBagLayout());
        if (timesChanged > 0) {
            try {
//                Component toRemove = auth.getComponent(timesChanged-1);
//                toRemove.setVisible(false);
//                toRemove.setMaximumSize(new Dimension(0,0));
//                toRemove.setPreferredSize(new Dimension(0,0));
//                toRemove.setMinimumSize(new Dimension(0,0));
                auth.removeAll();
//            GridBagLayout layout = (GridBagLayout) body.getLayout();
//            GridBagConstraints gbc = layout.getConstraints(toRemove);
//                body.remove(0);
//                body.remove(timesChanged-1);
//                auth.setLayout(new GridBagLayout());
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("");
            }
        }
        Dimension sizeOfNow = mainFrame.getSize();
        Point locationOfNow = mainFrame.getLocationOnScreen();

        if (authType == 0) {
            //its "Bearer Token" form
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 1;
            gbc.weighty = 1;
            gbc.insets = new Insets(5, 30, 5, 15);
            gbc.ipady = 0;
            gbc.ipadx = 0;
            gbc.anchor = GridBagConstraints.PAGE_START;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            //token label
            JLabel tokenLabel = new JLabel("Token");
            tokenLabel.setOpaque(false);
            tokenLabel.setFont(new Font("TimesRoman", Font.BOLD, 15));
            tokenLabel.setForeground(colorOfThemeBackground1);
            tokenLabel.setPreferredSize(new Dimension(50, 20));
            auth.add(tokenLabel, gbc);
            //token text field
            JTextField tokenTextField = new JTextField();
            tokenTextField.setOpaque(false);
            tokenTextField.setForeground(colorOfThemeBackground1);
            tokenTextField.setBackground(colorOfThemeBackground2);
            tokenTextField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, colorOfThemeBackground1));
            tokenTextField.setPreferredSize(new Dimension(150, 20));
            gbc.gridx = 1;
            auth.add(tokenTextField, gbc);
            //next line we have the Prefix
            JLabel prefix = new JLabel("Prefix", new ImageIcon((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\question-mark-icon.png"), JLabel.LEFT);
            prefix.setToolTipText("Prefix to use when sending the Authorization eader. Deafaults to Bearer");
            prefix.setForeground(colorOfThemeBackground1);
            prefix.setBackground(colorOfThemeBackground2);
            prefix.setOpaque(false);
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.anchor = GridBagConstraints.FIRST_LINE_START;
            auth.add(prefix, gbc);
            //prefix text field
            JTextField prefixTextField = new JTextField();
            prefixTextField.setOpaque(false);
            prefixTextField.setForeground(colorOfThemeBackground1);
            prefixTextField.setBackground(colorOfThemeBackground2);
            prefixTextField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, colorOfThemeBackground1));
            prefixTextField.setPreferredSize(new Dimension(150, 20));
            gbc.gridx = 1;
            auth.add(prefixTextField, gbc);
            //the Enabled Option
            JCheckBox enabledCheckBox = new JCheckBox("Enabled", true);
            enabledCheckBox.setOpaque(false);
            gbc.gridx = 1;
            gbc.gridy = 2;
            auth.add(enabledCheckBox, gbc);

        } else if (authType == 2) {
            //no authentication
            auth.removeAll();
            auth.setLayout(new GridBagLayout());
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.insets = new Insets(5, 5, 5, 5);
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.anchor = GridBagConstraints.PAGE_START;
            constraints.fill = GridBagConstraints.BOTH;
            JLabel openLock = new JLabel("", new ImageIcon((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\open-lock-icon1.png"), JLabel.CENTER);
            auth.add(openLock, constraints);
        }
        mainFrame.revalidate();
        mainFrame.repaint();
        mainFrame.pack();
        //a small bug here that i dont know how to fix// never mind now i have an idea but dont have the time
        mainFrame.setPreferredSize(sizeOfNow);
        mainFrame.setLocation(locationOfNow);
    }

    private void createQueryTab(JPanel query, GridBagConstraints constraints) {

        JPanel newNameValuePair = new JPanel();
        newNameValuePair.setLayout(new FlowLayout());
        newNameValuePair.setBackground(colorOfThemeBackground2);
//        //first component the 3 lines
        JLabel _3_lines = new JLabel(new ImageIcon((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\3-purple-lines-icon.png"));
        newNameValuePair.add(_3_lines);
        //then the JTextField for the name
        JTextField nameTextField = new JTextField(" New Name");
        nameTextField.setPreferredSize(new Dimension(150, 35));
        nameTextField.setFont(new Font("Serif", Font.PLAIN, 15));
        nameTextField.setForeground(colorOfThemeBackground1);
        nameTextField.setBackground(colorOfThemeBackground2);
        nameTextField.setBorder(BorderFactory.createMatteBorder(1, 1, 3, 1, colorOfThemeBackground1));
        nameTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                nameTextField.setText(" ");
                System.out.println("clicked on the name");
//                //and add a new Pair of name and values
                GridBagConstraints newGridConstraints = (GridBagConstraints) (constraints.clone());
                newGridConstraints.gridy = constraints.gridy + 1;
                createQueryTab(query, newGridConstraints);
                mainFrame.revalidate();
                mainFrame.repaint();
            }
        });
        newNameValuePair.add(nameTextField);
        //then the JTextField for the value
        JTextField valueTextField = new JTextField("New Value");
        valueTextField.setPreferredSize(new Dimension(150, 35));
        valueTextField.setFont(new Font("Serif", Font.PLAIN, 15));
        valueTextField.setForeground(colorOfThemeBackground1);
        valueTextField.setBackground(colorOfThemeBackground2);
        valueTextField.setBorder(BorderFactory.createMatteBorder(1, 1, 3, 1, colorOfThemeBackground1));
        valueTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                valueTextField.setText(" ");
                System.out.println("clicked on the value");
                //and add a new Pair of name and values
                GridBagConstraints newGridConstraints = (GridBagConstraints) (constraints.clone());
                newGridConstraints.gridy = constraints.gridy + 1;
//                newGridConstraints.weightx=1;
//                newGridConstraints.weighty=1;
//                constraints.weightx=0;
//                constraints.weighty=0;
                createQueryTab(query, newGridConstraints);
                mainFrame.revalidate();
                mainFrame.repaint();
            }
        });
        newNameValuePair.add(valueTextField);
        //now the JCheckBox
        JCheckBox checkBox = new JCheckBox(" ", true);
        checkBox.setBackground(colorOfThemeBackground2);
        checkBox.setOpaque(false);
        newNameValuePair.add(checkBox);
        //now the trash icon Button
        JButton trash = new JButton(new ImageIcon((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\delete-icon1.png"));
        trash.setBackground(colorOfThemeBackground2);
        trash.setOpaque(false);
        trash.setPreferredSize(new Dimension(18, 18));
        trash.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("user clicked on the trash JButton");
                query.remove(newNameValuePair);
                if (constraints.gridy == 0) {
                    createQueryTab(query, constraints);
                }
                mainFrame.revalidate();
                mainFrame.repaint();
            }
        });
        newNameValuePair.add(trash);
        query.add(newNameValuePair, constraints);


    }

    private void createHeaderTab(JPanel header, GridBagConstraints constraints) {


        //we create the first Jpanel containing the pair of key and value
        //if the user clicks on it this method should be called again with the same constaints but the gridx+1
//
        JPanel newKeyValuePair = new JPanel();
//        newKeyValuePair.setPreferredSize(new Dimension());
        newKeyValuePair.setLayout(new FlowLayout());
        newKeyValuePair.setBackground(colorOfThemeBackground2);
//        //first component the 3 lines
        JLabel _3_lines = new JLabel(new ImageIcon((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\3-grey-lines1-icon.png"));
        newKeyValuePair.add(_3_lines);
        //then the JTextField for the Header
        JTextField headerTextField = new JTextField("New Header");
        headerTextField.setPreferredSize(new Dimension(150, 35));
        headerTextField.setFont(new Font("Serif", Font.PLAIN, 15));
        headerTextField.setForeground(colorOfThemeBackground1);
        headerTextField.setBackground(colorOfThemeBackground2);
        headerTextField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, colorOfThemeBackground1));
//        boolean alreadyCreated = false;
        headerTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                headerTextField.setText(" ");
                System.out.println("clicked on the header");
//                //and add a new Pair of Header and values
                GridBagConstraints newGridConstraints = (GridBagConstraints) (constraints.clone());
                newGridConstraints.gridy = constraints.gridy + 1;
                createHeaderTab(header, newGridConstraints);
                mainFrame.revalidate();
                mainFrame.repaint();
            }
        });
        newKeyValuePair.add(headerTextField);
        //then the JTextField for the value
        JTextField valueTextField = new JTextField("New Value");
        valueTextField.setPreferredSize(new Dimension(150, 35));
        valueTextField.setFont(new Font("Serif", Font.PLAIN, 15));
        valueTextField.setForeground(colorOfThemeBackground1);
        valueTextField.setBackground(colorOfThemeBackground2);
        valueTextField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, colorOfThemeBackground1));
        valueTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                valueTextField.setText(" ");
                System.out.println("clicked on the value");
                //and add a new Pair of Header and values
                GridBagConstraints newGridConstraints = (GridBagConstraints) (constraints.clone());
                newGridConstraints.gridy = constraints.gridy + 1;
//                newGridConstraints.weightx=1;
//                newGridConstraints.weighty=1;
//                constraints.weightx=0;
//                constraints.weighty=0;
                createHeaderTab(header, newGridConstraints);
                mainFrame.revalidate();
                mainFrame.repaint();
            }
        });
        newKeyValuePair.add(valueTextField);
        //now the JCheckBox
        JCheckBox checkBox = new JCheckBox(" ", true);
        checkBox.setBackground(colorOfThemeBackground2);
        checkBox.setOpaque(false);
        newKeyValuePair.add(checkBox);
        //now the trash icon Button
        JButton trash = new JButton(new ImageIcon((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\delete-icon1.png"));
        trash.setBackground(colorOfThemeBackground2);
        trash.setOpaque(false);
        trash.setPreferredSize(new Dimension(18, 18));
        trash.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("user clicked on the trash JButton");
                header.remove(newKeyValuePair);
                if (constraints.gridy == 0) {
                    createHeaderTab(header, constraints);
                }
                mainFrame.revalidate();
                mainFrame.repaint();
            }
        });
        newKeyValuePair.add(trash);
        header.add(newKeyValuePair, constraints);
    }

    private void createRequestHistoryPanel() {

        //the second panel down the "Insomnia" label containig the command info of requests
        JTabbedPane queryHistoryPanel = new JTabbedPane();
        queryHistoryPanel.setPreferredSize(new Dimension(200, 500));
        queryHistoryPanel.setFont(new Font("SansSerif", Font.BOLD, 15));
        queryHistoryPanel.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground1));
        queryHistoryPanel.setBackground(colorOfThemeBackground2);
        queryHistoryPanel.setForeground(colorOfThemeBackground1);
        //now the panels
        JPanel preview = new JPanel();
        preview.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground1));
        preview.setBackground(colorOfThemeBackground2);
        queryHistoryPanel.add("preview", preview);
        //the type of body chosen in the popup menu down the Body word of the tab reference
        JButton previewButton = new JButton("Preview");
        previewButton.setToolTipText("right click here to see the different preview modes available");
        previewButton.setForeground(colorOfThemeBackground1);
        previewButton.setFont(new Font("SansSerif", Font.BOLD, 15));
        previewButton.setBackground(colorOfThemeBackground2);
        previewButton.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground2));


        //inja bayad bebini ke mituni enablesh bokoni ya na. enable mikoni agar ke un URL E ke dade shode b

        //creating the popup menu and adding each JMenuItem to the popUpMenu
        JPopupMenu previewModeMenu = new JPopupMenu();


        //-------Preview Mode
        JMenuItem previewModeMenuItem = new JMenuItem("Preview Mode----------------------");
        previewModeMenuItem.setFont(new Font("Serif", Font.PLAIN, 10));
        previewModeMenuItem.setForeground(colorOfThemeBackground1);
        previewModeMenuItem.setEnabled(false);
        previewModeMenu.add(previewModeMenuItem);
        //the user can chose only one of the next 2 choices so we make them as JRadioButtonMenuItem
        // and add them to the same group of JButtons
        ButtonGroup group = new ButtonGroup();
        //Visual Preview
        JRadioButtonMenuItem visualPreview = new JRadioButtonMenuItem("Visual Preview");
        visualPreview.setSelected(false);
        visualPreview.setFont(new Font("Serif", Font.PLAIN, 15));
        visualPreview.setForeground(new java.awt.Color(69, 162, 255));
        visualPreview.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                previewButton.setText("Visual Preview");
                mainFrame.revalidate();
                mainFrame.repaint();
                mainFrame.pack();
                System.out.println("Visual Preview");
            }
        });

        group.add(visualPreview);
        previewModeMenu.add(visualPreview);
//Source Code
//JMenuItem sourceCodeMenuItem= new JMenuItem("Source Code");
//sourceCodeMenuItem.setFont(new Font("Serif", Font.PLAIN, 15));
//previewModeMenu.add(sourceCodeMenuItem);
        //Raw Data
        JRadioButtonMenuItem rawData = new JRadioButtonMenuItem("Raw Data");
        rawData.setSelected(false);
        rawData.setFont(new Font("Serif", Font.PLAIN, 15));
        rawData.setForeground(new java.awt.Color(47, 198, 102));
        rawData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                previewButton.setText("Raw Data");
                mainFrame.revalidate();
                mainFrame.repaint();
                mainFrame.pack();
                System.out.println("Raw Data");

            }
        });

        group.add(rawData);
        previewModeMenu.add(rawData);
        //-------ACTIONS
        JMenuItem actionsSeparator = new JMenuItem("ACTIONS-------------------------------");
        actionsSeparator.setFont(new Font("Serif", Font.PLAIN, 10));
        actionsSeparator.setForeground(colorOfThemeBackground1);
        actionsSeparator.setEnabled(false);
        previewModeMenu.add(actionsSeparator);
        //Save Raw Response
        JMenuItem saveRawResponseMenuItem = new JMenuItem("Save Raw Response", new ImageIcon((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\save-icon.png"));
        saveRawResponseMenuItem.setFont(new Font("Serif", Font.PLAIN, 15));
        saveRawResponseMenuItem.setForeground(new java.awt.Color(255, 161, 20));
        saveRawResponseMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //here we are gona have to open a new JFrame in which the user is told to select an address to save the file,
                //but bether not to do a JChooser and just save the file to the same repository of this code

                System.out.println("Save Raw Response\\still in progress");
            }
        });

        previewModeMenu.add(saveRawResponseMenuItem);
//save HTTP Debug
//JMenuItem saveHTTPDebug= new JMenuItem("Save HTTP Debug", new ImageIcon((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\bug-icon.png"));
//saveHTTPDebug.setFont(new Font("Serif", Font.PLAIN, 15));
//saveHTTPDebug.setForeground(new java.awt.Color(255, 0, 1));
//previewModeMenu.add(saveHTTPDebug);
        //ading the pop up menu to the button
        previewButton.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                queryHistoryPanel.setSelectedIndex(0);
            }

            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    previewModeMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        //ading the button to the tab
        queryHistoryPanel.setTabComponentAt(0, previewButton);

        //next Panel is the HEADER Panel
        JPanel header = new JPanel();
        header.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground1));
        header.setBackground(colorOfThemeBackground2);
        queryHistoryPanel.add("Header", new JScrollPane(header));
        header.setLayout(new GridBagLayout());
        createStaticHeaderTab(header);


        //next Panel is the Cookie Panel
        JPanel cookie = new JPanel();
        cookie.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground1));
        cookie.setBackground(colorOfThemeBackground2);
        queryHistoryPanel.add("Cookie", cookie);

        //next Panel is the Timeline Panel

        JPanel timeline = new JPanel();
        timeline.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground1));
        timeline.setBackground(colorOfThemeBackground2);
        queryHistoryPanel.add("Timeline", timeline);

        GridBagConstraints settingRequestConstraints = new GridBagConstraints();
        settingRequestConstraints.gridx = 2;
        settingRequestConstraints.gridy = 1;
        //growing constant
        settingRequestConstraints.weightx = 1;
        settingRequestConstraints.weighty = 1;
        settingRequestConstraints.anchor = GridBagConstraints.LINE_START;
        settingRequestConstraints.fill = GridBagConstraints.BOTH;
        mainFrame.add(queryHistoryPanel, settingRequestConstraints);
    }

    private void createStaticHeaderTab(JPanel header) {
        //first we take in the information to be shown
        String[][] nameValueInfo = {
                {"name 111111111111111111111111", "value 11111111111111111"},
                {"name 2222222222222222222222222222222222222222222222222222222222222222222", "value 2222222222222222222222222222222222222222"},
                {"name 3", "value 3"},
                {"name 4444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444", "value 444444444444444444444444444444444444444444444444444444444444444444444444444"},
                {"name 55555555555555555 55555555555555555 55555555555555555 55555555555555555", "value 55555555555555555"},
                {"name 66666666666666666666666666 66666666666666666666666666 66666666666666666666666666 66666666666666666666666666", "value 66666666666666666666666666"},
                {"name 77777777777777777777777777777777777777777 77777777777777777777777777777777777777777 77777777777777777777777777777777777777777", "value 77777777777777777777777777777777777777777"},
                {"name 8888888888888888888888888888888888888", "value 8888888888888888888888888888888888888 8888888888888888888888888888888888888 8888888888888888888888888888888888888 8888888888888888888888888888888888888"},
                {"name 9999999999999999999999999999999999 ", "value 9999999999999999999999999999999999 9999999999999999999999999999999999"}
        };

        //better if the Header panel has GridBagLayout as its Layout Manager

        //creating the GridBagConstraints
        header.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0;
        constraints.weighty = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        //just for the first line, after this the insets wil change
        constraints.insets = new Insets(15, 5, 5, 5);

        JLabel name = new JLabel("NAME");
        if(colorOfThemeBackground2.equals(light2)){
            name.setForeground(new java.awt.Color(85, 85, 85));
        }else {
            name.setForeground(new java.awt.Color(166, 166, 166));
        }
        name.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground2));
        name.setBackground(colorOfThemeBackground2);
        name.setFont(new Font("Serif", Font.BOLD, 18));
        header.add(name, constraints);

        //constraints
        constraints.gridx = 1;
        JLabel value = new JLabel("VALUE");
        if(colorOfThemeBackground2.equals(light2)){
            value.setForeground(new java.awt.Color(85, 85, 85));
        }else {
            value.setForeground(new java.awt.Color(166, 166, 166));
        }
        value.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground2));
        value.setBackground(colorOfThemeBackground2);
        value.setFont(new Font("Serif", Font.BOLD, 18));
        header.add(value, constraints);

        for (int i = 0; i < nameValueInfo.length; i++) {
            String nameOfField = getStringOfField(nameValueInfo[i][0]);

            //constraints JTextArea aval
            int numRows = ( (nameValueInfo[i][0] . length())/35)+1;
            constraints.insets = new Insets(5, 10, 5, 5);
            constraints.gridx = 0;
            constraints.gridy = i+1;
            JTextArea nameTextArea = new JTextArea(nameOfField, numRows, 1);
            nameTextArea.setBorder(BorderFactory.createLineBorder( colorOfThemeBackground2 ));
            if(colorOfThemeBackground2.equals(light2)){
                nameTextArea.setForeground(Color.BLACK);
            }else {
                nameTextArea.setForeground(Color.WHITE);
            }
            if(i%2==0) {
                nameTextArea.setBackground(new java.awt.Color(117, 117, 117));
            }else{
                nameTextArea.setBackground(colorOfThemeBackground2);
            }
            nameTextArea.setEditable(false);
            header.add(nameTextArea, constraints);

            //constraints JTextArea dovom
            numRows = ( (nameValueInfo[i][1] . length())/35)+1;
            constraints.insets = new Insets(5, 5, 5, 10);
            constraints.gridx = 1;
            String valueOfField = getStringOfField(nameValueInfo[i][1]);
            JTextArea valueTextArea = new JTextArea(valueOfField, numRows, 1);
            valueTextArea.setBorder(BorderFactory.createLineBorder( colorOfThemeBackground2 ));
            if(colorOfThemeBackground2.equals(light2)){
                valueTextArea.setForeground(Color.BLACK);
            }else {
                valueTextArea.setForeground(Color.WHITE);
            }
            if(i%2==0) {
                valueTextArea.setBackground(new java.awt.Color(117, 117, 117));
            }else{
                valueTextArea.setBackground(colorOfThemeBackground2);
            }
            valueTextArea.setEditable(false);
            header.add(valueTextArea, constraints);
        }

        //now the "Copy to Clipboard" JButton
        constraints.gridx=0;
        constraints.gridy++;
        constraints.fill=GridBagConstraints.NONE;
        constraints.anchor=GridBagConstraints.FIRST_LINE_END;
        JButton copyToClipboard = new JButton("  Copy to Clipboard  ", new ImageIcon((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\copy-icon2.png"));
        copyToClipboard.setBackground(colorOfThemeBackground2);
        if(colorOfThemeForground.equals(purple)) {
            copyToClipboard.setBorder(BorderFactory.createLineBorder(new java.awt.Color(122, 141, 255)));
        }else{
            copyToClipboard.setBorder(BorderFactory.createLineBorder(new java.awt.Color(102, 243, 255)));
        }
        copyToClipboard.setForeground(new java.awt.Color(138, 138, 138));
        copyToClipboard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //we have to ACTUALLY SAVE THE FILE SOMEWHERE
                System.out.println("we have to ACTUALLY SAVE THE FILE SOMEWHERE");
                copyToClipboard.setIcon(new ImageIcon((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\tick-icon.png"));
                copyToClipboard.setText("  Copied  ");
                System.out.println("if he does this again this JButton should turn back to copy to clipboard format");
            }
        });
        header.add(copyToClipboard, constraints);

    }

    private String getStringOfField(String input) {
        String newString = "";
        for (int i = 0, j = 0; i < input.length(); i++, j++) {
            newString += input.charAt(i);
            if (j == 35) {
                j = 0;
                newString += "\n";
            }
        }
        return newString;
    }
}
