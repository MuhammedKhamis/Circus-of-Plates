package model.shapes.util;

import java.util.Collection;
import java.util.HashMap;

import logging.Logging;
import model.shapes.interfaces.Shape;

public class PlatesFactory {

	private static PlatesFactory factory
					= new PlatesFactory();
	private final HashMap<String, Class<? extends Shape>>
					registeredShapes;

	public PlatesFactory() {
		registeredShapes = new HashMap<>();
	}

	public static PlatesFactory getInstance() {
		return factory;
	}

	public void registerShape(final String key,
			final Class<? extends Shape> shapeClass) {
		Logging.info("Registered Successfully " + key);
		registeredShapes.put(key, shapeClass);
	}

	public Shape getShape(final String key) {
		try {
			final Class<? extends Shape> shapeClass
			= registeredShapes.get(key);
			return shapeClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			return null;
		}
	}

	public Collection<String> getRegisteredShapes() {
        return registeredShapes.keySet();
    }
}
