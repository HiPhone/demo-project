package decorator;

public class Fish extends Change {

    public Fish(TheGreatestSage sage) {
        super(sage);
    }

    @Override
    public void move() {
        System.out.println("Fish move");
    }

    public void test() {
        System.out.println("非覆盖的装饰");
    }
}
