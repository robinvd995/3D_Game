package game.client.audio;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.HashMap;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALContext;
import org.lwjgl.stb.STBVorbis;

import caesar.util.Vector3f;

@SuppressWarnings("unused")
public class AudioManager {

	private static HashMap<String,Integer> audioMap = new HashMap<String,Integer>();
	
	private static ALContext context;
	
	public static void init(){
		context = ALContext.create();
		initListener();
	}

	public static void playSound(int source, String sfx){
		if(!audioMap.containsKey(sfx)){
			loadSound(sfx);
		}
		
		int buffer = audioMap.get(sfx);
		AL10.alSourcei(source, AL10.AL_BUFFER, buffer);
		AL10.alSourcePlay(source);
	}
	
	private static void loadSound(String file){
		String filePath = "res/audio/" + file + ".ogg";
		
		System.out.println(filePath);
		
		IntBuffer channelBuffer = BufferUtils.createIntBuffer(1);
		IntBuffer sampleBuffer = BufferUtils.createIntBuffer(1);
		
		ShortBuffer buffer = STBVorbis.stb_vorbis_decode_filename(filePath, channelBuffer, sampleBuffer);
		
		int channels = channelBuffer.get();
		int sampleRate = sampleBuffer.get();
		
		int format = -1;
		switch(channels){
		default: format = -1; break;
		case 1: format = AL10.AL_FORMAT_MONO16; break;
		case 2: format = AL10.AL_FORMAT_STEREO16; break;
		}
		
		int bufferPointer = AL10.alGenBuffers();
		AL10.alBufferData(bufferPointer, format, buffer, sampleRate);
		errorCheck("point to buffer");
		//buffer.clear();
		audioMap.put(file, bufferPointer);
	}
	
	public static void cleanUp(){
		for(int i : audioMap.values()){
			deleteSound(i);
		}
	}

	private static void deleteSound(int bufferPointer){
		AL10.alDeleteBuffers(bufferPointer);
	}
	
	private static void errorCheck(String s){
		switch(AL10.alGetError()){
		case AL10.AL_NO_ERROR:
			System.out.println(s + ":AL_NO_ERROR");
			break;
		case AL10.AL_INVALID_NAME:
			System.out.println(s + ":AL_INVALID_NAME");
			break;
		case AL10.AL_INVALID_ENUM:
			System.out.println(s + ":AL_INVALID_ENUM");
			break;
		case AL10.AL_INVALID_VALUE:
			System.out.println(s + ":AL_INVALID_VALUE");
			break;
		case AL10.AL_INVALID_OPERATION:
			System.out.println(s + ":AL_INVALID_OPERATION");
			break;
		case AL10.AL_OUT_OF_MEMORY:
			System.out.println(s + ":AL_OUT_OF_MEMORY");
			break;
			
		}
	}
	
	public static void initListener(){
		AL10.alListener3f(AL10.AL_POSITION, .0f, .0f, .0f);
		AL10.alListener3f(AL10.AL_VELOCITY, .0f, .0f, .0f);
		FloatBuffer listenerOri = (FloatBuffer)BufferUtils.createFloatBuffer(6).put(new float[] { 0.0f, 0.0f, -1.0f,  0.0f, 1.0f, 0.0f }).rewind();
		AL10.alListenerfv(AL10.AL_ORIENTATION, listenerOri);
	}
}
