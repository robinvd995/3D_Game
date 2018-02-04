package converter.ui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import converter.ui.component.GUICModelModifierTexture;

public class MainUI implements WindowListener, ComponentListener{

	public static final String MAIN_TITLE = "Model Converter";

	public static final int MAIN_WIDTH = 1200;
	public static final int MAIN_HEIGHT = 900;

	private ModelScene scene;

	private MenuManager menuManager;

	private JFrame frame;

	private Thread glThread;
	private boolean isRunning = false;
	
	private Canvas canvas;

	public MainUI(){
		scene = new ModelScene();
		menuManager = new MenuManager(scene);
	}

	public void init() throws LWJGLException{

		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

		int monitorWidth = gd.getDisplayMode().getWidth();
		int monitorHeight = gd.getDisplayMode().getHeight();

		frame = new JFrame();
		frame.setTitle(MAIN_TITLE);
		frame.setBounds((monitorWidth - MAIN_WIDTH) / 2, (monitorHeight - MAIN_HEIGHT) / 2, MAIN_WIDTH, MAIN_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(this);

		menuManager.addMenuBar(frame);

		canvas = new Canvas() {
			@Override
			public void addNotify() {
				super.addNotify();
				startGLThread();
			}

			@Override
			public void removeNotify() {
				stopGLThread();
				super.removeNotify();
			}
		};
		canvas.setPreferredSize(new Dimension(900, 800));
		canvas.addComponentListener(this);
		//canvas.setIgnoreRepaint(true);

		SpringLayout layout = new SpringLayout();
		JPanel mainPanel = new JPanel(layout);
		mainPanel.setBackground(Color.BLACK);
		
		JPanel sidePanel = new JPanel();
		sidePanel.setPreferredSize(new Dimension(300, 800));
		
		layout.putConstraint(SpringLayout.EAST, sidePanel, 0, SpringLayout.EAST, mainPanel);
		layout.putConstraint(SpringLayout.NORTH, sidePanel, 0, SpringLayout.NORTH, mainPanel);
		layout.putConstraint(SpringLayout.SOUTH, sidePanel, 0, SpringLayout.SOUTH, mainPanel);
		
		layout.putConstraint(SpringLayout.EAST, canvas, 0, SpringLayout.WEST, sidePanel);
		layout.putConstraint(SpringLayout.WEST, canvas, 0, SpringLayout.WEST, mainPanel);
		layout.putConstraint(SpringLayout.NORTH, canvas, 0, SpringLayout.NORTH, mainPanel);
		layout.putConstraint(SpringLayout.SOUTH, canvas, 0, SpringLayout.SOUTH, mainPanel);

		sidePanel.setLayout(new FlowLayout());
		addComponentPanel(sidePanel);
		
		mainPanel.add(canvas);
		mainPanel.add(sidePanel);

		frame.getContentPane().add(mainPanel);

		try {
			Display.setParent(canvas);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		//frame.pack();
		frame.setVisible(true);
	}

	private void addComponentPanel(JPanel sidePanel){
		
		GUICModelModifierTexture mmt = new GUICModelModifierTexture(scene);
		
		int panelHeight = mmt.getPanelHeight() + 2;
		
		JPanel mainPanel = new JPanel();
		mainPanel.setPreferredSize(new Dimension(300, panelHeight + 22));
		mainPanel.setBorder(BorderFactory.createTitledBorder("Texture"));
		SpringLayout layout = new SpringLayout();
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(2, panelHeight));
		mmt.initModifierGui(panel);
		
		layout.putConstraint(SpringLayout.WEST, panel, 0, SpringLayout.WEST, mainPanel);
		layout.putConstraint(SpringLayout.EAST, panel, 0, SpringLayout.EAST, mainPanel);
		layout.putConstraint(SpringLayout.NORTH, panel, 0, SpringLayout.NORTH, mainPanel);
		layout.putConstraint(SpringLayout.SOUTH, panel, 0, SpringLayout.SOUTH, mainPanel);
		mainPanel.setLayout(layout);
		
		mainPanel.add(panel);
		sidePanel.add(mainPanel);
	}
	
	@Override
	public void windowClosing(WindowEvent evnt) {
		int result = JOptionPane.showConfirmDialog(frame, "Do you want to quit the Application?");
		if(result == JOptionPane.OK_OPTION){
			frame.setVisible(false);
			frame.dispose();
		}
	}

	@Override
	public void componentResized(ComponentEvent e) {
		int width = e.getComponent().getWidth();
		int height = e.getComponent().getHeight();
		scene.setBounds(width, height);
		canvas.repaint();
	}
	
	private void startGLThread(){
		glThread = new Thread(new Runnable() {
			@Override
			public void run() {
				isRunning = true;
				ContextAttribs attribs = new ContextAttribs(3,2).withForwardCompatible(true).withProfileCore(true);
				try {
					Display.setDisplayMode(new DisplayMode(800, 600));
					Display.create(new PixelFormat(), attribs);
				} catch (LWJGLException e) {
					e.printStackTrace();
				}

				scene.initOpenGL();

				while(isRunning) {
					scene.renderScene();
					Display.sync(60);
					Display.update();
					canvas.repaint();
				}

				Display.destroy();
			}
		}, "LWJGL Thread");

		glThread.start();
	}

	private void stopGLThread(){
		isRunning = false;
		try {
			glThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/*-----Start WindowListener-----*/
	@Override
	public void windowActivated(WindowEvent evnt) {}

	@Override
	public void windowClosed(WindowEvent evnt) {}

	@Override
	public void windowDeactivated(WindowEvent evnt) {}

	@Override
	public void windowDeiconified(WindowEvent evnt) {}

	@Override
	public void windowIconified(WindowEvent evnt) {}

	@Override
	public void windowOpened(WindowEvent evnt) {}
	/*-----End WindowListener-----*/
	
	/*-----Start ComponentListener-----*/
	@Override
	public void componentHidden(ComponentEvent arg0) {}

	@Override
	public void componentMoved(ComponentEvent arg0) {}

	@Override
	public void componentShown(ComponentEvent arg0) {}
	/*-----End ComponentListener-----*/
}
