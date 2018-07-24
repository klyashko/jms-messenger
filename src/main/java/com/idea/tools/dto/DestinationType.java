package com.idea.tools.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.jms.Destination;
import javax.jms.Session;
import java.util.function.BiFunction;

import static com.idea.tools.utils.Checked.biFunction;

@RequiredArgsConstructor
public enum DestinationType {

    QUEUE("Queues", biFunction((s, d) -> s.createQueue(d.getName()))),
    TOPIC("Topics", biFunction((s, d) -> s.createTopic(d.getName())));

    @Getter
    private final String label;
    private final BiFunction<Session, DestinationDto, Destination> destination;

    public Destination createDestination(Session session, DestinationDto dto) {
        return destination.apply(session, dto);
    }

}
