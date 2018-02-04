package converter.ui;

import javax.swing.JDialog;
import javax.swing.JFrame;

public class GuiSectionWindow extends JDialog{

	private ModelScene scene;
	
	public GuiSectionWindow(ModelScene scene){
		super((JFrame)null, false);
		this.scene = scene;
		this.setTitle("Title");
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setSize(800,600);
		this.setVisible(true);
	}
}
