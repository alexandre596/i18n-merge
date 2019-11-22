package com.celfocus.omnichannel.digital.services.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.celfocus.omnichannel.digital.dto.FinalMerge;
import com.celfocus.omnichannel.digital.dto.MergeStatus;
import com.celfocus.omnichannel.digital.dto.Project;
import com.celfocus.omnichannel.digital.dto.ResolvedMerge;
import com.celfocus.omnichannel.digital.exception.CouldNotLocateCorrectFileException;
import com.celfocus.omnichannel.digital.exception.InvalidFileException;
import com.celfocus.omnichannel.digital.exception.InvalidJsonException;
import com.celfocus.omnichannel.digital.helpers.FileHelper;
import com.celfocus.omnichannel.digital.services.MergeFilesService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
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
				File productionFile = FileHelper.getProductionFile(p, this.temporaryDirectory, this.i18nFileName);
				File localFile = FileHelper.getLocalFile(p, this.excludeDirectories, this.i18nFileName);
				
				Map<String, String> productionMap = FileHelper.getFileContent(productionFile);
				Map<String, String> localMap = FileHelper.getFileContent(localFile);
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
				File localFile = FileHelper.getLocalFile(resolvedMergeEntry.getKey(), this.excludeDirectories, this.i18nFileName);
				Map<String, String> localMap = FileHelper.getFileContent(localFile);
				finalMerge.setI18n(localMap);
				
				// Remove as novas que não é para adicionar
				for(Entry<String, String> newValue: resolvedMergeEntry.getValue().getNewLines().entrySet()) {
					finalMerge.getI18n().remove(newValue.getKey());
				}
				
				// Atualiza os valores das linhas a serem atualizadas
				finalMerge.getI18n().putAll(resolvedMergeEntry.getValue().getUpdatedLines());
				
				// Adiciona as que não são para remover.
				for(Entry<String, String> removedValue : resolvedMergeEntry.getValue().getRemovedLines().entrySet()) {
					finalMerge.getI18n().put(removedValue.getKey(), removedValue.getValue());
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
			File localFile = FileHelper.getLocalFile(finalMerge.getProject(), this.excludeDirectories, this.i18nFileName);
			
			if(!localFile.exists()) {
				throw new CouldNotLocateCorrectFileException("Local file does not exist.");	
			}
			
			FileHelper.saveToFile(finalMerge, localFile);
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
	
	private MergeStatus getDifferences(Map<String, String> productionMap, Map<String, String> localMap) {
		MergeStatus mergeStatus = new MergeStatus();
		
		MapDifference<String, String> diff = Maps.difference(productionMap, localMap);
		
		mergeStatus.setNewLines(diff.entriesOnlyOnRight());
		mergeStatus.setRemovedLines(diff.entriesOnlyOnLeft());
		
		Map<String, ValueDifference<String>> differences = diff.entriesDiffering();
		
		Map<String, com.celfocus.omnichannel.digital.dto.ValueDifference<String>> copy = differences.entrySet().stream()
			.collect(Collectors.toMap(Map.Entry::getKey,
                e -> new com.celfocus.omnichannel.digital.dto.ValueDifference<>(e.getValue().rightValue(), e.getValue().leftValue()) ));

		
		mergeStatus.setDifferences(copy);
		
		return mergeStatus;
	}
	
}
