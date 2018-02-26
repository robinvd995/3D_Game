package game.client.renderer.gui.event;

import game.client.renderer.gui.Gui;

public abstract class GuiEvent {

	private String guiId;
	
	public GuiEvent(String gui){
		guiId = gui;
	}
	
	public String getGuiId(){
		return guiId;
	}
	
	public static class GuiOpenEvent extends GuiEvent {

		private Gui gui;
		
		public GuiOpenEvent(Gui gui) {
			super(gui.getGuiId());
			this.gui = gui;
		}
		
		public Gui getGui(){
			return gui;
		}
	}
	
	public static class GuiCloseEvent extends GuiEvent {
		
		public GuiCloseEvent(String gui) {
			super(gui);
		}
	}
	
	/*public static class GuiToggleEvent extends GuiOpenEvent {
		
		public GuiToggleEvent(Gui gui) {
			super(gui);
		}
		
		
	}*/
}
