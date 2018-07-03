package com.idea.tools.dto;

import lombok.Data;

@Data
public class Queue {

    private String name;

    public Queue(String name) {
        this.name = name;
    }
}
