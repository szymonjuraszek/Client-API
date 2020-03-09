package com.szymon.testBeans;

import org.springframework.stereotype.Component;

@Component
public class FirstBeanExample {

    private final SecondBeanExample secondBeanExample;

    public FirstBeanExample(SecondBeanExample secondBeanExample) {
        this.secondBeanExample = secondBeanExample;
    }

    public int getValue() {
        secondBeanExample.someMethod();
        return 10;
    }
}
