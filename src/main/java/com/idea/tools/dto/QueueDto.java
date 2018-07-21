package com.idea.tools.dto;

import com.intellij.util.xmlb.annotations.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString(exclude = "server")
@EqualsAndHashCode(exclude = "server")
public class QueueDto {

    private String id;
    private String name;
    private ServerDto server;
    private boolean addedManually;

    public QueueDto(String id, String name, ServerDto server) {
        this.id = id;
        this.name = name;
        this.server = server;
    }

    public QueueDto(String id, String name, ServerDto server, boolean addedManually) {
        this(id, name, server);
        this.addedManually = addedManually;
    }

    @Transient
    public ServerDto getServer() {
        return server;
    }

}
