package game.client.renderer.gui.menu;

import game.client.network.Client;
import game.client.renderer.gui.Gui;
import game.client.renderer.gui.GuiAnchor;
import game.client.renderer.gui.component.GuiComponentButton;
import game.client.renderer.gui.component.GuiComponentDynamicText;
import game.client.renderer.gui.component.GuiComponentText;
import game.client.renderer.gui.component.IGuiComponent;
import game.client.renderer.gui.font.FontManager;
import game.common.network.packet.PacketTest1;
import game.common.network.packet.PacketTest2;

public class GuiConnectionTest extends Gui{

	public GuiConnectionTest() {
		super(0, 0);
	}

	@Override
	protected void initGui() {
		this.guiWidth = this.screenWidth;
		this.guiHeight = this.screenHeight;
		
		int y = screenHeight / 2 - 200;
		
		this.addComponent(new GuiComponentText(this, "Connection Test", "arial", screenWidth / 2, y, GuiAnchor.TOP));
		this.addComponent(new GuiComponentDynamicText(this, "Connection Message", FontManager.getFont("arial"), screenWidth / 2, y + 100, GuiAnchor.TOP));
		this.addComponent(new GuiComponentButton(this, screenWidth / 2 - 128, y + 164, 0, "Send Packet 1", "arial"));
		this.addComponent(new GuiComponentButton(this, screenWidth / 2 - 128, y + 228, 1, "Send Packet 2", "arial"));
	}
	
	@Override
	public void actionPerformed(IGuiComponent component, int action){
		switch(action){
		case 0:
			Client.sendPacket(new PacketTest1());
			break;
			
		case 1:
			Client.sendPacket(new PacketTest2());
			break;
		}
	}

	@Override
	public int getGuiLayer() {
		return 0;
	}

	@Override
	public String getGuiId() {
		return "main_menu";
	}

}