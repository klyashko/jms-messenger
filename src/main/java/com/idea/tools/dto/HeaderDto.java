package com.idea.tools.dto;

import static java.util.Comparator.nullsLast;

import java.util.Comparator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeaderDto implements Comparable<HeaderDto> {

    private static final Comparator<String> COMPARATOR = nullsLast(String::compareToIgnoreCase);

    private String name;
    private String value;

    @Override
    public int compareTo(@NotNull HeaderDto o) {
        return COMPARATOR.compare(name, o.name);
    }
}
