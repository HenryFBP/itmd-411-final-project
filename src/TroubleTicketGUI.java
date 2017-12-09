import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

public class TroubleTicketGUI extends JFrame
{

	private static final long serialVersionUID = -5012247422436531433L;
	public static int DEFAULT_LOGIN_MODE = 1; // Which entry should be used? If this is too large, the last one will be
												// used.
	public static final String LOGINS_FILE_PATH = "login.txt"; // Where are our logins and passwords stored? This is for
																// security and for not committing passwords to git.
	
	public static final int PADDING = 10;
	public static final int DEFAULT_DIVIDER_WIDTH = 150;
	
	private Boolean _ARE_MODIFYING_TICKET = false;
	private int _PREV_TICKET_ID	= -1;



	public static final String NESW = "NESW";
	public static final String PREFERRED_TIME_FORMAT = "MMM d, yyyy, hh:mm:ss aa";

	private static final String BUTTON_LOGOUT_NAME = "btnLogout";
	private static final String BUTTON_LOGIN_NAME = "btnLogin";
	private static final String BUTTON_NEW_TICKET_NAME = "btnNewTicket";
	private static final String BUTTON_NEW_TICKET_S = "New Ticket";
	private static final String BUTTON_MODIFY_TICKET_NAME = "btnModifyTicket";
	private static final String BUTTON_SEARCH_NAME = "btnSearch";
	private static final String BUTTON_S_SUBMIT_NEW_TICKET_S = "Submit new ticket";

	
	private static final String PANEL_RIGHT_CONTENT_NAME = "panelRightContent";

	public static final Border borderDefault = new JTextField().getBorder();
	public static final Border borderBad = BorderFactory.createLineBorder(Color.RED);

	public static Dao dao;



	/***
	 * Changes the right panel to show ONLY JPanel 'jp'.
	 * @param jp  The JPanel to be shown
	 * @param e  The actionEvent that tells us what button was clicked.
	 */
	public void modifyRightPane(JPanel jp, ActionEvent e, String panelName)
	{
		CardLayout cl = (CardLayout) panelRightContent.getLayout();
		JLabel label = lblCurrentPane;
		JButton button = (JButton) e.getSource();

		Util.println("Someone clicked button '" + button.getName() + "' or '" + button.getText() + "'");
		Util.printf("About to do cl.show('%s', '%s')\n", panelRightContent.getName(), jp.getName());

		cl.show(panelRightContent, jp.getName());
		label.setText(panelName);
	}

