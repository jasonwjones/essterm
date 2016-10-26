package com.jasonwjones.test.dialogs;

import java.util.List;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.ComboBox;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.GridLayout.Alignment;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LayoutData;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.RadioBoxList;
import com.googlecode.lanterna.gui2.RadioBoxList.Listener;
import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import com.jasonwjones.test.models.ChosenConnection;

public class ConnectionDialogWindow extends DialogWindow {

	private ComboBox<String> serverComboBox;
	
	private TextBox usernameTextBox;
	
	private TextBox passwordTextBox;
	
	private String server;
	
	private RadioBoxList<String> applicationsListBox;
	
	private RadioBoxList<String> cubesListBox;
	
	private Button loginButton;
	
	private ConnectionDialogModel model;
	
	private ConnectionWindowDataSource dataSource;
	
	public void setDataSource(ConnectionWindowDataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public ConnectionDialogWindow(String title) {
		super(title);
		
		model = new ConnectionDialogModel();
		setCloseWindowWithEscape(true);
		
		serverComboBox = new ComboBox<String>("Item 1", "item 2");
		serverComboBox.setReadOnly(false);
		serverComboBox.setPreferredSize(new TerminalSize(40, 1));
		// skip listener on combo box since it can take arbitrary text
				
		loginButton = new Button("Login", new Runnable() {
			public void run() {
				System.out.println("Let's login");
				model.setServer(serverComboBox.getText());
				
				applicationsListBox.clearItems();
				applicationsListBox.clearSelection();
				for (String application : dataSource.getApplications(model.getServer(), "admin", "password")) {
					applicationsListBox.addItem(application);
				}
				
				
			}});
		
		//GridLayout gridLayout = new GridLayout(2);
		//gridLayout.setVerticalSpacing(1);
		
		usernameTextBox = new TextBox();
		passwordTextBox = new TextBox();

		
		Panel panel = new Panel();
		
		// right side panel
		Panel rightPanel = new Panel();
		
		cubesListBox = new RadioBoxList<String>(new TerminalSize(20, 5));
//		cubes.addItem("Cube 1");
//		cubes.addItem("Cube 2");
		
		rightPanel.addComponent(cubesListBox.withBorder(Borders.singleLine("Cubes")));
		
		applicationsListBox = new RadioBoxList<String>(new TerminalSize(20, 5));
		applicationsListBox.addListener(new Listener() {

			@Override
			public void onSelectionChanged(int selectedIndex, int previousSelection) {
				System.out.println("Selected app " + applicationsListBox.getSelectedItem());
				String application = applicationsListBox.getSelectedItem();
				
				cubesListBox.clearItems();
				for (String cube : dataSource.getCubes("sever", "username", "password", application)) {
					cubesListBox.addItem(cube);
				};
				cubesListBox.takeFocus();
			}
		});
		
		Panel containerPanel = new Panel(new GridLayout(2));
		
		Panel topPanel = new Panel();
		LayoutData layoutData = GridLayout.createLayoutData(Alignment.FILL, Alignment.BEGINNING, true, false, 2, 1);
		topPanel.setLayoutData(layoutData);
		
		
		Panel innerTopPanel = new Panel(new GridLayout(2).setVerticalSpacing(1).setTopMarginSize(1).setBottomMarginSize(1));
		
		innerTopPanel.addComponent(new Label("Server"));
		innerTopPanel.addComponent(serverComboBox);
		
		innerTopPanel.addComponent(new Label("Password"));
		innerTopPanel.addComponent(passwordTextBox);

		innerTopPanel.addComponent(new Label("Username"));
		innerTopPanel.addComponent(usernameTextBox);
//.setLayoutData(GridLayout.createLayoutData(Alignment.BEGINNING, Alignment.CENTER, true, false))
		
		innerTopPanel.addComponent(loginButton);

		topPanel.addComponent(innerTopPanel);
				

		
		Panel leftPanel = new Panel();
		leftPanel.addComponent(applicationsListBox.withBorder(Borders.singleLine("Applications")));
		
		containerPanel.addComponent(topPanel);
		containerPanel.addComponent(leftPanel);
		containerPanel.addComponent(rightPanel);
		
		setComponent(containerPanel);
	}

    @Override
    public ChosenConnection showDialog(WindowBasedTextGUI textGUI) {
        //selectedFile = null;
        super.showDialog(textGUI);
        return new ChosenConnection();
    }
	
//	public static void main(String[] args) throws Exception {
//	    // Setup terminal and screen layers
//	    Terminal terminal = new DefaultTerminalFactory().createTerminal();
//	    Screen screen = new TerminalScreen(terminal);
//	    screen.startScreen();
//
//	    // Create window to hold the panel
//	    BasicWindow window = new BasicWindow();
//	    
//	    //window.setComponent();
//	    
//	    // Create gui and start gui
//	    MultiWindowTextGUI gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));
//	    gui.addWindowAndWait(new ConnectionDialogWindow("Connect"));
//	    
//	}
	
    public interface ConnectionWindowDataSource {
    	
    	public List<String> getApplications(String server, String username, String password);
    	
    	public List<String> getCubes(String server, String username, String password, String application);
    	
    }
    
	public static interface ConnectionDialogWindowDelegate {
		
		//spublic void connect(String server, String username, String password);
		
		public List<String> getApplications(String server, String username, String password);
		
		public List<String> getCubes(String application);
		
		public void choseCube(String server, String username, String password, String application, String cube);
		
	}
	
	public static class ConnectionDialogModel {
		
		private String server;

		public String getServer() {
			return server;
		}

		public void setServer(String server) {
			this.server = server;
			System.out.println("Model server is now: " + server);
		}
		
	}
	
}
