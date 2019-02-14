package factory.bean;

public class Bus implements Car {

    @Override
    public void gotoWork() {
        System.out.println("坐公交车去上班！");
    }
}
