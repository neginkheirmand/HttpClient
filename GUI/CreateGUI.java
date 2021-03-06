package GUI;

import Bash.Command;
import Bash.Executer;
import Bash.Response;
import GUI.BodyMessage.BodyMessage;
import GUI.BodyMessage.KeyValuePairBody;
import GUI.Header.HeaderPanel;
import GUI.Query.QueryPanel;
import InformationHandling.LoadInfo;
import InformationHandling.SaveInfo;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import static javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;
import static javax.swing.JOptionPane.OK_OPTION;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;



 /**
  * this class creates an frame in which the user can get and put information about requests
 * @author      Negin Kheirmand <venuskheirmand@gmail.com>
 * @version     1.1
 * @since       1.0
 */

public class CreateGUI {
    //the main frame of the app
    private JFrame mainFrame;
    //number of times the Body Type ComboBox changed
    private static int timesBodyTypeComboBoxChanged = 0;
    //number of times the auth ComboBox changed
    private static int timesAuthTypeComboBoxChanged = 0;


    //the photos color
    private final static String purple = "\\purple";
    private final static String blue = "\\blue";
    private static String colorOfThemeForground = blue;
    //the BackGround color
    private final static Color light1 = new java.awt.Color(118,118,118);
    private final static Color light2 = new java.awt.Color(229,229,229);
    private final static Color dark1 = new java.awt.Color(128,128,128);
    private final static Color dark2 = new java.awt.Color(38, 38, 38);
    private static Color colorOfThemeBackground1 = dark1;
    private static Color colorOfThemeBackground2 = dark2;

    //the next fields are basically for the display of JFrame and depends of the preference
    private PanelHandler insomniaPanelHandler = new PanelHandler();
    private boolean checkBoxSystemTray = true;
    private boolean checkBoxFollowRedirect = true;
    private boolean sideBar = true;
    private ImageIcon[] icons = new ImageIcon[26];

    private static int indexOfRequest = -1 ;

    //an array list holding all the requests
    private static ArrayList<Request> savedRequests = null;

    //for fixing the auth tab
    private String authBox = "";
    private boolean authEnabled = true;

    //for fixing the message body
    private String jsonContainer ="";
    private String pathContainer = "";
    private boolean wrongRequest = false;
    private int[] indexOfSearched = null;
    private ArrayList<KeyValuePairBody> bodyData = new ArrayList<>();
    private String filterSearched = "Filter";

    /**
     * the constructor of the class, here the frame is created and each one of its characteristics will be created by calling other methods
     */
    public CreateGUI() {

        //we load the requests saved in the last run of the application

        LoadInfo loader = new LoadInfo();
        if(!loader.getCouldReadWithOutProblems()){
            savedRequests = new ArrayList<>();
            System.out.println("nothing saved, the historial of the requests is empty");
        }else{
            savedRequests = loader.getSavedRequests();
            if(savedRequests == null){
                savedRequests = new ArrayList<>();
            }
            ArrayList<String> preferences = loader.loadPreferences();
            if(preferences == null){
                preferences.add("true");
                preferences.add("true");
                preferences.add("purple");
                preferences.add("dark");
            }
            if(!loader.isCouldReadPreferencesWithOutProblems()){
                System.out.println("could not load the preferences saved");
            }else {

                //the follow redirect
                if(preferences.get(0).equals("true")){
                    checkBoxFollowRedirect=true;
                    if(savedRequests!=null){
                        for(int i=0; i<savedRequests.size(); i++){
                            savedRequests.get(i).setFollowRedirect(true);
                        }
                    }
                }else{
                    checkBoxFollowRedirect=false;
                }

                //the hide on system tray
                if(preferences.get(1).equals("true")){
                    checkBoxSystemTray=true;
                }else{
                    checkBoxSystemTray=false;
                }

                //the forground color
                if(preferences.get(2).equals("blue")){
                    colorOfThemeForground=blue;
                }else{
                    colorOfThemeForground=purple;
                }

                //the background color
                if(preferences.get(3).equals("dark")){
                    colorOfThemeBackground1=dark1;
                    colorOfThemeBackground2=dark2;
                }else{
                    colorOfThemeBackground1=light1;
                    colorOfThemeBackground2=light2;
                }
            }
            System.out.println("the history of requests was updated");
        }
        Request.setListOfrequests(savedRequests);

        mainFrame = new JFrame("Insomnia");

        Image taskbarIcon = Toolkit.getDefaultToolkit().getImage((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\my-app-icon1.png");

        mainFrame.setIconImage(taskbarIcon);

        mainFrame.setMinimumSize(new Dimension(1600, 650));
        mainFrame.setMaximumSize(new Dimension(1520, 1080));
        mainFrame.setLocationRelativeTo(null);
//        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainFrame.setVisible(true);
        mainFrame.setBackground(new java.awt.Color(128,128, 128));
        mainFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {

                if (checkBoxSystemTray) {
                    saveInfoOfthisRun();
                    goToSystemTray();
                } else {
                    saveInfoOfthisRun();
                    mainFrame.dispose();
                    System.exit(0);
                }
            }
        });
        createMenuBar();

        //working with the layout -> layout choosen was GridBagLayout
        mainFrame.setLayout(new GridBagLayout());


        paintFrame();
    }


    public void paintFrame() {
        if (sideBar) {
            createInsomniaDisplayArea();
            createRequestClasifier();
        }
        createRequestInfoPanel();
        createHistorialRequest();
        createRequestInfo();
        createRequestHistoryPanel();

        createSystemTray();

    }

