package com.idea.tools.dto;

import com.intellij.util.xmlb.annotations.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

@Data
@NoArgsConstructor
@ToString(exclude = "server")
@EqualsAndHashCode(exclude = "server")
public class QueueDto implements Comparable<QueueDto> {

    private static final Comparator<String> COMPARATOR = Comparator.nullsLast(String::compareToIgnoreCase);

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

    @Override
    public int compareTo(@NotNull QueueDto o) {
        return COMPARATOR.compare(name, o.name);
    }
}
