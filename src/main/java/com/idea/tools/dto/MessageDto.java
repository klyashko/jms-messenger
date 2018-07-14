package com.idea.tools.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Data
@ToString(exclude = "queue")
@EqualsAndHashCode(exclude = "queue")
public class MessageDto {

    private String messageID;
    private String correlationId;
    private Long timestamp;
    private String jmsType;
    private ContentType type;
    private String payload;
    private Integer deliveryMode;
    private Integer priority;
    private Long expiration;
    private QueueDto queue;
    private Map<String, Object> headers = new HashMap<>();

}