    /**
     * this method is called to create the system tray capacity of the app
     */
    private void createSystemTray(){


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
                saveInfoOfthisRun();
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
                    } catch (AWTException ex) {
                        System.out.println("unable to add to tray");
                    }
                }
                //the code above is for fixing another little bug, but its basically the same as before
                if(e.getNewState()==7){
                    try{
                        tray.add(trayIcon);
                        mainFrame.setVisible(false);
                    }catch(AWTException ex){
                        System.out.println("unable to add to system tray");
                    }
                }
                if(e.getNewState()==JFrame.MAXIMIZED_BOTH){
                    tray.remove(trayIcon);
                    mainFrame.setVisible(true);
                }
                if(e.getNewState()==JFrame.NORMAL){
                    tray.remove(trayIcon);
                    mainFrame.setVisible(true);
                }
            }
        });
        //the icon in the top-left of the mainFrame window
        mainFrame.setIconImage(Toolkit.getDefaultToolkit().getImage((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\my-app-icon1.png"));

        mainFrame.pack();


    }

    /**
     * this method is called when we want to pass the frame of the app to the Tray System
     */
    private void goToSystemTray(){

//        mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_ICONIFIED));
//        mainFrame.dispatchEvent(new WindowEvent(mainFrame, 7));
//        createToSystemTray();
        PopupMenu trayPopupMenu=new PopupMenu();
        MenuItem exitOption=new MenuItem("Exit");
        exitOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //when clicked in this item the frame will close and the program will end
                System.out.println("Thanks for using my app!  \n:)");
                saveInfoOfthisRun();
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


        SystemTray tray=SystemTray.getSystemTray();
        Image image = Toolkit.getDefaultToolkit().getImage((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\my-app-icon1.png");
        TrayIcon trayIcon=new TrayIcon(image, ":)", trayPopupMenu);
        trayIcon.setImageAutoSize(true);
        try {
            tray.add(trayIcon);
            mainFrame.setVisible(false);
        }catch(AWTException ex){
            System.out.println("Unable to send the main frame to the system tray by the quit option");
        }

    }

    /**
     * this menu is called so the menu bar of the app is created
     */
    private void createMenuBar() {

        JMenuBar menuBarOfApplication = new JMenuBar();
        //---------------
        JMenu application = new JMenu("Application");
        application.setMnemonic(KeyEvent.VK_L);
        JMenu view = new JMenu("View");
        view.setMnemonic(KeyEvent.VK_V);
        JMenu help = new JMenu("Help");
        help.setMnemonic(KeyEvent.VK_H);
        //--------------
        /*
            menuItems of the application menu
         */

        //and the sub-menu of Application is Option and exit
        JMenuItem options = new JMenuItem("Options");
        application.setMnemonic(KeyEvent.VK_S);
        options.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                optionMenu();
            }
        });
        application.add(options);
        application.addSeparator();

        JMenuItem quit = new JMenuItem("Exit", new ImageIcon((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\log-off-icon.png"));
        quit.setMnemonic(KeyEvent.VK_Q);
        quit.setToolTipText("quit the application");
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(checkBoxSystemTray){
                    //gotta go to system tray
                    goToSystemTray();
                }else{
                    mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
                }
            }
        });
        application.add(quit);

        //--------------
        /*
            menuItems of the Edit menu
         */

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
                    mainFrame.remove(insomniaPanelHandler.getFirstPanel());
                    mainFrame.remove(insomniaPanelHandler.getScroolPanelPointer());
                }else{
                    mainFrame.add(insomniaPanelHandler.getFirstPanel(), insomniaPanelHandler.getFirstPanelConstraints());
                    mainFrame.add(insomniaPanelHandler.getScroolPanelPointer(), insomniaPanelHandler.getForthPanelConstraints());
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
        JMenuItem about = new JMenuItem("about");
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createHelpPanel(0);
            }
        });
        JMenuItem helpItem = new JMenuItem("Help");
        helpItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createHelpPanel(1);
            }
        });
        help.add(about);
        help.add(helpItem);
        //adding the menus created to the menu bar
        menuBarOfApplication.add(application);
        menuBarOfApplication.add(view);
        menuBarOfApplication.add(help);

        //adding the menu bar created to the frame of the application
        mainFrame.setJMenuBar(menuBarOfApplication);

    }


    public void createHelpPanel(int indexOption) {

        JFrame infoFrame = new JFrame("Information");
        infoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        infoFrame.setLocation(400, 250);
//        optionFrame.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2, Toolkit.getDefaultToolkit().getScreenSize().height/2);
        infoFrame.setVisible(true);
        infoFrame.setIconImage(Toolkit.getDefaultToolkit().getImage((new File(".").getAbsolutePath()) + "\\src\\GUI\\resource" + colorOfThemeForground + "\\settings-icon.png"));
        infoFrame.setPreferredSize(new Dimension(1000, 350));
        Color colorOfBackGround;
        if (colorOfThemeForground.equals(purple)) {
            colorOfBackGround = new java.awt.Color(174, 146, 255);
        } else {
            colorOfBackGround = new java.awt.Color(151, 255, 250);
        }
        infoFrame.setBackground(colorOfBackGround);

        JTabbedPane optionTabs = new JTabbedPane();
        optionTabs.setVisible(true);

        JPanel about = new JPanel();
        about.setPreferredSize(new Dimension(400, 300));
        about.setBackground(colorOfBackGround);

        Color secondColor;
        if (colorOfThemeForground.equals(purple)) {
            secondColor = new java.awt.Color(67, 51, 146);
        } else {
            secondColor = new java.awt.Color(48, 116, 146);
        }
        about.setLayout (new BoxLayout (about, BoxLayout.Y_AXIS));

        JLabel infoOfDev1 = new JLabel("developer: Negin Kheirmand", JLabel.CENTER);
        JLabel infoOfDev2 = new JLabel("   Email: neginkheirmand@aut.ac.ir");
        JLabel infoOfDev3 = new JLabel("   Student ID: 9831023");
        JLabel infoOfDev4 = new JLabel("Version: 2020.6.10");
        infoOfDev1.setFont(new Font("Serif", Font.BOLD, 20));
        infoOfDev2.setFont(new Font("Serif", Font.BOLD, 20));
        infoOfDev3.setFont(new Font("Serif", Font.BOLD, 20));
        infoOfDev4.setFont(new Font("Serif", Font.BOLD, 20));

        about.add(infoOfDev1);
        about.add(infoOfDev2);
        about.add(infoOfDev3);
        about.add(infoOfDev4);
        optionTabs.add("About", about);


        JPanel helpPanel = new JPanel();
        helpPanel.setBackground(colorOfBackGround);
        helpPanel.setLayout (new BoxLayout (helpPanel, BoxLayout.Y_AXIS));
        JLabel helpInfo1 = new JLabel("Coffee-mania -this program- is an API(application programming interface) development tool which helps to build, test and modify APIs.");
        JLabel helpInfo2 = new JLabel(" It has the ability to make various types of HTTP requests(GET, POST, PUT, PATCH, DELET, HEAD, OPTION) with supporting features like");
        JLabel helpInfo3 = new JLabel("authorization, query parameters, different types of body messages and a lot more.");
        JLabel helpInfo4 = new JLabel("Turorial:");
        JLabel helpInfo5 = new JLabel("1) Create a request by clicking in the Plus button in the left-top corner and choosing the \"Create new Request\" option.");
        JLabel helpInfo6 = new JLabel("2) Chose the type of request in the Combo Box allocated in top of the window in the middle panel");
        JLabel helpInfo7 = new JLabel("3) Enter the url to which you want to send a Request.");
        JLabel helpInfo8 = new JLabel("4) If you want the request to be saved click in the save button.");
        JLabel helpInfo9 = new JLabel("5) Set the features that you want in the different tabs of the middle panel.");
        JLabel helpInfo10 = new JLabel("6) Click the \"Send\" Button allocated in the upper middle panel when you are done.");
        JLabel helpInfo11 = new JLabel("7) The response and its info will be shown in the right panel.");
        JLabel helpInfo12 = new JLabel("8) Enjoy the program:)");
        helpInfo1.setFont(new Font("Serif", Font.BOLD, 15));
        helpInfo2.setFont(new Font("Serif", Font.BOLD, 15));
        helpInfo3.setFont(new Font("Serif", Font.BOLD, 15));
        helpInfo4.setFont(new Font("Serif", Font.BOLD, 15));
        helpInfo5.setFont(new Font("Serif", Font.BOLD, 15));
        helpInfo6.setFont(new Font("Serif", Font.BOLD, 15));
        helpInfo7.setFont(new Font("Serif", Font.BOLD, 15));
        helpInfo8.setFont(new Font("Serif", Font.BOLD, 15));
        helpInfo9.setFont(new Font("Serif", Font.BOLD, 15));
        helpInfo10.setFont(new Font("Serif", Font.BOLD, 15));
        helpInfo11.setFont(new Font("Serif", Font.BOLD, 15));
        helpInfo12.setFont(new Font("Serif", Font.BOLD, 15));

        helpPanel.add(helpInfo1);
        helpPanel.add(helpInfo2);
        helpPanel.add(helpInfo3);
        helpPanel.add(helpInfo4);
        helpPanel.add(helpInfo5);
        helpPanel.add(helpInfo6);
        helpPanel.add(helpInfo7);
        helpPanel.add(helpInfo8);
        helpPanel.add(helpInfo9);
        helpPanel.add(helpInfo10);
        helpPanel.add(helpInfo11);
        helpPanel.add(helpInfo12);

        optionTabs.add("Help", new JScrollPane(helpPanel));
        infoFrame.add(optionTabs, BorderLayout.CENTER);
        optionTabs.setSelectedIndex(indexOption);
        infoFrame.pack();
    }
    /**
     * this method is called so the Menu bar has an Option item on it
     */
    private void optionMenu(){
        JFrame optionFrame = new JFrame("Options");
        optionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        optionFrame.setLocationRelativeTo(null);
//        optionFrame.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2-optionFrame.getSize().width/2, Toolkit.getDefaultToolkit().getScreenSize().height/2-optionFrame.getSize().height/2);
        optionFrame.setLocation(700,250);
//        optionFrame.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2, Toolkit.getDefaultToolkit().getScreenSize().height/2);
        optionFrame.setVisible(true);
        optionFrame.setIconImage(Toolkit.getDefaultToolkit().getImage((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\settings-icon.png"));
        optionFrame.setPreferredSize(new Dimension(700, 700));
        Color colorOfBackGround ;
        if(colorOfThemeForground.equals(purple)) {
            colorOfBackGround = new java.awt.Color(174, 146, 255);
        }else{
            colorOfBackGround = new java.awt.Color(151, 255, 250);
        }
        optionFrame.setBackground(colorOfBackGround);


        JTabbedPane optionTabs = new JTabbedPane();
        optionTabs.setVisible(true);

        JPanel general = new JPanel();
        general.setPreferredSize(new Dimension(400, 300));
        general.setBackground(colorOfBackGround);
        general.setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridy=0;
        gridBagConstraints.gridx=0;
        gridBagConstraints.fill=GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor=GridBagConstraints.FIRST_LINE_END;
        gridBagConstraints.insets= new Insets(20,20,10, 20);

        Color secondColor;
        if(colorOfThemeForground.equals(purple)){
            secondColor = new java.awt.Color(67, 51, 146);
        }else{
            secondColor = new java.awt.Color(48, 116, 146);
        }

        //the general settings
        JCheckBox followRedirect;
        if(checkBoxFollowRedirect) {
            followRedirect = new JCheckBox("Follow Redirect is enabled", checkBoxFollowRedirect);
        }else{
            followRedirect = new JCheckBox("Follow Redirect is not enabled", checkBoxFollowRedirect);
        }
        followRedirect.setOpaque(true);
        followRedirect.setFont(new Font("Serif", Font.BOLD, 20));
        followRedirect.setPreferredSize(new Dimension(350, 100));
        followRedirect.setBackground(Color.WHITE);
        followRedirect.setForeground(Color.BLUE);
        followRedirect.setToolTipText("the Follow redirect Check Box");
        followRedirect.setBorderPainted(true);
        followRedirect.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, secondColor));
        followRedirect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(followRedirect.isSelected()){
                    followRedirect.setText("Follow Redirect is enabled");
                    checkBoxFollowRedirect=true;
                }else{
                    followRedirect.setText("Follow Redirect is not enabled");
                    checkBoxFollowRedirect=false;
                }

                if(savedRequests!=null){
                    for(int i=0; i<savedRequests.size(); i++){
                        savedRequests.get(i).setFollowRedirect(followRedirect.isSelected());
                    }
                }
            }
        });

        general.add(followRedirect, gridBagConstraints);

        gridBagConstraints.gridy=1;
        gridBagConstraints.insets= new Insets(20, 20, 50, 20);
        JCheckBox systemTray;
        if(checkBoxSystemTray) {
            systemTray = new JCheckBox("Hide in System Tray when closing the program", checkBoxSystemTray);
        }else{
            systemTray = new JCheckBox("Close completely when exiting the program", checkBoxSystemTray);

        }
        systemTray.setBorderPainted(true);
        systemTray.setForeground(Color.BLUE);
        systemTray.setFont(new Font("Serif", Font.BOLD, 20));
        systemTray.setPreferredSize(new Dimension(550, 100));
        systemTray.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, secondColor));
        systemTray.setOpaque(true);
        systemTray.setBackground(Color.WHITE);
        systemTray.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(systemTray.isSelected()){
                    systemTray.setText("Hide in System Tray when closing the program");
                    checkBoxSystemTray=true;
                }else{
                    systemTray.setText("Close completely when exiting the program");
                    checkBoxSystemTray=false;
                }
            }
        });
        systemTray.setForeground(Color.BLACK);
        systemTray.setToolTipText("the closing settings of the program");
        general.add(systemTray, gridBagConstraints);

        optionTabs.add("General", general);


        JPanel preferences = new JPanel();
        preferences.setBackground(colorOfBackGround);
        preferences.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy=0;
        gbc.gridx=0;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        gbc.anchor=GridBagConstraints.FIRST_LINE_START;


        preferences.add(new JLabel(new ImageIcon(new File(".").getAbsolutePath()+"\\src\\GUI\\resource\\themes\\blue-black12.png")), gbc);
        gbc.gridy=1;
        JButton B_B_T=new JButton(new ImageIcon(new File(".").getAbsolutePath()+"\\src\\GUI\\resource\\themes\\blue-black.png"));
        B_B_T.setOpaque(true);
        B_B_T.setBackground(new java.awt.Color(25, 74, 255));
        B_B_T.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(createPanelBeforeClose()) {
                    if(colorOfThemeForground.equals(purple)){
                        //should update the option frame too
                        optionFrame.setBackground(new java.awt.Color(151, 255, 250));
                        general.setBackground(new java.awt.Color(151, 255, 250));
                        preferences.setBackground(new java.awt.Color(151, 255, 250));
                        Color borderColor;
                        if(colorOfThemeForground.equals(purple)){
                            borderColor = new java.awt.Color(67, 51, 146);
                        }else{
                            borderColor = new java.awt.Color(48, 116, 146);
                        }
                        followRedirect.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, borderColor));
                        systemTray.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, borderColor));
                    }
                    colorOfThemeForground = blue;
                    colorOfThemeBackground1 = dark1;
                    colorOfThemeBackground2 = dark2;
                    updateFrame();

                    return;
                }else{
                    return;
                }
            }
        });
        preferences.add(B_B_T, gbc);

        gbc.gridx=1;
        gbc.gridy=0;
        preferences.add(new JLabel(new ImageIcon(new File(".").getAbsolutePath()+"\\src\\GUI\\resource\\themes\\blue-white12.png")), gbc);
        gbc.gridy=1;
        JButton B_W_T=new JButton(new ImageIcon(new File(".").getAbsolutePath()+"\\src\\GUI\\resource\\themes\\blue-white.png"));
        B_W_T.setOpaque(true);
        B_W_T.setBackground(new java.awt.Color(74, 248, 255));
        B_W_T.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //but first an informating panel
                if(createPanelBeforeClose()) {
                    if(colorOfThemeForground.equals(purple)){
                        //should update the option frame too
                        optionFrame.setBackground(new java.awt.Color(151, 255, 250));
                        general.setBackground(new java.awt.Color(151, 255, 250));
                        preferences.setBackground(new java.awt.Color(151, 255, 250));
                        Color borderColor;
                        if(colorOfThemeForground.equals(purple)){
                            borderColor = new java.awt.Color(67, 51, 146);
                        }else{
                            borderColor = new java.awt.Color(48, 116, 146);
                        }
                        followRedirect.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, borderColor));
                        systemTray.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, borderColor));
                    }
                    colorOfThemeForground = blue;
                    colorOfThemeBackground1 = light1;
                    colorOfThemeBackground2 = light2;
                    updateFrame();
                }else{
                    return;
                }
            }
        });

        preferences.add(B_W_T, gbc);

        gbc.gridy=2;
        gbc.gridx=0;
        preferences.add(new JLabel(new ImageIcon(new File(".").getAbsolutePath()+"\\src\\GUI\\resource\\themes\\purple-white12.png")), gbc);
        gbc.gridy=3;
        JButton P_W_T=new JButton(new ImageIcon(new File(".").getAbsolutePath()+"\\src\\GUI\\resource\\themes\\purple-white.png"));
        P_W_T.setOpaque(true);
        P_W_T.setBackground(new java.awt.Color(171, 145, 255));
        P_W_T.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //but first an informating panel
                if(createPanelBeforeClose()) {
                    if(colorOfThemeForground.equals(blue)){
                        //should update the option frame too
                        optionFrame.setBackground(new java.awt.Color(171, 145, 255));
                        general.setBackground(new java.awt.Color(171, 145, 255));
                        preferences.setBackground(new java.awt.Color(171, 145, 255));
                        Color borderColor;
                        if(colorOfThemeForground.equals(purple)){
                            borderColor = new java.awt.Color(67, 51, 146);
                        }else{
                            borderColor = new java.awt.Color(48, 116, 146);
                        }
                        followRedirect.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, borderColor));
                        systemTray.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, borderColor));
                    }
                    colorOfThemeForground = purple;
                    colorOfThemeBackground1 = light1;
                    colorOfThemeBackground2 = light2;
                    updateFrame();
                }else{
                    return;
                }
            }
        });
        preferences.add(P_W_T, gbc);

        gbc.gridx=1;
        gbc.gridy=2;
        preferences.add(new JLabel(new ImageIcon(new File(".").getAbsolutePath()+"\\src\\GUI\\resource\\themes\\purple-black12.png")), gbc);
        gbc.gridy=3;
        JButton P_B_T=new JButton(new ImageIcon(new File(".").getAbsolutePath()+"\\src\\GUI\\resource\\themes\\purple-black.png"));
        P_B_T.setOpaque(true);
        P_B_T.setBackground(new java.awt.Color(68, 19, 118));
        P_B_T.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //but first an informating panel
                if(createPanelBeforeClose()) {
                    if(colorOfThemeForground.equals(blue)){
                        //should update the option frame too
                        optionFrame.setBackground(new java.awt.Color(171, 145, 255));
                        general.setBackground(new java.awt.Color(171, 145, 255));
                        preferences.setBackground(new java.awt.Color(171, 145, 255));
                        Color borderColor;
                        if(colorOfThemeForground.equals(purple)){
                            borderColor = new java.awt.Color(67, 51, 146);
                        }else{
                            borderColor = new java.awt.Color(48, 116, 146);
                        }
                        followRedirect.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, borderColor));
                        systemTray.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, borderColor));
                    }
                    colorOfThemeForground = purple;
                    colorOfThemeBackground1 = dark1;
                    colorOfThemeBackground2 = dark2;
                    updateFrame();

                }else{
                    return;
                }
            }
        });
        preferences.add(P_B_T, gbc);

        optionTabs.add("Preferences", new JScrollPane(preferences));
        optionFrame.add(optionTabs, BorderLayout.CENTER);
        optionFrame.pack();


    }

    /**
     * this method is called so if the close option is chosed the app does what is suposted to in the case the user hits the close button
     * @return true if the user choose to close the app and false if the user did'nt choose to close the app
     */
    private boolean createPanelBeforeClose(){
        String str = "Do you actually want to change the theme of the application?";
        int warningDialog = JOptionPane.showConfirmDialog(mainFrame, str, "WARNING", JOptionPane.WARNING_MESSAGE);

        if(warningDialog== OK_OPTION){
            return true;
        }else{
            return false;
        }

    }

    /**
     * this method is called so that the insomnia display area is created
     */
    private void createInsomniaDisplayArea() {

        JLabel title = new InsomniaDisplayPanel(colorOfThemeForground);
        mainFrame.add(title, insomniaPanelHandler.getFirstPanelConstraints());
        insomniaPanelHandler.setFirstPanel(title);
    }

    /**
     * this method is called so that the second upper panel is created (the one containing the info about request
     */
    private void createRequestInfoPanel() {

        //preparing the second head part as a panel
        JPanel secondUpPart = new JPanel(new FlowLayout(FlowLayout.CENTER));
        secondUpPart.setBorder(BorderFactory.createLineBorder(new java.awt.Color(218, 218, 218)));
        //secondUpPart.setBounds(new Rectangle(200,60));
        secondUpPart.setSize(new Dimension(200, 60));


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
                {new Font("Serif", Font.BOLD, 15), Color.BLACK, "SEPARATOR"}

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
        }else if(colorOfThemeForground.equals(purple)) {
            method.setBackground(new java.awt.Color(187, 171, 255));
        }
        method.setRenderer(renderer);
        if(indexOfRequest!=-1 && indexOfRequest<savedRequests.size()){
            method.setSelectedIndex(savedRequests.get(indexOfRequest).getTypeOfRequest().ordinal());
        }

        method.addActionListener (new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                if (indexOfRequest > -1 && indexOfRequest < savedRequests.size()) {
                    savedRequests.get(indexOfRequest).setType(TYPE.getTypeByIndex(method.getSelectedIndex()));
                    updateFrame();
                }
            }
        });

        secondUpPart.add(method);


        JTextField addressField = new JTextField("https://api.myproduct.com/v1/users");
        addressField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(addressField.getText().equals("https://api.myproduct.com/v1/users"))
                addressField.setText("");
            }
        });
        addressField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateLabel(e);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                updateLabel(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateLabel(e);
            }

            private void updateLabel(DocumentEvent e) {
                if(indexOfRequest<savedRequests.size() && indexOfRequest>-1){
                    savedRequests.get(indexOfRequest).setUrl(addressField.getText());
                }
            }
        });


        if(indexOfRequest!=-1 && indexOfRequest<savedRequests.size()){
            addressField.setText(savedRequests.get(indexOfRequest).getUrl());
        }

        addressField.setForeground(new java.awt.Color(166, 166, 166));
        addressField.setFont(new Font("Serif", Font.PLAIN, 17));
        addressField.setPreferredSize(new Dimension(350, 48));
        secondUpPart.add(addressField);

        JButton sendButton = new JButton("Send");
        sendButton.setEnabled(true);

        sendButton.setBackground(Color.white);
        sendButton.setPreferredSize(new Dimension(100, 48));
        sendButton.setForeground(new java.awt.Color(166, 166, 166));
        CreateGUI gui =this;
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(indexOfRequest>=0&&indexOfRequest<savedRequests.size()) {
                    //got execute the request
                    //first we set everything and then we execute it
                    //, , the format of data, the query, the auth, the headers
                    //the url
                    savedRequests.get(indexOfRequest).setUrl(addressField.getText());
                    //auth boolean
                    if (((JComboBox) insomniaPanelHandler.getFifthPanel().getTabComponentAt(1)).getSelectedIndex() == 0) {
                        savedRequests.get(indexOfRequest).setAuth(true);
                        //and the auth, you got take it and set it
                        //the auth is saved
                    } else {
                        savedRequests.get(indexOfRequest).setAuth(false);
                    }
                    //the type of request
                    savedRequests.get(indexOfRequest).setTypeOfRequest(method.getSelectedIndex());
                    if(savedRequests.get(indexOfRequest).getTypeOfRequest().equals(TYPE.PUT)
                            || savedRequests.get(indexOfRequest).getTypeOfRequest().equals(TYPE.POST)
                            || savedRequests.get(indexOfRequest).getTypeOfRequest().equals(TYPE.PATCH)) {
                        //format of body type
                        savedRequests.get(indexOfRequest).setTypeOfData(((JComboBox) insomniaPanelHandler.getFifthPanel().getTabComponentAt(0)).getSelectedIndex());
                        //and the info of the data has already been set
                    }
                    //should give a name to the output file container
                    savedRequests.get(indexOfRequest).setNameOutPutContainer(Command.getNameOfOutputFile());
                    //the next statement can take a while so should be done inside the swing worker
                    ExecuterSwingWorker exe = new ExecuterSwingWorker(savedRequests.get(indexOfRequest), gui);
                    System.out.println("calling the executor");
                    exe.execute();
//                    Executer methodExecuter = new Executer( savedRequests.get(indexOfRequest) );
                }
