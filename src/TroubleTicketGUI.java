import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JLayeredPane;
import javax.swing.SpringLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.CardLayout;
import java.awt.Component;

import javax.swing.JLabel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTextField;
import java.awt.FlowLayout;
import net.miginfocom.swing.MigLayout;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;



public class TroubleTicketGUI
{

	private static final int PADDING = 10;
	private static final int DEFAULT_DIVIDER_WIDTH = 150;
	
	private static final String NORTH = SpringLayout.NORTH;
	private static final String EAST = SpringLayout.EAST;
	private static final String SOUTH = SpringLayout.SOUTH;
	private static final String WEST = SpringLayout.WEST;

	private static final String BUTTON_LOGIN_NAME = "btnLogin";
	private static final String BUTTON_NEW_TICKET_NAME = "btnNewTicket";
	private static final String BUTTON_MODIFY_TICKET_NAME = "btnModifyTicket";
	private static final String BUTTON_SEARCH_NAME = "btnSearch";

	private static final String PANEL_RIGHT_CONTENT_NAME = "panelRightContent";
	
	
	
	
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
	
	private JFrame frame;
	private JTextField textField;

	private JButton btnLogin;
	private JButton btnNewTicket;
	private JButton btnModifyTicket;
	private JButton btnSearch;
	
	private JButton[] menuButtons = {btnLogin,btnNewTicket,btnModifyTicket,btnSearch};

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
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
		frame.setBounds(100, 100, 694, 518);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setName("splitPane");
		springLayout.putConstraint(NORTH, splitPane, 0, NORTH, frame.getContentPane());
		springLayout.putConstraint(WEST, splitPane, 0, WEST, frame.getContentPane());
		springLayout.putConstraint(SOUTH, splitPane, 480, NORTH, frame.getContentPane());
		springLayout.putConstraint(EAST, splitPane, 678, WEST, frame.getContentPane());
		frame.getContentPane().add(splitPane, splitPane.getName());
		
		JPanel panelLeft = new JPanel();
		splitPane.setLeftComponent(panelLeft);
		panelLeft.setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPaneLeft = new JSplitPane();
		splitPaneLeft.setOrientation(JSplitPane.VERTICAL_SPLIT);
		panelLeft.add(splitPaneLeft);
		JPanel panelMenu = new JPanel();
		splitPaneLeft.setRightComponent(panelMenu);
		SpringLayout sl_panelMenu = new SpringLayout();
		panelMenu.setLayout(sl_panelMenu);
		
		btnLogin = new JButton("Login");
		sl_panelMenu.putConstraint(SpringLayout.NORTH, btnLogin, 5, SpringLayout.NORTH, panelMenu);
		sl_panelMenu.putConstraint(SpringLayout.WEST, btnLogin, 10, SpringLayout.WEST, panelMenu);
		btnLogin.setName(BUTTON_LOGIN_NAME);
		panelMenu.add(btnLogin);

		btnNewTicket = new JButton("New Ticket");
		sl_panelMenu.putConstraint(SpringLayout.NORTH, btnNewTicket, 36, SpringLayout.NORTH, panelMenu);
		sl_panelMenu.putConstraint(SpringLayout.WEST, btnNewTicket, 0, SpringLayout.WEST, btnLogin);
		sl_panelMenu.putConstraint(SpringLayout.EAST, btnNewTicket, 0, SpringLayout.EAST, btnLogin);
		btnNewTicket.setName(BUTTON_NEW_TICKET_NAME);
		panelMenu.add(btnNewTicket);

		btnModifyTicket = new JButton("Modify Ticket");
		sl_panelMenu.putConstraint(SpringLayout.NORTH, btnModifyTicket, 67, SpringLayout.NORTH, panelMenu);
		sl_panelMenu.putConstraint(SpringLayout.WEST, btnModifyTicket, 0, SpringLayout.WEST, btnLogin);
		sl_panelMenu.putConstraint(SpringLayout.EAST, btnModifyTicket, 0, SpringLayout.EAST, btnLogin);
		btnModifyTicket.setName(BUTTON_MODIFY_TICKET_NAME);
		panelMenu.add(btnModifyTicket);

