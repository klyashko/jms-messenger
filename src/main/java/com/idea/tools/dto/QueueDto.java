package com.idea.tools.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString(exclude = "server")
@EqualsAndHashCode(exclude = "server")
public class QueueDto {

    private Integer id;
    private String name;
    private Server server;
    private boolean addedManually;

    public QueueDto(Integer id, String name, Server server) {
        this.id = id;
        this.name = name;
        this.server = server;
    }

    public QueueDto(Integer id, String name, Server server, boolean addedManually) {
        this(id, name, server);
        this.addedManually = addedManually;
    }
}
