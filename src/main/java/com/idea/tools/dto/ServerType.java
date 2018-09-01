package com.idea.tools.dto;

import lombok.Getter;

import javax.swing.*;

import static com.idea.tools.dto.ConnectionType.*;
import static com.idea.tools.utils.IconUtils.*;

@Getter
public enum ServerType {

	ACTIVE_MQ(getActiveMqIcon(), ACTIVE_MQ_CONNECTION_TYPES),
	ARTEMIS(getArtemisIcon(), ARTEMIS_CONNECTION_TYPES),
	HORNETQ(getHornetIcon(), HORNETQ_CONNECTION_TYPES),
	KAFKA(getKafkaIcon(), KAFKA_CONNECTION_TYPES),
	RABBIT_MQ(getRabbitMqIcon(), RABBIT_MQ_CONNECTION_TYPES);

	private final Icon icon;
	private final ConnectionType[] connectionTypes;

	ServerType(Icon icon, ConnectionType... connectionTypes) {
		this.icon = icon;
		this.connectionTypes = connectionTypes;
	}

}
