package com.idea.tools.dto;

import static com.idea.tools.dto.ConnectionType.ACTIVE_MQ_CONNECTION_TYPES;
import static com.idea.tools.dto.ConnectionType.ARTEMIS_CONNECTION_TYPES;
import static com.idea.tools.dto.ConnectionType.HORNETQ_CONNECTION_TYPES;
import static com.idea.tools.dto.ConnectionType.KAFKA_CONNECTION_TYPES;
import static com.idea.tools.dto.ConnectionType.RABBIT_MQ_CONNECTION_TYPES;
import static com.idea.tools.utils.IconUtils.getActiveMqIcon;
import static com.idea.tools.utils.IconUtils.getArtemisIcon;
import static com.idea.tools.utils.IconUtils.getHornetIcon;
import static com.idea.tools.utils.IconUtils.getKafkaIcon;
import static com.idea.tools.utils.IconUtils.getRabbitMqIcon;

import javax.swing.*;
import lombok.Getter;

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
