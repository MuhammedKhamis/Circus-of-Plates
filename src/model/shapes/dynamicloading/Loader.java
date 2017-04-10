package model.shapes.dynamicloading;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import model.shapes.interfaces.Shape;

public class Loader {

    private static Loader mainloader;

    private static final String classExt = ".class";
    private static final String ShapeDir = "model" + File.separator + "shapes" + File.separator;
    private static final String BiName = "model.shapes.";
    

    private Loader() {

    }

    public static Loader getInstance() {
        if (mainloader == null) {
            mainloader = new Loader();
        }
        return mainloader;
    }

    // loading any .Class that includes Shape
    public void invokeClassMethod(File file) {
        try {
            if (file.isDirectory())
                return;
            String classFile = file.getName();
            String className = "";
            String filePath = file.getPath();
            if (classFile.endsWith(classExt)) {
                className = classFile.replaceAll(classExt, "");
                if (filePath.contains(ShapeDir)) {
                    file = new File(filePath.substring(0, filePath.indexOf(ShapeDir)));
                }
            }

            URLClassLoader loader = URLClassLoader.newInstance(new URL[] {file.toURI().toURL()});
            @SuppressWarnings("unchecked")
            Class<Shape> loadedClass = (Class<Shape>) (loader.loadClass(BiName + className));
            Class.forName(loadedClass.getName());
            // final Constructor<Shape> constructor =
            // loadedClass.getConstructor();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
