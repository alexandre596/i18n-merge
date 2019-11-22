package com.celfocus.omnichannel.digital.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;

import com.celfocus.omnichannel.digital.dto.FinalMerge;
import com.celfocus.omnichannel.digital.dto.Project;
import com.celfocus.omnichannel.digital.io.filter.util.CustomFileFilterUtils;
import com.celfocus.omnichannel.digital.utils.StringUtils;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class FileHelper {
	
	private FileHelper() {
		super();
	}
	
	public static File getProductionFile(Project p, String directory, String fileName) throws FileNotFoundException {
		File productionFile = new File(directory + "\\jcr_root\\apps\\" + p.getProjectName() + "\\i18n\\" + fileName + "");
		if(!productionFile.exists()) {
			throw new FileNotFoundException("Could not locate production i18n for project " + p.getProjectName());
		}
		
		return productionFile;
	}
	
	public static File getLocalFile(Project p, String excludeDirectories, String fileName) throws FileNotFoundException {
		List<File> files = (List<File>) FileUtils.listFiles(new File(p.getProjectPath() + "\\packages\\digital.cms.apps"), FileFilterUtils.nameFileFilter(fileName),
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
	
	public static Map<String, String> getFileContent(File file) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		InputStream is = new FileInputStream(file);
		return mapper.readValue(is, new TypeReference<Map<String, String>>(){});
	}
	
	public static void saveToFile(final FinalMerge finalMerge, final File file) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(finalMerge.getI18n());
		json = StringUtils.replaceCharacters(json);
		FileUtils.writeStringToFile(file, json, Charset.defaultCharset());
	}

}
