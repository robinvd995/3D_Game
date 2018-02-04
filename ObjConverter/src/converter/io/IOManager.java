package converter.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import converter.api.model.IndexedModel;
import converter.model.RawModel;

public class IOManager {

	public static RawModel loadRawModel(File file){
		try {
			RawModel model = ObjLoader.loadOBJModel(file);
			return model;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean exportModel(IndexedModel theModel, File file){
		IndexedModelExporter exporter = new IndexedModelExporter(file);
		try {
			exporter.exportModel(theModel);
			return true;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static int loadTexture(String fileName){
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream("res/"+fileName+".png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int textureID = texture.getTextureID();
		return textureID;
	}
}
