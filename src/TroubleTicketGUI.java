import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import java.awt.Font;
import java.awt.LayoutManager;



public class TroubleTicketGUI
{
	public  static       int 	DEFAULT_LOGIN_MODE = 0; //Which entry should be used? If this is too large, the last one will be used.
	public  static final String LOGINS_FILE_PATH = "login.txt"; //Where are our logins and passwords stored? This is for security and for not committing passwords to git.
	
	public  static final int 	PADDING = 10;
	public  static final int 	DEFAULT_DIVIDER_WIDTH = 150;
	
	public	static final String NORTH = SpringLayout.NORTH;
	public 	static final String EAST = SpringLayout.EAST;
	public 	static final String SOUTH = SpringLayout.SOUTH;
	public 	static final String WEST = SpringLayout.WEST;
	
	public 	static final String NESW = "NESW";

	private static final String BUTTON_LOGIN_NAME = "btnLogin";
	private static final String BUTTON_NEW_TICKET_NAME = "btnNewTicket";
	private static final String BUTTON_MODIFY_TICKET_NAME = "btnModifyTicket";
	private static final String BUTTON_SEARCH_NAME = "btnSearch";

	private static final String PANEL_RIGHT_CONTENT_NAME = "panelRightContent";
	
	private static final String NOT_LOGGED_IN = "Not currently logged in.";

	
	
	public static String oppositeDirection(String direction)
	{
		switch(direction.toUpperCase().charAt(0))
		{
			case 'N':
				return SOUTH;
			case 'E':
				return WEST;
			case 'S':
				return NORTH;
			case 'W':
				return EAST;
			default:
				return null;
		}
		
	}
	
	public static void printf(String s, Object... args)
	{
		System.out.printf(s, args);
	}
	
	public static void print(String s)
	{
		System.out.print(s);
	}
	
	public static void println(String s)
	{
		System.out.println(s);
	}
	
	public static Component getVisibleComponent(JPanel jp)
	{
		for(Component c : jp.getComponents())
		{
			if(c.isVisible())
			{
				return c;
			}
		}
		return null;
	}
	
	public static ArrayList<JButton> getButtonsFromJPanel(JPanel jp)
	{
		ArrayList<JButton> ret = new ArrayList<>();
		
		for(Component c : jp.getComponents())
		{
			if(c instanceof JButton)
			{
				ret.add((JButton)c);
			}
		}
		return ret;
	}
	
	
	/**
	 * @param buttonContainer The container that has the buttons inside of it.
	 * 
	 * With a bunch of buttons, they will be laid out using springLayout offset by 10px from eachother.
	 */
	public void formatButtons(JPanel buttonContainer)
	{
		//insert formatting for all buttons.
		ArrayList<JButton> menuButtons = getButtonsFromJPanel(buttonContainer);
		SpringLayout tempSL = (SpringLayout)buttonContainer.getLayout();
		
		
		for(int i = 0; i < menuButtons.size(); i++)
		{
			JButton tempButton = menuButtons.get(i);
			printf("menuButtons[%d] = %s\n",i,tempButton);
			
			tempSL.putConstraint(WEST, tempButton, PADDING, WEST, buttonContainer);
			tempSL.putConstraint(EAST, tempButton, -PADDING, EAST, buttonContainer);
			
			if(i == 0) //first button aligns vertically w/ container
			{
				tempSL.putConstraint(NORTH, tempButton, PADDING, NORTH, buttonContainer);
			}
			else //all others align with south side of prev. button
			{
				JButton prevButton = menuButtons.get(i - 1);
				tempSL.putConstraint(NORTH, tempButton, PADDING, SOUTH, prevButton);
			}
		}
	}
	
	
	public void modifyRightPane(JPanel jp, ActionEvent e)
	{
		JPanel panel = panelModifyTicket;

		CardLayout cl = (CardLayout) panelRightContent.getLayout();
		JLabel label = lblCurrentPane;
		JButton button = (JButton)e.getSource();
		
		println("Someone clicked button \'" + button.getName() +"\' or \'" + button.getText() + "\'");
		printf("About to do cl.show(\"%s\", \"%s\")\n",panelRightContent.getName(),jp.getName());
		
		cl.show(panelRightContent, jp.getName());
		label.setText(button.getText());
	}
	
