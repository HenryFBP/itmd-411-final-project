import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

public class TroubleTicketGUI
{
	public static int DEFAULT_LOGIN_MODE = 1; // Which entry should be used? If this is too large, the last one will be
												// used.
	public static final String LOGINS_FILE_PATH = "login.txt"; // Where are our logins and passwords stored? This is for
																// security and for not committing passwords to git.
	
	public static final int PADDING = 10;
	public static final int DEFAULT_DIVIDER_WIDTH = 150;



	public static final String NESW = "NESW";
	public static final String PREFERRED_TIME_FORMAT = "MMM d, yyyy, hh:mm:ss aa";

	private static final String BUTTON_LOGOUT_NAME = "btnLogout";
	private static final String BUTTON_LOGIN_NAME = "btnLogin";
	private static final String BUTTON_NEW_TICKET_NAME = "btnNewTicket";
	private static final String BUTTON_MODIFY_TICKET_NAME = "btnModifyTicket";
	private static final String BUTTON_SEARCH_NAME = "btnSearch";
	

	
	private static final String PANEL_RIGHT_CONTENT_NAME = "panelRightContent";

	public static final Border borderDefault = new JTextField().getBorder();
	public static final Border borderBad = BorderFactory.createLineBorder(Color.RED);

	public static Dao dao;



