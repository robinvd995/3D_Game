package game.renderer.texture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import javax.imageio.ImageIO;

public class TextureStitcher {

	public static final int TEXTURE_DIMENSIONS = 256;
	
	private static final int TEXTURE_SPOT_SIZE = 32;
	
	public static StitchResult stitchTextures(String filePath, String fileExtension, Set<String> textures){
		
		Queue<StitchEntry> stitchEntries = new PriorityQueue<StitchEntry>();
		
		int currentSpotsOccupied = 0;
		
		for(String texture : textures){
			String textureFilePath = filePath + texture + "." + fileExtension;
			File textureFile = new File(textureFilePath);
			try {
				BufferedImage image = ImageIO.read(textureFile);
				int width = image.getWidth();
				int height = image.getHeight();
				if(width != height || width % 16 != 0){
					System.err.println("Texture " + textureFilePath + " is not a valid texture!");
					continue;
				}
				int spotsNeeded = width / TEXTURE_SPOT_SIZE;
				System.out.println(width);
				currentSpotsOccupied += spotsNeeded * spotsNeeded;
				stitchEntries.add(new StitchEntry(image, texture, spotsNeeded));
				if(currentSpotsOccupied > 256){
					System.out.println("To many spots!");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		BufferedImage stitchedTexture  = new BufferedImage(TEXTURE_DIMENSIONS, TEXTURE_DIMENSIONS, BufferedImage.TYPE_INT_ARGB);
		LinkedList<TexturePosition> freeTextureSlots = createEmptyTextureRoster();
		
		StitchResult result = new StitchResult();
		
		while(!stitchEntries.isEmpty()){
			StitchEntry entry = stitchEntries.poll();
			TexturePosition pos = findSpot(entry.spotsNeeded, freeTextureSlots);
			if(pos == null){
				System.out.println("Could not find spot!");
				continue;
			}
			
			int imgWidth = entry.image.getWidth();
			int imgHeight = entry.image.getHeight();
			for(int i = 0; i < imgWidth; i++){
				for(int j = 0; j < imgHeight; j++){
					int texturePosX = (pos.posX * TEXTURE_SPOT_SIZE) + i;
					int texturePosY = (pos.posY * TEXTURE_SPOT_SIZE) + j;
					int rgb = entry.image.getRGB(i, j);
					stitchedTexture.setRGB(texturePosX, texturePosY, rgb);
				}
			}
			
			float texPosX = (float)(pos.posX * TEXTURE_SPOT_SIZE) / (float)TEXTURE_DIMENSIONS;
			float texPosY = (float)(pos.posY * TEXTURE_SPOT_SIZE) / (float)TEXTURE_DIMENSIONS;
			float width = (float)(entry.spotsNeeded * TEXTURE_SPOT_SIZE) / (float)TEXTURE_DIMENSIONS;
			float height = (float)(entry.spotsNeeded * TEXTURE_SPOT_SIZE) / (float)TEXTURE_DIMENSIONS;
			MappedTexture mappedTexture = new MappedTexture(texPosX, texPosY, width, height);
			result.textures.put(entry.texture, mappedTexture);
		}
		
		result.image = stitchedTexture;
		
		try {
			ImageIO.write(stitchedTexture, "png", new File("StitchedTexture.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	private static TexturePosition findSpot(int spotsNeeded, LinkedList<TexturePosition> freeTextureSlots){
		System.out.println(spotsNeeded);
		if(spotsNeeded == 1){
			TexturePosition pos = freeTextureSlots.getFirst();
			freeTextureSlots.removeFirst();
			return pos;
		}
		else{
			for(TexturePosition pos : freeTextureSlots){
				int x = pos.posX;
				int y = pos.posY;
				if(isSpaceFree(freeTextureSlots, x, y, spotsNeeded, spotsNeeded)){
					return pos;
				}
			}
			return null;
		}
	}
	
	private static boolean isSpaceFree(LinkedList<TexturePosition> freeTextureSlots, int x, int y, int w, int h){
		List<TexturePosition> list = new ArrayList<TexturePosition>();
		for(int i = 0; i < w; i++){
			for(int j = 0; j < h; j++){
				TexturePosition pos = new TexturePosition(x + i, y + j);
				list.add(pos);
				if(!freeTextureSlots.contains(pos)){
					return false;
				}
			}
		}
		freeTextureSlots.removeAll(freeTextureSlots);
		return true;
	}
	
	private static LinkedList<TexturePosition> createEmptyTextureRoster(){
		LinkedList<TexturePosition> list = new LinkedList<TexturePosition>();
		int size = TEXTURE_DIMENSIONS / TEXTURE_SPOT_SIZE;
		for(int i = 0; i < size * size; i++){
			int posX = i % size;
			int posY = i / size;
			list.add(new TexturePosition(posX, posY));
		}
		return list;
	}
	
	public static class StitchResult {
		
		private BufferedImage image;
		private HashMap<String,MappedTexture> textures;
		
		private StitchResult(){
			textures = new HashMap<String,MappedTexture>();
		}
		
		public BufferedImage getImage(){
			return image;
		}
		
		public HashMap<String,MappedTexture> getTextures(){
			return textures;
		}
	}
	
	private static class TexturePosition {
		
		private int posX;
		private int posY;
		
		public TexturePosition(int x, int y){
			this.posX = x;
			this.posY = y;
		}
		
		public boolean equals(Object o){
			return o instanceof TexturePosition ? equals((TexturePosition) o) : false;
		}
		
		public boolean equals(TexturePosition other){
			return posX == other.posX && posY == other.posY;
		}
	}
	
	private static class StitchEntry implements Comparable<StitchEntry>{
		
		private BufferedImage image;
		private String texture;
		private int spotsNeeded;
		
		public StitchEntry(BufferedImage image, String texture, int spotsNeeded){
			this.image = image;
			this.texture = texture;
			this.spotsNeeded = spotsNeeded;
		}

		@Override
		public int compareTo(StitchEntry o) {
			return spotsNeeded - o.spotsNeeded;
		}
	}
}
