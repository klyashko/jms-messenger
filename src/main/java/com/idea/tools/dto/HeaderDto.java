package com.idea.tools.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeaderDto implements Comparable<HeaderDto> {

    private static final Comparator<String> COMPARATOR = Comparator.nullsLast(String::compareToIgnoreCase);

    private String name;
    private String value;

    @Override
    public int compareTo(@NotNull HeaderDto o) {
        return COMPARATOR.compare(name, o.name);
    }
}
