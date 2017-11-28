import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class TroubleTicketGUI
{
	public static int DEFAULT_LOGIN_MODE = 0; // Which entry should be used? If this is too large, the last one will be
												// used.
	public static final String LOGINS_FILE_PATH = "login.txt"; // Where are our logins and passwords stored? This is for
																// security and for not committing passwords to git.

	public static final int PADDING = 10;
	public static final int DEFAULT_DIVIDER_WIDTH = 150;

	public static final String NORTH = SpringLayout.NORTH;
	public static final String EAST = SpringLayout.EAST;
	public static final String SOUTH = SpringLayout.SOUTH;
	public static final String WEST = SpringLayout.WEST;

	public static final String NESW = "NESW";

	private static final String BUTTON_LOGIN_NAME = "btnLogin";
	private static final String BUTTON_NEW_TICKET_NAME = "btnNewTicket";
	private static final String BUTTON_MODIFY_TICKET_NAME = "btnModifyTicket";
	private static final String BUTTON_SEARCH_NAME = "btnSearch";

	private static final String PANEL_RIGHT_CONTENT_NAME = "panelRightContent";

	private static final String NOT_LOGGED_IN = "Not currently logged in.";

	public static final Border borderDefault = new JTextField().getBorder();
	public static final Border borderBad = BorderFactory.createLineBorder(Color.RED);

	public static Dao dao;

	public static String oppositeDirection(String direction)
	{
		switch (direction.toUpperCase().charAt(0))
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
		for (Component c : jp.getComponents())
		{
			if (c.isVisible())
			{
				return c;
			}
		}
		return null;
	}

	public static ArrayList<JButton> getButtonsFromJPanel(JPanel jp)
	{
		ArrayList<JButton> ret = new ArrayList<>();

		for (Component c : jp.getComponents())
		{
			if (c instanceof JButton)
			{
				ret.add((JButton) c);
			}
		}
		return ret;
	}

	/***
	 * This grabs the comparator ('>', '=', '!='), and the two operands (name, id,
	 * date), ("henry", 1, 11/27/2017) and puts it into a query. It then uses the
	 * results of that query to format the table.
	 * 
	 * @param s
	 */
	public static void setupResultsView(String s)
	{

	}

	/**
	 * @param buttonContainer
	 *            The container that has the buttons inside of it.
	 * 
	 *            With a bunch of buttons, they will be laid out using springLayout
	 *            offset by 10px from eachother.
	 */
	public void formatButtons(JPanel buttonContainer)
	{
		// insert formatting for all buttons.
		ArrayList<JButton> menuButtons = getButtonsFromJPanel(buttonContainer);
		SpringLayout tempSL = (SpringLayout) buttonContainer.getLayout();

		for (int i = 0; i < menuButtons.size(); i++)
		{
			JButton tempButton = menuButtons.get(i);
			printf("menuButtons[%d] = %s\n", i, tempButton);

			tempSL.putConstraint(WEST, tempButton, PADDING, WEST, buttonContainer);
			tempSL.putConstraint(EAST, tempButton, -PADDING, EAST, buttonContainer);

			if (i == 0) // first button aligns vertically w/ container
			{
				tempSL.putConstraint(NORTH, tempButton, PADDING, NORTH, buttonContainer);
			}
			else // all others align with south side of prev. button
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
		JButton button = (JButton) e.getSource();

		println("Someone clicked button '" + button.getName() + "' or '" + button.getText() + "'");
		printf("About to do cl.show('%s', '%s')\n", panelRightContent.getName(), jp.getName());

		cl.show(panelRightContent, jp.getName());
		label.setText(button.getText());
	}

	/***
	 * Gets the info from the two login info fields. This is its own method so that
	 * either pressing ENTER or clicking "Submit" will log you in.
	 */
	public String[] getLogin()
	{
		String[] ret = { "", "" };

		ret[0] = textFieldUsername.getText();
		ret[1] = textFieldPassword.getText();

		return ret;
	}

	private void doLogin(String username, String password)
	{
		lblLoginErrorMsg.setText("");
		textFieldUsername.setBorder(borderDefault);
		textFieldPassword.setBorder(borderDefault);

		int result = dao.login(username, password);

		if (result == Dao.NORMAL_USER || result == Dao.ADMINISTRATOR)
		{// log them in!
			if (result == Dao.NORMAL_USER)
			{
				lblWhosLoggedIn.setText(String.format(Dao.NORMAL_USER_S, username));
			}
			else if (result == Dao.ADMINISTRATOR)
			{
				lblWhosLoggedIn.setText(String.format(Dao.ADMINISTRATOR_S, username));
			}
			printf("Welcome, '%s'!", username);
			lblWhosLoggedIn.setText(username);
		}
		if (result == Dao.PASSWORD_INCORRECT) // wrong pass
		{
			textFieldPassword.setBorder(borderBad);
			textFieldPassword.requestFocus();
			lblLoginErrorMsg.setText(String.format(Dao.PASSWORD_INCORRECT_S, username));
		}
		if (result == Dao.USERNAME_NOT_FOUND) // username not found
		{
			textFieldUsername.setBorder(borderBad);
			textFieldUsername.requestFocus();
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

	private JPanel panelRightContent;

	private JPanel panelLogin;
	private JPanel panelNewTicket;
	private JPanel panelSearch;
	private JPanel panelModifyTicket;
	private JButton btnModify;
	private JPanel panelRight;
	private SpringLayout sl_panelRight;
	private SpringLayout sl_panelLogin;
	private JPanel panelUsername;
	private SpringLayout sl_panelNewTicket;
	private JLabel lblUsername;
	private JPanel panelRightTop;
	private JPanel panelPassword;
	private JLabel lblPassword;
	private JButton btnSubmitNewTicket;
	private JButton btnSearchThroughRecords;
	private SpringLayout springLayout_1;
	private JPanel panelStatus;
	private JLabel lblCurrentlyLoggedIn;
	private JSplitPane splitPane;
	private JPanel panelLeft;
	private JSplitPane splitPaneLeft;
	private JPanel panelMenu;
	private JLabel lblLoginErrorMsg;
	private JPanel panelLoginErrorMsg;
	private SpringLayout springLayout;
	private SpringLayout sl_panelMenu;
	private JPanel panelShortDesc;
	private JTextArea textAreaShortDesc;
	private JPanel panelTextAreaShortDesc;
	private SpringLayout sl_panelShortDesc;
	private JLabel lblShortDesc;
	private JPanel panelCategory;
	private JLabel labelCategory;
	private JPanel panelButtonSubmitNewTicket;
	private JPanel panelTextAreaLongDesc;
	private JTextArea textAreaLongDesc;
	private JPanel panelAdminOptionsNewTicket;
	private JPanel panelUsernameMiddle;
	private JTextField textFieldUsername;
	private JPanel panelPasswordMiddle;
	private JScrollPane scrollPaneLongDesc;
	private SpringLayout sl_panelLongDesc;
	private JPanel panelLongDesc;
	private SpringLayout sl_panelCategory;
	private JComboBox spinnerCategory;
	private SpringLayout sl_panelUsername;
	private JLabel lblLongDescription;
	private JPanel panelBtnLoginSubmitMiddle;
	private JButton btnLoginSubmit;
	private SpringLayout sl_panelPassword;
	private JTable tableResults;
	private JLabel lblRecordsThatHave;
	private JComboBox<String> comboBoxField;
	private JComboBox<String> comboBoxOperator;
	private JTextField textField;
	private JSplitPane splitPaneSearch;
	private JPanel panelSearchTop;
	private JPanel panelSearchResultsTop;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{

		ArrayList<ArrayList<String>> logins = Dao.getLoginsFromFile(LOGINS_FILE_PATH);

		int i = 0;
		for (ArrayList<String> login : logins)
		{
			// printf("Login is %d long and is %s",login.size(),login);
			// printf("Login %2d: '%s','%s','%s'",i,login.get(0),login.get(1),login.get(2));
			i++;
		}

		if (DEFAULT_LOGIN_MODE >= logins.size())
		{
			DEFAULT_LOGIN_MODE = logins.size() - 1;
		}

		printf("\n\nWe're going to use the %dth entry in '%s', aka '%s'.\n\n", DEFAULT_LOGIN_MODE, LOGINS_FILE_PATH,
				logins.get(DEFAULT_LOGIN_MODE));

		ArrayList<String> login = logins.get(DEFAULT_LOGIN_MODE);

		dao = new Dao(login.get(0), login.get(1), login.get(2));

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
		springLayout = new SpringLayout();
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
		sl_panelMenu = new SpringLayout();
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

		panelLoginErrorMsg = new JPanel();
		sl_panelLogin.putConstraint(SpringLayout.NORTH, panelLoginErrorMsg, 10, SpringLayout.NORTH, panelLogin);
		sl_panelLogin.putConstraint(SpringLayout.WEST, panelLoginErrorMsg, 10, SpringLayout.WEST, panelLogin);
		sl_panelLogin.putConstraint(SpringLayout.EAST, panelLoginErrorMsg, -10, SpringLayout.EAST, panelLogin);
		panelLogin.add(panelLoginErrorMsg);
		panelLoginErrorMsg.setLayout(new BorderLayout(0, 0));

		lblLoginErrorMsg = new JLabel("THIS_IS_A_DEFAULT_ERROR_MESSAGE");
		lblLoginErrorMsg.setHorizontalAlignment(SwingConstants.CENTER);
		panelLoginErrorMsg.add(lblLoginErrorMsg, BorderLayout.CENTER);

		panelUsername = new JPanel();
		sl_panelLogin.putConstraint(SpringLayout.NORTH, panelUsername, 60, SpringLayout.NORTH, panelLogin);
		sl_panelLogin.putConstraint(SpringLayout.SOUTH, panelLoginErrorMsg, -6, SpringLayout.NORTH, panelUsername);
		sl_panelLogin.putConstraint(SpringLayout.WEST, panelUsername, 10, SpringLayout.WEST, panelLogin);
		sl_panelLogin.putConstraint(SpringLayout.EAST, panelUsername, -10, SpringLayout.EAST, panelLogin);
		panelLogin.add(panelUsername);
		sl_panelUsername = new SpringLayout();
		panelUsername.setLayout(sl_panelUsername);

		lblUsername = new JLabel("Username");
		sl_panelUsername.putConstraint(SpringLayout.WEST, lblUsername, 10, SpringLayout.WEST, panelUsername);
		sl_panelUsername.putConstraint(SpringLayout.EAST, lblUsername, -10, SpringLayout.EAST, panelUsername);
		lblUsername.setHorizontalAlignment(SwingConstants.CENTER);
		sl_panelUsername.putConstraint(SpringLayout.NORTH, lblUsername, 5, SpringLayout.NORTH, panelUsername);
		panelUsername.add(lblUsername);
		sl_panelLogin.putConstraint(WEST, lblUsername, 41, WEST, panelLogin);
		sl_panelLogin.putConstraint(SpringLayout.SOUTH, lblUsername, -397, SpringLayout.SOUTH, panelLogin);

		panelPassword = new JPanel();
		sl_panelLogin.putConstraint(SpringLayout.NORTH, panelPassword, 129, SpringLayout.NORTH, panelLogin);
		sl_panelLogin.putConstraint(SpringLayout.SOUTH, panelPassword, -36, SpringLayout.SOUTH, panelLogin);
		sl_panelLogin.putConstraint(SpringLayout.SOUTH, panelUsername, 0, SpringLayout.NORTH, panelPassword);
		sl_panelLogin.putConstraint(SpringLayout.WEST, panelPassword, 10, SpringLayout.WEST, panelLogin);
		sl_panelLogin.putConstraint(SpringLayout.EAST, panelPassword, -10, SpringLayout.EAST, panelLogin);

		panelUsernameMiddle = new JPanel();
		sl_panelUsername.putConstraint(SpringLayout.NORTH, panelUsernameMiddle, 0, SpringLayout.SOUTH, lblUsername);
		sl_panelUsername.putConstraint(SpringLayout.WEST, panelUsernameMiddle, 10, SpringLayout.WEST, panelUsername);
		sl_panelUsername.putConstraint(SpringLayout.SOUTH, panelUsernameMiddle, -10, SpringLayout.SOUTH, panelUsername);
		sl_panelUsername.putConstraint(SpringLayout.EAST, panelUsernameMiddle, -10, SpringLayout.EAST, panelUsername);
		panelUsername.add(panelUsernameMiddle);

		textFieldUsername = new JTextField();
		textFieldUsername.setToolTipText("username");
		textFieldUsername.setColumns(10);
		panelUsernameMiddle.add(textFieldUsername);
		panelLogin.add(panelPassword);
		sl_panelPassword = new SpringLayout();
		panelPassword.setLayout(sl_panelPassword);

		lblPassword = new JLabel("Password");
		sl_panelPassword.putConstraint(SpringLayout.WEST, lblPassword, 0, SpringLayout.WEST, panelPassword);
		sl_panelPassword.putConstraint(SpringLayout.EAST, lblPassword, 0, SpringLayout.EAST, panelPassword);
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		sl_panelPassword.putConstraint(SpringLayout.NORTH, lblPassword, 5, SpringLayout.NORTH, panelPassword);
		panelPassword.add(lblPassword);

		panelPasswordMiddle = new JPanel();
		sl_panelPassword.putConstraint(SpringLayout.NORTH, panelPasswordMiddle, 6, SpringLayout.SOUTH, lblPassword);
		sl_panelPassword.putConstraint(SpringLayout.WEST, panelPasswordMiddle, 10, SpringLayout.WEST, lblPassword);
		sl_panelPassword.putConstraint(SpringLayout.EAST, panelPasswordMiddle, -10, SpringLayout.EAST, lblPassword);
		panelPassword.add(panelPasswordMiddle);

		textFieldPassword = new JPasswordField();
		panelPasswordMiddle.add(textFieldPassword);
		sl_panelPassword.putConstraint(SpringLayout.NORTH, textFieldPassword, 26, SpringLayout.NORTH, panelPassword);
		sl_panelPassword.putConstraint(SpringLayout.WEST, textFieldPassword, 12, SpringLayout.WEST, panelPassword);
		textFieldPassword.setToolTipText("password");
		textFieldPassword.setColumns(10);

		panelBtnLoginSubmitMiddle = new JPanel();
		sl_panelPassword.putConstraint(SpringLayout.NORTH, panelBtnLoginSubmitMiddle, 6, SpringLayout.SOUTH,
				panelPasswordMiddle);
		sl_panelPassword.putConstraint(SpringLayout.WEST, panelBtnLoginSubmitMiddle, 10, SpringLayout.WEST,
				panelPassword);
		sl_panelPassword.putConstraint(SpringLayout.SOUTH, panelBtnLoginSubmitMiddle, 42, SpringLayout.SOUTH,
				panelPasswordMiddle);
		sl_panelPassword.putConstraint(SpringLayout.EAST, panelBtnLoginSubmitMiddle, -10, SpringLayout.EAST,
				panelPassword);
		panelPassword.add(panelBtnLoginSubmitMiddle);

		btnLoginSubmit = new JButton("Submit");
		panelBtnLoginSubmitMiddle.add(btnLoginSubmit);

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

		panelShortDesc = new JPanel();
		sl_panelNewTicket.putConstraint(SpringLayout.NORTH, panelShortDesc, 10, SpringLayout.NORTH, panelNewTicket);
		sl_panelNewTicket.putConstraint(SpringLayout.WEST, panelShortDesc, 10, SpringLayout.WEST, panelNewTicket);
		sl_panelNewTicket.putConstraint(SpringLayout.EAST, panelShortDesc, 299, SpringLayout.WEST, panelNewTicket);
		panelNewTicket.add(panelShortDesc);
		sl_panelShortDesc = new SpringLayout();
		panelShortDesc.setLayout(sl_panelShortDesc);

		lblShortDesc = new JLabel("Short description / name");
		sl_panelShortDesc.putConstraint(SpringLayout.NORTH, lblShortDesc, 10, SpringLayout.NORTH, panelShortDesc);
		lblShortDesc.setHorizontalAlignment(SwingConstants.CENTER);
		sl_panelShortDesc.putConstraint(SpringLayout.WEST, lblShortDesc, 10, SpringLayout.WEST, panelShortDesc);
		sl_panelShortDesc.putConstraint(SpringLayout.EAST, lblShortDesc, -10, SpringLayout.EAST, panelShortDesc);
		panelShortDesc.add(lblShortDesc);

		panelTextAreaShortDesc = new JPanel();
		sl_panelShortDesc.putConstraint(SpringLayout.NORTH, panelTextAreaShortDesc, 10, SpringLayout.SOUTH,
				lblShortDesc);
		sl_panelShortDesc.putConstraint(SpringLayout.WEST, panelTextAreaShortDesc, 10, SpringLayout.WEST,
				panelShortDesc);
		sl_panelShortDesc.putConstraint(SpringLayout.EAST, panelTextAreaShortDesc, -10, SpringLayout.EAST,
				panelShortDesc);
		panelTextAreaShortDesc.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		sl_panelShortDesc.putConstraint(SpringLayout.SOUTH, panelTextAreaShortDesc, -10, SpringLayout.SOUTH,
				panelShortDesc);
		panelShortDesc.add(panelTextAreaShortDesc);
		panelTextAreaShortDesc.setLayout(new BorderLayout(0, 0));

		textAreaShortDesc = new JTextArea();
		panelTextAreaShortDesc.add(textAreaShortDesc);
		sl_panelShortDesc.putConstraint(SpringLayout.NORTH, textAreaShortDesc, 6, SpringLayout.SOUTH, lblShortDesc);
		sl_panelShortDesc.putConstraint(SpringLayout.WEST, textAreaShortDesc, 10, SpringLayout.WEST, panelShortDesc);
		sl_panelShortDesc.putConstraint(SpringLayout.SOUTH, textAreaShortDesc, -10, SpringLayout.SOUTH, panelShortDesc);
		sl_panelShortDesc.putConstraint(SpringLayout.EAST, textAreaShortDesc, -10, SpringLayout.EAST, panelShortDesc);
		textAreaShortDesc.setColumns(10);

		panelCategory = new JPanel();
		sl_panelNewTicket.putConstraint(SpringLayout.NORTH, panelCategory, 10, SpringLayout.NORTH, panelNewTicket);
		sl_panelNewTicket.putConstraint(SpringLayout.WEST, panelCategory, 10, SpringLayout.EAST, panelShortDesc);
		sl_panelNewTicket.putConstraint(SpringLayout.SOUTH, panelCategory, 0, SpringLayout.SOUTH, panelShortDesc);
		sl_panelNewTicket.putConstraint(SpringLayout.EAST, panelCategory, -10, SpringLayout.EAST, panelNewTicket);
		panelNewTicket.add(panelCategory);
		sl_panelCategory = new SpringLayout();
		panelCategory.setLayout(sl_panelCategory);

		labelCategory = new JLabel("Category");
		sl_panelCategory.putConstraint(SpringLayout.NORTH, labelCategory, 6, SpringLayout.NORTH, panelCategory);
		sl_panelCategory.putConstraint(SpringLayout.WEST, labelCategory, 10, SpringLayout.WEST, panelCategory);
		sl_panelCategory.putConstraint(SpringLayout.EAST, labelCategory, -10, SpringLayout.EAST, panelCategory);
		labelCategory.setHorizontalAlignment(SwingConstants.CENTER);
		panelCategory.add(labelCategory);

		spinnerCategory = new JComboBox<Object>();
		sl_panelCategory.putConstraint(SpringLayout.SOUTH, spinnerCategory, -29, SpringLayout.SOUTH, panelCategory);
		spinnerCategory.setFont(new Font("Dialog", Font.BOLD, 15));
		spinnerCategory.setModel(new DefaultComboBoxModel(
				new String[] { "THESE", "SHOULD", "BE", "AUTO", "REPLACED", "FROM", "THE", "CATEGORIES", "TABLE" }));
		sl_panelCategory.putConstraint(SpringLayout.NORTH, spinnerCategory, 10, SpringLayout.SOUTH, labelCategory);
		sl_panelCategory.putConstraint(SpringLayout.WEST, spinnerCategory, 0, SpringLayout.WEST, labelCategory);
		sl_panelCategory.putConstraint(SpringLayout.EAST, spinnerCategory, -10, SpringLayout.EAST, panelCategory);
		panelCategory.add(spinnerCategory);

		panelButtonSubmitNewTicket = new JPanel();
		sl_panelNewTicket.putConstraint(SpringLayout.WEST, panelButtonSubmitNewTicket, 0, SpringLayout.WEST,
				panelNewTicket);
		sl_panelNewTicket.putConstraint(SpringLayout.SOUTH, panelButtonSubmitNewTicket, 0, SpringLayout.SOUTH,
				panelNewTicket);
		sl_panelNewTicket.putConstraint(SpringLayout.EAST, panelButtonSubmitNewTicket, 0, SpringLayout.EAST,
				panelNewTicket);
		panelNewTicket.add(panelButtonSubmitNewTicket);

		btnSubmitNewTicket = new JButton("Submit new ticket");

		panelButtonSubmitNewTicket.add(btnSubmitNewTicket);
		sl_panelNewTicket.putConstraint(SpringLayout.WEST, btnSubmitNewTicket, 231, SpringLayout.WEST, panelNewTicket);
		sl_panelNewTicket.putConstraint(SpringLayout.SOUTH, btnSubmitNewTicket, -10, SpringLayout.SOUTH,
				panelNewTicket);
		btnSubmitNewTicket.setName("btnNewTiket");

		panelLongDesc = new JPanel();
		sl_panelNewTicket.putConstraint(SpringLayout.SOUTH, panelShortDesc, -10, SpringLayout.NORTH, panelLongDesc);
		sl_panelNewTicket.putConstraint(SpringLayout.NORTH, panelLongDesc, 110, SpringLayout.NORTH, panelNewTicket);
		sl_panelNewTicket.putConstraint(SpringLayout.SOUTH, panelLongDesc, -223, SpringLayout.NORTH,
				panelButtonSubmitNewTicket);
		sl_panelNewTicket.putConstraint(SpringLayout.WEST, panelLongDesc, 10, SpringLayout.WEST, panelNewTicket);
		sl_panelNewTicket.putConstraint(SpringLayout.EAST, panelLongDesc, -10, SpringLayout.EAST, panelNewTicket);
		panelNewTicket.add(panelLongDesc);
		sl_panelLongDesc = new SpringLayout();
		panelLongDesc.setLayout(sl_panelLongDesc);

		lblLongDescription = new JLabel("Long description");
		sl_panelLongDesc.putConstraint(SpringLayout.NORTH, lblLongDescription, 0, SpringLayout.NORTH, panelLongDesc);
		sl_panelLongDesc.putConstraint(SpringLayout.WEST, lblLongDescription, 10, SpringLayout.WEST, panelLongDesc);
		sl_panelLongDesc.putConstraint(SpringLayout.EAST, lblLongDescription, -10, SpringLayout.EAST, panelLongDesc);
		lblLongDescription.setHorizontalAlignment(SwingConstants.CENTER);
		panelLongDesc.add(lblLongDescription);

		panelTextAreaLongDesc = new JPanel();
		panelTextAreaLongDesc.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		sl_panelLongDesc.putConstraint(SpringLayout.NORTH, panelTextAreaLongDesc, 10, SpringLayout.SOUTH,
				lblLongDescription);
		sl_panelLongDesc.putConstraint(SpringLayout.SOUTH, panelTextAreaLongDesc, -10, SpringLayout.SOUTH,
				panelLongDesc);
		sl_panelLongDesc.putConstraint(SpringLayout.WEST, panelTextAreaLongDesc, 10, SpringLayout.WEST, panelLongDesc);
		sl_panelLongDesc.putConstraint(SpringLayout.EAST, panelTextAreaLongDesc, -10, SpringLayout.EAST, panelLongDesc);
		panelLongDesc.add(panelTextAreaLongDesc);
		panelTextAreaLongDesc.setLayout(new BorderLayout(0, 0));

		scrollPaneLongDesc = new JScrollPane();
		panelTextAreaLongDesc.add(scrollPaneLongDesc, BorderLayout.CENTER);

		textAreaLongDesc = new JTextArea();
		scrollPaneLongDesc.setViewportView(textAreaLongDesc);
		textAreaLongDesc.setColumns(10);

		panelAdminOptionsNewTicket = new JPanel();
		sl_panelNewTicket.putConstraint(SpringLayout.NORTH, panelAdminOptionsNewTicket, 96, SpringLayout.SOUTH,
				panelLongDesc);
		sl_panelNewTicket.putConstraint(SpringLayout.WEST, panelAdminOptionsNewTicket, 10, SpringLayout.WEST,
				panelNewTicket);
		sl_panelNewTicket.putConstraint(SpringLayout.SOUTH, panelAdminOptionsNewTicket, -6, SpringLayout.NORTH,
				panelButtonSubmitNewTicket);
		sl_panelNewTicket.putConstraint(SpringLayout.EAST, panelAdminOptionsNewTicket, 0, SpringLayout.EAST,
				panelCategory);
		panelNewTicket.add(panelAdminOptionsNewTicket);
		panelSearch.setLayout(new BorderLayout(0, 0));

		splitPaneSearch = new JSplitPane();
		splitPaneSearch.setOrientation(JSplitPane.VERTICAL_SPLIT);
		panelSearch.add(splitPaneSearch);

		panelSearchTop = new JPanel();
		splitPaneSearch.setLeftComponent(panelSearchTop);

		lblRecordsThatHave = new JLabel("Records that have");
		panelSearchTop.add(lblRecordsThatHave);

		comboBoxField = new JComboBox<String>();
		comboBoxField.setModel(new DefaultComboBoxModel<>(new String[] { "THESE", "SHOULD", "BE", "REPLACED", "BY",
				"VALID", "FIELDS", "THAT", "ARE", "PART", "OF", "THE", "TICKETS", "OR", "USERS", "TABLE" }));
		panelSearchTop.add(comboBoxField);

		comboBoxOperator = new JComboBox<String>();
		comboBoxOperator.setModel(new DefaultComboBoxModel<>(new String[] { "THESE", "SHOULD", "BE", "NUMBERICAL", "OR",
				"LOGICAL", "OPERATORS", "VALID", "FOR", "THEIR", "TWO", "OPERANDS", "", "MAYBE", "MAKE", "A", "HELPER",
				"FUNCTION", "OR", "AN", "ENUM", "OR", "SOMETHING" }));
		panelSearchTop.add(comboBoxOperator);

		textField = new JTextField();
		panelSearchTop.add(textField);
		textField.setColumns(10);

		btnSearchThroughRecords = new JButton("Go");

		panelSearchTop.add(btnSearchThroughRecords);

		JPanel panelSearchBottom = new JPanel();
		splitPaneSearch.setRightComponent(panelSearchBottom);
		panelSearchBottom.setLayout(new BorderLayout(0, 0));

		panelSearchResultsTop = new JPanel();
		panelSearchBottom.add(panelSearchResultsTop, BorderLayout.NORTH);

		JScrollPane scrollPaneResults = new JScrollPane();
		panelSearchBottom.add(scrollPaneResults, BorderLayout.CENTER);

		tableResults = new JTable();
		tableResults.setModel(new DefaultTableModel(new Object[][] {

		}, new String[] { "hi", "please", "replace", "me" }));
		scrollPaneResults.setViewportView(tableResults);

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

		btnLogin.addActionListener(new ActionListener()
		{
			/***
			 * 'Login' button clicked.
			 */
			public void actionPerformed(ActionEvent e)
			{
				modifyRightPane(panelLogin, e);
			}
		});

		btnModifyTicket.addActionListener(new ActionListener()
		{
			/***
			 * 'Modify ticket' button clicked.
			 */
			public void actionPerformed(ActionEvent e)
			{
				modifyRightPane(panelModifyTicket, e);
			}
		});

		btnSearch.addActionListener(new ActionListener()
		{
			/***
			 * 'Search' button clicked.
			 */
			public void actionPerformed(ActionEvent e)
			{
				modifyRightPane(panelSearch, e);

				comboBoxField.removeAllItems();
				comboBoxOperator.removeAllItems();

				for (int i = 0; i < Dao.OPERATOR_TYPES_S.length; i++)
				{
					// add all operator types
					comboBoxOperator.addItem(Dao.OPERATOR_TYPES_S[i]);
				}

				for (int i = 0; i < Dao.VALID_SEARCH_FIELDS_USERS_S.length; i++)
				{
					// add valid fields that belong to users table
					comboBoxField.addItem(Dao.VALID_SEARCH_FIELDS_USERS_S[i]);
				}

				for (int i = 0; i < Dao.VALID_SEARCH_FIELDS_TICKETS_S.length; i++)
				{
					// add valid fields that belong to tickets table
					comboBoxField.addItem(Dao.VALID_SEARCH_FIELDS_TICKETS_S[i]);
				}

				// set up the table to have a default view of all notes...

			}

		});

		btnSearchThroughRecords.addActionListener(new ActionListener()
		{
			/***
			 * Someone wants to search for records!
			 */
			public void actionPerformed(ActionEvent arg0)
			{

			}
		});

		btnNewTicket.addActionListener(new ActionListener()
		{
			/***
			 * 'New ticket' button clicked.
			 */
			public void actionPerformed(ActionEvent e)
			{
				modifyRightPane(panelNewTicket, e);
			}
		});

		btnSubmitNewTicket.addActionListener(new ActionListener()
		{
			/***
			 * Someone clicks "submit new ticket"
			 */
			public void actionPerformed(ActionEvent arg0)
			{

				dao.submitTicket(null, null, null, null, null, null, null);
			}
		});

		textFieldUsername.addActionListener(new ActionListener()
		{
			/***
			 * Someone presses ENTER on our username field, goto password field
			 */
			public void actionPerformed(ActionEvent e)
			{
				textFieldPassword.grabFocus();
			}
		});

		textFieldPassword.addActionListener(new ActionListener()
		{
			/***
			 * Someone presses ENTER on our password field
			 */
			public void actionPerformed(ActionEvent arg0)
			{
				btnLoginSubmit.doClick();
			}

		});

		btnLoginSubmit.addActionListener(new ActionListener()
		{
			/***
			 * Someone clicks on our "submit" for password + username
			 */
			public void actionPerformed(ActionEvent arg0)
			{
				String[] login = getLogin();
				doLogin(login[0], login[1]);
			}
		});

		formatButtons(panelMenu);

		btnLogin.doClick(); // to setup the top text
		lblLoginErrorMsg.setText(""); // empty login error message
	}
}