	public void modifyRightPane(JPanel jp, String panelName)
	{
		CardLayout cl = (CardLayout) panelRightContent.getLayout();
		JLabel label = lblCurrentPane;

		Util.println("Don't know what button someone clicked. Just changing right panel.");
		
		cl.show(panelRightContent, jp.getName());
		label.setText(panelName);		
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
			
			Util.setTooltips(panelMenu,new String[] {}, ""); //reset tooltips
			Util.enableButtons(panelMenu);					 //enable all buttons
			
			
			if (result == Dao.NORMAL_USER)
			{
				lblWhosLoggedIn.setText(String.format(Dao.NORMAL_USER_S, username));
			}
			else if (result == Dao.ADMINISTRATOR)
			{
				lblWhosLoggedIn.setText(String.format(Dao.ADMINISTRATOR_S, username));
			}
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

	private JButton btnLogout;
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
	private JButton btnSubmitChanges;
	private JPanel panelRight;
	private SpringLayout sl_panelRight;
	private SpringLayout sl_panelLogin;
	private JPanel panelUsername;
	private JLabel lblUsername;
	private JPanel panelRightTop;
	private JPanel panelPassword;
	private JLabel lblPassword;
	private JButton btnNTSubmitNewTicket;
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
	private JPanel panelNTShortDesc;
	private JTextArea textAreaShortDesc;
	private JPanel panelTextAreaShortDesc;
	private SpringLayout sl_panelNTShortDesc;
	private JLabel lblShortDesc;
	private JPanel panelNTCategory;
	private JPanel panelNTButtonSubmitNewTicket;
	private JPanel panelTextAreaLongDesc;
	private JTextArea textAreaLongDesc;
	private JPanel panelUsernameMiddle;
	private JTextField textFieldUsername;
	private JPanel panelPasswordMiddle;
	private JScrollPane scrollPaneLongDesc;
	private SpringLayout sl_panelNTLongDesc;
	private JPanel panelNTLongDesc;
	private JComboBox comboBoxCategory;
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
	private JLabel labelCategory;
	private JLabel labelSeverity;
	private JComboBox<String> comboBoxSeverity;
	private JPanel panelSearchBottom;
	private JPanel panelNTSeverity;
	private JPanel panelNTDateStarted;
	private JLabel labelDateStarted;
	private JDateChooser dateStartedChooser;
	private JPanel panelNTDateEnded;
	private JLabel labelDateEnded;
	private JDateChooser dateEndedChooser;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		ArrayList<ArrayList<String>> logins = Util.getLoginsFromFile(LOGINS_FILE_PATH);
		
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

		Util.printf("\n\nWe're going to use the %dth entry in '%s', aka '%s'.\n\n", DEFAULT_LOGIN_MODE, LOGINS_FILE_PATH,
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
		frame = new JFrame();
		frame.setBounds(100, 100, 926, 682);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);

		splitPane = new JSplitPane();
		splitPane.setName("splitPane");
		springLayout.putConstraint(Util.SOUTH, splitPane, 0, Util.SOUTH, frame.getContentPane());
		springLayout.putConstraint(Util.EAST, splitPane, 0, Util.EAST, frame.getContentPane());
		springLayout.putConstraint(Util.NORTH, splitPane, 0, Util.NORTH, frame.getContentPane());
		springLayout.putConstraint(Util.WEST, splitPane, 0, Util.WEST, frame.getContentPane());
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
		
		btnLogout = new JButton("Logout");
		btnLogout.setName(BUTTON_LOGOUT_NAME);
		panelMenu.add(btnLogout);
		
		btnLogin = new JButton("Login");
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

		lblCurrentlyLoggedIn = new JLabel(GUIC.CURRENTLY_LOGGED_IN_TEXT.s());
		panelStatus.add(lblCurrentlyLoggedIn);

		lblWhosLoggedIn = new JLabel(GUIC.NOT_LOGGED_IN+"");
		lblWhosLoggedIn.setFont(new Font("Dialog", Font.PLAIN, 12));
		panelStatus.add(lblWhosLoggedIn);
		splitPaneLeft.setDividerLocation(100);

		panelRight = new JPanel();
		splitPane.setRightComponent(panelRight);
		sl_panelRight = new SpringLayout();
		panelRight.setLayout(sl_panelRight);

		panelRightContent = new JPanel();
		sl_panelRight.putConstraint(Util.SOUTH, panelRightContent, 0, Util.SOUTH, panelRight);
		sl_panelRight.putConstraint(Util.EAST, panelRightContent, 0, Util.EAST, panelRight);
		panelRightContent.setName(PANEL_RIGHT_CONTENT_NAME);
		sl_panelRight.putConstraint(Util.WEST, panelRightContent, 0, Util.WEST, panelRight);
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

		panelSearch = new JPanel();
		panelSearch.setName("panelSearch");
		panelRightContent.add(panelSearch, panelSearch.getName());

		panelLoginErrorMsg = new JPanel();
		sl_panelLogin.putConstraint(Util.NORTH, panelLoginErrorMsg, PADDING, Util.NORTH, panelLogin);
		sl_panelLogin.putConstraint(Util.WEST, panelLoginErrorMsg, PADDING, Util.WEST, panelLogin);
		sl_panelLogin.putConstraint(Util.EAST, panelLoginErrorMsg, -PADDING, Util.EAST, panelLogin);
		panelLogin.add(panelLoginErrorMsg);
		panelLoginErrorMsg.setLayout(new BorderLayout(0, 0));

		lblLoginErrorMsg = new JLabel("THIS_IS_A_DEFAULT_ERROR_MESSAGE");
		lblLoginErrorMsg.setHorizontalAlignment(SwingConstants.CENTER);
		panelLoginErrorMsg.add(lblLoginErrorMsg, BorderLayout.CENTER);

		panelUsername = new JPanel();
		sl_panelLogin.putConstraint(Util.NORTH, panelUsername, 60, Util.NORTH, panelLogin);
		sl_panelLogin.putConstraint(Util.SOUTH, panelLoginErrorMsg, -6, Util.NORTH, panelUsername);
		sl_panelLogin.putConstraint(Util.WEST, panelUsername, PADDING, Util.WEST, panelLogin);
		sl_panelLogin.putConstraint(Util.EAST, panelUsername, -PADDING, Util.EAST, panelLogin);
		panelLogin.add(panelUsername);
		sl_panelUsername = new SpringLayout();
		panelUsername.setLayout(sl_panelUsername);

		lblUsername = new JLabel("Username");
		sl_panelUsername.putConstraint(Util.WEST, lblUsername, PADDING, Util.WEST, panelUsername);
		sl_panelUsername.putConstraint(Util.EAST, lblUsername, -PADDING, Util.EAST, panelUsername);
		lblUsername.setHorizontalAlignment(SwingConstants.CENTER);
		sl_panelUsername.putConstraint(Util.NORTH, lblUsername, 5, Util.NORTH, panelUsername);
		panelUsername.add(lblUsername);
		sl_panelLogin.putConstraint(Util.WEST, lblUsername, 41, Util.WEST, panelLogin);
		sl_panelLogin.putConstraint(Util.SOUTH, lblUsername, -397, Util.SOUTH, panelLogin);

		panelPassword = new JPanel();
		sl_panelLogin.putConstraint(Util.NORTH, panelPassword, 129, Util.NORTH, panelLogin);
		sl_panelLogin.putConstraint(Util.SOUTH, panelPassword, -36, Util.SOUTH, panelLogin);
		sl_panelLogin.putConstraint(Util.SOUTH, panelUsername, 0, Util.NORTH, panelPassword);
		sl_panelLogin.putConstraint(Util.WEST, panelPassword, PADDING, Util.WEST, panelLogin);
		sl_panelLogin.putConstraint(Util.EAST, panelPassword, -PADDING, Util.EAST, panelLogin);

		panelUsernameMiddle = new JPanel();
		sl_panelUsername.putConstraint(Util.NORTH, panelUsernameMiddle, 0, Util.SOUTH, lblUsername);
		sl_panelUsername.putConstraint(Util.WEST, panelUsernameMiddle, PADDING, Util.WEST, panelUsername);
		sl_panelUsername.putConstraint(Util.SOUTH, panelUsernameMiddle, -PADDING, Util.SOUTH, panelUsername);
		sl_panelUsername.putConstraint(Util.EAST, panelUsernameMiddle, -PADDING, Util.EAST, panelUsername);
		panelUsername.add(panelUsernameMiddle);

		textFieldUsername = new JTextField();
		textFieldUsername.setToolTipText("username");
		textFieldUsername.setColumns(10);
		panelUsernameMiddle.add(textFieldUsername);
		panelLogin.add(panelPassword);
		sl_panelPassword = new SpringLayout();
		panelPassword.setLayout(sl_panelPassword);

		lblPassword = new JLabel("Password");
		sl_panelPassword.putConstraint(Util.WEST, lblPassword, 0, Util.WEST, panelPassword);
		sl_panelPassword.putConstraint(Util.EAST, lblPassword, 0, Util.EAST, panelPassword);
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		sl_panelPassword.putConstraint(Util.NORTH, lblPassword, 5, Util.NORTH, panelPassword);
		panelPassword.add(lblPassword);

		panelPasswordMiddle = new JPanel();
		sl_panelPassword.putConstraint(Util.NORTH, panelPasswordMiddle, 6, Util.SOUTH, lblPassword);
		sl_panelPassword.putConstraint(Util.WEST, panelPasswordMiddle, PADDING, Util.WEST, lblPassword);
		sl_panelPassword.putConstraint(Util.EAST, panelPasswordMiddle, -PADDING, Util.EAST, lblPassword);
		panelPassword.add(panelPasswordMiddle);

		textFieldPassword = new JPasswordField();
		panelPasswordMiddle.add(textFieldPassword);
		sl_panelPassword.putConstraint(Util.NORTH, textFieldPassword, 26, Util.NORTH, panelPassword);
		sl_panelPassword.putConstraint(Util.WEST, textFieldPassword, 12, Util.WEST, panelPassword);
		textFieldPassword.setToolTipText("password");
		textFieldPassword.setColumns(10);

		panelBtnLoginSubmitMiddle = new JPanel();
		sl_panelPassword.putConstraint(Util.NORTH, panelBtnLoginSubmitMiddle, 6, Util.SOUTH,panelPasswordMiddle);
		sl_panelPassword.putConstraint(Util.WEST, panelBtnLoginSubmitMiddle, PADDING, Util.WEST,panelPassword);
		sl_panelPassword.putConstraint(Util.SOUTH, panelBtnLoginSubmitMiddle, 42, Util.SOUTH,panelPasswordMiddle);
		sl_panelPassword.putConstraint(Util.EAST, panelBtnLoginSubmitMiddle, -PADDING, Util.EAST,panelPassword);
		panelPassword.add(panelBtnLoginSubmitMiddle);

		btnLoginSubmit = new JButton("Submit");
		panelBtnLoginSubmitMiddle.add(btnLoginSubmit);

		panelRightTop = new JPanel();
		sl_panelRight.putConstraint(Util.NORTH, panelRightContent, 27, Util.NORTH, panelRightTop);
		panelRightTop.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		sl_panelRight.putConstraint(Util.NORTH, panelRightTop, 0, Util.NORTH, panelRight);
		sl_panelRight.putConstraint(Util.WEST, panelRightTop, 0, Util.WEST, panelRightContent);
		sl_panelRight.putConstraint(Util.SOUTH, panelRightTop, 0, Util.NORTH, panelRightContent);
		sl_panelRight.putConstraint(Util.EAST, panelRightTop, 0, Util.EAST, panelRightContent);
		panelRight.add(panelRightTop);

		lblCurrentPane = new JLabel("THIS_SHOULD_BE_AUTO_REPLACED");
		panelRightTop.add(lblCurrentPane);
		GridBagLayout gbl_panelNewTicket = new GridBagLayout();
		gbl_panelNewTicket.columnWidths = new int[] { 12, 179, 112, 105, 46, 137, 0, 0, 0 };
		gbl_panelNewTicket.rowHeights = new int[] { 50, 60, 36, 55, 161, 121, 91, 0, 1 };
		gbl_panelNewTicket.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE };
		gbl_panelNewTicket.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panelNewTicket.setLayout(gbl_panelNewTicket);

