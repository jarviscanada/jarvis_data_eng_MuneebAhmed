package ca.jrvs.apps.jdbc.practice;

public class NotSoSimpleCalculatorImp implements NotSoSimpleCalculator{
    private SimpleCalculator calc;

    public NotSoSimpleCalculatorImp(SimpleCalculator calc) {
        this.calc = calc;
    }
    @Override
    public int power(int x, int y) {
        if(y == 0){
            return 1;
        }
        int power = x;
        for(int i = 1; i < y; i++){
            power = calc.multiply(power, x);
        }
        return power;
    }

    @Override
    public int abs(int x) {
        if (x < 0) return calc.subtract(0, x);
        return x;
    }

    @Override
    public double sqrt(int x) {
        if (x < 0) throw new IllegalArgumentException("Square root of negative number is not defined.");
        double sqrt = x;
        double temp;
        do {
            temp = sqrt;
            sqrt = (temp + (x / temp)) / 2;
        } while ((temp - sqrt) != 0);
        return sqrt;
    }
}
