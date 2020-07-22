package com.szymon.testBeans;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FirstBeanExampleTest {

    private FirstBeanExample firstBeanExample;

    @Mock
    private SecondBeanExample secondBeanExample;

    @BeforeEach
    void setUp() {
        firstBeanExample = new FirstBeanExample(secondBeanExample);
    }

    //    verify sprawdza ile razy zostala wykonanan dana metoda
    @Test
    void getValue() {
        firstBeanExample.getValue();
        firstBeanExample.getValue();

        verify(secondBeanExample, times(2)).someMethod();
    }
}