		btnSearch = new JButton("Search");
		sl_panelMenu.putConstraint(SpringLayout.EAST, btnLogin, 0, SpringLayout.EAST, btnSearch);
		sl_panelMenu.putConstraint(SpringLayout.NORTH, btnSearch, 98, SpringLayout.NORTH, panelMenu);
		sl_panelMenu.putConstraint(SpringLayout.WEST, btnSearch, 10, SpringLayout.WEST, panelMenu);
		sl_panelMenu.putConstraint(SpringLayout.EAST, btnSearch, -10, SpringLayout.EAST, panelMenu);
		btnSearch.setName(BUTTON_SEARCH_NAME);
		panelMenu.add(btnSearch);
		
		JPanel panelStatus = new JPanel();
		splitPaneLeft.setLeftComponent(panelStatus);
		
		JLabel lblCurrentlyLoggedIn = new JLabel("Currently logged in as:");
		panelStatus.add(lblCurrentlyLoggedIn);
		splitPaneLeft.setDividerLocation(200);
		
		

		


		JPanel panelRight = new JPanel();
		splitPane.setRightComponent(panelRight);
		SpringLayout sl_panelRight = new SpringLayout();
		panelRight.setLayout(sl_panelRight);

		JPanel panelRightContent = new JPanel();
		panelRightContent.setName(PANEL_RIGHT_CONTENT_NAME);
		sl_panelRight.putConstraint(NORTH, panelRightContent, 27, NORTH, panelRight);
		sl_panelRight.putConstraint(WEST, panelRightContent, 0, WEST, panelRight);
		sl_panelRight.putConstraint(SOUTH, panelRightContent, 478, NORTH, panelRight);
		sl_panelRight.putConstraint(EAST, panelRightContent, 517, WEST, panelRight);
		panelRight.add(panelRightContent, panelRightContent.getName());
		panelRightContent.setLayout(new CardLayout(0, 0));
		
		JPanel panelLogin = new JPanel();
		panelLogin.setName("panelLogin");
		panelRightContent.add(panelLogin, panelLogin.getName());
		SpringLayout sl_panelLogin = new SpringLayout();
		panelLogin.setLayout(sl_panelLogin);
		
		JPanel panelNewTicket = new JPanel();
		panelNewTicket.setName("panelNewTicket");
		panelRightContent.add(panelNewTicket, panelNewTicket.getName());
		SpringLayout sl_panelNewTicket = new SpringLayout();
		panelNewTicket.setLayout(sl_panelNewTicket);
		
		JPanel panelSearch = new JPanel();
		panelSearch.setName("panelSearch");
		panelRightContent.add(panelSearch, panelSearch.getName());
		SpringLayout sl_panelSearch = new SpringLayout();
		panelSearch.setLayout(sl_panelSearch);
		
		JButton btnLogin_1 = new JButton("lawg in???");
		sl_panelLogin.putConstraint(NORTH, btnLogin_1, 171, NORTH, panelLogin);
		sl_panelLogin.putConstraint(WEST, btnLogin_1, 135, WEST, panelLogin);
		panelLogin.add(btnLogin_1);
		
		textField = new JTextField();
		textField.setToolTipText("username");
		sl_panelLogin.putConstraint(SpringLayout.NORTH, textField, 120, SpringLayout.NORTH, panelLogin);
		sl_panelLogin.putConstraint(SpringLayout.WEST, textField, 10, SpringLayout.WEST, panelLogin);
		panelLogin.add(textField);
		textField.setColumns(10);
		
		JLabel lblUsername = new JLabel("Username");
		sl_panelLogin.putConstraint(SpringLayout.WEST, lblUsername, 41, SpringLayout.WEST, panelLogin);
		sl_panelLogin.putConstraint(SpringLayout.SOUTH, lblUsername, -6, SpringLayout.NORTH, textField);
		panelLogin.add(lblUsername);
		
		JPanel panelRightTop = new JPanel();
		panelRightTop.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		sl_panelRight.putConstraint(NORTH, panelRightTop, 0, NORTH, panelRight);
		sl_panelRight.putConstraint(WEST, panelRightTop, 0, WEST, panelRightContent);
		sl_panelRight.putConstraint(SOUTH, panelRightTop, 0, NORTH, panelRightContent);
		sl_panelRight.putConstraint(EAST, panelRightTop, 0, EAST, panelRightContent);
		panelRight.add(panelRightTop);
		
