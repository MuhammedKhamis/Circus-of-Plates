package model.gamestates;

public abstract class GameState {

	protected String message;
	public GameState() {

	}
	public String printProperMessage() {
		return message;
	}
}
