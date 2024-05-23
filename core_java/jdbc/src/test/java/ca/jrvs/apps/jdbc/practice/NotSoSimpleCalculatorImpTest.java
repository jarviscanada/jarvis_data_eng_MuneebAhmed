package ca.jrvs.apps.jdbc.practice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotSoSimpleCalculatorImpTest {
    NotSoSimpleCalculator calc;

    @Mock
    SimpleCalculator mockSimpleCalc;

    @BeforeEach
    void setUp() {
        calc = new NotSoSimpleCalculatorImp(mockSimpleCalc);
    }

    @Test
    void power() {
        when(mockSimpleCalc.multiply(2, 2)).thenReturn(4);
        when(mockSimpleCalc.multiply(4, 2)).thenReturn(8);
        assertEquals(8, calc.power(2, 3));
    }

    @Test
    void abs() {
        when(mockSimpleCalc.subtract(0, -10)).thenReturn(10);
        int expected = 10;
        int actual = calc.abs(-10);
        assertEquals(expected, actual);
    }

    @Test
    void sqrt() {
        assertEquals(2.0, calc.sqrt(4), 0.01);
    }
}