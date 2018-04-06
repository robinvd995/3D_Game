package game.client.renderer.gui;

import com.google.common.eventbus.Subscribe;

import caesar.util.Vector4f;
import game.client.Game;
import game.client.renderer.gui.component.GuiComponentCrosshair;
import game.client.renderer.gui.component.GuiComponentDynamicText;
import game.client.renderer.gui.component.GuiComponentHudTexture;
import game.client.renderer.gui.component.GuiComponentText;
import game.client.renderer.gui.font.FontManager;
import game.client.renderer.shader.Shader;
import game.common.entity.Player;
import game.common.event.EventManager;
import game.common.event.entity.PlayerEvent.OnPlayerSwitchItemEvent;

public class GuiHud extends Gui {
	
	private static final int HOTBAR_INACTIVE_U = 64;
	private static final int HOTBAR_ACTIVE_U = 128;
	
	private GuiComponentDynamicText fpsCounter;
	
	private Player thePlayer;
	
	private GuiComponentHudTexture[] hotbarTextures;
	
	public GuiHud(Player player) {
		super(0, 0);
		this.thePlayer = player;
	}

	@Override
	public void initGui() {
		this.guiWidth = screenWidth;
		this.guiHeight = screenHeight;
		int screenCenterX = screenWidth / 2;
		int screenCenterY = screenHeight / 2;
		
		//Crosshair
		this.addComponent(new GuiComponentCrosshair(this, screenCenterX, screenCenterY));
		
		//Hotbar
		int slotAmount = thePlayer.getInventoryHotbar().getSize();
		hotbarTextures = new GuiComponentHudTexture[slotAmount];
		int offset = slotAmount / 2;
		for(int i = 0; i < slotAmount; i++){
			int posX = screenCenterX + ((i - offset) * 66);
			int u = HOTBAR_INACTIVE_U;
			if(i == thePlayer.getCurrentHeldItemIndex()){
				u = HOTBAR_ACTIVE_U;
			}
			
			Vector4f color = thePlayer.getInventoryHotbar().getItemAtIndex(i) != null ? new Vector4f(1.0f, 0.0f, 0.0f, 1.0f) : new Vector4f(0.0f, 0.0f, 1.0f, 1.0f);
			
			this.addComponent(hotbarTextures[i] = new GuiComponentHudTexture(this, posX, screenHeight - 66, 0, 0, u, 0, 64, 64));
			this.addComponent(new GuiComponentText(this, String.valueOf((i + 1) % 10), FontManager.getFont("arial"), posX + 32, screenHeight, GuiAnchor.BOTTOM, color, 0.5f));
		}
		
		//Fps counter
		this.addComponent(new GuiComponentText(this, "Fps:", FontManager.getFont("arial"), 10 , 10, GuiAnchor.TOP_LEFT, new Vector4f(1.0f), 0.33f));
		this.fpsCounter = new GuiComponentDynamicText(this, "", FontManager.getFont("arial"), 50, 10, GuiAnchor.TOP_LEFT, new Vector4f(1.0f), 0.33f);
		this.addComponent(fpsCounter);
	}
	
	@Override
	public void renderGui(Shader shader, int renderPass){
		fpsCounter.updateText(String.valueOf(Game.INSTANCE.timer.getFps()));
		super.renderGui(shader, renderPass);
	}
	
	@Override
	public int getGuiLayer() {
		return 0;
	}

	@Override
	public String getGuiId() {
		return "hud";
	}
	
	@Override
	public boolean doesGuiDisablePlayerMouse(){
		return false;
	}
	
	@Override
	public boolean doesGuiDisablePlayerMovement(){
		return false;
	}
	
	@Override
	public boolean canBeActive(){
		return false;
	}
	
	@Override
	public void onGuiOpened(){
		EventManager.registerEventListener(this);
	}
	
	@Override
	public void onGuiClosed(){
		EventManager.unregisterEventListener(this);
	}
	
	@Subscribe
	public void onPlayerSwitchItem(OnPlayerSwitchItemEvent event){
		int prev = event.getPrevHeldItemIndex();
		hotbarTextures[prev].updateTexture(HOTBAR_INACTIVE_U, 0);
		hotbarTextures[event.getPlayer().getCurrentHeldItemIndex()].updateTexture(HOTBAR_ACTIVE_U, 0);
	}
}