//                updateFrame();


            }
        });

        secondUpPart.add(sendButton);


//        /-

        JButton saveButton = new JButton("Save");
        saveButton.setToolTipText("Ctrl + S");

        Action action = new AbstractAction("Save") {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(indexOfRequest>=0&&indexOfRequest<savedRequests.size()) {
                    savedRequests.get(indexOfRequest).setSaved(true);
                    savedRequests.get(indexOfRequest).setUrl(addressField.getText());
                    if (((JComboBox) insomniaPanelHandler.getFifthPanel().getTabComponentAt(1)).getSelectedIndex() == 0) {
                        savedRequests.get(indexOfRequest).setAuth(true);
                        String[] authInfoThisRequest = {"Authorization", authBox, "true"};
                        savedRequests.get(indexOfRequest).setAuthInfo(authInfoThisRequest);
                        System.out.println("auth box ="+authBox);
                    } else {
                        savedRequests.get(indexOfRequest).setAuth(false);
                        String[] authInfoThisRequest = {"Authorization", authBox, "false"};
                        System.out.println("auth box ="+authBox);
                        savedRequests.get(indexOfRequest).setAuthInfo(authInfoThisRequest);
                    }
                    savedRequests.get(indexOfRequest).setTypeOfRequest(method.getSelectedIndex());
                    //format of body
                    savedRequests.get(indexOfRequest).setTypeOfData(((JComboBox) insomniaPanelHandler.getFifthPanel().getTabComponentAt(0)).getSelectedIndex());

                }
                updateFrame();
                System.out.println(authBox);
            }

        };
        action.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
        saveButton.setAction(action);
        saveButton.getInputMap(WHEN_IN_FOCUSED_WINDOW).put( (KeyStroke)action.getValue(Action.ACCELERATOR_KEY),"myAction");

        saveButton.setBackground(Color.white);
        saveButton.setPreferredSize(new Dimension(100, 48));
        saveButton.setForeground(new java.awt.Color(166, 166, 166));
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(indexOfRequest>=0&&indexOfRequest<savedRequests.size()) {
                    savedRequests.get(indexOfRequest).setSaved(true);
                    savedRequests.get(indexOfRequest).setUrl(addressField.getText());
                    if (((JComboBox) insomniaPanelHandler.getFifthPanel().getTabComponentAt(1)).getSelectedIndex() == 0) {
                        savedRequests.get(indexOfRequest).setAuth(true);
                    } else {
                        savedRequests.get(indexOfRequest).setAuth(false);
                    }
                    savedRequests.get(indexOfRequest).setTypeOfRequest(method.getSelectedIndex());
                    //format of body
                    savedRequests.get(indexOfRequest).setTypeOfData((  (JComboBox) insomniaPanelHandler.getFifthPanel().getTabComponentAt(0) ).getSelectedIndex());

                }
                updateFrame();
            }
        });

        saveButton.getActionMap().put("myAction", action);

        secondUpPart.add(saveButton);



        insomniaPanelHandler.setSecondPanel(secondUpPart);
        mainFrame.add(insomniaPanelHandler.getSecondPanel(), insomniaPanelHandler.getSecondPanelConstraints());
    }

    /**
     * this method is called so that the third upper panel is called
     */
    private void createHistorialRequest() {

        //preparing the third head part as a panel
        JPanel thirdUpPart = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
//        thirdUpPart.setPreferredSize(new Dimension(200,60));
        thirdUpPart.setMaximumSize(new Dimension(200, 60));
        thirdUpPart.setBounds(new Rectangle(200, 60));
        thirdUpPart.setBorder(BorderFactory.createLineBorder(new java.awt.Color(218, 218, 218)));

        //badan bayad time, hajmesh va code status esh ro bedast biyari

        //creating components of the JPanel
        JLabel statusLabel = null;
        if(indexOfRequest<0||indexOfRequest>=savedRequests.size()) {
            statusLabel = new StatusCodeLabel(StatusCodeLabel.statusCodeText(0), 0);
        }else{
            //its OK
            if(savedRequests.get(indexOfRequest).getResponse()!=null) {
                statusLabel = new StatusCodeLabel(StatusCodeLabel.statusCodeText(savedRequests.get(indexOfRequest).getResponse().getStatusCode()),savedRequests.get(indexOfRequest).getResponse().getStatusCode());
            }else{
                statusLabel = new StatusCodeLabel(StatusCodeLabel.statusCodeText(0), 0);
            }
        }
        thirdUpPart.add(statusLabel);

        //the time taken
        JLabel timeTakenLabel = null;
        if(indexOfRequest<0||indexOfRequest>=savedRequests.size()) {
            timeTakenLabel = new ResponseTimeInfo(0L, colorOfThemeBackground1);
        }else {
            //its OK
            if (savedRequests.get(indexOfRequest).getResponse() != null) {
                timeTakenLabel = new ResponseTimeInfo(savedRequests.get(indexOfRequest).getResponse().getTimeTaken(), colorOfThemeBackground1);
            } else {
                timeTakenLabel = new ResponseTimeInfo(0L, colorOfThemeBackground1);
            }
        }
        thirdUpPart.add(timeTakenLabel);

        //the size
        JLabel contentSizeLabel = null;
        if(indexOfRequest<0||indexOfRequest>=savedRequests.size()) {
            contentSizeLabel =  new ResponseSizeInfo(0L, colorOfThemeBackground1);
        }else {
            //its OK
            if (savedRequests.get(indexOfRequest).getResponse() != null) {
                contentSizeLabel = new ResponseSizeInfo(savedRequests.get(indexOfRequest).getResponse().getContentSize(), colorOfThemeBackground1);
            } else {
                contentSizeLabel = new ResponseSizeInfo(0L, colorOfThemeBackground1);
            }
        }
        thirdUpPart.add(contentSizeLabel);
/*
        //badan bayad barash pup up menu ham bezani ke bere historial ghabli haro ham bebine
        //and the historial of this request
        JButton historialThisRequest = new JButton("Last Time");
        historialThisRequest.setBackground(Color.white);
        historialThisRequest.setPreferredSize(new Dimension(100, 48));
        historialThisRequest.setForeground(new java.awt.Color(166, 166, 166));
        thirdUpPart.add(historialThisRequest);

 */

        insomniaPanelHandler.setThirdPanel(thirdUpPart);
        mainFrame.add(insomniaPanelHandler.getThirdPanel(), insomniaPanelHandler.getThirdPanelConstraints());
    }

    /**
     * this method is called so that the first panel containing the requests is created and they are calisified in different folder or different types
     */
    private void createRequestClasifier() {
        //the first panel down the "Insomnia" label containig the history of requests
        JPanel historialOfRequest = new JPanel();
//        int numberOfButtons = 1;
        int numberOfButtons = savedRequests.size();
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
        newRequestItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //create new Request and add it to the list
                //this request is not automatically saved you have to press the save button
                createNewRequest();
            }
        });
        leftPanelPopUpMenu.add(newRequestItem);
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

        insomniaPanelHandler.setForthPanelConstraints(historialConstraints);

        JScrollPane js = new JScrollPane(historialOfRequest,
                VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        js.setPreferredSize(new Dimension(112, 500));
        insomniaPanelHandler.setScroolPanelPointer(js);
        mainFrame.add(insomniaPanelHandler.getScroolPanelPointer(), insomniaPanelHandler.getForthPanelConstraints());

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
        JTextField searchField = new JTextField(filterSearched);
        searchField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                searchField.setText("");
            }
        });
        searchField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateLabel(e);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                updateLabel(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateLabel(e);
            }

            private void updateLabel(DocumentEvent e) {
                filterSearched=searchField.getText();
//                search(searchField.getText());
//                //the int[] of indexes was updated now repaint every thing again
//                updateFrame();
//                //put everything again on were everything was
//                indexOfSearched=null;
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
        plusButton.setComponentPopupMenu(leftPanelPopUpMenu);
        plusButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //this pop up menu is to be shown when the plus button is pressed
                leftPanelPopUpMenu.show(plusButton, plusButton.getX()-70, plusButton.getY()-10);
            }
        });
        upperPart.add(plusButton);


        //creating the "search Button"
        JButton searchButton = new JButton(new ImageIcon((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\magnifier-icon.png"));
        searchButton.setPreferredSize(new Dimension(25, 25));
        searchButton.setBackground(colorOfThemeBackground2);
        searchButton.setComponentPopupMenu(leftPanelPopUpMenu);
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                search(searchField.getText());
                //the int[] of indexes was updated now repaint every thing again
                updateFrame();
                //put everything again on were everything was
                indexOfSearched=null;
            }
        });
        upperPart.add(searchButton);



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

        for (int i = 0; i < savedRequests.size(); i++) {
            if (i == savedRequests.size()-1) {
                constraints.weightx = 1;
                constraints.weighty = 1;
            }
            historialOfRequest.setPreferredSize(new Dimension(112, 100 + numberOfButtons * 25));
            JButton protoTypeButton = new RequestButton(i, savedRequests.get(i).getNameOfRequest(), new ImageIcon((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\"+savedRequests.get(i).getTypeOfRequest()+".png"),
                    plusButton.getPreferredSize().height, colorOfThemeBackground2, colorOfThemeBackground1);
            ((RequestButton)protoTypeButton).setRequest(savedRequests.get(i));
            ((RequestButton)protoTypeButton).setGUI(this);

            protoTypeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    bodyData = new ArrayList<>();
                    indexOfRequest = ((RequestButton)protoTypeButton).getIndexOfRequest();
                    updateFrame();
                }
            });

            if(isIndexOfRequest(i)){
                if(colorOfThemeForground.equals(purple)) {
                    protoTypeButton.setBackground(new Color(149, 119, 161));
                    protoTypeButton.setForeground(colorOfThemeBackground2);
                }else{
                    protoTypeButton.setBackground(new Color(104, 171, 155));
                }
            }

            historialOfRequest.add(protoTypeButton, constraints);
            constraints.gridy += 1.0;
            numberOfButtons++;

        }
    }


    private boolean isIndexOfRequest(int i){
        if(indexOfSearched==null){
            return false;
        }
        for(int j=0; j<indexOfSearched.length; j++){
            if(indexOfSearched[j]==i){
                return true;
            }
        }
        return false;
    }


    private void search(String nameOfRequest){
        if(savedRequests==null || nameOfRequest.length()==0){
            return;
        }

        ArrayList<Integer> indexes = new ArrayList<>();
        for(int i=0; i<savedRequests.size(); i++){
            if(savedRequests.get(i).getNameOfRequest().contains(nameOfRequest)){
                indexes.add(i);
            }
        }
        indexOfSearched = new int[indexes.size()];
        for(int i=0; i<indexes.size(); i++){
            indexOfSearched[i]=indexes.get(i);
        }
        return;
    }


    /**
     * this method is method is called so that the panel containing the information about an request is created
     */
    private void createRequestInfo() {

        //the second panel down the "Insomnia" label containig the command info of requests
        JTabbedPane setRequestTabedPane = new JTabbedPane();
        setRequestTabedPane.setPreferredSize(new Dimension(600, 500));
        setRequestTabedPane.setFont(new Font("SansSerif", Font.BOLD, 15));
        setRequestTabedPane.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground1));
        setRequestTabedPane.setBackground(colorOfThemeBackground2);
        setRequestTabedPane.setForeground(colorOfThemeBackground1);
        //now the panels
        JPanel body;
        if(indexOfRequest>-1 && indexOfRequest<savedRequests.size()){
            body= new BodyMessage(savedRequests.get(indexOfRequest));
        }else{
            body = new BodyMessage(null);
        }

        body.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground1));
        body.setBackground(colorOfThemeBackground2);
