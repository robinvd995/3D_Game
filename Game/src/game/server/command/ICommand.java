package game.server.command;

public interface ICommand {

	String[] getPrefix();
	void execute(String[] args);
	int[] argumentCounts();
}
