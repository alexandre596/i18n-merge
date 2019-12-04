package com.celfocus.omnichannel.digital.ui;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.celfocus.omnichannel.digital.dto.MergeStatus;
import com.celfocus.omnichannel.digital.dto.Project;
import com.celfocus.omnichannel.digital.exception.InvalidFileException;
import com.celfocus.omnichannel.digital.helpers.FileHelper;
import com.celfocus.omnichannel.digital.helpers.InternationalizationHelper;
import com.celfocus.omnichannel.digital.services.MergeFilesService;
import com.celfocus.omnichannel.digital.swing.component.JFileSearchAutoComplete;

@Component
public class ProjectsUI extends JFrame {

	private static final long serialVersionUID = -5913253237090837696L;
	private static final Logger LOG = LoggerFactory.getLogger(ProjectsUI.class);
	
	private Font defaultFont;
	private Locale locale;
	private ResourceBundle rb;

	private MergeFilesService mergeFilesService;
	private MergeUI mergeUI;

	@Value("${font.family}")
	private String fontFamily;

	@Value("${font.size}")
	private int fontSize;

	@Value("${default.workspace.location}")
	private String defaultWorkspaceLocation;
	
	private List<String> projectList;
	private String productionFilePath;
	
	@Autowired
	public ProjectsUI(MergeFilesService mergeFilesService, MergeUI mergeUI) throws HeadlessException {
		super();
		this.mergeFilesService = mergeFilesService;
		this.mergeUI = mergeUI;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	void initialize(String productionFilePath, List<String> projectList) {
		this.projectList = projectList;
		this.productionFilePath = productionFilePath;
		
		this.defaultFont = new Font(fontFamily, Font.PLAIN, fontSize);

		this.locale = Locale.getDefault();
		this.rb = ResourceBundle.getBundle("i18n/Translation", locale);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle(rb.getString("projectsUiTitle"));
		this.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Create the button panel
		JPanel buttonPanel = buildButtonPanel();

		this.projectList.forEach(project -> {
			JPanel standard = this.buildContentPanel(project);
			this.add(standard, gbc);
		});

		this.add(buttonPanel, gbc);

		this.pack();
		this.setLocationRelativeTo(null);

		this.setVisible(true);
	}

	private JPanel buildContentPanel(String project) {
		LOG.debug("Building panel for project {}", project);
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		JLabel lblUploadFile = new JLabel(project + ":");
		lblUploadFile.setFont(defaultFont);

		JFileSearchAutoComplete txtUploadFile = new JFileSearchAutoComplete();
		lblUploadFile.setLabelFor(lblUploadFile);
		txtUploadFile.setColumns(30);
		txtUploadFile.setText(defaultWorkspaceLocation);
		txtUploadFile.setFont(defaultFont);

		JButton btnSelectFileButton = new JButton(rb.getString("projectToUploadButton"));
		btnSelectFileButton.setFont(defaultFont);
		btnSelectFileButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				LOG.trace("Select new directory clicked");
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				jfc.setDialogTitle(rb.getString("uploadTitle"));
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				jfc.setCurrentDirectory(FileHelper.getFileOrParent(txtUploadFile.getText()));

				int returnValue = jfc.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = jfc.getSelectedFile();
					txtUploadFile.setText(selectedFile.getAbsolutePath());
					txtUploadFile.hideAutoCompleteOptions();
				}
			}
		});

		panel.add(lblUploadFile);
		panel.add(txtUploadFile);
		panel.add(btnSelectFileButton);

		// Add a border around the panel.
		panel.setBorder(BorderFactory.createTitledBorder(rb.getString("blockProjectTitle")));

		return panel;
	}

	private JPanel buildButtonPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton btnNextStep = new JButton(rb.getString("nextStep2Button"));
		btnNextStep.setFont(defaultFont);
		btnNextStep.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!isDataValid()) {
					LOG.warn("The data inputed is invalid");
					return;
				}
				
				List<Project> projects = getProjectList(projectList);
				try {
					Map<Project, MergeStatus> mergeStatus = mergeFilesService.getMergeStatus(productionFilePath, projects);
					mergeUI.initialize(mergeStatus);
					dispose();
				} catch (InvalidFileException e1) {
					LOG.error("Could not merge the files successfully.", e1);
					JOptionPane.showMessageDialog(null, e1.getMessage(),
							rb.getString("errorMessageCheckProjectTitle"), JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});

		panel.add(btnNextStep);
		return panel;
	}
	
	private List<Project> getProjectList(List<String> projectList) {
		List<Project> projects = new ArrayList<>();
		List<JTextField> textFields = this.getAllTextFieldComponents(this);
		
		int counter = 0;
		for (JTextField textField : textFields) {
			Project project = new Project();
			project.setProjectName(projectList.get(counter));
			project.setProjectPath(textField.getText());
			
			projects.add(project);
			LOG.info("New project '{}' added!", textField.getText());
			
			counter = counter + 1;
		}
		
		LOG.debug("Total of {} projects listed", counter);
		return projects;
	}

	private boolean isDataValid() {
		List<JTextField> textFields = this.getAllTextFieldComponents(this);

		int counter = 1;
		for (JTextField textField : textFields) {
			if (!this.isValid(textField)) {
				JOptionPane.showMessageDialog(this, InternationalizationHelper.formatMessage(locale, rb.getString("errorMessageCheckProjectMessage"), counter),
						rb.getString("errorMessageCheckProjectTitle"), JOptionPane.ERROR_MESSAGE);

				return false;
			}
			counter = counter + 1;
		}

		return true;
	}

	private boolean isValid(JTextField textField) {
		if (textField == null || !StringUtils.isNoneBlank(textField.getText())) {
			LOG.warn("Empty textField detected!");
			return false;
		}

		File directory = new File(textField.getText());

		if (!directory.exists() || !directory.isDirectory()) {
			LOG.warn("The directory '{}' is not a valid directory", textField.getText());
			return false;
		}

		return true;
	}

	private List<java.awt.Component> getAllComponents(final Container c) {
		java.awt.Component[] comps = c.getComponents();
		List<java.awt.Component> compList = new ArrayList<>();
		for (java.awt.Component comp : comps) {
			compList.add(comp);
			if (comp instanceof Container)
				compList.addAll(getAllComponents((Container) comp));
		}
		return compList;
	}

	private List<JTextField> getAllTextFieldComponents(final Container container) {
		List<java.awt.Component> components = getAllComponents(container);
		return components.stream().filter(c -> c instanceof JTextField).map(JTextField.class::cast)
				.collect(Collectors.toList());
	}

}