	/***
	 * Gets the info from the two login info fields.
	 * This is its own method so that either pressing ENTER or clicking "Submit" will log you in.
	 */
	public String[] getLogin()
	{
		String[] ret = {"",""};
		
		ret[0] = textFieldUsername.getText();
		ret[1] = textFieldPassword.getText();
		
		return ret;
	}
	
	private void doLogin(String username, String password)
	{
//		lblloginer
		lblLoginErrorMsg.setText("");
		int result = Dao.login(username, password);
		

			
			if(result == Dao.NORMAL_USER || result == Dao.ADMINISTRATOR)
			{//log them in!
				if(result == Dao.NORMAL_USER)
				{
					lblWhosLoggedIn.setText(String.format(Dao.NORMAL_USER_S,username));
				}
				else if(result == Dao.ADMINISTRATOR)
				{
					lblWhosLoggedIn.setText(String.format(Dao.ADMINISTRATOR_S,username));
				}
				printf("Welcome, '%s'!",username);
				lblWhosLoggedIn.setText(username);
			}
			if(result == Dao.PASSWORD_INCORRECT)
			{
				lblLoginErrorMsg.setText(String.format(Dao.PASSWORD_INCORRECT_S, username));
			}
			if(result == Dao.USERNAME_NOT_FOUND)
			{
				lblLoginErrorMsg.setText(String.format(Dao.USERNAME_NOT_FOUND_S, username));
			}
		
		
	}	
	

	private JFrame frame;
	private JLabel lblWhosLoggedIn;
	private JLabel lblCurrentPane;
	
	private JButton btnLogin;
	private JButton btnNewTicket;
	private JButton btnModifyTicket;
	private JButton btnSearch;
	
	private JPasswordField textFieldPassword;
	private JTextField textFieldUsername;
	
	private JPanel panelRightContent;

	private JPanel panelLogin;
	private JPanel panelNewTicket;
	private JPanel panelSearch;
	private JPanel panelModifyTicket;
	private JButton btnModify;
	private JPanel panelRight;
	private SpringLayout sl_panelRight;
	private JButton btnLoginSubmit;
	private SpringLayout sl_panelLogin;
	private JPanel panelUsername;
	private SpringLayout sl_panelSearch;
	private SpringLayout sl_panelNewTicket;
	private JLabel lblUsername;
	private JPanel panelRightTop;
	private JPanel panelPassword;
	private JLabel lblPassword;
	private JButton btnNewTiket;
	private JButton btnSurch;
	private SpringLayout springLayout_1;
	private JPanel panelStatus;
	private JLabel lblCurrentlyLoggedIn;
	private JSplitPane splitPane;
	private JPanel panelLeft;
	private JSplitPane splitPaneLeft;
	private JPanel panelMenu;
	private JLabel lblLoginErrorMsg;
	private JPanel panelLoginErrorMsg;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		
		ArrayList<ArrayList<String>> logins = Dao.getLoginsFromFile(LOGINS_FILE_PATH);
		
		int i = 0;
		for (ArrayList<String> login : logins)
		{
//			printf("Login is %d long and is %s",login.size(),login);
//			printf("Login %2d: '%s','%s','%s'",i,login.get(0),login.get(1),login.get(2));
			i++;
		}
		
		if(DEFAULT_LOGIN_MODE >= logins.size())
		{
			DEFAULT_LOGIN_MODE = logins.size()-1; 
		}
		
		printf("\n\nWe're going to use the %dth entry in '%s', aka '%s'.\n\n",DEFAULT_LOGIN_MODE,LOGINS_FILE_PATH,logins.get(DEFAULT_LOGIN_MODE));
		
		ArrayList<String> login = logins.get(DEFAULT_LOGIN_MODE);
		
