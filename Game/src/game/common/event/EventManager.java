package game.common.event;

import java.util.ArrayList;
import java.util.List;

import com.google.common.eventbus.EventBus;

public class EventManager {

	private static final EventBus EVENT_BUS = new EventBus();
	
	private static List<Object> preUpdateEvents = new ArrayList<Object>();
	private static List<Object> postUpdateEvents = new ArrayList<Object>();
	
	public static void registerEventListener(Object eventListener){
		EVENT_BUS.register(eventListener);
	}
	
	public static void unregisterEventListener(Object eventListener){
		EVENT_BUS.unregister(EVENT_BUS);
	}
	
	public static void postPreUpdateEvent(Object event){
		preUpdateEvents.add(event);
	}
	
	public static void postEvent(Object event){
		EVENT_BUS.post(event);
	}
	
	public static void postPostUpdateEvent(Object event){
		postUpdateEvents.add(event);
	}
	
	public static void firePreUpdateEvents(){
		Object[] array = preUpdateEvents.toArray();
		for(int i = 0; i < array.length; i++){
			EVENT_BUS.post(array[i]);
		}
		preUpdateEvents.clear();
	}
	
	public static void firePostUpdateEvents(){
		for(int i = 0; i < postUpdateEvents.size(); i++){
			Object o = postUpdateEvents.get(i);
			EVENT_BUS.post(o);
		}
		postUpdateEvents.clear();
	}
}
