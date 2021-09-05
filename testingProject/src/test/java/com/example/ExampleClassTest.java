package com.example;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class ExampleClassTest {

    @Test
    public void sumReturnsSumOfLeftAndRight() {
        int x = 5;
        int y = 2;
        int sum = new ExampleClass().sum(x, y);
        assertThat(sum).isEqualTo(x + y);
    }
}