	/***
	 * Changes the right panel to show ONLY JPanel 'jp'.
	 * @param jp  The JPanel to be shown
	 * @param e  The actionEvent that tells us what button was clicked.
	 */
	public void modifyRightPane(JPanel jp, ActionEvent e)
	{
		JPanel panel = panelModifyTicket;

		CardLayout cl = (CardLayout) panelRightContent.getLayout();
		JLabel label = lblCurrentPane;
		JButton button = (JButton) e.getSource();

		Util.println("Someone clicked button '" + button.getName() + "' or '" + button.getText() + "'");
		Util.printf("About to do cl.show('%s', '%s')\n", panelRightContent.getName(), jp.getName());

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
	private JButton btnModify;
	private JPanel panelRight;
	private SpringLayout sl_panelRight;
	private SpringLayout sl_panelLogin;
	private JPanel panelUsername;
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
	private JPanel panelButtonSubmitNewTicket;
	private JPanel panelTextAreaLongDesc;
	private JTextArea textAreaLongDesc;
	private JPanel panelUsernameMiddle;
	private JTextField textFieldUsername;
	private JPanel panelPasswordMiddle;
	private JScrollPane scrollPaneLongDesc;
	private SpringLayout sl_panelLongDesc;
	private JPanel panelLongDesc;
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
	private JPanel panelSeverity;
	private JPanel panelDateStarted;
	private JLabel labelDateStarted;
	private JDateChooser dateStartedChooser;
	private JPanel panelDateEnded;
	private JLabel labelDateEnded;
	private JDateChooser dateEndedChooser;

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

		panelShortDesc = new JPanel();
		GridBagConstraints gbc_panelShortDesc = new GridBagConstraints();
		gbc_panelShortDesc.insets = new Insets(0, 0, 5, 5);
		gbc_panelShortDesc.fill = GridBagConstraints.BOTH;
		gbc_panelShortDesc.gridheight = 2;
		gbc_panelShortDesc.gridx = 1;
		gbc_panelShortDesc.gridy = 0;
		panelNewTicket.add(panelShortDesc, gbc_panelShortDesc);
		sl_panelShortDesc = new SpringLayout();
		panelShortDesc.setLayout(sl_panelShortDesc);

		lblShortDesc = new JLabel("Short description / name");
		sl_panelShortDesc.putConstraint(Util.NORTH, lblShortDesc, PADDING, Util.NORTH, panelShortDesc);
		lblShortDesc.setHorizontalAlignment(SwingConstants.CENTER);
		sl_panelShortDesc.putConstraint(Util.WEST, lblShortDesc, PADDING, Util.WEST, panelShortDesc);
		sl_panelShortDesc.putConstraint(Util.EAST, lblShortDesc, -PADDING, Util.EAST, panelShortDesc);
		panelShortDesc.add(lblShortDesc);

		panelTextAreaShortDesc = new JPanel();
		sl_panelShortDesc.putConstraint(Util.NORTH, panelTextAreaShortDesc, PADDING, Util.SOUTH,lblShortDesc);
		sl_panelShortDesc.putConstraint(Util.WEST, panelTextAreaShortDesc, PADDING, Util.WEST,panelShortDesc);
		sl_panelShortDesc.putConstraint(Util.EAST, panelTextAreaShortDesc, -PADDING, Util.EAST,panelShortDesc);
		panelTextAreaShortDesc.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		sl_panelShortDesc.putConstraint(Util.SOUTH, panelTextAreaShortDesc, -PADDING, Util.SOUTH,panelShortDesc);
		panelShortDesc.add(panelTextAreaShortDesc);
		panelTextAreaShortDesc.setLayout(new BorderLayout(0, 0));

		textAreaShortDesc = new JTextArea();
		panelTextAreaShortDesc.add(textAreaShortDesc);
		sl_panelShortDesc.putConstraint(Util.NORTH, textAreaShortDesc, 6, Util.SOUTH, lblShortDesc);
		sl_panelShortDesc.putConstraint(Util.WEST, textAreaShortDesc, PADDING, Util.WEST, panelShortDesc);
		sl_panelShortDesc.putConstraint(Util.SOUTH, textAreaShortDesc, -PADDING, Util.SOUTH, panelShortDesc);
		sl_panelShortDesc.putConstraint(Util.EAST, textAreaShortDesc, -PADDING, Util.EAST, panelShortDesc);
		textAreaShortDesc.setColumns(10);

		panelCategory = new JPanel();
		GridBagConstraints gbc_panelCategory = new GridBagConstraints();
		gbc_panelCategory.gridwidth = 2;
		gbc_panelCategory.fill = GridBagConstraints.BOTH;
		gbc_panelCategory.insets = new Insets(0, 0, 5, 5);
		gbc_panelCategory.gridx = 2;
		gbc_panelCategory.gridy = 0;
		panelNewTicket.add(panelCategory, gbc_panelCategory);
		panelCategory.setLayout(new BorderLayout(0, 0));

		comboBoxCategory = new JComboBox<Object>();
		comboBoxCategory.setFont(new Font("Dialog", Font.BOLD, 15));
		comboBoxCategory.setModel(new DefaultComboBoxModel(
				new String[] { "THESE", "SHOULD", "BE", "AUTO", "REPLACED", "FROM", "THE", "CATEGORIES", "TABLE" }));
		panelCategory.add(comboBoxCategory);

		labelCategory = new JLabel("Category");
		labelCategory.setHorizontalAlignment(SwingConstants.CENTER);
		panelCategory.add(labelCategory, BorderLayout.NORTH);

		Util.setupCategories(comboBoxCategory,this.dao); // grab list of possible categories
											// from server's category table

		panelDateStarted = new JPanel();
		GridBagConstraints gbc_panelDateStarted = new GridBagConstraints();
		gbc_panelDateStarted.gridwidth = 2;
		gbc_panelDateStarted.anchor = GridBagConstraints.NORTHWEST;
		gbc_panelDateStarted.insets = new Insets(5, 5, 5, 5);
		gbc_panelDateStarted.gridx = 4;
		gbc_panelDateStarted.gridy = 0;
		panelNewTicket.add(panelDateStarted, gbc_panelDateStarted);
		panelDateStarted.setLayout(new BorderLayout(0, 0));

		dateStartedChooser = new JDateChooser();
		dateStartedChooser.setDateFormatString(PREFERRED_TIME_FORMAT);
		panelDateStarted.add(dateStartedChooser);

		labelDateStarted = new JLabel("Date started");
		labelDateStarted.setHorizontalAlignment(SwingConstants.CENTER);
		panelDateStarted.add(labelDateStarted, BorderLayout.NORTH);

		panelSeverity = new JPanel();
		GridBagConstraints gbc_panelSeverity = new GridBagConstraints();
		gbc_panelSeverity.fill = GridBagConstraints.HORIZONTAL;
		gbc_panelSeverity.gridwidth = 2;
		gbc_panelSeverity.insets = new Insets(0, 0, 5, 5);
		gbc_panelSeverity.gridx = 2;
		gbc_panelSeverity.gridy = 1;
		panelNewTicket.add(panelSeverity, gbc_panelSeverity);
		panelSeverity.setLayout(new BorderLayout(0, 0));

		labelSeverity = new JLabel("Severity");
		labelSeverity.setHorizontalAlignment(SwingConstants.CENTER);
		panelSeverity.add(labelSeverity, BorderLayout.NORTH);

		comboBoxSeverity = new JComboBox<String>();
		comboBoxSeverity.setModel(new DefaultComboBoxModel(new String[] { "THESE", "SHOULD", "BE", "POPULATED", "WITH",
				"ENTRIES", "FROM", "THE", "SEVERITY", "TABLE" }));
		comboBoxSeverity.setFont(new Font("Dialog", Font.BOLD, 15));
		panelSeverity.add(comboBoxSeverity, BorderLayout.CENTER);


		Util.setupSeverities(comboBoxSeverity, this.dao); 	// grab list of possible severities
															// from server's severity table

		panelDateEnded = new JPanel();
		GridBagConstraints gbc_panelDateEnded = new GridBagConstraints();
		gbc_panelDateEnded.anchor = GridBagConstraints.WEST;
		gbc_panelDateEnded.gridwidth = 2;
		gbc_panelDateEnded.insets = new Insets(5, 5, 5, 5);
		gbc_panelDateEnded.gridx = 4;
		gbc_panelDateEnded.gridy = 1;
		panelNewTicket.add(panelDateEnded, gbc_panelDateEnded);
		panelDateEnded.setLayout(new BorderLayout(0, 0));

		dateEndedChooser = new JDateChooser();
		dateEndedChooser.setDateFormatString("MMM d, yyyy, hh:mm:ss aa");
		panelDateEnded.add(dateEndedChooser, BorderLayout.CENTER);

		labelDateEnded = new JLabel("Date ended");
		labelDateEnded.setHorizontalAlignment(SwingConstants.CENTER);
		panelDateEnded.add(labelDateEnded, BorderLayout.NORTH);

		panelLongDesc = new JPanel();
		GridBagConstraints gbc_panelLongDesc = new GridBagConstraints();
		gbc_panelLongDesc.gridheight = 3;
		gbc_panelLongDesc.fill = GridBagConstraints.BOTH;
		gbc_panelLongDesc.insets = new Insets(0, 0, 5, 5);
		gbc_panelLongDesc.gridwidth = 6;
		gbc_panelLongDesc.gridx = 1;
		gbc_panelLongDesc.gridy = 2;
		panelNewTicket.add(panelLongDesc, gbc_panelLongDesc);
		sl_panelLongDesc = new SpringLayout();
		panelLongDesc.setLayout(sl_panelLongDesc);

		lblLongDescription = new JLabel("Long description");
		sl_panelLongDesc.putConstraint(Util.NORTH, lblLongDescription, 0, Util.NORTH, panelLongDesc);
		sl_panelLongDesc.putConstraint(Util.WEST, lblLongDescription, PADDING, Util.WEST, panelLongDesc);
		sl_panelLongDesc.putConstraint(Util.EAST, lblLongDescription, -PADDING, Util.EAST, panelLongDesc);
		lblLongDescription.setHorizontalAlignment(SwingConstants.CENTER);
		panelLongDesc.add(lblLongDescription);

		panelTextAreaLongDesc = new JPanel();
		panelTextAreaLongDesc.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		sl_panelLongDesc.putConstraint(Util.NORTH, panelTextAreaLongDesc, PADDING, Util.SOUTH,
				lblLongDescription);
		sl_panelLongDesc.putConstraint(Util.SOUTH, panelTextAreaLongDesc, -PADDING, Util.SOUTH,
				panelLongDesc);
		sl_panelLongDesc.putConstraint(Util.WEST, panelTextAreaLongDesc, PADDING, Util.WEST, panelLongDesc);
		sl_panelLongDesc.putConstraint(Util.EAST, panelTextAreaLongDesc, -PADDING, Util.EAST, panelLongDesc);
		panelLongDesc.add(panelTextAreaLongDesc);
		panelTextAreaLongDesc.setLayout(new BorderLayout(0, 0));

		scrollPaneLongDesc = new JScrollPane();
		panelTextAreaLongDesc.add(scrollPaneLongDesc, BorderLayout.CENTER);

		textAreaLongDesc = new JTextArea();
		scrollPaneLongDesc.setViewportView(textAreaLongDesc);
		textAreaLongDesc.setColumns(10);

		btnSubmitNewTicket = new JButton("Submit new ticket");
		GridBagConstraints gbc_btnSubmitNewTicket = new GridBagConstraints();
		gbc_btnSubmitNewTicket.fill = GridBagConstraints.BOTH;
		gbc_btnSubmitNewTicket.gridwidth = 2;
		gbc_btnSubmitNewTicket.insets = new Insets(0, 0, 5, 5);
		gbc_btnSubmitNewTicket.gridx = 2;
		gbc_btnSubmitNewTicket.gridy = 5;
		panelNewTicket.add(btnSubmitNewTicket, gbc_btnSubmitNewTicket);
		btnSubmitNewTicket.setName("btnNewTiket");

		panelButtonSubmitNewTicket = new JPanel();
		GridBagConstraints gbc_panelButtonSubmitNewTicket = new GridBagConstraints();
		gbc_panelButtonSubmitNewTicket.insets = new Insets(0, 0, 5, 5);
		gbc_panelButtonSubmitNewTicket.fill = GridBagConstraints.BOTH;
		gbc_panelButtonSubmitNewTicket.gridwidth = 2;
		gbc_panelButtonSubmitNewTicket.gridx = 2;
		gbc_panelButtonSubmitNewTicket.gridy = 6;
		panelNewTicket.add(panelButtonSubmitNewTicket, gbc_panelButtonSubmitNewTicket);
		panelButtonSubmitNewTicket.setLayout(new BorderLayout(0, 0));
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
		springLayout_1.putConstraint(Util.NORTH, btnModify, 184, Util.NORTH, panelModifyTicket);
		springLayout_1.putConstraint(Util.WEST, btnModify, 112, Util.WEST, panelModifyTicket);
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
		
		
		btnLogout.addActionListener(new ActionListener()
		{
			/***
			 * 'Logout' button clocked.
			 */

			@Override
			public void actionPerformed(ActionEvent e)
			{
				modifyRightPane(panelLogin, e);
				Util.disableButtons(panelMenu, new String[]{BUTTON_LOGIN_NAME}); //disable all but log in

				textFieldPassword.setText("");
				textFieldUsername.setText("");
				
				dao.logout(); //log them out
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
				modifyRightPane(panelNewTicket, e);
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
		
		btnSubmitNewTicket.addActionListener(new ActionListener()
		{
			/***
			 * Someone clicks "submit new ticket"
			 */
			public void actionPerformed(ActionEvent arg0)
			{

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
