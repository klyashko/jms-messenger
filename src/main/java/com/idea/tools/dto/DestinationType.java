package com.idea.tools.dto;

import static com.idea.tools.utils.Checked.biFunction;

import java.util.function.BiFunction;
import javax.jms.Destination;
import javax.jms.Session;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

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

    public static DestinationType[] getTypes(ServerType type) {
        if (type != null) {
            switch (type) {
                case KAFKA:
                    return new DestinationType[]{TOPIC};
                default:
                    return new DestinationType[]{QUEUE, TOPIC};
            }
        }
        return new DestinationType[]{};
    }

}
