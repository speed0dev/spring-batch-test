package com.example.demo.custom;

import org.springframework.batch.item.file.transform.LineAggregator;

public class CustomPassThroughLineAggregator<T> implements LineAggregator<T> {

    @Override
    public String aggregate(T item) {

        return "Add}" + item.toString();
    }

}
