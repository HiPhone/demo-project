package singleton;

public class Hunger {

    private static Hunger hunger = new Hunger();

    private Hunger() {}

    public static Hunger getInstance() {
        return hunger;
    }
}
