package game.renderer;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import caesar.util.Matrix4f;
import caesar.util.Vector3f;
import game.Game;
import game.renderer.block.BlockRenderManager;
import game.renderer.block.BlockRenderRegistry;
import game.renderer.entity.EntityRenderManager;
import game.renderer.gui.GuiRenderManager;
import game.renderer.gui.TestGui;
import game.renderer.model.ModelLoader;
import game.renderer.texture.TextureManager;
import game.renderer.world.SkyboxRenderManager;
import game.world.ClientWorld;

public class RenderManager {

	private BlockRenderManager blockRenderer;
	private EntityRenderManager entityRenderer;
	private GuiRenderManager guiRenderer;
	private SkyboxRenderManager skyboxRenderer;
	
	public RenderManager(){
		blockRenderer = new BlockRenderManager();
		entityRenderer = new EntityRenderManager();
		guiRenderer = new GuiRenderManager();
		skyboxRenderer = new SkyboxRenderManager();
		DebugRenderer.INSTANCE.init();
	}

	public void initOpenGL(){
		GL.createCapabilities();
	}
	
	public void initRenderer(){
		BlockRenderRegistry.loadAllRenderData();
		TextureManager.loadAllTextures();
		
		entityRenderer.initRenderer();
		blockRenderer.initRenderer();
		guiRenderer.initRenderer();
		skyboxRenderer.initRenderer();
		
		TestGui testGui = new TestGui();
		Game.INSTANCE.openGui(testGui);
	}
	
	private void prepare(){
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glEnable(GL_DEPTH_TEST);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	public void renderWorld(Camera camera, ClientWorld world, double delta, Vector3f lightDir){
		//buffer.bindTextureBuffer();
		prepare();
		Matrix4f viewMatrix = camera.createViewMatrix();
		
		skyboxRenderer.renderSkybox(viewMatrix);
		
		blockRenderer.prepare(lightDir, viewMatrix);
		blockRenderer.renderBlocks(world, false);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		blockRenderer.renderBlocks(world, true);
		GL11.glDisable(GL11.GL_BLEND);
		blockRenderer.end();
		
		entityRenderer.prepare(lightDir, viewMatrix);
		entityRenderer.renderEntity(world.getWorldObj(), world.getWorldObj().getPlayer());
		entityRenderer.end();
		
		DebugRenderer.INSTANCE.prepare(viewMatrix);
		DebugRenderer.INSTANCE.renderAll();
		DebugRenderer.INSTANCE.end();
		
		glDisable(GL_CULL_FACE);
		glDisable(GL_DEPTH_TEST);
		guiRenderer.renderGuis();
	}

	public void cleanUp() {
		/*try {
			//TextureLoader.saveBufferColorTexture(buffer, TextureStitcher.TEXTURE_DIMENSIONS, TextureStitcher.TEXTURE_DIMENSIONS, "buffer", "png");
			//TextureLoader.saveBufferColorTexture(buffer, DisplayManager.INSTANCE.getDisplayWidth(), DisplayManager.INSTANCE.getDisplayHeight(), "buffer", "png");
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		System.out.println("cleanup");
		ModelLoader.INSTANCE.cleanUp();
	}
	
	public GuiRenderManager getGuiRenderer(){
		return guiRenderer;
	}
}
