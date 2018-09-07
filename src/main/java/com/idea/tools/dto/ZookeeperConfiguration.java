package com.idea.tools.dto;

import lombok.Data;

@Data
public class ZookeeperConfiguration {

	private String clientId;
	private String zookeeperHost;
	private Integer zookeeperPort;

}
