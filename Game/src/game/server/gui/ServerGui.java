package game.server.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import game.server.Server;
import game.server.io.IStreamListener;
import game.server.io.Logger;

public class ServerGui extends Thread {

	private static final int MAX_COMMANDS = 10;
	
	private JFrame frame;

	private JSplitPane mainPanel;

	private JPanel consolePanel;
	private JTabbedPane tabbedPane;

	private JTextArea allTextArea;
	private JTextArea infoTextArea;
	private JTextArea errorTextArea;
	private JTextArea chatTextArea;

	private JPanel commandPanel;
	private JTextField commandField;
	private JButton commandButton;

	private JPanel statusPanel;
	
	private JPanel playerPanel;
	private JList<String> playerList;
	private DefaultListModel<String> playerListModel;

	private static volatile boolean isInitialized = false;

	private LinkedList<String> commandsEntered = new LinkedList<String>();
	private int currentCommandPosition = -1;
	
	private ServerGui(){}

	@Override
	public void run(){
		init();
	}

	private void init(){
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000, 600);
		frame.setLocationRelativeTo(null);
		frame.setTitle("Game Server 0.0.1");

		SpringLayout mainLayout = new SpringLayout();

		initLogPanel(mainLayout);
		initStatusPanel(mainLayout);

		JScrollPane statusScrollPane = new JScrollPane(statusPanel);
		
		mainPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, statusScrollPane, consolePanel);
		mainPanel.setOneTouchExpandable(true);
		mainPanel.setDividerSize(3);
		mainPanel.setDividerLocation(324);
		frame.add(mainPanel);

		frame.setVisible(true);

		Logger.addInfoStreamListener(new GuiStreamListener(allTextArea, infoTextArea));
		Logger.addErrorStreamListener(new GuiStreamListener(allTextArea, errorTextArea));

		isInitialized = true;
	}

	private void initLogPanel(SpringLayout mainLayout){

		consolePanel = new JPanel();
		consolePanel.setPreferredSize(new Dimension(400,400));
		consolePanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

		tabbedPane = new JTabbedPane();
		tabbedPane.setPreferredSize(new Dimension(400, 370));
		Font f = new Font("arial", Font.PLAIN, 14);
		tabbedPane.setFont(f);

		allTextArea = new JTextArea();
		allTextArea.setEditable(false);
		infoTextArea = new JTextArea();
		infoTextArea.setEditable(false);
		errorTextArea = new JTextArea();
		errorTextArea.setEditable(false);
		chatTextArea = new JTextArea();
		chatTextArea.setEditable(false);

		JScrollPane scrollAll = new JScrollPane(allTextArea);
		JScrollPane scrollInfo = new JScrollPane(infoTextArea);
		JScrollPane scrollError = new JScrollPane(errorTextArea);
		JScrollPane scrollChat = new JScrollPane(chatTextArea);

		tabbedPane.addTab("All", scrollAll);
		tabbedPane.addTab("Info", scrollInfo);
		tabbedPane.addTab("Error", scrollError);
		tabbedPane.addTab("Chat", scrollChat);

		commandPanel = new JPanel();
		commandPanel.setPreferredSize(new Dimension(400, 30));

		Action actioncommand = new ActionSubmitCommand();

		commandField = new JTextField();
		commandField.setPreferredSize(new Dimension(350, 30));
		commandField.addActionListener(actioncommand);
		commandField.addKeyListener(new CommandHistoryListener());

		commandButton = new JButton("Send");
		commandButton.setPreferredSize(new Dimension(80, 30));
		commandButton.addActionListener(actioncommand);

		SpringLayout consoleLayout = new SpringLayout();

		consoleLayout.putConstraint(SpringLayout.WEST, tabbedPane, 0, SpringLayout.WEST, consolePanel);
		consoleLayout.putConstraint(SpringLayout.NORTH, tabbedPane, 4, SpringLayout.NORTH, consolePanel);
		consoleLayout.putConstraint(SpringLayout.EAST, tabbedPane, 0, SpringLayout.EAST, consolePanel);

		consoleLayout.putConstraint(SpringLayout.WEST, commandPanel, 0, SpringLayout.WEST, consolePanel);
		consoleLayout.putConstraint(SpringLayout.SOUTH, commandPanel, 0, SpringLayout.SOUTH, consolePanel);
		consoleLayout.putConstraint(SpringLayout.EAST, commandPanel, 0, SpringLayout.EAST, consolePanel);

		consoleLayout.putConstraint(SpringLayout.SOUTH, tabbedPane, -2, SpringLayout.NORTH, commandPanel);

		consolePanel.setLayout(consoleLayout);

		SpringLayout commandLayout = new SpringLayout();

		commandLayout.putConstraint(SpringLayout.WEST, commandField, 0, SpringLayout.WEST, commandPanel);
		commandLayout.putConstraint(SpringLayout.NORTH, commandField, 0, SpringLayout.NORTH, commandPanel);
		commandLayout.putConstraint(SpringLayout.SOUTH, commandField, 0, SpringLayout.SOUTH, commandPanel);

		commandLayout.putConstraint(SpringLayout.EAST, commandButton, 0, SpringLayout.EAST, commandPanel);
		commandLayout.putConstraint(SpringLayout.NORTH, commandButton, 0, SpringLayout.NORTH, commandPanel);
		commandLayout.putConstraint(SpringLayout.SOUTH, commandButton, 0, SpringLayout.SOUTH, commandPanel);

		commandLayout.putConstraint(SpringLayout.EAST, commandField, 0, SpringLayout.WEST, commandButton);

		commandPanel.setLayout(commandLayout);

		commandPanel.add(commandField);
		commandPanel.add(commandButton);

		consolePanel.add(tabbedPane);
		consolePanel.add(commandPanel);

		mainLayout.putConstraint(SpringLayout.WEST, consolePanel, 2, SpringLayout.WEST, mainPanel);
		mainLayout.putConstraint(SpringLayout.SOUTH, consolePanel, -2, SpringLayout.SOUTH, mainPanel);
		mainLayout.putConstraint(SpringLayout.NORTH, consolePanel, 4, SpringLayout.NORTH, mainPanel);

		//mainPanel.add(consolePanel);
	}

	private void initStatusPanel(SpringLayout mainLayout){

		SpringLayout statusLayout = new SpringLayout();
		
		statusPanel = new JPanel(statusLayout);
		statusPanel.setPreferredSize(new Dimension(300, 200));
		statusPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

		mainLayout.putConstraint(SpringLayout.EAST, consolePanel, -4, SpringLayout.WEST, statusPanel);
		mainLayout.putConstraint(SpringLayout.SOUTH, statusPanel, 0, SpringLayout.SOUTH, mainPanel);
		mainLayout.putConstraint(SpringLayout.EAST, statusPanel, 0, SpringLayout.EAST, mainPanel);
		mainLayout.putConstraint(SpringLayout.NORTH, statusPanel, 0, SpringLayout.NORTH, mainPanel);
		
		playerPanel = new JPanel();
		playerPanel.setPreferredSize(new Dimension(300, 200));
		playerPanel.setBorder(BorderFactory.createTitledBorder("Players"));
		playerPanel.setLayout(new BorderLayout());
		
		playerList = new JList<String>();
		playerListModel = new DefaultListModel<String>();
		playerList.setModel(playerListModel);
		playerListModel.addElement("Player");
		playerListModel.addElement("Hello");
		playerList.setPreferredSize(new Dimension(280, 300));
		playerList.setBorder(BorderFactory.createEtchedBorder());
		
		statusLayout.putConstraint(SpringLayout.NORTH, playerPanel, 0, SpringLayout.NORTH, statusPanel);
		statusLayout.putConstraint(SpringLayout.EAST, playerPanel, 0, SpringLayout.EAST, statusPanel);
		statusLayout.putConstraint(SpringLayout.WEST, playerPanel, 0, SpringLayout.WEST, statusPanel);
		
		playerPanel.add(playerList, BorderLayout.CENTER);
		
		statusPanel.add(playerPanel);
	}

	public static void initGui(){
		ServerGui gui = new ServerGui();
		gui.setName("Server-Gui");
		gui.start();
	}

	public static synchronized boolean isInitialzied(){
		return isInitialized;
	}

	private static class GuiStreamListener implements IStreamListener{

		private JTextArea[] textAreas;

		private GuiStreamListener(JTextArea... textAreas){
			this.textAreas = textAreas;
		}

		@Override
		public void onWrite(byte[] bytes) {
			onWrite(bytes, 0, bytes.length);
		}

		@Override
		public void onWrite(int b) {
			char c = (char) (b & 0xFF);
			for(JTextArea textArea : textAreas)
				textArea.append(String.valueOf(c));
		}

		@Override
		public void onWrite(byte[] bytes, int off, int len) {
			String text = "";
			for(int i = off; i < len; i++){
				char c = (char) (bytes[i] & 0xFF);
				text = text + c;
			}

			for(JTextArea textArea : textAreas)
				textArea.append(text);
		}
	}

	@SuppressWarnings("serial")
	private class ActionSubmitCommand extends AbstractAction {

		@Override
		public void actionPerformed(ActionEvent e) {
			String command = commandField.getText();
			
			if(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()).equals("Chat")){
				command = "chat " + command;
			}
			
			Server.INSTANCE.addCommand(command);
			commandField.setText("");
			commandsEntered.addFirst(command);
			currentCommandPosition = -1;
			if(commandsEntered.size() > MAX_COMMANDS){
				commandsEntered.removeLast();
			}
		}
	}

	private class CommandHistoryListener implements KeyListener {

		private String currentTyped = "";
		
		@Override
		public void keyPressed(KeyEvent e) {
			
			if(commandsEntered.size() == 0){
				return;
			}
			
			boolean flag = false;
			JTextField source = (JTextField) e.getSource();
			if(e.getKeyCode() == KeyEvent.VK_UP){
				int max = Math.min(commandsEntered.size(), MAX_COMMANDS);
				if(currentCommandPosition == -1){
					currentTyped = source.getText();
				}
				if(currentCommandPosition < max - 1){
					currentCommandPosition++;
					flag = true;
				}
			}
			else if(e.getKeyCode() == KeyEvent.VK_DOWN){
				if(currentCommandPosition > 0){
					currentCommandPosition--;
					flag = true;
				}
				else if(currentCommandPosition == 0){
					currentCommandPosition = -1;
					source.setText(currentTyped);
					currentTyped = "";
				}
			}
			
			if(flag){
				String text = commandsEntered.get(currentCommandPosition);
				source.setText(text);
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {}

		@Override
		public void keyTyped(KeyEvent e) {}

	}
}