//        JScrollPane bodyContainer = new JScrollPane();
        setRequestTabedPane.add("", new JScrollPane(body));

        Object dataTypes[][] = {
            {new Font("Serif", Font.BOLD, 15), colorOfThemeBackground1, new ImageIcon((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\url-icon.png"), "Form Url Encoded"},
            {new Font("Serif", Font.BOLD, 15), new java.awt.Color(255, 161, 20),new ImageIcon((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\json-icon-1.png") , "JSON"},
            {new Font("Serif", Font.BOLD, 15), Color.BLACK, null, "SEPARATOR"},
            {new Font("Serif", Font.BOLD, 15), colorOfThemeBackground1, new ImageIcon((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\binary-file-icon1.png"), "Binary File"},
            {new Font("Serif", Font.BOLD, 15), colorOfThemeBackground1, new ImageIcon((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\multipartform-icon.png"), "Multi-Part Form"}
        };
        //the costume renderer for the JcomboBox containing the methods for sending the data
        ListCellRenderer renderer = new CellRendererForBodyComboBox();
        JComboBox dataType = new JComboBox(dataTypes);
        dataType.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if(dataType.getSelectedIndex()==2){
                    dataType.setSelectedIndex(3);
                }
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
        dataType.setForeground(Color.WHITE);
        dataType.setRenderer(renderer);
        dataType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(indexOfRequest<savedRequests.size() && indexOfRequest>-1){
                    //ba method payin faghat type body ro save mikonim
                    savedRequests.get(indexOfRequest).setTypeOfBody(MESSAGEBODY_TYPE.getFormByIndex(dataType.getSelectedIndex()));
//                    if(savedRequests.get(indexOfRequest).getFormDataInfo().getClass().equals(java.util.ArrayList.class)){
//                        System.out.println("size of arraylist is "+((ArrayList<String[]>)savedRequests.get(indexOfRequest).getFormDataInfo()).size());
//                    }
                }
                //now we are sure we have the type of the body message set correctly
                createBodyTab(body, dataType.getSelectedIndex(), timesBodyTypeComboBoxChanged);
                timesBodyTypeComboBoxChanged++;
                mainFrame.revalidate();
                mainFrame.repaint();
                mainFrame.pack();
//                updateFrame();
            }
        });
        if( indexOfRequest>-1 && indexOfRequest<savedRequests.size() ) {
            if (savedRequests.get(indexOfRequest).getTypeOfData().equals(MESSAGEBODY_TYPE.FORM_URL)) {
                dataType.setSelectedIndex(0);
            }else if(savedRequests.get(indexOfRequest).getTypeOfData().equals(MESSAGEBODY_TYPE.JSON)){
                dataType.setSelectedIndex(1);
            }else if(savedRequests.get(indexOfRequest).getTypeOfData().equals(MESSAGEBODY_TYPE.BINARY)){
                dataType.setSelectedIndex(3);
            }else if(savedRequests.get(indexOfRequest).getTypeOfData().equals(MESSAGEBODY_TYPE.MULTIPART_FORM)){
                dataType.setSelectedIndex(4);
            }
        }
        setRequestTabedPane.setTabComponentAt(0, dataType);


        JPanel auth = new JPanel();
        auth.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground1));
        auth.setBackground(colorOfThemeBackground2);
        setRequestTabedPane.add("", new JScrollPane(auth));

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

        authType.setForeground(Color.WHITE);
        authType.setRenderer(authRenderer);
        authType.setSelectedIndex(2);
        authType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (indexOfRequest > -1 && indexOfRequest < savedRequests.size()) {
                    if (authType.getSelectedIndex() == 0) {
                        if (!savedRequests.get(indexOfRequest).getAuth()) {
                            authBox = "";
                        }
                        savedRequests.get(indexOfRequest).setAuth(true);
                        authEnabled=true;
                    } else {
                        savedRequests.get(indexOfRequest).setAuth(false);
                        authBox = "";
                        authEnabled=false;
                    }
                }
                createAuthTab(auth, authType.getSelectedIndex(), timesAuthTypeComboBoxChanged);
                timesAuthTypeComboBoxChanged++;
                mainFrame.revalidate();
                mainFrame.repaint();
                mainFrame.pack();
            }
        });
        if(indexOfRequest>-1&&indexOfRequest<savedRequests.size() ){
            if(savedRequests.get(indexOfRequest).getAuth()){
                authType.setSelectedIndex(0);
            }else{
                authType.setSelectedIndex(2);
            }
        }
        setRequestTabedPane.setTabComponentAt(1, authType);


        QueryPanel query;
        if(indexOfRequest!=-1 && indexOfRequest<savedRequests.size()) {
            if(savedRequests.get(indexOfRequest).getQueryInfo()==null || savedRequests.get(indexOfRequest).getQueryInfo().size()==0){
                //doesnt have any so we create one for it and
                query = new QueryPanel(colorOfThemeBackground1, colorOfThemeBackground2, savedRequests.get(indexOfRequest), this, colorOfThemeForground);
            }else {
                //its the first run, we need a constructor for this class that supports taking in info
                query = new QueryPanel(colorOfThemeBackground1, colorOfThemeBackground2, this, colorOfThemeForground, savedRequests.get(indexOfRequest));
            }
        }else{
            query = new QueryPanel();
        }
        query.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground1));
        query.setBackground(colorOfThemeBackground2);
//        query.setLayout(new GridBagLayout());
        setRequestTabedPane.add("Query", new JScrollPane(query, VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));
        //query tab its basically the same as the header tab

        JPanel header;
        if(indexOfRequest<savedRequests.size() && indexOfRequest>-1){
            header = new HeaderPanel(colorOfThemeBackground1, colorOfThemeBackground2, this, colorOfThemeForground, savedRequests.get(indexOfRequest));
        }else{
            header = new HeaderPanel();
        }
        header.setBackground(colorOfThemeBackground2);
        setRequestTabedPane.add("Header", new JScrollPane(header, VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));


        insomniaPanelHandler.setFifthPanel(setRequestTabedPane);
        mainFrame.add(insomniaPanelHandler.getFifthPanel(), insomniaPanelHandler.getFifthPanelConstraints());

    }

    /**
     * this method creates the first tab of the panel created in the method before
     * @param body the panel to be added the components at
     * @param bodyType the type of body which is the number of the type in the CheckBox
     * @param timesChanged the number of times the check box is changed
     */
    private void createBodyTab(JPanel body, int bodyType, int timesChanged) {
//
        Dimension sizeOfNow = mainFrame.getSize();
        Point locationOfNow = mainFrame.getLocationOnScreen();
        body.setLayout(new GridBagLayout());


        if (timesChanged > 0) {
            try {
                body.removeAll();
                body.setLayout(new GridBagLayout());
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println(e.getStackTrace());
            }
        }

        if(indexOfRequest<savedRequests.size() && indexOfRequest>-1){
            if(!savedRequests.get(indexOfRequest).getTypeOfRequest().equals(TYPE.PATCH) &&
                    !savedRequests.get(indexOfRequest).getTypeOfRequest().equals(TYPE.POST) &&
                    !savedRequests.get(indexOfRequest).getTypeOfRequest().equals(TYPE.PUT) ){
                JLabel empty = new JLabel("Only put, post and patch request can have body message");
                body.add(empty, new GridBagConstraints());
                return;
            }
        }else{
            JLabel empty = new JLabel("Please select a request");
            body.add(empty, new GridBagConstraints());
            return;
        }

//        if(indexOfRequest>-1 && indexOfRequest< savedRequests.size()){
//            System.out.println("request exist");
//            if(savedRequests.get(indexOfRequest).getTypeOfRequest().equals(TYPE.PUT) ||
//                    savedRequests.get(indexOfRequest).getTypeOfRequest().equals(TYPE.POST) ||
//                    savedRequests.get(indexOfRequest).getTypeOfRequest().equals(TYPE.PATCH) ){
//                System.out.println("request has saved info");
//                b(this, savedRequests.get(indexOfRequest), colorOfThemeBackground1, colorOfThemeBackground2, colorOfThemeForground);
//            }else{
//                System.out.println("request does not have saved info");
//                new BodyMessage(savedRequests.get(indexOfRequest));
//            }
//        }else{
//            System.out.println("request doesnt exist");
//            new BodyMessage();
//        }

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.fill = GridBagConstraints.BOTH;

        if (bodyType == 0) {
            //its "From URL Encoded
            GridBagConstraints constraints1 = new GridBagConstraints();
            constraints1.gridy=0;
            constraints1.gridx=0;
            constraints1.weightx=0;
            constraints1.weighty=0;
            constraints1.fill=GridBagConstraints.HORIZONTAL;
            constraints1.anchor=GridBagConstraints.FIRST_LINE_START;
            createFormData(body,/* constraints1,*/ null, null, true);
            mainFrame.revalidate();
            mainFrame.repaint();
            mainFrame.pack();

        } else if (bodyType == 1) {
            //its "JSON"
            String start="";
                if(savedRequests.get(indexOfRequest).getTypeOfData().equals(MESSAGEBODY_TYPE.JSON)){
                    Object obj = savedRequests.get(indexOfRequest).getFormDataInfo();
                    if(obj == null){
                        savedRequests.get(indexOfRequest).setFormDataInfo("");
                    }else if(obj instanceof String){
                        start = (String) obj;
                    }else{
                        savedRequests.get(indexOfRequest).setFormDataInfo("");
                    }
                }else{
                    savedRequests.get(indexOfRequest).setTypeOfBody(MESSAGEBODY_TYPE.JSON);
                    savedRequests.get(indexOfRequest).setFormDataInfo("");
                }

            JTextArea bodyReqJSON = new JTextArea(start);
            bodyReqJSON.setSize(new Dimension(600, 900));
            bodyReqJSON.setPreferredSize(new Dimension(600, 900));
            bodyReqJSON.setFont(new Font("DialogInput", Font.PLAIN, 15));
            bodyReqJSON.setForeground(colorOfThemeBackground1);
            bodyReqJSON.setBackground(colorOfThemeBackground2);
            bodyReqJSON.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground1));
            bodyReqJSON.getDocument().addDocumentListener(new DocumentListener() {

                @Override
                public void changedUpdate(DocumentEvent e) {
                    updateLabel(e);
                }

                @Override
                public void insertUpdate(DocumentEvent e) {
                    updateLabel(e);
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    updateLabel(e);
                }

                private void updateLabel(DocumentEvent e) {
                    jsonContainer = bodyReqJSON.getText();
                    if(indexOfRequest>-1 && indexOfRequest< savedRequests.size()){
                        savedRequests.get(indexOfRequest).setFormDataInfo(jsonContainer);
                    }
                }
            });
            body.add(bodyReqJSON, constraints);
//            new JsonPanel( colorOfThemeBackground1, colorOfThemeBackground2, this, body);
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

            if(indexOfRequest<savedRequests.size() && indexOfRequest>-1) {
                if(savedRequests.get(indexOfRequest).getTypeOfData().equals(MESSAGEBODY_TYPE.BINARY)) {
                    try{
                        if( ((String) savedRequests.get(indexOfRequest).getFormDataInfo()).length()!=0) {
                            selectedFile.setText((String) savedRequests.get(indexOfRequest).getFormDataInfo());
                            pathContainer = selectedFile.getText();
                        }else{
                            selectedFile.setText("No file selected");
                            pathContainer = "";
                        }
                    }catch (java.lang.ClassCastException exception){
                        savedRequests.get(indexOfRequest).setFormDataInfo("");
                        selectedFile.setText("No file selected");
                        pathContainer = "";
                    }
                }else{
                    savedRequests.get(indexOfRequest).setTypeOfBody(MESSAGEBODY_TYPE.BINARY);
                    selectedFile.setText("No file selected");
                    savedRequests.get(indexOfRequest).setFormDataInfo("");
                    pathContainer = "";
                }
            }else{
                selectedFile.setText("No file selected");
                pathContainer = "";
            }
            selectedFile.setOpaque(true);
            selectedFile.setForeground(colorOfThemeBackground1);
            selectedFile.setBackground(new java.awt.Color(73, 73, 73));
            selectedFile.setBorder(BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
            selectedFile.setPreferredSize(new Dimension(150, 30));
            selectedFile.setFont(new Font("Serif", Font.BOLD, 20));
            selectedFile.setEditable(false);
            selectedFile.getDocument().addDocumentListener(new DocumentListener() {

                @Override
                public void changedUpdate(DocumentEvent e) {
                    updateLabel(e);
                }

                @Override
                public void insertUpdate(DocumentEvent e) {
                    updateLabel(e);
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    updateLabel(e);
                }

                private void updateLabel(DocumentEvent e) {
                    if(!selectedFile.getText().equals("No file selected") && selectedFile.getText().length()!=0){
                        pathContainer = selectedFile.getText();
                    }else {
                        pathContainer = "";
                    }
                }
            });
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
            JButton resetFile = new JButton("Reset File", new ImageIcon((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\reset-icon.png"));
            resetFile.setBackground(colorOfThemeBackground2);
            resetFile.setForeground(colorOfThemeBackground1);
            resetFile.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground1));
            if(selectedFile.getText().equals("No file selected") || selectedFile.getText().length()==0){
                resetFile.setEnabled(false);
            }else{
                resetFile.setEnabled(true);
            }
            resetFile.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    resetFile.setEnabled(false);
                    resetFile.setForeground(new java.awt.Color(94, 94, 94));
                    resetFile.setFont(new Font("Serif", Font.BOLD, 20));
                    resetFile.setBackground(colorOfThemeBackground2);
                    selectedFile.setText("No file selected");
                    if(indexOfRequest<savedRequests.size() && indexOfRequest>-1){
                        savedRequests.get(indexOfRequest).setFormDataInfo("");
                    }
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
                        selectedFile.setText(fileChooser.getSelectedFile().getAbsolutePath());
                        pathContainer = selectedFile.getText();
                        if(indexOfRequest>-1 && indexOfRequest<savedRequests.size()) {
                            savedRequests.get(indexOfRequest).setFormDataInfo(fileChooser.getSelectedFile().getAbsolutePath());
                        }
                    }
                    if (selectedFile.getText().equals("No file selected") || selectedFile.getText().length()==0) {
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


        }else if(bodyType == 4) {
            //it form data
            GridBagConstraints constraints1 = new GridBagConstraints();
            constraints1.gridy = 0;
            constraints1.gridx = 0;
            constraints1.weightx = 0;
            constraints1.weighty = 0;
            constraints1.fill = GridBagConstraints.HORIZONTAL;
            constraints1.anchor = GridBagConstraints.FIRST_LINE_START;
            createFormData(body,/* constraints1,*/ null, null, true);
            mainFrame.revalidate();
            mainFrame.repaint();
            mainFrame.pack();

        }
        mainFrame.revalidate();
        mainFrame.repaint();
        mainFrame.pack();
        //a small bug here that i dont know how to fix// never mind now i have an idea but dont have the time
        mainFrame.setPreferredSize(sizeOfNow);
        mainFrame.setLocation(locationOfNow);
    }

    /**
     * this method creates the second tab of the panel created in the method before
     * @param body the in which the components are added at
     */
    private void createFormData(JPanel body /*, GridBagConstraints constraints*/, String name, String value, boolean isEnabled){
        ((BodyMessage)body).createBodyMessage(this, colorOfThemeBackground1, colorOfThemeBackground2, colorOfThemeForground);
    }

    /**
     * the third tab which is the authentication tab is created in this method
     * @param auth panel to be added the components at
     * @param authType the type of auth which is the number of the type in the CheckBox
     * @param timesChanged the number of times the check box is changed
     */
    private void createAuthTab(JPanel auth, int authType, int timesChanged) {

        auth.setLayout(new GridBagLayout());
        if (timesChanged > 0) {
            try {
                auth.removeAll();
            } catch (ArrayIndexOutOfBoundsException e) {
            }
        }
        Dimension sizeOfNow = mainFrame.getSize();
        Point locationOfNow = mainFrame.getLocationOnScreen();

        if(indexOfRequest<0 || indexOfRequest>=savedRequests.size()){
            JLabel empty;
            if(savedRequests.size()==0){
                empty=new JLabel("Please create a new Request and select it");
            }else {
                empty = new JLabel("Please select a request");
            }
            auth.add(empty);
            return;
        }

        if (authType == 0) {
            if(savedRequests.get(indexOfRequest).getAuthInfo()==null){
                savedRequests.get(indexOfRequest).setAuthInfo(new String[3]);
            }
            //its "Bearer Token" form
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 0;
            gbc.weighty = 0;
            gbc.insets = new Insets(5, 30, 5, 15);
            gbc.ipady = 0;
            gbc.ipadx = 0;
            gbc.anchor = GridBagConstraints.PAGE_START;
            gbc.fill = GridBagConstraints.BOTH;

            //token label
            JLabel tokenLabel = new JLabel("Token");
            tokenLabel.setOpaque(false);
            tokenLabel.setFont(new Font("TimesRoman", Font.BOLD, 15));
            tokenLabel.setForeground(colorOfThemeBackground1);
            tokenLabel.setPreferredSize(new Dimension(50, 20));
            auth.add(tokenLabel, gbc);
            //token text field
            String authOfThisRequest = "";
            if(indexOfRequest>-1 && indexOfRequest <savedRequests.size()){
                if(savedRequests.get(indexOfRequest).getAuthInfo()[1]!=null && savedRequests.get(indexOfRequest).getAuthInfo()[1].length()!=0){
                    authOfThisRequest =savedRequests.get(indexOfRequest).getAuthInfo()[1];
                }
            }
            JTextField tokenTextField = new JTextField(authOfThisRequest);
            tokenTextField.setOpaque(false);
            tokenTextField.setForeground(colorOfThemeBackground1);
            tokenTextField.setBackground(colorOfThemeBackground2);
            tokenTextField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, colorOfThemeBackground1));
            tokenTextField.setPreferredSize(new Dimension(150, 20));

            tokenTextField.getDocument().addDocumentListener(new DocumentListener() {

                @Override
                public void changedUpdate(DocumentEvent e) {
                    updateLabel(e);
                }

                @Override
                public void insertUpdate(DocumentEvent e) {
                    updateLabel(e);
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    updateLabel(e);
                }

                private void updateLabel(DocumentEvent e) {
                    if(indexOfRequest<savedRequests.size() && indexOfRequest>-1) {
                        authBox = tokenTextField.getText();
                        String[] authInfo = null;
                        if(authEnabled){
                            String[] authinf = {"Authorization", authBox, "true"};
                            authInfo=authinf;
                        }else{
                            String[] authInf = {"Authorization", authBox, "false"};
                            authInfo=authInf;
                        }
                        savedRequests.get(indexOfRequest).setAuth(true);
                        savedRequests.get(indexOfRequest).setAuthInfo(authInfo);
                    }else{
                        authBox="";
                    }
                }
            });
            gbc.gridx = 1;
            auth.add(tokenTextField, gbc);
            //the Enabled Option
            boolean isEnabled = true;
            if(indexOfRequest<savedRequests.size() && indexOfRequest> -1){
                if(savedRequests.get(indexOfRequest).getAuthInfo()[2]!=null && savedRequests.get(indexOfRequest).getAuthInfo()[2].equals("true")){
                    isEnabled =true;
                }else if(savedRequests.get(indexOfRequest).getAuthInfo()[2]!=null && savedRequests.get(indexOfRequest).getAuthInfo()[2].equals("false")){
                    isEnabled=false;
                }
            }
            JCheckBox enabledCheckBox = new JCheckBox("Enabled", isEnabled);
            enabledCheckBox.setOpaque(false);
            enabledCheckBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (enabledCheckBox.isSelected()) {
                        authEnabled = true;
                    } else {
                        authEnabled = false;
                    }
                    if(indexOfRequest<savedRequests.size() && indexOfRequest>-1){
                        savedRequests.get(indexOfRequest).setAuth(enabledCheckBox.isSelected());
                    }
                }
            });
            gbc.gridx = 1;
            gbc.gridy = 2;
            auth.add(enabledCheckBox, gbc);

        } else if (authType == 2) {
            //no authentication
            authBox="";
            authEnabled=false;
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



    /**
     * this method is called so that the down part of the third panel containing info about the answer is created
     */
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
        queryHistoryPanel.add("preview", new JScrollPane( preview , VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED));
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
                CreatePreview(preview, 0);
                mainFrame.revalidate();
                mainFrame.repaint();
                mainFrame.pack();
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
                //and change the panel
                CreatePreview(preview, 1);
                mainFrame.revalidate();
                mainFrame.repaint();
                mainFrame.pack();

            }
        });

        group.add(rawData);
        previewModeMenu.add(rawData);

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


        insomniaPanelHandler.setSixthPanel(queryHistoryPanel);
        mainFrame.add(insomniaPanelHandler.getSixthPanel(), insomniaPanelHandler.getSixthPanelConstraints());
    }

    /**
     * this method is called so that the down part of the third panel containing info about the answer is created
     */
    private void CreatePreview(JPanel preview, int typeOfPreview){
        preview.removeAll();

        if(indexOfRequest<0 || indexOfRequest>=savedRequests.size()){
            JLabel empty = new JLabel("Please select a request");
            preview.add(empty);
            return;
        }
        if (savedRequests.get(indexOfRequest).getResponse() == null) {
            if(wrongRequest){
                JLabel empty = new JLabel("Wrong Request");
                empty.setForeground(Color.RED);
                preview.add(empty);
                return;
            }
            JLabel empty = new JLabel("waiting for response");
            preview.add(empty);
            return;
        }


        if(typeOfPreview==0) {
            //its visual preview

            //first we have to check if the response is ready or not
            if (savedRequests.get(indexOfRequest).getResponse() == null) {
                JLabel empty = new JLabel("waiting for response");
                preview.add(empty);
                return;
            }

            //first we have to check its actually an image
            String name = new File(savedRequests.get(indexOfRequest).getResponse().getPathOutputFile()).getName();
            if (("" + name.charAt(name.length() - 4) + name.charAt(name.length() - 3) + name.charAt(name.length() - 2) + name.charAt(name.length() - 1)).equals(".png")) {
                if (new File(savedRequests.get(indexOfRequest).getResponse().getPathOutputFile()).exists() && new File(savedRequests.get(indexOfRequest).getResponse().getPathOutputFile()).isFile()) {
                    //i will use of an image just to see the result
                    JLabel visualPreview = new JLabel(new ImageIcon(savedRequests.get(indexOfRequest).getResponse().getPathOutputFile()));
                    preview.add(visualPreview);
                }else{
                    JLabel visualPreview = new JLabel("The response of the server is unreadable");
                    preview.add(visualPreview);
                }
            } else {
                JLabel empty = new JLabel("this program supports visual preview only for .png files");
                preview.add(empty);
                return;
            }
        }else if(typeOfPreview==1){
            //its raw info
            String fileAsString = "";
            try {
                InputStream is = new FileInputStream(savedRequests.get(indexOfRequest).getResponse().getPathOutputFile());
                BufferedReader buf = new BufferedReader(new InputStreamReader(is));

                String line = buf.readLine();
                StringBuilder sb = new StringBuilder();

                while (line != null) {
                    sb.append(line).append("\n");
                    line = buf.readLine();
                }

                fileAsString = sb.toString();
                fileAsString = getStringOfField(fileAsString, 30);
            }catch (FileNotFoundException excp){
                fileAsString = "Empty Response body";
            } catch (IOException e) {
                System.out.println("unable to open file");
            }
            JTextArea rawData = new JTextArea(fileAsString);
            rawData.setEditable(false);
            rawData.setFont(new Font("Serif", Font.BOLD, 18));
            rawData.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground1, 2, true));
            if(colorOfThemeBackground1.equals(light1)) {
                rawData.setForeground(Color.BLACK);
                rawData.setBackground(Color.WHITE);
            }else{
                rawData.setForeground(Color.WHITE);
                rawData.setBackground(Color.BLACK);
            }
            rawData.setPreferredSize(new Dimension(preview.getWidth()-20, preview.getHeight()-20));
            preview.add(rawData);
        }
    }

    /**
     * this method is called so that the Header tab in the third panel is created
     * @param header the panel in which the components are added at
     */
    private void createStaticHeaderTab(JPanel header) {
        if(indexOfRequest<0 || indexOfRequest>=savedRequests.size()){
            JLabel empty = new JLabel("Select a Request");
            header.add(empty);
            return;
        }
        if(savedRequests.get(indexOfRequest).getResponse()==null){
            if(wrongRequest){
                JLabel empty = new JLabel("Wrong Request");
                empty.setForeground(Color.RED);
                header.add(empty);
                return;
            }
            JLabel empty = new JLabel("Waiting for response form the server");
            header.add(empty);
            return;
        }
        //first we take in the information to be shown
        String[][] nameValueInfo = savedRequests.get(indexOfRequest).getResponse().getStandardFormHeaders();


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
            String nameOfField = getStringOfField(nameValueInfo[i][0], 35);

            if(nameOfField.length()==0 || getStringOfField(nameValueInfo[i][1], 35).length()==0){
                continue;
            }
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
            String valueOfField = getStringOfField(nameValueInfo[i][1], 35);

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
                if(copyToClipboard.getText().equals("  Copy to Clipboard  ")) {
                    copyToClipboard.setIcon(new ImageIcon((new File(".").getAbsolutePath()) + "\\src\\GUI\\resource" + colorOfThemeForground + "\\tick-icon.png"));
                    copyToClipboard.setText("  Copied  ");
                    Clipboard cb = Toolkit.getDefaultToolkit()
                            .getSystemClipboard();

                    String clipBoardInfo = "";
                    for(int i=0; i<nameValueInfo.length; i++){
                        clipBoardInfo+=nameValueInfo[i][0]+": "+nameValueInfo[i][1]+"\n";
                    }
                    cb.setContents(new StringSelection(clipBoardInfo), null);
                }else{
                    copyToClipboard.setIcon(new ImageIcon((new File(".").getAbsolutePath())+"\\src\\GUI\\resource"+colorOfThemeForground+"\\copy-icon2.png"));
                    copyToClipboard.setText("  Copy to Clipboard  ");
                }
            }
        });
        header.add(copyToClipboard, constraints);

    }

    /**
     * this method takes an string and inserts some "\n" into it so that they will look better on a JTextArea
     * @param input the input string
     * @return the string with "\n" on it
     */
    private String getStringOfField(String input, int size) {
        String newString = "";
        if(input==null){
            return newString;
        }
        for (int i = 0, j = 0; i < input.length(); i++, j++) {
            newString += input.charAt(i);
            if (j == size) {
                j = 0;
                newString += "\n";
            }
        }
        return newString;
    }

    private void createNewRequest(){
        JFrame newRequest = new JFrame("New Request");
        newRequest.setLocation(800,500);
        newRequest.setVisible(true);
//        newRequest.setPreferredSize(new Dimension(300, 100));
        newRequest.setBackground(colorOfThemeBackground2);
        newRequest.setForeground(colorOfThemeBackground1);

        JTextField nameTextField =  new JTextField("Enter the name of the Request");
        nameTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (nameTextField.getText().equals("Enter the name of the Request")) {
                    nameTextField.setText("");
                }
            }
        });
        nameTextField.setPreferredSize(new Dimension(300,50));
        nameTextField.setBackground(colorOfThemeBackground1);
        nameTextField.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground1, 1, true));
        if(colorOfThemeBackground1.equals(dark1)){
            nameTextField.setForeground(Color.WHITE);
        }else{
            nameTextField.setForeground(Color.BLACK);
        }
        nameTextField.setOpaque(true);
        nameTextField.setVisible(true);

        newRequest.add(nameTextField, BorderLayout.NORTH);

        JButton create = new JButton("Create");
        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nameNewRequest="new Request";
                nameNewRequest = nameTextField.getText();
                newRequest.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));

                Request newRequest = new Request(nameNewRequest, TYPE.GET, "https://api.myproduct.com/v1/users", MESSAGEBODY_TYPE.FORM_URL);
