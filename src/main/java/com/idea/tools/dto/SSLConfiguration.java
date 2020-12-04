package com.idea.tools.dto;

import lombok.Data;

@Data
public class SSLConfiguration {

	private String truststore;
	private String truststorePassword;
	private String keystore;
	private String keystorePassword;

}
