package model.levels.util;

import controller.MainController;

public abstract class LevelSpeedStrategy {

	private final MainController mainController;
	private final int speed;

	public LevelSpeedStrategy(final int speed, final MainController mainController) {
		this.speed = speed;
		this.mainController = mainController;
	}

	public void start() {
		mainController.setDifficulty(speed);
		//mainController.initialize();
	}
}
