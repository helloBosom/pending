package proxy;

public class Student implements Study {
    @Override
    public void write() {
        System.out.println("I am writing");
    }

    @Override
    public void read() {
        System.out.println("I am reading");
    }
}
