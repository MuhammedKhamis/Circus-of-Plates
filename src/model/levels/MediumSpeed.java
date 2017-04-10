package model.levels;

import controller.MainController;
import model.levels.util.LevelSpeedStrategy;

public class MediumSpeed extends LevelSpeedStrategy{

	private static final int mediumSpeed = 15;
	public MediumSpeed(final MainController mainController) {
		super(mediumSpeed, mainController);
	}

}
