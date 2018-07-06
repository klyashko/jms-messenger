package com.idea.tools.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString(exclude = "server")
@EqualsAndHashCode(exclude = "server")
public class Queue {

    private Integer id;
    private String name;
    private Server server;
    private boolean addedManually;

    public Queue(Integer id, String name, Server server) {
        this.id = id;
        this.name = name;
        this.server = server;
    }
}
