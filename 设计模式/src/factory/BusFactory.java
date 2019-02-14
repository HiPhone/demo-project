package factory;

import factory.bean.Bus;
import factory.bean.Car;

public class BusFactory implements ICarFactory {

    @Override
    public Car getCar() {
        return new Bus();
    }
}
