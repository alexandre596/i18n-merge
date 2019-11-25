package com.celfocus.omnichannel.digital.configuration;

import java.io.File;
import java.io.IOException;

import javax.annotation.PreDestroy;

import org.apache.commons.io.FileUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;

@Component
public class ShutdownHookConfiguration {

	@Value("${temporary.directory}")
	private String temporaryDirectory;
	
	private static final Logger LOG = LoggerFactory.getLogger(ShutdownHookConfiguration.class);
	
	@PreDestroy
    public void destroy() {
		File tempDir = new File(temporaryDirectory);
		
		if(tempDir.exists() && tempDir.isDirectory()) {
			try {
				FileUtils.deleteDirectory(tempDir);
				LOG.info("Temporary directory deleted successfully");
			} catch (IOException e) {
				LOG.error("Fail to delete temporary directory", e);
			}
		} else {
			LOG.warn("No temporary directory to delete");
		}
    }
}
