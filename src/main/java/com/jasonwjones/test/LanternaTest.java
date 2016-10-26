package com.jasonwjones.test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.CheckBoxList;
import com.googlecode.lanterna.gui2.DefaultWindowManager;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.jasonwjones.test.dialogs.AdhocOptionsDialogWindow;

public class LanternaTest {

	//private Table<String> table;
	
	private MultiWindowTextGUI gui;
	
	public static void main(String[] args) throws IOException {
		new LanternaTest();
	    
//	    Table<String> table = new Table<String>("Column 1", "Column 2", "Column 3");
//	    table.getTableModel().addRow("1", "2", "3");

	    //new LanternaTest();
	    
//	    table.setSelectAction(new Runnable() {
//	        @Override
//	        public void run() {
//	            List<String> data = table.getTableModel().getRow(table.getSelectedRow());
//	            for(int i = 0; i < data.size(); i++) {
//	                System.out.println(data.get(i));
//	            }
//	        }
//	    });
	    
	}
	
	public LanternaTest() throws IOException {
		
	    // Setup terminal and screen layers
	    Terminal terminal = new DefaultTerminalFactory().createTerminal();
	    Screen screen = new TerminalScreen(terminal);
	    screen.startScreen();

	    // Create window to hold the panel
	    BasicWindow window = new BasicWindow();

	    //window.setHints(Arrays.asList(Window.Hint.FULL_SCREEN));
	   
	    final EssTable<String> table = new EssTable<String>("Column 1", "Column 2", "Column 3", "Column 4 Has This Long Title", "And another pretty long title");
	    table.setCellSelection(true);
	    table.getTableModel().addRow("1", "2", "3", "4", "5");
	    table.getTableModel().addRow("a1", "2", "3", "4", "5");
	   
	    //window.setSize(TerminalSize.);
	    
	    table.setSelectAction(new Runnable() {
	        //@Override
	        public void run() {
	            List<String> data = table.getTableModel().getRow(table.getSelectedRow());
	            for(int i = 0; i < data.size(); i++) {
	                System.out.println(data.get(i));
	            }
//                String result = new TextInputDialogBuilder()
//                        .setTitle("Multi-line editor")
//                        .setTextBoxSize(new TerminalSize(35, 5))
//                        .build()
//                        .showDialog(gui);
//                System.out.println("Result: " + result);

	            BasicWindow window2 = new BasicWindow("Options");
	            Panel optionsPanel = new Panel();
	            
	            Panel checkPanel = new Panel();
	            
	            TerminalSize size = new TerminalSize(14, 2);
	            CheckBoxList<String> checkBoxList = new CheckBoxList<String>(size);

                checkBoxList.addItem("item 1");
                checkBoxList.addItem("item 2");
                checkBoxList.addItem("item 3");
	            
                checkPanel.addComponent(checkBoxList);
                
                optionsPanel.addComponent(checkPanel.withBorder(Borders.singleLine("Right Panel")));
                window2.setComponent(optionsPanel);
	            window2.setHints(Arrays.asList(Window.Hint.EXPANDED, Window.Hint.CENTERED));
	            gui.addWindowAndWait(window2);
	        }
	    });
	    
	    table.setVisibleColumns(3);
	    
	    Panel panel = new Panel();
	    
	    Panel rightPanel = new Panel();
	    panel.addComponent(rightPanel.withBorder(Borders.singleLine("Right Panel")));
	    rightPanel.addComponent(new Button("Enter", new Runnable() {
			public void run() {
				gui.addWindowAndWait(new AdhocOptionsDialogWindow());
			}}));
	    
	    panel.addComponent(table);
	    
	    window.setComponent(panel);
	    
	    // Create gui and start gui
	    gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));
	    gui.addWindowAndWait(window);
		
	}
	
	public static void main2(String[] args) throws IOException {
        // Setup terminal and screen layers
        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        Screen screen = new TerminalScreen(terminal);
        screen.startScreen();

        // Create panel to hold components
        Panel panel = new Panel();
        panel.setLayoutManager(new GridLayout(2));

        panel.addComponent(new Label("Forename"));
        panel.addComponent(new TextBox());

        panel.addComponent(new Label("Surname"));
        panel.addComponent(new TextBox());

        panel.addComponent(new EmptySpace(new TerminalSize(0,0))); // Empty space underneath labels
        panel.addComponent(new Button("Submit"));

        // Create window to hold the panel
        BasicWindow window = new BasicWindow();
        window.setComponent(panel);

        // Create gui and start gui
        MultiWindowTextGUI gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));
        gui.addWindowAndWait(window);
	}

}
