package game.renderer.gui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.lwjgl.opengl.GL11;

import caesar.util.Vector2f;
import game.input.IKeyListener;
import game.input.IMouseListener;
import game.renderer.gui.component.IGuiComponent;
import game.renderer.gui.component.IGuiMouseComponent;
import game.renderer.gui.component.IGuiRenderComponent;
import game.renderer.gui.component.IGuiTextComponent;
import game.renderer.gui.font.Font;
import game.renderer.gui.font.FontMesh;
import game.renderer.gui.font.FontMeshBuilder;
import game.renderer.model.ModelLoader;
import game.renderer.model.SimpleModel;
import game.renderer.model.StreamModel;
import game.renderer.shader.Shader;
import game.renderer.texture.TextureData;
import game.renderer.texture.TextureManager;

public abstract class Gui implements IKeyListener, IMouseListener{

	private List<IGuiComponent> components;

	private List<IGuiTextComponent> textComponents;
	private List<IGuiRenderComponent> renderComponents;
	
	private List<IGuiMouseComponent> mouseInputComponents;

	protected int guiPosX;
	protected int guiPosY;
	protected int guiWidth;
	protected int guiHeight;
	protected int screenWidth;
	protected int screenHeight;
	
	private String currentBoundTexture = "";

	private boolean isInitialized = false;

	protected GuiFrameBuffer frameBuffer;

	private SimpleModel guiQuad;

	private boolean isDragged = false;
	private int dragOffX;
	private int dragOffY;
	
	protected int mouseX;
	protected int mouseY;

	private Gui(){
		this.components = new ArrayList<IGuiComponent>();
		this.textComponents = new ArrayList<IGuiTextComponent>();
		this.renderComponents = new ArrayList<IGuiRenderComponent>();
		this.mouseInputComponents = new ArrayList<IGuiMouseComponent>();
	}

	public Gui(int guiPosX, int guiPosY){
		this();
		this.guiPosX = guiPosX;
		this.guiPosY = guiPosY;
		this.guiWidth = 400;
		this.guiHeight = 400;
	}

	public Gui(int guiPosX, int guiPosY, int guiWidth, int guiHeight){
		this(guiPosX, guiPosY);
		this.guiWidth = guiWidth;
		this.guiHeight = guiHeight;
	}

	public void init(int width, int height){
		this.screenWidth = width;
		this.screenHeight = height;
		initGui();
		sortComponents();
		this.frameBuffer = GuiFrameBuffer.createFrameBuffer(this);
		this.guiQuad = createGuiQuad(0, 0, guiWidth, guiHeight, 0, 0, guiWidth, guiHeight, width, height);
		isInitialized = true;
	}

	protected abstract void initGui();

	protected void addComponent(IGuiComponent component){
		component.onComponentAdded();
		components.add(component);

		if(component instanceof IGuiTextComponent){
			textComponents.add((IGuiTextComponent) component);
		}

		if(component instanceof IGuiRenderComponent){
			renderComponents.add((IGuiRenderComponent) component);
		}

		if(component instanceof IGuiMouseComponent){
			mouseInputComponents.add((IGuiMouseComponent) component);
		}
		
		if(isInitialized){
			sortComponents();
		}
	}

	public void renderGui(Shader shader, int renderPass){
		frameBuffer.bindBuffer();
		Vector2f elementPosition = new Vector2f();
		currentBoundTexture = "";
		switch(renderPass){
		case 0:
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			for(IGuiRenderComponent comp : renderComponents){
				float posX = (float)((/*guiPosX + */comp.getRenderPosX()) * 2.0f) / (float)guiWidth - 1.0f;
				float posY = -((float)((/*guiPosY + */comp.getRenderPosY()) * 2.0f) / (float)guiHeight - 1.0f);
				elementPosition.set(posX, posY);
				shader.loadVector2f("elementPosition", elementPosition);
				bindTexture(comp.getTexture());
				comp.renderComponent(shader);
			}
			break;

		case 1:
			for(IGuiTextComponent comp : textComponents){
				float posX = (float)((/*guiPosX + */comp.getTextPosX()) * 2.0f) / (float)guiWidth - 1.0f;
				float posY = -((float)((/*guiPosY + */comp.getTextPosY()) * 2.0f) / (float)guiHeight - 1.0f);
				elementPosition.set(posX, posY);
				shader.loadVector2f("elementPosition", elementPosition);
				Font font = comp.getFont();
				bindTexture(font.getTextureData());
				comp.renderText(shader);
			}
			break;
		}
		frameBuffer.unbindBuffer();
	}

	public SimpleModel createQuad(GuiAnchor anchor, int width, int height, int u, int v, int textureWidth, int textureHeight){
		int x = (int) ((float)width * anchor.getOffX());
		int y = (int) ((float)height * anchor.getOffY());
		return createQuad(-x, -y, width, height, u, v, textureWidth, textureHeight);
	}

	public SimpleModel createQuad(int x, int y, int width, int height, int u, int v, int textureWidth, int textureHeight){
		return createQuad(x, y, width, height, u, v, textureWidth, textureHeight, guiWidth, guiHeight);
	}

