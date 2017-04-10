package controller;

public class Paused {

    private static boolean isPaused = false;

    public  synchronized static void changeState() {
        isPaused = !isPaused;
    }

    public static boolean getState() {
        return isPaused;
    }
    
    public static void setState(boolean state){
        isPaused = state;
    }

}
