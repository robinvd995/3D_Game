package converter.ui.component;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import converter.model.modifier.ModelModifier;
import converter.ui.ModelScene;

public class GUICModelModifierTexture extends GUICModelModifier{

	public GUICModelModifierTexture(ModelScene scene) {
		super(scene, new ModelModifier("Texture", "texture"));
	}

	@Override
	public void initModifierGui(JPanel panel) {
		JTextField textField = new JTextField();
		textField.setPreferredSize(new Dimension(240, 24));
		JButton browse = new JButton("...");
		browse.setPreferredSize(new Dimension(60, 24));
		JButton apply = new JButton("Apply");
		apply.setPreferredSize(new Dimension(150, 24));
		JButton remove = new JButton("Remove");
		remove.setPreferredSize(new Dimension(150, 24));
		
		SpringLayout layout = new SpringLayout();
		//textfield constraints
		layout.putConstraint(SpringLayout.NORTH, textField, 0, SpringLayout.NORTH, panel);
		layout.putConstraint(SpringLayout.WEST, textField, 0, SpringLayout.WEST, panel);
		
		//BrowseConstraints
		layout.putConstraint(SpringLayout.NORTH, browse, 0, SpringLayout.NORTH, panel);
		layout.putConstraint(SpringLayout.EAST, browse, 0, SpringLayout.EAST, panel);
		layout.putConstraint(SpringLayout.WEST, browse, 0, SpringLayout.EAST, textField);
		
		layout.putConstraint(SpringLayout.NORTH, apply, 0, SpringLayout.SOUTH, textField);
		layout.putConstraint(SpringLayout.WEST, apply, 0, SpringLayout.WEST, panel);
		
		layout.putConstraint(SpringLayout.NORTH, remove, 0, SpringLayout.SOUTH, textField);
		layout.putConstraint(SpringLayout.EAST, remove, 0, SpringLayout.EAST, panel);
		layout.putConstraint(SpringLayout.WEST, remove, 0, SpringLayout.EAST, apply);
		
		panel.setLayout(layout);
		panel.add(textField);
		panel.add(browse);
		panel.add(apply);
		panel.add(remove);
	}

	@Override
	public int getPanelHeight() {
		return 48;
	}

}
