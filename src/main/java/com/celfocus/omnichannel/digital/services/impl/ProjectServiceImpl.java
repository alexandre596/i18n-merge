package com.celfocus.omnichannel.digital.services.impl;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.celfocus.omnichannel.digital.exception.InvalidFileException;
import com.celfocus.omnichannel.digital.services.ProjectService;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

@Service
public class ProjectServiceImpl implements ProjectService {
	
	private static final Logger LOG = LoggerFactory.getLogger(ProjectServiceImpl.class);

	@Override
	public List<String> getProjectsFromZip(final String filePath) throws InvalidFileException {
		if (!this.fileExists(filePath)) {
			throw new InvalidFileException();
		}

		try {
			if (!this.isZipFileValid(filePath)) {
				throw new InvalidFileException();
			}

			return this.getProjectData(filePath);
		} catch (ZipException e) {
			throw new InvalidFileException(e);
		}
	}

	private boolean fileExists(final String filePath) {
		LOG.debug("Checking if file exists");
		return new File(filePath).exists();
	}

	private boolean isZipFileValid(final String filePath) throws ZipException {
		LOG.debug("Checking if the zip file contains all the files we need");
		List<FileHeader> fileHeaders = new ZipFile(filePath).getFileHeaders();
		return fileHeaders.stream()
				.anyMatch(fileHeader -> fileHeader.getFileName().matches("(jcr_root\\/apps\\/[a-zA-Z-]*\\/i18n\\/)"));
	}

	private List<String> getProjectData(final String filePath) throws ZipException {
		LOG.debug("Retrieving all the project names from the zip file");
		List<FileHeader> fileHeaders = new ZipFile(filePath).getFileHeaders();
		return fileHeaders.stream()
				.filter(fileHeader -> fileHeader.getFileName().matches("(jcr_root\\/apps\\/[a-zA-Z-]*\\/i18n\\/)"))
				.map(fileHeader -> StringUtils.substringBetween(fileHeader.getFileName(), "jcr_root/apps/", "/i18n"))
				.sorted().collect(Collectors.toList());
	}

}