	public SimpleModel createQuad(int x, int y, int width, int height, int u, int v, int textureWidth, int textureHeight, int sourceWidth, int sourceHeight){
		float x0 = (float)x / (float)(/*screenWidth*/sourceWidth * 0.5f);
		float y0 = -((float)y / (float)(/*screenHeight*/sourceHeight * 0.5f));
		float x1 = (float)(x + width) / (float)(/*screenWidth*/sourceWidth * 0.5f);
		float y1 = -((float)(y + height) / (float)(/*screenHeight*/sourceHeight * 0.5f));
		float u0 = (float)u / (float)textureWidth;
		float v0 = (float)v / (float)textureHeight;
		float u1 = (float)(u + width) / (float)textureWidth;
		float v1 = (float)(v + height) / (float)textureHeight;

		float[] vertices = {
				x0, y0,
				x1, y0,
				x1, y1,
				x0, y1,
		};

		float[] uvs = {
				u0, v0,
				u1, v0,
				u1, v1,
				u0, v1
		};

		int[] indices = {
				0,1,2,
				2,3,0
		};

		return ModelLoader.INSTANCE.load2DIndexedSimpleModel(vertices, uvs, indices);
	}
	
	public StreamModel createDynamicQuad(GuiAnchor anchor, int width, int height, int u, int v, int textureWidth, int textureHeight){
		int x = (int) ((float)width * anchor.getOffX());
		int y = (int) ((float)height * anchor.getOffY());
		return createDynamicQuad(-x, -y, width, height, u, v, textureWidth, textureHeight);
	}
	
	public StreamModel createDynamicQuad(int x, int y, int width, int height, int u, int v, int textureWidth, int textureHeight) {
		return createDynamicQuad(x, y, width, height, u, v, textureWidth, textureHeight, guiWidth, guiHeight);
	}

	public StreamModel createDynamicQuad(int x, int y, int width, int height, int u, int v, int textureWidth, int textureHeight, int sourceWidth, int sourceHeight){
		float x0 = (float)x / (float)(/*screenWidth*/sourceWidth * 0.5f);
		float y0 = -((float)y / (float)(/*screenHeight*/sourceHeight * 0.5f));
		float x1 = (float)(x + width) / (float)(/*screenWidth*/sourceWidth * 0.5f);
		float y1 = -((float)(y + height) / (float)(/*screenHeight*/sourceHeight * 0.5f));
		float u0 = (float)u / (float)textureWidth;
		float v0 = (float)v / (float)textureHeight;
		float u1 = (float)(u + width) / (float)textureWidth;
		float v1 = (float)(v + height) / (float)textureHeight;

		float[] vertices = {
				x0, y0,
				x1, y0,
				x1, y1,
				x0, y1,
		};

		float[] uvs = {
				u0, v0,
				u1, v0,
				u1, v1,
				u0, v1
		};

		int[] indices = {
				0,1,2,
				2,3,0
		};
		
		StreamModel model = ModelLoader.INSTANCE.createDynamicModel();
		model.bindModel();
		model.createIndexBuffer(indices);
		model.createDataBuffer("vertices", 0, 2, vertices);
		model.createDataBuffer("uvs", 1, 2, uvs);
		return model;
	}
	
	public void updateQuadTexture(StreamModel model, int width, int height, int u, int v, int textureWidth, int textureHeight){
		float u0 = (float)u / (float)textureWidth;
		float v0 = (float)v / (float)textureHeight;
		float u1 = (float)(u + width) / (float)textureWidth;
		float v1 = (float)(v + height) / (float)textureHeight;
		
		float[] uvs = {
				u0, v0,
				u1, v0,
				u1, v1,
				u0, v1
		};
		
		model.bindModel();
		model.updateDataBuffer("uvs", 2, uvs);
		model.unbindModel();
	}

	public SimpleModel createGuiQuad(int x, int y, int width, int height, int u, int v, int textureWidth, int textureHeight, int sourceWidth, int sourceHeight){
		float x0 = (float)x / (float)(/*screenWidth*/sourceWidth * 0.5f);
		float y0 = -((float)y / (float)(/*screenHeight*/sourceHeight * 0.5f));
		float x1 = (float)(x + width) / (float)(/*screenWidth*/sourceWidth * 0.5f);
		float y1 = -((float)(y + height) / (float)(/*screenHeight*/sourceHeight * 0.5f));
		float u0 = (float)u / (float)textureWidth;
		float v0 = (float)v / (float)textureHeight;
		float u1 = (float)(u + width) / (float)textureWidth;
		float v1 = (float)(v + height) / (float)textureHeight;

		float[] vertices = {
				x0, y0,
				x1, y0,
				x1, y1,
				x0, y1,
		};

		float[] uvs = {
				u0, v1,
				u1, v1,
				u1, v0,
				u0, v0
		};

		int[] indices = {
				0,1,2,
				2,3,0
		};

		return ModelLoader.INSTANCE.load2DIndexedSimpleModel(vertices, uvs, indices);
	}

