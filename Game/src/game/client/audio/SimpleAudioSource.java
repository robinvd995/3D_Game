package game.client.audio;

import org.lwjgl.openal.AL10;

public class SimpleAudioSource implements IAudioSource{

	private final int sourcePointer;
	
	public SimpleAudioSource(){
		this.sourcePointer = AL10.alGenSources();
		this.setPosition(0, 0, 0);
		this.setVelocity(0, 0, 0);
		this.setGain(1.0f);
		this.setPitch(1.0f);
	}
	
	public void setPosition(float x, float y, float z){
		AL10.alSource3f(sourcePointer, AL10.AL_POSITION, x, y, z);
	}
	
	public void setVelocity(float x, float y, float z){
		AL10.alSource3f(sourcePointer, AL10.AL_VELOCITY, x, y, z);
	}
	
	public void setPitch(float pitch){
		AL10.alSourcef(sourcePointer, AL10.AL_PITCH, 1.0f);
	}
	
	public void setGain(float gain){
		AL10.alSourcef(sourcePointer, AL10.AL_GAIN, 1.0f);
	}
	
	public void playSound(String sound){
		AudioManager.playSound(sourcePointer, sound);
	}

	public void setReferenceDistance(float ref){
		AL10.alSourcef(sourcePointer, AL10.AL_REFERENCE_DISTANCE, ref);
	}
	
	public void setMaximumDistance(float max){
		AL10.alSourcef(sourcePointer, AL10.AL_MAX_DISTANCE, max);
	}
	
	public void setRollofFactor(float rollof){
		AL10.alSourcef(sourcePointer, AL10.AL_ROLLOFF_FACTOR, rollof);
	}
	
	@Override
	public void delete() {
		AL10.alDeleteSources(sourcePointer);
	}
}
