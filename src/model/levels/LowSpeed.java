package model.levels;

import controller.MainController;
import model.levels.util.LevelSpeedStrategy;

public class LowSpeed extends LevelSpeedStrategy{

	private static final int lowSpeed = 30;
	public LowSpeed(final MainController mainController) {
		super(lowSpeed, mainController);
	}

}
