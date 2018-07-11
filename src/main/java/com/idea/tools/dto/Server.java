package com.idea.tools.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class Server {

    private UUID id;
    private String name;
    private ServerType type;
    private ConnectionType connectionType;
    private String host;
    private Integer port;
    private String login;
    private String password;
    private List<QueueDto> queues = new ArrayList<>();

    private boolean nameIsAutogenerated = true;

}
