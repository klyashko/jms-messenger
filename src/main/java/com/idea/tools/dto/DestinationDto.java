package com.idea.tools.dto;

import com.intellij.util.xmlb.annotations.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
@ToString(exclude = {"server", "templates"})
@EqualsAndHashCode(exclude = {"server", "templates"})
public class DestinationDto implements Comparable<DestinationDto> {

    private static final Comparator<String> COMPARATOR = Comparator.nullsLast(String::compareToIgnoreCase);

    private String id;
    private String name;
    private ServerDto server;
    private boolean addedManually;
    private DestinationType type;
    private List<TemplateMessageDto> templates = new ArrayList<>();

    @Transient
    public ServerDto getServer() {
        return server;
    }

    @Override
    public int compareTo(@NotNull DestinationDto o) {
        return COMPARATOR.compare(name, o.name);
    }

}
