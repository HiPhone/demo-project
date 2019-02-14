package adapter;

public abstract class CarController {

    public void move() {
        System.out.println("玩具汽车正在移动");
    }

    //抽象两个方法一个控制声音，一个控制灯光，这两个方法是另外两个接口的方法，需要通过适配器获取。
    public abstract void voice();
    public abstract void light();

}
