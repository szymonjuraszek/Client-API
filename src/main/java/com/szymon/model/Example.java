package com.szymon.model;

import lombok.Data;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

@Data
public class Example {

    private Long id;

    private String name;

    private Integer value;

    public Example() {
    }
}
