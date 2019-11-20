package com.celfocus.omnichannel.digital.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.celfocus.omnichannel.digital.dto.FinalMerge;
import com.celfocus.omnichannel.digital.dto.MergeStatus;
import com.celfocus.omnichannel.digital.dto.Project;
import com.celfocus.omnichannel.digital.dto.ResolvedMerge;
import com.celfocus.omnichannel.digital.exception.CouldNotLocateCorrectFileException;
import com.celfocus.omnichannel.digital.exception.InvalidFileException;
import com.celfocus.omnichannel.digital.exception.InvalidJsonException;
import com.celfocus.omnichannel.digital.io.filter.util.CustomFileFilterUtils;
import com.celfocus.omnichannel.digital.services.MergeFilesService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.MapDifference;
import com.google.common.collect.MapDifference.ValueDifference;
import com.google.common.collect.Maps;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

@Service
public class MergeFilesServiceImpl implements MergeFilesService {
	
	@Value("${i18n.file.name}")
	private String i18nFileName;
	
	@Value("${temporary.directory}")
	private String temporaryDirectory;
	
	@Value("${exclude.directories}")
	private String excludeDirectories;

	@Override
	public Map<Project, MergeStatus> getMergeStatus(final String productionFilePath, final List<Project> projects) throws InvalidFileException {
		
		Map<Project, MergeStatus> mergeStatusList = new LinkedHashMap<>();

		try {
			this.extractAllProductionFiles(productionFilePath);
			
			for(Project p : projects) {
				File productionFile = this.getProductionFile(p);
				File localFile = this.getLocalFile(p);
				
				Map<String, String> productionMap = this.getFileContent(productionFile);
				Map<String, String> localMap = this.getFileContent(localFile);
				mergeStatusList.put(p, this.getDifferences(productionMap, localMap));
			}
			
		} catch (ZipException e) {
			throw new InvalidFileException("Could not read the zip file properly", e);
		} catch (FileNotFoundException e) {
			throw new CouldNotLocateCorrectFileException(e);
		} catch (JsonParseException | JsonMappingException e) {
			throw new InvalidJsonException(e);
		} catch (IOException e) {
			throw new InvalidFileException("Failed IO Operation", e);
		}

		return mergeStatusList;
	}
	
	@Override
	public List<FinalMerge> doMerge(final Map<Project, ResolvedMerge> resolvedMergeMap) throws InvalidFileException {
		List<FinalMerge> finalMergeList = new ArrayList<>();
		
		for(Entry<Project, ResolvedMerge> resolvedMergeEntry : resolvedMergeMap.entrySet()) {
			try {
				FinalMerge finalMerge = new FinalMerge(resolvedMergeEntry.getKey());
				File localFile = this.getLocalFile(resolvedMergeEntry.getKey());
				Map<String, String> localMap = this.getFileContent(localFile);
				finalMerge.setI18n(localMap);
				
				// Remove as novas que não é para adicionar
				for(Entry<String, String> newValue: resolvedMergeEntry.getValue().getNewLines().entrySet()) {
					finalMerge.getI18n().remove(newValue.getKey());
				}
				
				// Atualiza os valores das linhas a serem atualizadas
				finalMerge.getI18n().putAll(resolvedMergeEntry.getValue().getUpdatedLines());
				
				// Remove quem tem que remover.
				for(Entry<String, String> removedValue : resolvedMergeEntry.getValue().getRemovedLines().entrySet()) {
					finalMerge.getI18n().remove(removedValue.getKey());
				}
				
				finalMergeList.add(finalMerge);
			} catch (FileNotFoundException e) {
				throw new CouldNotLocateCorrectFileException(e);
			} catch (JsonParseException | JsonMappingException e) {
				throw new InvalidJsonException(e);
			} catch (IOException e) {
				throw new InvalidFileException(e);
			}
		}
		
		return finalMergeList;
	}
	
	@Override
	public void saveToFile(FinalMerge finalMerge) throws InvalidFileException {
		try {
			File localFile = this.getLocalFile(finalMerge.getProject());
			
			if(!localFile.exists()) {
				throw new CouldNotLocateCorrectFileException("Local file does not exist.");	
			}
			
			this.saveToFile(finalMerge, localFile);
		} catch (FileNotFoundException e) {
			throw new CouldNotLocateCorrectFileException(e);
		} catch (IOException e) {
			throw new InvalidFileException(e);
		}
	}

	private void extractAllProductionFiles(final String productionFilePath) throws ZipException {
		ZipFile zipFile = new ZipFile(productionFilePath);
		List<FileHeader> productionFiles = this.getProductionI18nFile(zipFile);
		
		for(FileHeader p : productionFiles) {
			zipFile.extractFile(p, this.temporaryDirectory);
		}
	}

	private List<FileHeader> getProductionI18nFile(final ZipFile zipFile) throws ZipException {
		List<FileHeader> fileHeaders = zipFile.getFileHeaders();
		return fileHeaders.stream()
				.filter(fileHeader -> fileHeader.getFileName()
						.matches("(jcr_root\\/apps\\/[a-zA-Z-]*\\/i18n\\/" + this.i18nFileName + ")"))
				.collect(Collectors.toList());
	}
	
	private File getProductionFile(Project p) throws FileNotFoundException {
		File productionFile = new File(temporaryDirectory + "\\jcr_root\\apps\\" + p.getProjectName() + "\\i18n\\" + this.i18nFileName + "");
		if(!productionFile.exists()) {
			throw new FileNotFoundException("Could not locate production i18n for project " + p.getProjectName());
		}
		
		return productionFile;
	}
	
	private File getLocalFile(Project p) throws FileNotFoundException {
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
	
	private Map<String, String> getFileContent(File file) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		InputStream is = new FileInputStream(file);
		return mapper.readValue(is, new TypeReference<Map<String, String>>(){});
	}
	
	private void saveToFile(final FinalMerge finalMerge, final File file) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(finalMerge.getI18n());
		FileUtils.writeStringToFile(file, json, Charset.defaultCharset());
	}
	
	private MergeStatus getDifferences(Map<String, String> productionMap, Map<String, String> localMap) {
		MergeStatus mergeStatus = new MergeStatus();
		
		MapDifference<String, String> diff = Maps.difference(productionMap, localMap);
		
		mergeStatus.setNewLines(diff.entriesOnlyOnLeft());
		mergeStatus.setRemovedLines(diff.entriesOnlyOnRight());
		
		Map<String, ValueDifference<String>> differences = diff.entriesDiffering();
		
		Map<String, com.celfocus.omnichannel.digital.dto.ValueDifference<String>> copy = differences.entrySet().stream()
			.collect(Collectors.toMap(Map.Entry::getKey,
                e -> new com.celfocus.omnichannel.digital.dto.ValueDifference<>(e.getValue().leftValue(), e.getValue().rightValue()) ));

		
		mergeStatus.setDifferences(copy);
		
		return mergeStatus;
	}

}
