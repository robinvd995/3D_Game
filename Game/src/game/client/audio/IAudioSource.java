package game.client.audio;

public interface IAudioSource {

	void setGain(float gain);
	void setPitch(float pitch);
	void setPosition(float x, float y, float z);
	void setVelocity(float x, float y, float z);
	void playSound(String sound);
	void delete();
}
