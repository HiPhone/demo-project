package builder;

import builder.computers.Computer;
import builder.computers.ComputerBuilder;
import builder.computers.HPComputerBuilder;

public class Test {

    public static void main(String[] args) {
        Director director = new Director();
        ComputerBuilder hp = new HPComputerBuilder();

        director.setComputerBuilder(hp);
        director.constructComputer();
        Computer pc = director.getComputer();
    }
}
