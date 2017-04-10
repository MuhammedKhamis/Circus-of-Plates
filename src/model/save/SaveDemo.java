package model.save;


public class SaveDemo {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        Snapshot test = new Snapshot();
        String t = "test";
        test.saveShot(System.getProperty("user.dir"), t);
        test.LoadDate(System.getProperty("user.dir"), t);
    }

}