//                nameNewRequest="new Request";
                newRequest.setSaved(false);

                //since we are creating a new Request with the default format of FORM URL
                savedRequests.add(newRequest);

                //inja bayad update konim
                updateFrame();
            }
        });
        create.setBackground(colorOfThemeBackground2);
        create.setBorder(BorderFactory.createLineBorder(colorOfThemeBackground1, 1, true));
        if(colorOfThemeBackground1.equals(dark1)){
            create.setForeground(Color.WHITE);
        }else{
            create.setForeground(Color.BLACK);
        }
        create.setPreferredSize(new Dimension(300,50));
        newRequest.add(create, BorderLayout.SOUTH);
        newRequest.setVisible(true);
        newRequest.pack();
    }

    private void saveInfoOfthisRun(){

        ArrayList<Request> savedRequests2 = new ArrayList<>();
        for(int i=0 ; i<savedRequests.size(); i++){
            if(savedRequests.get(i).isSaved()){
                savedRequests2.add(savedRequests.get(i));
            }
        }

        //save the requests:
        SaveInfo saveInfo = new SaveInfo(savedRequests2);
        //save the preferences:
        ArrayList<String> preferences = new ArrayList<>();
        preferences.add(checkBoxFollowRedirect+"");
        preferences.add(checkBoxSystemTray+"");
        if(colorOfThemeForground==purple) {
            preferences.add("purple");
        }else{
            preferences.add("blue");
        }
        if(colorOfThemeBackground1 == dark1){
            preferences.add("dark");
        }else{
            preferences.add("light");
        }
        saveInfo.savePreferences(preferences);
    }

    public static void checkIndexRequest(){
        if(savedRequests.size()==0){
            indexOfRequest=-1;
        }
    }

    public JFrame getMainFrame(){
        return mainFrame;
    }

    public void updateFrame() {
        try {
            mainFrame.remove(insomniaPanelHandler.getFirstPanel());
            mainFrame.remove(insomniaPanelHandler.getSecondPanel());
            mainFrame.remove(insomniaPanelHandler.getThirdPanel());
            mainFrame.remove(insomniaPanelHandler.getScroolPanelPointer());
            mainFrame.remove(insomniaPanelHandler.getFifthPanel());
            mainFrame.remove(insomniaPanelHandler.getSixthPanel());
            //the next one wont work
//            mainFrame.removeAll();
            mainFrame.revalidate();
            mainFrame.repaint();
            paintFrame();
        } catch (NullPointerException exception) {
            System.out.println("help me");
        }

    }


     class ExecuterSwingWorker extends SwingWorker<Object, Object>{

        Request request;
        CreateGUI gui;
        public ExecuterSwingWorker(Request request, CreateGUI gui){
            this.request=request;
            request.setResponse(null);
            this.gui=gui;
            gui.updateFrame();
        }

         @Override
         protected Object doInBackground() throws Exception {
             new Executer(request);
             return null;
         }


         @Override
         protected void done() {
            if(request.getResponse()==null){
                wrongRequest = true;
            }
            Response response = request.getResponse();
             System.out.println();
             gui.updateFrame();
             wrongRequest = true;
         }
     }


 }
