package model.players;

import java.io.File;

import controller.ResourcesManager;
import model.players.util.Observer;

public class Player2 extends AbstractPlayer {

    public Player2(final ResourcesManager resourcesManager) {
    	super(resourcesManager);
        imageName = "Clown2.png";
        imagePath = System.getProperty("user.dir") + File.separator + "ClownImages" + File.separator + imageName;
        stacksCenter = new float[][] {{21, -61}, {21 + 104, -73}};
        playerPosition = new int[][]{{100,520}};
        loadImage();
    }

    @Override
	public void addObserver(final Observer observer) {
		observers.add(observer);
	}

	@Override
	public void notifyObserver() {
		for (final Observer observer : observers) {
			observer.update(1);
		}
	}

}
