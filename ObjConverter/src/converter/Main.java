package converter;

import org.lwjgl.LWJGLException;

import converter.ui.MainUI;

public class Main {

	public static void main(String[] args){	
		MainUI ui = new MainUI();
		try {
			ui.init();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
}
