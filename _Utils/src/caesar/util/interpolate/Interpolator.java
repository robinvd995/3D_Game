package caesar.util.interpolate;

import java.util.LinkedList;

public class Interpolator<T extends IInterpolatable<T>> {

	private final double maxTime;
	private final LinkedList<InterpolatorEntry<T>> interpolationList;
	
	public Interpolator(double maxTime){
		this.maxTime = maxTime;
		this.interpolationList = new LinkedList<InterpolatorEntry<T>>();
	}
	
	public void addInterpolation(double time, T object){
		if(time >= maxTime)
			throw new IllegalArgumentException("The given time exceeds the maximum time!");
		
		InterpolatorEntry<T> newEntry = new InterpolatorEntry<T>(time, object);
		
		if(time == 0.0f || interpolationList.isEmpty()){
			interpolationList.addFirst(newEntry);
			return;
		}
		
		double prevTime = 0.0f;
		for(int i = 0; i < interpolationList.size(); i++){
			InterpolatorEntry<T> entry = interpolationList.get(i);
			if(time > prevTime && time <= entry.time){
				interpolationList.add(i, newEntry);
				return;
			}
		}
		
		interpolationList.addLast(newEntry);
	}
	
	public T getInterpolatedValue(double time){
		
		if(interpolationList.size() == 1)
			return interpolationList.get(0).object;
		
		int currentIndex = calcCurrentIndex(time);
		
		int nextIndex = (currentIndex + 1) % interpolationList.size();
		
		InterpolatorEntry<T> currentEntry = interpolationList.get(currentIndex);
		InterpolatorEntry<T> nextEntry = interpolationList.get(nextIndex);
		
		double startTime = currentEntry.time;
		double endTime = nextEntry.time;
		
		if(currentIndex > nextIndex){
			endTime += maxTime;
		}
		
		double interpolationFactor = (time - startTime) / (endTime - startTime);
		T interpolatable = currentEntry.object;
		return interpolatable.interpolate(interpolationFactor, nextEntry.object);
	}
	
	public int calcCurrentIndex(double time){
		int size = interpolationList.size();
		for(int i = 0; i < size; i++){
			int curIndex = i % size;
			int nextIndex = (curIndex + 1) % size;
			
			InterpolatorEntry<T> curEntry = interpolationList.get(curIndex);
			InterpolatorEntry<T> nextEntry = interpolationList.get(nextIndex);
			
			double startTime = curEntry.time;
			double endTime = nextEntry.time;
			
			if(endTime < startTime){
				endTime += maxTime;
			}
			
			if(time >= startTime && time < endTime){
				return curIndex;
			}
		}
		return size - 1;
		//throw new RuntimeException("Could not find current index");
	}
	
	private static class InterpolatorEntry<T extends IInterpolatable<T>> {
		
		private final double time;
		private final T object;
		
		public InterpolatorEntry(double time, T object){
			this.time = time;
			this.object = object;
		}
	}
}
