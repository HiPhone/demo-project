package factory;

import factory.bean.Bike;
import factory.bean.Car;

public class BikeFactory implements ICarFactory {

    @Override
    public Car getCar() {
        return new Bike();
    }
}
