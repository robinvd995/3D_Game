package game;

import java.util.LinkedList;
import java.util.Queue;

public class Timer {

	private long lastTime;
	private long lastTick;
	
	private int tickRate;
	
	private long timeSinceTick;
	
	private double deltaTime;
	private double tickTime;
	
	private FpsCounter fpsCounter = null;
	
	public Timer(int tickRate){
		this.tickRate = tickRate;
	}
	
	public void setInitialTime(){
		lastTime = System.nanoTime();
		lastTick = System.nanoTime();
	}
	
	public boolean update(){
		
		final long timerResolution = 1000000000L;
		boolean tick = false;
		
		long curTime = System.nanoTime();
		long delta = curTime - lastTime;
		deltaTime = ((double)delta / timerResolution);
		lastTime = curTime;
		
		timeSinceTick += delta;
		final float tickDelta = timerResolution / tickRate;
		
		if(timeSinceTick > tickDelta){
			if(timeSinceTick > timerResolution){
				long ticksSkipped = (long) (timeSinceTick / tickDelta);
				System.out.println("Skipping " + ticksSkipped + " ticks!");
				timeSinceTick = 0;
			}
			else{
				timeSinceTick -= tickDelta;
			}
			tickTime = (double)(curTime - lastTick) / timerResolution;
			lastTick = curTime;
			tick = true;
		}
		
		if(fpsCounter != null){
			fpsCounter.addFrame(deltaTime);
		}
		
		return tick;
	}
	
	public void setFpsTracking(boolean shouldTrack){
		if(shouldTrack){
			fpsCounter = new FpsCounter();
		}
		else{
			fpsCounter = null;
		}
	}
	
	public boolean isTrackingFps(){
		return fpsCounter != null;
	}
	
	public int getTickRate(){
		return tickRate;
	}
	
	public double getDeltaTime(){
		return deltaTime;
	}
	
	public double getTickTime(){
		return tickTime;
	}
	
	public int getFps(){
		return fpsCounter != null ? fpsCounter.getFps() : 0;
	}
	
	private static class FpsCounter {
		
		private Queue<Double> frames = new LinkedList<Double>();
		private float totalTime = 0.0f;
		
		private FpsCounter(){}
		
		public void addFrame(double delta){
			totalTime += delta;
			frames.add(delta);
			while(totalTime > 1.0f) pollFrame();
		}
		
		public void pollFrame(){
			double time = frames.poll();
			totalTime -= time;
		}
		
		public int getFps(){
			return frames.size();
		}
	}
}