		panelNTShortDesc = new JPanel();
		GridBagConstraints gbc_panelNTShortDesc = new GridBagConstraints();
		gbc_panelNTShortDesc.insets = new Insets(0, 0, 5, 5);
		gbc_panelNTShortDesc.fill = GridBagConstraints.BOTH;
		gbc_panelNTShortDesc.gridheight = 2;
		gbc_panelNTShortDesc.gridx = 1;
		gbc_panelNTShortDesc.gridy = 0;
		panelNewTicket.add(panelNTShortDesc, gbc_panelNTShortDesc);
		sl_panelNTShortDesc = new SpringLayout();
		panelNTShortDesc.setLayout(sl_panelNTShortDesc);

		lblShortDesc = new JLabel("Short description / name");
		sl_panelNTShortDesc.putConstraint(Util.NORTH, lblShortDesc, PADDING, Util.NORTH, panelNTShortDesc);
		lblShortDesc.setHorizontalAlignment(SwingConstants.CENTER);
		sl_panelNTShortDesc.putConstraint(Util.WEST, lblShortDesc, PADDING, Util.WEST, panelNTShortDesc);
		sl_panelNTShortDesc.putConstraint(Util.EAST, lblShortDesc, -PADDING, Util.EAST, panelNTShortDesc);
		panelNTShortDesc.add(lblShortDesc);