		Dao dao = new Dao(login.get(0), login.get(1), login.get(2));
		
		
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				TroubleTicketGUI window = new TroubleTicketGUI();
				window.frame.setVisible(true);
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TroubleTicketGUI()
	{
		initialize();
	}
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		Dao dao = new Dao();
		
		frame = new JFrame();
		frame.setBounds(100, 100, 785, 578);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);
		
		splitPane = new JSplitPane();
		splitPane.setName("splitPane");
		springLayout.putConstraint(SOUTH, splitPane, 0, SOUTH, frame.getContentPane());
		springLayout.putConstraint(EAST, splitPane, 0, EAST, frame.getContentPane());
		springLayout.putConstraint(NORTH, splitPane, 0, NORTH, frame.getContentPane());
		springLayout.putConstraint(WEST, splitPane, 0, WEST, frame.getContentPane());
		frame.getContentPane().add(splitPane, splitPane.getName());
		
		panelLeft = new JPanel();
		splitPane.setLeftComponent(panelLeft);
		panelLeft.setLayout(new BorderLayout(0, 0));
		
		splitPaneLeft = new JSplitPane();
		splitPaneLeft.setOrientation(JSplitPane.VERTICAL_SPLIT);
		panelLeft.add(splitPaneLeft);
		panelMenu = new JPanel();
		splitPaneLeft.setRightComponent(panelMenu);
		SpringLayout sl_panelMenu = new SpringLayout();
		panelMenu.setLayout(sl_panelMenu);
		
		btnLogin = new JButton("Login");
		sl_panelMenu.putConstraint(NORTH, btnLogin, 5, NORTH, panelMenu);
		sl_panelMenu.putConstraint(WEST, btnLogin, PADDING, WEST, panelMenu);
		btnLogin.setName(BUTTON_LOGIN_NAME);
		panelMenu.add(btnLogin);

		btnNewTicket = new JButton("New Ticket");
		btnNewTicket.setName(BUTTON_NEW_TICKET_NAME);
		panelMenu.add(btnNewTicket);

		btnModifyTicket = new JButton("Modify Ticket");
		btnModifyTicket.setName(BUTTON_MODIFY_TICKET_NAME);
		panelMenu.add(btnModifyTicket);

		btnSearch = new JButton("Search");
		btnSearch.setName(BUTTON_SEARCH_NAME);
		panelMenu.add(btnSearch);
		
		panelStatus = new JPanel();
		splitPaneLeft.setLeftComponent(panelStatus);
		
		lblCurrentlyLoggedIn = new JLabel("Currently logged in as:");
		panelStatus.add(lblCurrentlyLoggedIn);
		
		lblWhosLoggedIn = new JLabel(NOT_LOGGED_IN);
		lblWhosLoggedIn.setFont(new Font("Dialog", Font.PLAIN, 12));
		panelStatus.add(lblWhosLoggedIn);
		splitPaneLeft.setDividerLocation(100);
		
		

		


		panelRight = new JPanel();
		splitPane.setRightComponent(panelRight);
		sl_panelRight = new SpringLayout();
		panelRight.setLayout(sl_panelRight);

		panelRightContent = new JPanel();
		sl_panelRight.putConstraint(SOUTH, panelRightContent, 0, SOUTH, panelRight);
		sl_panelRight.putConstraint(EAST, panelRightContent, 0, EAST, panelRight);
		panelRightContent.setName(PANEL_RIGHT_CONTENT_NAME);
		sl_panelRight.putConstraint(NORTH, panelRightContent, 27, NORTH, panelRight);
		sl_panelRight.putConstraint(WEST, panelRightContent, 0, WEST, panelRight);
		panelRight.add(panelRightContent, panelRightContent.getName());
		panelRightContent.setLayout(new CardLayout(0, 0));
		
		panelLogin = new JPanel();
		panelLogin.setName("panelLogin");
		panelRightContent.add(panelLogin, panelLogin.getName());
		sl_panelLogin = new SpringLayout();
		panelLogin.setLayout(sl_panelLogin);
		
		panelNewTicket = new JPanel();
		panelNewTicket.setName("panelNewTicket");
		panelRightContent.add(panelNewTicket, panelNewTicket.getName());
		sl_panelNewTicket = new SpringLayout();
		panelNewTicket.setLayout(sl_panelNewTicket);
		
		panelSearch = new JPanel();
		panelSearch.setName("panelSearch");
		panelRightContent.add(panelSearch, panelSearch.getName());
		sl_panelSearch = new SpringLayout();
		panelSearch.setLayout(sl_panelSearch);
		
		btnLoginSubmit = new JButton("Submit");

		panelLogin.add(btnLoginSubmit);
		
		panelLoginErrorMsg = new JPanel();
		sl_panelLogin.putConstraint(SpringLayout.NORTH, panelLoginErrorMsg, 21, SpringLayout.NORTH, panelLogin);
		sl_panelLogin.putConstraint(SpringLayout.WEST, panelLoginErrorMsg, PADDING, SpringLayout.WEST, panelLogin);
		sl_panelLogin.putConstraint(SpringLayout.SOUTH, panelLoginErrorMsg, 61, SpringLayout.NORTH, panelLogin);
		sl_panelLogin.putConstraint(SpringLayout.EAST, panelLoginErrorMsg, -PADDING, SpringLayout.EAST, panelLogin);
		panelLogin.add(panelLoginErrorMsg);
		panelLoginErrorMsg.setLayout(new BorderLayout(0, 0));
		
		lblLoginErrorMsg = new JLabel("");
		lblLoginErrorMsg.setHorizontalAlignment(SwingConstants.CENTER);
		panelLoginErrorMsg.add(lblLoginErrorMsg, BorderLayout.CENTER);
		
		panelUsername = new JPanel();
		sl_panelLogin.putConstraint(SpringLayout.WEST, btnLoginSubmit, 0, SpringLayout.WEST, panelUsername);
		sl_panelLogin.putConstraint(SpringLayout.EAST, btnLoginSubmit, 0, SpringLayout.EAST, panelUsername);
		sl_panelLogin.putConstraint(SpringLayout.NORTH, panelUsername, 86, SpringLayout.NORTH, panelLogin);
		sl_panelLogin.putConstraint(SpringLayout.WEST, panelUsername, 227, SpringLayout.WEST, panelLogin);
		sl_panelLogin.putConstraint(SpringLayout.SOUTH, panelUsername, -363, SpringLayout.SOUTH, panelLogin);
		sl_panelLogin.putConstraint(SpringLayout.EAST, panelUsername, -242, SpringLayout.EAST, panelLogin);
		panelLogin.add(panelUsername);
		
		lblUsername = new JLabel("Username");
		panelUsername.add(lblUsername);
		sl_panelLogin.putConstraint(WEST, lblUsername, 41, WEST, panelLogin);
		sl_panelLogin.putConstraint(SpringLayout.SOUTH, lblUsername, -397, SpringLayout.SOUTH, panelLogin);
		
		textFieldUsername = new JTextField();

		panelUsername.add(textFieldUsername);
		textFieldUsername.setToolTipText("username");
		sl_panelLogin.putConstraint(NORTH, textFieldUsername, 120, NORTH, panelLogin);
		sl_panelLogin.putConstraint(WEST, textFieldUsername, PADDING, WEST, panelLogin);
		textFieldUsername.setColumns(10);
		
		panelPassword = new JPanel();
		sl_panelLogin.putConstraint(SpringLayout.NORTH, panelPassword, PADDING, SpringLayout.SOUTH, panelUsername);
		sl_panelLogin.putConstraint(SpringLayout.WEST, panelPassword, 0, SpringLayout.WEST, btnLoginSubmit);
		sl_panelLogin.putConstraint(SpringLayout.SOUTH, panelPassword, 78, SpringLayout.SOUTH, panelUsername);
		sl_panelLogin.putConstraint(SpringLayout.NORTH, btnLoginSubmit, PADDING, SpringLayout.SOUTH, panelPassword);
		sl_panelLogin.putConstraint(SpringLayout.EAST, panelPassword, 0, SpringLayout.EAST, panelUsername);
		panelLogin.add(panelPassword);
		
		lblPassword = new JLabel("Password");
		panelPassword.add(lblPassword);
		
		textFieldPassword = new JPasswordField();
		textFieldPassword.setToolTipText("password");
		textFieldPassword.setColumns(10);
		panelPassword.add(textFieldPassword);
		
		panelRightTop = new JPanel();
		panelRightTop.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		sl_panelRight.putConstraint(NORTH, panelRightTop, 0, NORTH, panelRight);
		sl_panelRight.putConstraint(WEST, panelRightTop, 0, WEST, panelRightContent);
		sl_panelRight.putConstraint(SOUTH, panelRightTop, 0, NORTH, panelRightContent);
		sl_panelRight.putConstraint(EAST, panelRightTop, 0, EAST, panelRightContent);
		panelRight.add(panelRightTop);
		
		lblCurrentPane = new JLabel("THIS_SHOULD_BE_AUTO_REPLACED");
		panelRightTop.add(lblCurrentPane);
		sl_panelNewTicket.putConstraint(NORTH, lblCurrentPane, PADDING, NORTH, panelNewTicket);
		sl_panelNewTicket.putConstraint(EAST, lblCurrentPane, -230, EAST, panelNewTicket);
		
		btnNewTiket = new JButton("new TIKET??");
		btnNewTiket.setName("btnNewTiket");
		sl_panelNewTicket.putConstraint(NORTH, btnNewTiket, 127, NORTH, panelNewTicket);
		sl_panelNewTicket.putConstraint(WEST, btnNewTiket, 93, WEST, panelNewTicket);
		panelNewTicket.add(btnNewTiket, btnNewTiket.getName());
		

		
		btnSurch = new JButton("surch???");
		sl_panelSearch.putConstraint(NORTH, btnSurch, 178, NORTH, panelSearch);
		sl_panelSearch.putConstraint(EAST, btnSurch, -177, EAST, panelSearch);
		panelSearch.add(btnSurch, "btnSurch");
		
		panelModifyTicket = new JPanel();
		panelModifyTicket.setName("panelModifyTicket");
		panelRightContent.add(panelModifyTicket, panelModifyTicket.getName());
		springLayout_1 = new SpringLayout();
		panelModifyTicket.setLayout(springLayout_1);
		
		btnModify = new JButton("modify???");
		springLayout_1.putConstraint(NORTH, btnModify, 184, NORTH, panelModifyTicket);
		springLayout_1.putConstraint(WEST, btnModify, 112, WEST, panelModifyTicket);
		panelModifyTicket.add(btnModify);
		splitPane.setDividerLocation(DEFAULT_DIVIDER_WIDTH);
		
		/***
		 * 'Login' button clicked.
		 */
		btnLogin.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{				
				modifyRightPane(panelLogin, e);
			}
		});
		
		/***
		 * 'Modify ticket' button clicked.
		 */
		btnModifyTicket.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				modifyRightPane(panelModifyTicket, e);
			}
		});
		
		/***
		 * 'Search' button clicked.
		 */
		btnSearch.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{				
				modifyRightPane(panelSearch, e);
			}
		});
		
		/***
		 * 'New ticket' button clicked.
		 */
		btnNewTicket.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				modifyRightPane(panelNewTicket, e);
			}
		});
		
		/***
		 * Someone presses 'ENTER' on our username field.
		 */
		textFieldUsername.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				textFieldPassword.requestFocus();
			}
		});
		
		/***
		 * Someone presses 'ENTER' on our password field.
		 */
		textFieldPassword.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				btnLoginSubmit.doClick();
			}

		});
		
		/***
		 * Someone clicks on our "submit" for password + username
		 */
		btnLoginSubmit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				String[] login = getLogin();
				doLogin(login[0],login[1]);
			}
		});
		
		
		formatButtons(panelMenu);
		
		
		
		
		
		btnLogin.doClick(); //to setup the top text
	}
}
