package game.client.renderer.gui.menu;

import game.client.display.DisplayManager;
import game.client.network.Client;
import game.client.renderer.gui.Gui;
import game.client.renderer.gui.GuiAnchor;
import game.client.renderer.gui.GuiNotification;
import game.client.renderer.gui.component.GuiComponentButton;
import game.client.renderer.gui.component.GuiComponentText;
import game.client.renderer.gui.component.GuiComponentTextBox;
import game.client.renderer.gui.component.IGuiComponent;
import game.client.renderer.gui.event.GuiEvent.GuiCloseEvent;
import game.client.renderer.gui.event.GuiEvent.GuiOpenEvent;
import game.client.renderer.shader.Shader;
import game.common.event.EventManager;

public class GuiMenuMultiplayer extends Gui {

	private GuiComponentTextBox ipTextBox;
	private GuiComponentTextBox portTextBox;

	private GuiNotification notificationGui;

	public GuiMenuMultiplayer() {
		super(0, 0);
	}

	@Override
	protected void initGui() {
		this.guiWidth = DisplayManager.INSTANCE.getDisplayWidth();
		this.guiHeight = DisplayManager.INSTANCE.getDisplayHeight();
		int x = guiWidth / 2;
		int y = guiHeight / 2;
		this.addComponent(new GuiComponentText(this, "Connect", "arial", x, y - 200, GuiAnchor.TOP));
		this.addComponent(new GuiComponentText(this, "IP:", "arial", x - 80, y - 94, GuiAnchor.TOP_RIGHT, 0.5f));
		this.addComponent(new GuiComponentText(this, "Port:", "arial", x - 80, y - 34, GuiAnchor.TOP_RIGHT, 0.5f));
		this.addComponent(ipTextBox = new GuiComponentTextBox(this, x - 60, y - 100, "arial"));
		this.addComponent(portTextBox = new GuiComponentTextBox(this, x -60, y - 40, "arial"));
		this.addComponent(new GuiComponentButton(this, x - 266, y + 40, 0, "Connect", "arial"));
		this.addComponent(new GuiComponentButton(this, x + 10, y + 40, 1, "Cancel", "arial"));
	}

	public void actionPerformed(IGuiComponent comp, int action){
		switch(action){
		case 0:
			String ip = ipTextBox.getText();
			String portS = portTextBox.getText();
			
			if(ip == "" || portS == ""){
				ip = "localhost";
				portS = "25565";
			}
			
			int port = Integer.valueOf(portS);
			
			Client.createClient(ip, port, 2000);
			notificationGui = new GuiNotification();
			EventManager.postPostUpdateEvent(new GuiOpenEvent(notificationGui));
			break;
		case 1:
			EventManager.postPostUpdateEvent(new GuiOpenEvent(new GuiMainMenu()));
			EventManager.postPostUpdateEvent(new GuiCloseEvent(getGuiId()));
			break;
		}
	}

	public void renderGui(Shader shader, int renderpass){
		if(renderpass == 0 && notificationGui != null){
			String message = Client.getConnectionStatus().toString();
			notificationGui.setMessage(message);
		}
		super.renderGui(shader, renderpass);
	}

	@Override
	public int getGuiLayer() {
		return 0;
	}

	@Override
	public String getGuiId() {
		return "menu_multiplayer";
	}

	public static class GuiConnectionStatus extends Gui{

		public GuiConnectionStatus() {
			super(0, 0, 400, 200);
		}

		@Override
		protected void initGui() {

		}

		@Override
		public int getGuiLayer() {
			return 0;
		}

		@Override
		public String getGuiId() {
			return null;
		}

	}
}
