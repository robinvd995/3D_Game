package game.common.entity;

import caesar.util.GlobalAxis;
import caesar.util.Quaternion;
import game.client.display.DisplayManager;
import game.client.input.IMouseListener;
import game.client.input.IScrollListener;
import game.client.input.InputManager;
import game.client.input.Key;
import game.client.input.KeyTracker;
import game.client.renderer.Camera;
import game.client.renderer.animation.AnimationLoader;
import game.client.renderer.animation.AnimationTransformation;
import game.client.renderer.animation.Animator;
import game.client.renderer.entity.IEntityRenderable;
import game.client.renderer.gui.GuiInventory;
import game.client.renderer.gui.GuiMenu;
import game.client.renderer.gui.event.GuiEvent.GuiOpenEvent;
import game.client.renderer.texture.TextureLoader;
import game.common.event.EventManager;
import game.common.event.entity.PlayerEvent.BeforePlayerSwitchItemEvent;
import game.common.event.entity.PlayerEvent.OnPlayerSwitchItemEvent;
import game.common.inventory.Inventory;
import game.common.item.Item;

public class Player extends Entity implements IScrollListener, IEntityRenderable, IMouseListener{

	private final float movementSpeed = 10.0f;
	private final float jumpStrength = 0.08f;

	private float sensitivity = 0.03f;

	private final KeyTracker jumpTracker = new KeyTracker(Key.SPACE);
	private KeyTracker leftCtrlTracker = new KeyTracker(Key.LEFT_CTRL);
	private KeyTracker screenShotTracker = new KeyTracker(Key.F12);
	private KeyTracker menuKeyTracker = new KeyTracker(Key.ESC);
	private KeyTracker inventoryKeyTracker = new KeyTracker(Key.E);

	private Camera camera;

	private Inventory inventoryHotbar;
	private int currentHeldItem = 0;

	private Animator animator;
	
	public Player(){
		super();
		this.transform.setScale(0.75f, 0.75f, 0.75f);
		this.camera = new Camera();
		this.inventoryHotbar = new Inventory(10);

		//Temp inventory filler
		this.inventoryHotbar.addItem(Item.PICKAXE);
		this.inventoryHotbar.addItem(Item.SWORD);
		this.inventoryHotbar.addItem(Item.BOW);

		this.animator = new Animator();
		AnimationTransformation at = AnimationLoader.loadTransformationAnimation("res/animation/test");
		this.animator.setAnimation(at);
		
		InputManager.addMouseListener(this);
		InputManager.addScrollListener(this);
		EventManager.registerEventListener(this);
	}

	public Inventory getInventoryHotbar(){
		return inventoryHotbar;
	}

	public int getCurrentHeldItemIndex(){
		return currentHeldItem;
	}

	public Item getCurrentHeldItem(){
		return this.inventoryHotbar.getItemAtIndex(currentHeldItem);
	}

	@Override
	public void update(double delta){

		super.update(delta);

		double rotY = Math.toRadians(InputManager.getMouseDeltaX() * sensitivity);
		transform.rotate((float)0.0f, (float)-rotY, 0.0f);

		if(InputManager.isKeyDown(Key.UP)){
			transform.translate(GlobalAxis.Z.toVector(), (float) (-movementSpeed * delta));
		}

		if(InputManager.isKeyDown(Key.DOWN)){
			transform.translate(GlobalAxis.Z.toVector(), (float) (movementSpeed * delta));
		}

		if(InputManager.isKeyDown(Key.LEFT)){
			transform.translate(GlobalAxis.X.toVector(), (float) (-movementSpeed * delta));
		}

		if(InputManager.isKeyDown(Key.RIGHT)){
			transform.translate(GlobalAxis.X.toVector(), (float) (movementSpeed * delta));
		}

		if(jumpTracker.isKeyAction(0)){
			this.jump(jumpStrength);
		}

		if(leftCtrlTracker.isKeyAction(1)){
			DisplayManager.INSTANCE.nextDisplaySize();
		}

		if(screenShotTracker.isKeyAction(1)){
			TextureLoader.takeScreenshot();
		}

		if(menuKeyTracker.isKeyAction(1)){
			EventManager.postPreUpdateEvent(new GuiOpenEvent(new GuiMenu()));
		}

		if(inventoryKeyTracker.isKeyAction(1)){
			EventManager.postPreUpdateEvent(new GuiOpenEvent(new GuiInventory()));
		}
	}

	@Override
	public void lateUpdate(double delta){
		super.lateUpdate(delta);

		camera.setPosition(transform.getPosition().copy().add(0.0f, 1.8f, 0.0f));
		camera.setCameraYaw(-transform.getRotation().getY());
		camera.addCameraPitch((float) Math.toRadians(InputManager.getMouseDeltaY() * sensitivity));
	}

	public Camera getCamera(){
		return camera;
	}

	public Quaternion getCameraOrientation(){
		return camera.getTransform().getOrientation();
	}

	@Override
	public void onScroll(double scrollX, double scrollY) {
		BeforePlayerSwitchItemEvent beforeEvent = new BeforePlayerSwitchItemEvent(this);
		EventManager.postEvent(beforeEvent);
		if(!beforeEvent.isCanceled()){
			int prev = currentHeldItem;
			if(scrollY > 0){
				currentHeldItem = Math.max(currentHeldItem - 1, 0);
			}
			else if(scrollY < 0){
				currentHeldItem = Math.min(currentHeldItem + 1, inventoryHotbar.getSize() - 1);
			}
			if(prev != currentHeldItem){
				OnPlayerSwitchItemEvent onEvent = new OnPlayerSwitchItemEvent(this, prev);
				EventManager.postEvent(onEvent);
			}
		}

	}
	
	public Animator getAnimator(){
		return animator;
	}

	@Override
	public void onMouseMoved(int mouseX, int mouseY) {}

	@Override
	public void onMouseClicked(int button, int action, int mods) {
		if(button == 0 && action == 0 && !animator.isPlaying()){
			animator.play();
		}
	}

	/*@Subscribe
	public void onSwitch(OnPlayerItemSwitchEvent e){
		System.out.println("on");
	}

	@Subscribe
	public void beforeSwitch(BeforePlayerItemSwitchEvent e){
		System.out.println("before");
	}*/
}