		panelTextAreaShortDesc = new JPanel();
		sl_panelNTShortDesc.putConstraint(Util.NORTH, panelTextAreaShortDesc, PADDING, Util.SOUTH,lblShortDesc);
		sl_panelNTShortDesc.putConstraint(Util.WEST, panelTextAreaShortDesc, PADDING, Util.WEST,panelNTShortDesc);
		sl_panelNTShortDesc.putConstraint(Util.EAST, panelTextAreaShortDesc, -PADDING, Util.EAST,panelNTShortDesc);
		panelTextAreaShortDesc.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		sl_panelNTShortDesc.putConstraint(Util.SOUTH, panelTextAreaShortDesc, -PADDING, Util.SOUTH,panelNTShortDesc);
		panelNTShortDesc.add(panelTextAreaShortDesc);
		panelTextAreaShortDesc.setLayout(new BorderLayout(0, 0));

		textAreaShortDesc = new JTextArea();
		panelTextAreaShortDesc.add(textAreaShortDesc);
		sl_panelNTShortDesc.putConstraint(Util.NORTH, textAreaShortDesc, 6, Util.SOUTH, lblShortDesc);
		sl_panelNTShortDesc.putConstraint(Util.WEST, textAreaShortDesc, PADDING, Util.WEST, panelNTShortDesc);
		sl_panelNTShortDesc.putConstraint(Util.SOUTH, textAreaShortDesc, -PADDING, Util.SOUTH, panelNTShortDesc);
		sl_panelNTShortDesc.putConstraint(Util.EAST, textAreaShortDesc, -PADDING, Util.EAST, panelNTShortDesc);
		textAreaShortDesc.setColumns(10);

		panelNTCategory = new JPanel();
		GridBagConstraints gbc_panelNTCategory = new GridBagConstraints();
		gbc_panelNTCategory.gridwidth = 2;
		gbc_panelNTCategory.fill = GridBagConstraints.BOTH;
		gbc_panelNTCategory.insets = new Insets(0, 0, 5, 5);
		gbc_panelNTCategory.gridx = 2;
		gbc_panelNTCategory.gridy = 0;
		panelNewTicket.add(panelNTCategory, gbc_panelNTCategory);
		panelNTCategory.setLayout(new BorderLayout(0, 0));

		comboBoxCategory = new JComboBox<Object>();
		comboBoxCategory.setFont(new Font("Dialog", Font.BOLD, 15));
		comboBoxCategory.setModel(new DefaultComboBoxModel(
				new String[] { "THESE", "SHOULD", "BE", "AUTO", "REPLACED", "FROM", "THE", "CATEGORIES", "TABLE" }));
		panelNTCategory.add(comboBoxCategory);

		labelCategory = new JLabel("Category");
		labelCategory.setHorizontalAlignment(SwingConstants.CENTER);
		panelNTCategory.add(labelCategory, BorderLayout.NORTH);

		Util.setupCategories(comboBoxCategory,this.dao); // grab list of possible categories
											// from server's category table

		panelNTDateStarted = new JPanel();
		GridBagConstraints gbc_panelDateStarted = new GridBagConstraints();
		gbc_panelDateStarted.gridwidth = 2;
		gbc_panelDateStarted.anchor = GridBagConstraints.NORTHWEST;
		gbc_panelDateStarted.insets = new Insets(5, 5, 5, 5);
		gbc_panelDateStarted.gridx = 4;
		gbc_panelDateStarted.gridy = 0;
		panelNewTicket.add(panelNTDateStarted, gbc_panelDateStarted);
		panelNTDateStarted.setLayout(new BorderLayout(0, 0));

		dateStartedChooser = new JDateChooser();
		dateStartedChooser.setDateFormatString(PREFERRED_TIME_FORMAT);
		panelNTDateStarted.add(dateStartedChooser);

		labelDateStarted = new JLabel("Date started");
		labelDateStarted.setHorizontalAlignment(SwingConstants.CENTER);
		panelNTDateStarted.add(labelDateStarted, BorderLayout.NORTH);

		panelNTSeverity = new JPanel();
		GridBagConstraints gbc_panelSeverity = new GridBagConstraints();
		gbc_panelSeverity.fill = GridBagConstraints.HORIZONTAL;
		gbc_panelSeverity.gridwidth = 2;
		gbc_panelSeverity.insets = new Insets(0, 0, 5, 5);
		gbc_panelSeverity.gridx = 2;
		gbc_panelSeverity.gridy = 1;
		panelNewTicket.add(panelNTSeverity, gbc_panelSeverity);
		panelNTSeverity.setLayout(new BorderLayout(0, 0));