	public SimpleModel createTextMesh(Font font, String text, GuiAnchor anchor, float scale){
		FontMeshBuilder builder = new FontMeshBuilder(font, scale);
		String[] lines = text.split("\n");
		for(String line : lines) builder.addLine(line);
		FontMesh mesh = builder.build(guiWidth, guiHeight, anchor);
		return ModelLoader.INSTANCE.load2DIndexedSimpleModel(mesh.getVertices(), mesh.getUvs(), mesh.getIndices());
	}
	
	public StreamModel createDynamicTextMesh(Font font, String text, GuiAnchor anchor, float scale){
		FontMeshBuilder builder = new FontMeshBuilder(font, scale);
		String[] lines = text.split("\n");
		for(String line : lines) builder.addLine(line);
		FontMesh mesh = builder.build(guiWidth, guiHeight, anchor);
		
		StreamModel model = ModelLoader.INSTANCE.createStreamModel();
		model.bindModel();
		model.createIndexBuffer(mesh.getIndices());
		model.createDataBuffer("vertices", 0, 2, mesh.getVertices());
		model.createDataBuffer("uvs", 1, 2, mesh.getUvs());
		return model;
	}
	
	public void updateText(StreamModel model, Font font, String text, GuiAnchor anchor, float scale){
		FontMeshBuilder builder = new FontMeshBuilder(font, scale);
		String[] lines = text.split("\n");
		for(String line : lines) builder.addLine(line);
		FontMesh mesh = builder.build(guiWidth, guiHeight, anchor);
		
		model.bindModel();
		model.updateIndexBuffer(mesh.getIndices());
		model.updateDataBuffer("vertices", 2, mesh.getVertices());
		model.updateDataBuffer("uvs", 2, mesh.getUvs());
		model.unbindModel();
	}

	public void dispose(){
		for(IGuiComponent gui : components){
			gui.onComponentDeleted();
		}

		guiQuad.delete();
		frameBuffer.deleteBuffer();
		components.clear();
		renderComponents.clear();
		textComponents.clear();
		mouseInputComponents.clear();
		isInitialized = false;
	}

	public void bindTexture(TextureData textureData){
		if(currentBoundTexture.equals(textureData.getTexture())){
			return;
		}
		TextureManager.bindTexture(textureData);
		currentBoundTexture = textureData.getTexture();
	}

	public int getGuiPosX(){
		return guiPosX;
	}

	public int getGuiPosY(){
		return guiPosY;
	}

	public int getGuiWidth(){
		return guiWidth;
	}

	public int getGuiHeight(){
		return guiHeight;
	}

	public int getScreenHeight(){
		return screenHeight;
	}

	public int getScreenWidth(){
		return screenWidth;
	}

	public abstract int getGuiLayer();

	public boolean doesGuiDisablePlayerMouse(){
		return true;
	}

	public boolean doesGuiDisablePlayerMovement(){
		return true;
	}

	public abstract String getGuiId();

	public boolean equals(Object other){
		return other instanceof Gui ? ((Gui)other).getGuiId().equals(getGuiId()) : false;
	}

	private void sortComponents(){

		textComponents.sort(new Comparator<IGuiTextComponent>(){

			@Override
			public int compare(IGuiTextComponent o1, IGuiTextComponent o2) {
				return o1.getFont().getTextureData().equals(o2.getFont().getTextureData()) ? 0 : -1;
			}

		});

		renderComponents.sort(new Comparator<IGuiRenderComponent>(){

			@Override
			public int compare(IGuiRenderComponent o1, IGuiRenderComponent o2) {
				return 0;
			}

		});
	}

	public final int getGuiColorTexture(){
		return frameBuffer.getColorTexture();
	}

	public SimpleModel getGuiQuad(){
		return guiQuad;
	}

	public boolean canDrag(){
		return false;
	}

	public int getHeaderWidth(){
		return guiWidth;
	}
	
	public int getHeaderHeight(){
		return 32;
	}

	@Override
	public void onMouseMoved(int mx, int my) {
		this.mouseX = mx - guiPosX;
		this.mouseY = my - guiPosY;
		
		if(isDragged){
			guiPosX = this.mouseX + guiPosX - dragOffX;
			guiPosY = this.mouseY + guiPosY - dragOffY;
		}
		
		for(IGuiMouseComponent comp : mouseInputComponents){
			comp.onMouseMoved(mouseX, mouseY);
		}
	}

	@Override
	public void onMouseClicked(int button, int action, int mods) {
		if(button == 0 && action == 1 && canDrag() && isValidDragPosition(mouseX, mouseY)){
			isDragged = true;
			dragOffX = this.mouseX;
			dragOffY = this.mouseY;
		}
		if(button == 0 && action == 0 && isDragged){
			isDragged = false;
			dragOffX = 0;
			dragOffY = 0;
		}
		for(IGuiMouseComponent comp : mouseInputComponents){
			comp.onMouseClicked(mouseX, mouseY, button, action);
		}
	}

	@Override
	public void onKeyAction(int keyCode, int scanCode, int mod, int action) {

	}
	
	protected boolean isValidDragPosition(int x, int y){
		return x > 0 && x < getHeaderWidth() && y > 0 && y < getHeaderHeight();
	}
	
	public void actionPerformed(int action){}
}
