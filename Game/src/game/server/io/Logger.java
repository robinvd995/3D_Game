package game.server.io;

public class Logger {

	private static String prefix;
	
	private static OutStream infoStream;
	private static OutStream errorStream;
	
	public static void initOutputStreams(String pre){
		prefix = pre;
		infoStream = new OutStream(System.out);
		System.setOut(infoStream);
		
		errorStream = new OutStream(System.err);
		System.setErr(errorStream);
	}
	
	public static void addInfoStreamListener(IStreamListener listener){
		if(infoStream != null)
			infoStream.addStreamListener(listener);
	}
	
	public static void addErrorStreamListener(IStreamListener listener){
		if(errorStream != null)
			errorStream.addStreamListener(listener);
	}
	
	public boolean hasInfoStream(){
		return infoStream != null;
	}
	
	public boolean hasErrorStream(){
		return errorStream != null;
	}
	
	public static void logError(String message){
		String pre = "[" + prefix + "][ERROR]: ";
		System.err.println(pre + message);
	}
	
	public static void logInfo(String message){
		String pre = "[" + prefix + "][INFO]: ";
		System.out.println(pre + message);
	}
}
