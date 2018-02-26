package game.client.renderer.gui.event;

import game.client.renderer.gui.component.GuiComponentButton;

public class GuiButtonEvent {

	public abstract static class ButtonEvent {
		
		private GuiComponentButton button;
		private int action;
		
		public GuiComponentButton getButton(){
			return button;
		}
		
		public int getAction(){
			return action;
		}
	}
	
	public static class ButtonPressedEvent extends GuiButtonEvent {
		
	}
}
