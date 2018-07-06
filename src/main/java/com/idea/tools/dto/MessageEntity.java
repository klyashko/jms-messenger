package com.idea.tools.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(exclude = "queue")
@EqualsAndHashCode(exclude = "queue")
public class MessageEntity {

    private Long timestamp;
    private String jmsType;
    private ContentType type;
    private String payload;
    private Queue queue;

}
