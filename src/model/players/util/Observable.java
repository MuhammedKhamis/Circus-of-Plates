package model.players.util;

public interface Observable {

	public void addObserver(Observer observer);

	public void notifyObserver();
}