		labelSeverity = new JLabel("Severity");
		labelSeverity.setHorizontalAlignment(SwingConstants.CENTER);
		panelNTSeverity.add(labelSeverity, BorderLayout.NORTH);

		comboBoxSeverity = new JComboBox<String>();
		comboBoxSeverity.setModel(new DefaultComboBoxModel(new String[] { "THESE", "SHOULD", "BE", "POPULATED", "WITH",
				"ENTRIES", "FROM", "THE", "SEVERITY", "TABLE" }));
		comboBoxSeverity.setFont(new Font("Dialog", Font.BOLD, 15));
		panelNTSeverity.add(comboBoxSeverity, BorderLayout.CENTER);


		Util.setupSeverities(comboBoxSeverity, this.dao); 	// grab list of possible severities
															// from server's severity table

		panelNTDateEnded = new JPanel();
		GridBagConstraints gbc_panelNTDateEnded = new GridBagConstraints();
		gbc_panelNTDateEnded.anchor = GridBagConstraints.WEST;
		gbc_panelNTDateEnded.gridwidth = 2;
		gbc_panelNTDateEnded.insets = new Insets(5, 5, 5, 5);
		gbc_panelNTDateEnded.gridx = 4;
		gbc_panelNTDateEnded.gridy = 1;
		panelNewTicket.add(panelNTDateEnded, gbc_panelNTDateEnded);
		panelNTDateEnded.setLayout(new BorderLayout(0, 0));

		dateEndedChooser = new JDateChooser();
		dateEndedChooser.setDateFormatString("MMM d, yyyy, hh:mm:ss aa");
		panelNTDateEnded.add(dateEndedChooser, BorderLayout.CENTER);

		labelDateEnded = new JLabel("Date ended");
		labelDateEnded.setHorizontalAlignment(SwingConstants.CENTER);
		panelNTDateEnded.add(labelDateEnded, BorderLayout.NORTH);

		panelNTLongDesc = new JPanel();
		GridBagConstraints gbc_panelNTLongDesc = new GridBagConstraints();
		gbc_panelNTLongDesc.gridheight = 3;
		gbc_panelNTLongDesc.fill = GridBagConstraints.BOTH;
		gbc_panelNTLongDesc.insets = new Insets(0, 0, 5, 5);
		gbc_panelNTLongDesc.gridwidth = 6;
		gbc_panelNTLongDesc.gridx = 1;
		gbc_panelNTLongDesc.gridy = 2;
		panelNewTicket.add(panelNTLongDesc, gbc_panelNTLongDesc);
		sl_panelNTLongDesc = new SpringLayout();
		panelNTLongDesc.setLayout(sl_panelNTLongDesc);

		lblLongDescription = new JLabel("Long description");
		sl_panelNTLongDesc.putConstraint(Util.NORTH, lblLongDescription, 0, Util.NORTH, panelNTLongDesc);
		sl_panelNTLongDesc.putConstraint(Util.WEST, lblLongDescription, PADDING, Util.WEST, panelNTLongDesc);
		sl_panelNTLongDesc.putConstraint(Util.EAST, lblLongDescription, -PADDING, Util.EAST, panelNTLongDesc);
		lblLongDescription.setHorizontalAlignment(SwingConstants.CENTER);
		panelNTLongDesc.add(lblLongDescription);

		panelTextAreaLongDesc = new JPanel();
		panelTextAreaLongDesc.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		sl_panelNTLongDesc.putConstraint(Util.NORTH, panelTextAreaLongDesc, PADDING, Util.SOUTH,
				lblLongDescription);
		sl_panelNTLongDesc.putConstraint(Util.SOUTH, panelTextAreaLongDesc, -PADDING, Util.SOUTH,
				panelNTLongDesc);
		sl_panelNTLongDesc.putConstraint(Util.WEST, panelTextAreaLongDesc, PADDING, Util.WEST, panelNTLongDesc);
		sl_panelNTLongDesc.putConstraint(Util.EAST, panelTextAreaLongDesc, -PADDING, Util.EAST, panelNTLongDesc);
		panelNTLongDesc.add(panelTextAreaLongDesc);
		panelTextAreaLongDesc.setLayout(new BorderLayout(0, 0));

		scrollPaneLongDesc = new JScrollPane();
		panelTextAreaLongDesc.add(scrollPaneLongDesc, BorderLayout.CENTER);

		textAreaLongDesc = new JTextArea();
		scrollPaneLongDesc.setViewportView(textAreaLongDesc);
		textAreaLongDesc.setColumns(10);

		btnNTSubmitNewTicket = new JButton(BUTTON_S_SUBMIT_NEW_TICKET_S);
		GridBagConstraints gbc_btnNTSubmitNewTicket = new GridBagConstraints();
		gbc_btnNTSubmitNewTicket.fill = GridBagConstraints.BOTH;
		gbc_btnNTSubmitNewTicket.gridwidth = 2;
		gbc_btnNTSubmitNewTicket.insets = new Insets(0, 0, 5, 5);
		gbc_btnNTSubmitNewTicket.gridx = 2;
		gbc_btnNTSubmitNewTicket.gridy = 5;
		panelNewTicket.add(btnNTSubmitNewTicket, gbc_btnNTSubmitNewTicket);
		btnNTSubmitNewTicket.setName("btnNewTiket");

