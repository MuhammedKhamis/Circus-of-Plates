package model.shapes.dynamicloading;

public class HelloWorld {

    static {
        System.out.println("static block is initialised indicating that the class was initialised somewhere");
    }

    public static void main(String[] args) {
        System.out.println("Hello World");
    }

}
