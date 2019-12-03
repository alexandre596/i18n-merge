package com.celfocus.omnichannel.digital.swing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.celfocus.omnichannel.digital.helpers.FileHelper;

public class JFileSearchAutoComplete extends JAutoComplete {
	
	private static final long serialVersionUID = -548332481284325467L;

	@Override
	public List<String> getSourceList() {
		File file = FileHelper.getFileOrParent(getText());
		if (file.exists()) {
			try (Stream<Path> walk = Files.walk(Paths.get(file.getAbsolutePath()), 1)) {
				return walk.filter(p -> Files.isRegularFile(p) || Files.isDirectory(p))
						.map(x -> x.toString())
						.collect(Collectors.toList());
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		
		return new ArrayList<>();
	}

}
