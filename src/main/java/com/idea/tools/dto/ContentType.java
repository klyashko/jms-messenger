package com.idea.tools.dto;

import lombok.RequiredArgsConstructor;

import javax.jms.Message;
import javax.jms.Session;
import java.util.function.BiFunction;

import static com.idea.tools.utils.Checked.biFunction;

@RequiredArgsConstructor
public enum ContentType {

    TEXT(biFunction((session, msg) -> session.createTextMessage(msg.getPayload())));

    private final BiFunction<Session, MessageEntity, Message> creator;

    public Message create(Session session, MessageEntity msg) {
        return creator.apply(session, msg);
    }

}
