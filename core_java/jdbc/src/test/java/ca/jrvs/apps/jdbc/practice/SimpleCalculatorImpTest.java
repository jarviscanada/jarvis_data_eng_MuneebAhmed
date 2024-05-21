package ca.jrvs.apps.jdbc.practice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleCalculatorImpTest {
    SimpleCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new SimpleCalculatorImp();
    }

    @Test
    void add() {
        int expected = 2;
        int actual = calculator.add(1, 1);
        assertEquals(expected, actual);
    }

    @Test
    void subtract() {
        assertEquals(0, calculator.subtract(1, 1));
        assertEquals(-1, calculator.subtract(1, 2));
        assertEquals(10, calculator.subtract(20, 10));
    }

    @Test
    void multiply() {
        assertEquals(1, calculator.multiply(1, 1));
        assertEquals(0, calculator.multiply(1, 0));
        assertEquals(50, calculator.multiply(5, 10));
    }

    @Test
    void divide() {
        assertEquals(1.0, calculator.divide(1, 1));
        assertThrows(ArithmeticException.class, () -> calculator.divide(1, 0));
        assertEquals(0.5, calculator.divide(1, 2));
        assertEquals(2.5, calculator.divide(5, 2));
    }
}