package com.idea.tools.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TemplateMessageDto extends MessageDto implements Comparable<TemplateMessageDto> {

    private static final Comparator<String> COMPARATOR = Comparator.nullsLast(String::compareToIgnoreCase);

    private String id;
    private String name;

    @Override
    public int compareTo(@NotNull TemplateMessageDto template) {
        return COMPARATOR.compare(name, template.name);
    }
}
