package com.celfocus.omnichannel.digital.configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.celfocus.omnichannel.digital.dto.Project;
import com.celfocus.omnichannel.digital.io.filter.util.CustomFileFilterUtils;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

@Component
public class FileComponent {
	
	@Value("${i18n.file.name}")
	private String i18nFileName;
	
	@Value("${temporary.directory}")
	private String temporaryDirectory;
	
	@Value("${exclude.directories}")
	private String excludeDirectories;
	
	public List<FileHeader> getProductionI18nFile(final ZipFile zipFile) throws ZipException {
		List<FileHeader> fileHeaders = zipFile.getFileHeaders();
		return fileHeaders.stream()
				.filter(fileHeader -> fileHeader.getFileName()
						.matches("(jcr_root\\/apps\\/[a-zA-Z-]*\\/i18n\\/" + this.i18nFileName + ")"))
				.collect(Collectors.toList());
	}
	
	public File getProductionFile(Project p) throws FileNotFoundException {
		File productionFile = new File(temporaryDirectory + "\\jcr_root\\apps\\" + p.getProjectName() + "\\i18n\\" + this.i18nFileName + "");
		if(!productionFile.exists()) {
			throw new FileNotFoundException("Could not locate production i18n for project " + p.getProjectName());
		}
		
		return productionFile;
	}
	
	public File getLocalFile(Project p) throws FileNotFoundException {
		List<File> files = (List<File>) FileUtils.listFiles(new File(p.getProjectPath() + "\\packages\\digital.cms.apps"), FileFilterUtils.nameFileFilter(i18nFileName),
				CustomFileFilterUtils.unNameFileFilter(excludeDirectories));
		
		Optional<File> optional = files.stream()
				.filter(f -> f.getParentFile() != null && f.getParentFile().getAbsolutePath().endsWith("i18n"))
				.findFirst();
		
		if(!optional.isPresent()) {
			throw new FileNotFoundException("Could not locate local i18n for project " + p.getProjectName());
		}
		
		File file = optional.get();
		
		if(!file.exists()) {
			throw new FileNotFoundException("Could not locate local i18n for project " + p.getProjectName());
		}
		
		return file;
	}

}