		panelNTButtonSubmitNewTicket = new JPanel();
		GridBagConstraints gbc_panelNTButtonSubmitNewTicket = new GridBagConstraints();
		gbc_panelNTButtonSubmitNewTicket.insets = new Insets(0, 0, 5, 5);
		gbc_panelNTButtonSubmitNewTicket.fill = GridBagConstraints.BOTH;
		gbc_panelNTButtonSubmitNewTicket.gridwidth = 2;
		gbc_panelNTButtonSubmitNewTicket.gridx = 2;
		gbc_panelNTButtonSubmitNewTicket.gridy = 6;
		panelNewTicket.add(panelNTButtonSubmitNewTicket, gbc_panelNTButtonSubmitNewTicket);
		panelNTButtonSubmitNewTicket.setLayout(new BorderLayout(0, 0));
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

		panelSearchBottom = new JPanel();
		splitPaneSearch.setRightComponent(panelSearchBottom);
		panelSearchBottom.setLayout(new BorderLayout(0, 0));

		panelSearchResultsTop = new JPanel();
		panelSearchBottom.add(panelSearchResultsTop, BorderLayout.NORTH);

		JScrollPane scrollPaneResults = new JScrollPane();
		
		tableResults = new JTable();
		tableResults.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "hi", "please", "replace", "me" }));
		scrollPaneResults.setViewportView(tableResults);
		
		JPopupMenu popupMenuResults = new JPopupMenu();
		
		JMenuItem deleteTicketMI = new JMenuItem("Delete -----------------");
		
		JMenuItem modifyTicketMI = new JMenuItem("Modify -----------------"); //TODO why can't I resize these?
		
		popupMenuResults.add(deleteTicketMI,0);
		popupMenuResults.add(modifyTicketMI,1);
		
		tableResults.setComponentPopupMenu(popupMenuResults); //add popup menu for table

		panelSearchBottom.add(scrollPaneResults, BorderLayout.CENTER);


		panelModifyTicket = new JPanel();
		panelModifyTicket.setName("panelModifyTicket");
		panelRightContent.add(panelModifyTicket, panelModifyTicket.getName());
		springLayout_1 = new SpringLayout();
		panelModifyTicket.setLayout(springLayout_1);

		btnSubmitChanges = new JButton("Submit Changes");

		springLayout_1.putConstraint(SpringLayout.WEST, btnSubmitChanges, 303, SpringLayout.WEST, panelModifyTicket);
		springLayout_1.putConstraint(SpringLayout.SOUTH, btnSubmitChanges, -54, SpringLayout.SOUTH, panelModifyTicket);
		panelModifyTicket.add(btnSubmitChanges);
		
		JPanel panel = new JPanel();
		springLayout_1.putConstraint(SpringLayout.NORTH, panel, 10, SpringLayout.NORTH, panelModifyTicket);
		springLayout_1.putConstraint(SpringLayout.WEST, panel, 10, SpringLayout.WEST, panelModifyTicket);
		panelModifyTicket.add(panel);
		
		JLabel lblTicketId = new JLabel("Ticket ID");
		panel.add(lblTicketId);
		
		textField_1 = new JTextField();
		panel.add(textField_1);
		springLayout_1.putConstraint(SpringLayout.NORTH, textField_1, 60, SpringLayout.NORTH, panelModifyTicket);
		springLayout_1.putConstraint(SpringLayout.WEST, textField_1, 61, SpringLayout.WEST, panelModifyTicket);
		textField_1.setColumns(10);
		splitPane.setDividerLocation(DEFAULT_DIVIDER_WIDTH);

		btnLogin.addActionListener(new ActionListener()
		{
			/***
			 * 'Login' button clicked.
			 */
			public void actionPerformed(ActionEvent e)
			{
				modifyRightPane(panelLogin, e, btnLogin.getText());
			}
		});
		
		
		btnLogout.addActionListener(new ActionListener()
		{
			/***
			 * 'Logout' button clocked.
			 */

			@Override
			public void actionPerformed(ActionEvent e)
			{
				Util.disableButtons(panelMenu, new String[]{BUTTON_LOGIN_NAME}); //disable all but log in
				modifyRightPane(panelLogin, e, btnLogin.getText());
				
				textFieldPassword.setText("");
				textFieldUsername.setText("");
				lblWhosLoggedIn.setText(GUIC.NOT_LOGGED_IN.s());
				
				textFieldUsername.grabFocus();
				
				dao.logout(); //log them out
			}
		});
		

		popupMenuResults.addPopupMenuListener(new PopupMenuListener()
		{
			/***
			 * When the popup menu gets invoked.
			 */
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent pme)
			{

				SwingUtilities.invokeLater(new Runnable()
				{
					/***
					 * This part selects the table when you right-click.
					 */
					@Override
					public void run()
					{
						JPopupMenu parentMenu = (JPopupMenu)pme.getSource();
						
						
						int rowAtPoint = tableResults.rowAtPoint(SwingUtilities.convertPoint(popupMenuResults, new Point(0, 0), tableResults));
						if (rowAtPoint > -1)
						{
							tableResults.setRowSelectionInterval(rowAtPoint, rowAtPoint);
							Util.printf("Setting selection to row [%d] from rightclick.\n",rowAtPoint,rowAtPoint);
							
							DefaultTableModel dtm = (DefaultTableModel)tableResults.getModel(); //get data model
							
							int id = (int) Integer.parseInt((String)dtm.getValueAt(rowAtPoint, 0)); //get 0th item at nth row
							
							JMenuItem jmiDelete = ((JMenuItem)((JPopupMenu)pme.getSource()).getComponent(0)); //get menu item so we can make it say "Delete ticket id {}"
							JMenuItem jmiModify = ((JMenuItem)((JPopupMenu)pme.getSource()).getComponent(1)); //get menu item so we can make it say "Modify ticket id {}"
							
							jmiDelete.setText(String.format("Delete Ticket ID '%d'",id));
							jmiModify.setText(String.format("Modify Ticket ID '%d'",id));
							
							
							//TODO why do I have to put a bunch of ------'s? Can't repaint the jpopupmenu to scale to width of text? hm..
						} 
					}
				});
			}
			
			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent arg0)
			{
				// TODO Auto-generated method stub
			}
			
			@Override
			public void popupMenuCanceled(PopupMenuEvent arg0)
			{
				// TODO Auto-generated method stub
			}
		});
		
		modifyTicketMI.addActionListener(new AbstractAction()
		{
			/***
			 * When someone clicks the "Modify ticket n" menu item.
			 */
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				
				int row = Util.getSelectedPos(tableResults)[0];
				
				DefaultTableModel dtm = (DefaultTableModel) tableResults.getModel();
				
				int id = ((int) Integer.parseInt((String)dtm.getValueAt(row, 0)));
				
				System.out.printf("Setting up 'new ticket' view for ticket ID '%d'\n",id);
				
				
				ResultSet oneticket = dao.getTicketRS(id);
				
				
				//first, fill in the field and change the name of the "submit" button;
				try
				{				
					oneticket.next();
					
					textAreaShortDesc.setText(oneticket.getString(SQLC.SHORT_DESC_COLUMN_NAME.s()));
					textAreaLongDesc.setText(oneticket.getString(SQLC.LONG_DESC_COLUMN_NAME.s()));
					comboBoxCategory.setSelectedIndex(oneticket.getInt(SQLC.CATEGORY_ID_COLUMN_NAME.s()));
					comboBoxSeverity.setSelectedIndex(oneticket.getInt(SQLC.SEVERITY_ID_COLUMN_NAME.s()));
					dateStartedChooser.setDate(oneticket.getDate(SQLC.START_DATE_COLUMN_NAME.s()));
					dateEndedChooser.setDate(oneticket.getDate(SQLC.END_DATE_COLUMN_NAME.s()));
					
					btnNTSubmitNewTicket.setText(btnNTSubmitNewTicket.getText() + " (modify)");
					_ARE_MODIFYING_TICKET = true;
					_PREV_TICKET_ID = id;
					
					

				}
				catch (SQLException e)
				{
					System.out.printf("Failed to populate 'new ticket' view with ticket ID %d's info!",id);
					System.out.println(e.getSQLState());
					System.out.println(e.getMessage());
					e.printStackTrace();
					
				}

				
				
				modifyRightPane(panelNewTicket, btnModifyTicket.getText()); //use our 'new ticket' GUI elt..why not?
				
				
				
			}
		});
		
		deleteTicketMI.addActionListener(new AbstractAction()
		{
			private static final long serialVersionUID = 2440586029673540511L;

			/***
			 * Someone clicks "Delete ticket n" on our right-click menu.
			 */
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				int row = Util.getSelectedPos(tableResults)[0];
				
				DefaultTableModel dtm = (DefaultTableModel) tableResults.getModel();
				
				int id = ((int) Integer.parseInt((String)dtm.getValueAt(row, 0)));
				
				System.out.printf("We should delete ticket with ID '%d'.\n",id);
				
				if(dao.deleteTicket(id)) //if successfully deleted
				{
					dtm.removeRow(row);
				}
				else
				{
					System.out.println("Failed to delete...:(");
				}
				
				
			}
		});
		
		
		
		btnModifyTicket.addActionListener(new ActionListener()
		{
			/***
			 * 'Modify ticket' button clicked.
			 */
			public void actionPerformed(ActionEvent e)
			{
				btnModifyTicket.setEnabled(false); //we're not gonna use it.
				btnModifyTicket.setToolTipText("Use the right-click menu in the \"Search\" view to modify a ticket.");
			}
		});
		
		
		
		
		
		btnSubmitChanges.addActionListener(new ActionListener()
		{
			/***
			 * 'Submit changes' button clicked.
			 */
			public void actionPerformed(ActionEvent e)
			{
			}
		});

		btnSearch.addActionListener(new ActionListener()
		{
			/***
			 * 'Search' button clicked.
			 */
			public void actionPerformed(ActionEvent e)
			{
				modifyRightPane(panelSearch, e, btnSearch.getText());

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

				
				
				ResultSet rs = null; //get results for table
				
				try
				{
					//execute default query to populate search pane
					System.out.printf("About to execute this query to populate our default results view: \n'%s'\n",SQLC.SEARCH_PANE_DEFAULT_QUERY_PAPADEMAS.toString());
					rs = dao.c.createStatement().executeQuery(SQLC.SEARCH_PANE_DEFAULT_QUERY_PAPADEMAS.toString());
				}
				catch (SQLException e1)
				{
					e1.printStackTrace();
				}
				
				
				ArrayList<ArrayList<Object>> data = dao.resultSetToList(rs); //get 2d list of data
				ArrayList<ArrayList<Object>> dataHeaders = dao.resultSetMetadataToList(rs);
				
				
				
				Object[][] dataA = data.stream().map(aRow -> aRow.toArray(new Object[0])).toArray(Object[][]::new);
				//ArrayList<ArrayList<Object>> converted to Object[][]
				
				Object[] dataHeadersA = dataHeaders.get(0).toArray();
				//0th header converted to Object[]
				
				// set up the table to have a default view of all notes...
				DefaultTableModel dtm = new DefaultTableModel(dataA,dataHeadersA);

				
				tableResults.setModel(dtm);
				
//				dao.resultSetToList(rs)

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
				((JButton)e.getSource()).setText(BUTTON_NEW_TICKET_S);
				modifyRightPane(panelNewTicket, e, btnNewTicket.getText());
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
		
		frame.addWindowListener(new WindowAdapter()
		{
			/***
			 * When window is opened, select username field.
			 */
			public void windowOpened(WindowEvent we)
			{
				textFieldUsername.requestFocus();

			}
		});		
		
		btnNTSubmitNewTicket.addActionListener(new ActionListener()
		{
			/***
			 * Someone clicks "submit new ticket"
			 */
			public void actionPerformed(ActionEvent arg0)
			{
				
				if(_ARE_MODIFYING_TICKET) //to only delete if they submit
				{
					dao.deleteTicket(_PREV_TICKET_ID); //delete previous ticket
					
					_ARE_MODIFYING_TICKET = false;
					_PREV_TICKET_ID = -1;
					btnNTSubmitNewTicket.setText(BUTTON_S_SUBMIT_NEW_TICKET_S);
				}

				dateStartedChooser.setBorder(borderDefault);
				dateEndedChooser.setBorder(borderDefault);  //reset borders
				
				dateStartedChooser.setToolTipText(PREFERRED_TIME_FORMAT);
				dateEndedChooser.setToolTipText(PREFERRED_TIME_FORMAT);
				dateStartedChooser.getCalendarButton().setToolTipText(PREFERRED_TIME_FORMAT); 
				dateEndedChooser.getCalendarButton().setToolTipText(PREFERRED_TIME_FORMAT); //reset tooltips
				dateStartedChooser.getDateEditor().getUiComponent().setToolTipText(PREFERRED_TIME_FORMAT);
				dateStartedChooser.getDateEditor().getUiComponent().setToolTipText(PREFERRED_TIME_FORMAT);
				
				
				Boolean allFormsOK = true;
				if(dateStartedChooser.getDate() == null) //if start date isn't filled out, complain
				{
					allFormsOK = false;
					dateStartedChooser.setBorder(borderBad);
					dateStartedChooser.requestFocus();
					dateStartedChooser.getDateEditor().getUiComponent().setToolTipText(GUIC.MUST_ENTER_DATE + " (" + PREFERRED_TIME_FORMAT + ")");
				}
				
				if(allFormsOK)
				{
					dao.submitTicket(dao.USER_NAME,						//username
									textAreaShortDesc.getText(), 		//short desc
									textAreaLongDesc.getText(),			//long desc
									(Integer)Integer.parseInt(((String)comboBoxCategory.getSelectedItem()).substring(0, 1)),	//1st char of category dropdown as Integer
									(Integer)Integer.parseInt(((String)comboBoxSeverity.getSelectedItem()).substring(0, 1)),	//1st char of severity dropdown as Integer
									dateStartedChooser.getDate(),
									dateEndedChooser.getDate());
					
					// clear the text we just used.
					textAreaShortDesc.setText("");
					textAreaLongDesc.setText("");
					comboBoxCategory.setSelectedIndex(0);
					comboBoxSeverity.setSelectedIndex(0);
					((JTextField)dateStartedChooser.getDateEditor().getUiComponent()).setText(""); //set text of date started to nothing
					((JTextField)dateEndedChooser.getDateEditor().getUiComponent()).setText(""); //set text of date started to nothing
					
				}
			}
		});
		
		

		Util.formatButtons(panelMenu);

		btnLogin.doClick(); // to setup the top text
		lblLoginErrorMsg.setText(""); // empty login error message
		
		Util.disableButtons(panelMenu,new String[] {BUTTON_LOGIN_NAME}); //disable all buttons until user logs in
		Util.setTooltips(panelMenu, new String[] {BUTTON_LOGIN_NAME}, "You must log in to be able to use this button.");
		
		
		
		


	}
}
