package com.celfocus.omnichannel.digital.services.impl;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.celfocus.omnichannel.digital.exception.InvalidFileException;
import com.celfocus.omnichannel.digital.services.ProjectService;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

@Service
public class ProjectServiceImpl implements ProjectService {

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
		return new File(filePath).exists();
	}

	private boolean isZipFileValid(final String filePath) throws ZipException {
		List<FileHeader> fileHeaders = new ZipFile(filePath).getFileHeaders();
		return fileHeaders.stream()
				.anyMatch(fileHeader -> fileHeader.getFileName().matches("(jcr_root\\/apps\\/[a-zA-Z-]*\\/i18n\\/)"));
	}

	private List<String> getProjectData(final String filePath) throws ZipException {
		List<FileHeader> fileHeaders = new ZipFile(filePath).getFileHeaders();
		return fileHeaders.stream()
				.filter(fileHeader -> fileHeader.getFileName().matches("(jcr_root\\/apps\\/[a-zA-Z-]*\\/i18n\\/)"))
				.map(fileHeader -> StringUtils.substringBetween(fileHeader.getFileName(), "jcr_root/apps/", "/i18n"))
				.sorted().collect(Collectors.toList());
	}

}
