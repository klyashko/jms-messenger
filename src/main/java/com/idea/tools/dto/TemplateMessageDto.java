package com.idea.tools.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TemplateMessageDto extends MessageDto {

    private static final Comparator<String> COMPARATOR = Comparator.nullsLast(String::compareToIgnoreCase);

    private String id;
    private String name;

    @Override
    public int compareTo(@NotNull MessageDto that) {
        if (that instanceof TemplateMessageDto) {
            return COMPARATOR.compare(name, ((TemplateMessageDto) that).name);
        }
        return super.compareTo(that);
    }

}
