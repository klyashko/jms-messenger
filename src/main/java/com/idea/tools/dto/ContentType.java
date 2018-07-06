package com.idea.tools.dto;

import lombok.RequiredArgsConstructor;

import javax.jms.Message;
import javax.jms.Session;
import java.util.function.BiFunction;

import static com.idea.tools.utils.Checked.biFunction;

@RequiredArgsConstructor
public enum ContentType {

    TEXT(biFunction((session, msg) -> session.createTextMessage(msg.getPayload())));

    private final BiFunction<Session, MessageDto, Message> creator;

    public Message create(Session session, MessageDto msg) {
        return creator.apply(session, msg);
    }

}