		JLabel lblCurrentPane = new JLabel("THIS_SHOULD_BE_AUTO_REPLACED");
		panelRightTop.add(lblCurrentPane);
		sl_panelNewTicket.putConstraint(NORTH, lblCurrentPane, 10, NORTH, panelNewTicket);
		sl_panelNewTicket.putConstraint(EAST, lblCurrentPane, -230, EAST, panelNewTicket);
		
		JButton btnNewTiket = new JButton("new TIKET??");
		btnNewTiket.setName("btnNewTiket");
		sl_panelNewTicket.putConstraint(NORTH, btnNewTiket, 127, NORTH, panelNewTicket);
		sl_panelNewTicket.putConstraint(WEST, btnNewTiket, 93, WEST, panelNewTicket);
		panelNewTicket.add(btnNewTiket, btnNewTiket.getName());
		

		
		JButton btnSurch = new JButton("surch???");
		sl_panelSearch.putConstraint(SpringLayout.NORTH, btnSurch, 178, SpringLayout.NORTH, panelSearch);
		sl_panelSearch.putConstraint(SpringLayout.EAST, btnSurch, -177, SpringLayout.EAST, panelSearch);
		panelSearch.add(btnSurch, "btnSurch");
		
		JPanel panelModifyTicket = new JPanel();
		panelModifyTicket.setName("panelModifyTicket");
		panelRightContent.add(panelModifyTicket, panelModifyTicket.getName());
		SpringLayout springLayout_1 = new SpringLayout();
		panelModifyTicket.setLayout(springLayout_1);
		
		JButton btnModify = new JButton("modify???");
		springLayout_1.putConstraint(SpringLayout.NORTH, btnModify, 184, SpringLayout.NORTH, panelModifyTicket);
		springLayout_1.putConstraint(SpringLayout.WEST, btnModify, 112, SpringLayout.WEST, panelModifyTicket);
		panelModifyTicket.add(btnModify);
		splitPane.setDividerLocation(DEFAULT_DIVIDER_WIDTH);
		

		btnLogin.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JPanel panel = panelLogin;
				
				CardLayout cl = (CardLayout) panelRightContent.getLayout();
				JLabel label = lblCurrentPane;
				JButton button = (JButton)e.getSource();
				
				println("Someone clicked button \'" + button.getName() +"\' or \'" + button.getText() + "\'");
				printf("About to do cl.show(\"%s\", \"%s\")\n",panelRightContent.getName(),panel.getName());
				
				cl.show(panelRightContent, panel.getName());
				label.setText(button.getText());
			}
		});
		
		btnModifyTicket.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JPanel panel = panelModifyTicket;

				CardLayout cl = (CardLayout) panelRightContent.getLayout();
				JLabel label = lblCurrentPane;
				JButton button = (JButton)e.getSource();
				
				println("Someone clicked button \'" + button.getName() +"\' or \'" + button.getText() + "\'");
				printf("About to do cl.show(\"%s\", \"%s\")\n",panelRightContent.getName(),panel.getName());
				
				cl.show(panelRightContent, panel.getName());
				label.setText(button.getText());
			}
		});
		
		btnSearch.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JPanel panel = panelSearch;
				
				CardLayout cl = (CardLayout) panelRightContent.getLayout();
				JLabel label = lblCurrentPane;
				JButton button = (JButton)e.getSource();
				
				println("Someone clicked button \'" + button.getName() +"\' or \'" + button.getText() + "\'");
				printf("About to do cl.show(\"%s\", \"%s\")\n",panelRightContent.getName(),panel.getName());
				
				cl.show(panelRightContent, panel.getName());
				label.setText(button.getText());
			}
		});
		
		btnNewTicket.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JPanel panel = panelNewTicket;
				
				CardLayout cl = (CardLayout) panelRightContent.getLayout();
				JLabel label = lblCurrentPane;
				JButton button = (JButton)e.getSource();
				
				println("Someone clicked button \'" + button.getName() +"\' or \'" + button.getText() + "\'");
				printf("About to do cl.show(\"%s\", \"%s\")\n",panelRightContent.getName(),panel.getName());
				
				cl.show(panelRightContent, panel.getName());
				label.setText(button.getText());
			}
		});
		
		btnLogin.doClick(); //to setup the top text
	}
}
