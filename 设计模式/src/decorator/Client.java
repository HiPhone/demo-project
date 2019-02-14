package decorator;

public class Client {

    //在不必改变原类文件和原类使用的继承的情况下，动态地扩展一个对象的功能。
    public static void main(String[] args) {
        TheGreatestSage sage = new Monkey();
        //第一种写法 单层装饰
        TheGreatestSage bird = new Bird(sage);
        TheGreatestSage fish = new Fish(bird);

        //第二种写法 双层装饰
        //TheGreatestSage fish = new Fish(new Bird(sage));
        fish.move();
        ((Fish) fish).test();
    }
}
