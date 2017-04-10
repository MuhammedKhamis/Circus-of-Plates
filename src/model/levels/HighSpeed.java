package model.levels;

import controller.MainController;
import model.levels.util.LevelSpeedStrategy;

public class HighSpeed extends LevelSpeedStrategy{

	private static final int highSpeed = 5;
	public HighSpeed(final MainController mainController) {
		super(highSpeed, mainController);
	}

}
