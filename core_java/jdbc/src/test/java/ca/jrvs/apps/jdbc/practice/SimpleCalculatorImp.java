package ca.jrvs.apps.jdbc.practice;

public class SimpleCalculatorImp implements SimpleCalculator{

    @Override
    public int add(int x, int y) {
        return x + y;
    }

    @Override
    public int subtract(int x, int y) {
        return x - y;
    }

    @Override
    public int multiply(int x, int y) {
        return x * y;
    }

    @Override
    public double divide(int x, int y) {
        if(y == 0){
            throw new ArithmeticException();
        }
        return (double) x / y;
    }
}
