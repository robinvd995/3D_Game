package converter.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import converter.api.OIMModelLoader;
import converter.api.model.IndexedModel;
import converter.io.IOManager;
import converter.model.RawModel;
import converter.model.modifier.ModelModifier;

public class MenuManager implements ActionListener{
	
	private final ModelScene scene;
	
	public MenuManager(ModelScene scene){
		this.scene = scene;
	}
	
	public void addMenuBar(JFrame frame){
		
		JMenuBar menuBar = new JMenuBar();
		
		JMenu menuFile = new JMenu("File");
		JMenu menuModel = new JMenu("Model");
		
		JMenuItem itemNew = createMenuItem(EnumAction.NEW);
		JMenuItem itemOpen = createMenuItem(EnumAction.OPEN);
		JMenuItem itemSave = createMenuItem(EnumAction.SAVE);
		JMenuItem itemExit = createMenuItem(EnumAction.EXIT);
		
		JMenuItem itemSections = createMenuItem(EnumAction.SECTIONS);
		
		menuFile.add(itemNew);
		menuFile.addSeparator();
		menuFile.add(itemOpen);
		menuFile.add(itemSave);
		menuFile.addSeparator();
		menuFile.add(itemExit);
		
		menuModel.add(itemSections);
		
		menuBar.add(menuFile);
		menuBar.add(menuModel);
		
		frame.setJMenuBar(menuBar);
	}

	@Override
	public void actionPerformed(ActionEvent evnt) {
		EnumAction action = EnumAction.getActionFromCommand(evnt.getActionCommand());
		switch(action){
		case NEW:
			
			break;
			
		case OPEN:
			openFile();
			break;
			
		case SAVE:
			saveFile();
			break;
			
		case EXIT:
			
			break;
		case TEXTURE:
			String[] args = evnt.getActionCommand().split(".")[0].split("/");
			scene.addModifier(new ModelModifier(args[0], args[1]));
			break;
			
		case SECTIONS:
			JDialog dialog = new GuiSectionWindow(null);
			break;
		}
	}
	
	private void openFile(){
		JFileChooser chooser = new JFileChooser();
		int result = chooser.showOpenDialog(null);
		if(result == JFileChooser.APPROVE_OPTION){
			File file = chooser.getSelectedFile();
			if(file.getAbsolutePath().endsWith(".oim")){
				IndexedModel model = OIMModelLoader.loadModel(file);
				scene.loadModel(model);
			}
			else{
				RawModel model = IOManager.loadRawModel(file);
				scene.loadModel(model);
			}
		}
	}
	
	private void saveFile(){
		JFileChooser chooser = new JFileChooser();
		int result = chooser.showOpenDialog(null);
		if(result == JFileChooser.APPROVE_OPTION){
			File file = chooser.getSelectedFile();
			IOManager.exportModel(scene.getModel(), file);
		}
	}
	
	private JMenuItem createMenuItem(EnumAction action){
		JMenuItem item = new JMenuItem(action.getActionName());
		item.setActionCommand(action.getActionCommand());
		item.addActionListener(this);
		return item;
	}
}
