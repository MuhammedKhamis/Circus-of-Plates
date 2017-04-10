package model.levels.util;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.paint.Color;

public class ColorMap {

	private final Map<String, Color> colorMap;
	public ColorMap() {
		colorMap = new HashMap<>();
		colorMap.put(Color.RED.toString(), Color.RED);
		colorMap.put(Color.YELLOW.toString(), Color.YELLOW);
	}
	public Color getColor(final String color) {
		return colorMap.get(color);
	}

}
