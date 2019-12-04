package com.celfocus.omnichannel.digital.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.celfocus.omnichannel.digital.exception.InvalidFileException;
import com.celfocus.omnichannel.digital.helpers.FileHelper;
import com.celfocus.omnichannel.digital.services.ProjectService;
import com.celfocus.omnichannel.digital.swing.component.JFileSearchAutoComplete;

@Component
public class UploadFileUI extends JFrame {

	private static final long serialVersionUID = -2482434625236865064L;
	private static final Logger LOG = LoggerFactory.getLogger(UploadFileUI.class);
	
	private Font defaultFont;
	private JFileSearchAutoComplete txtUploadFile;
    private Locale locale;
    private ResourceBundle rb;
    
    private ProjectService projectService;
    private ProjectsUI projectsUI;
    
    @Value("${default.i18n.zip.location}")
    private String defaultI18nLocation;
    
    @Value("${font.family}")
    private String fontFamily;
    
    @Value("${font.size}")
    private int fontSize;
    
    @Autowired
	public UploadFileUI(ProjectService projectService, ProjectsUI projectsUI) throws HeadlessException {
		super();
		this.projectService = projectService;
		this.projectsUI = projectsUI;
	}

	/**
	 * Initialize the contents of the frame.
	 */
    public void initialize() {
		this.defaultFont = new Font(fontFamily, Font.PLAIN, fontSize);
		
		this.locale = Locale.getDefault();
		this.rb = ResourceBundle.getBundle("i18n/Translation", locale);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle(rb.getString("uploadFileUiTitle"));
		this.getContentPane().setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JLabel lblUploadFile = new JLabel(rb.getString("fileToUploadLabel"));
		lblUploadFile.setFont(defaultFont);
		
		txtUploadFile = new JFileSearchAutoComplete();
		lblUploadFile.setLabelFor(txtUploadFile);
		txtUploadFile.setColumns(30);
		txtUploadFile.setFont(defaultFont);
		
		txtUploadFile.setText(defaultI18nLocation);
		
		JButton btnSelectFileButton = new JButton(rb.getString("fileToUploadButton"));
		btnSelectFileButton.setFont(defaultFont);
		btnSelectFileButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				LOG.trace("Select new file clicked");
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				jfc.setDialogTitle(rb.getString("uploadTitle"));
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jfc.setCurrentDirectory(FileHelper.getFileOrParent(txtUploadFile.getText()));

				FileNameExtensionFilter filter = new FileNameExtensionFilter(rb.getString("fileToUploadButton"), rb.getString("uploadableFileExtensions"));
				jfc.addChoosableFileFilter(filter);
				jfc.setAcceptAllFileFilterUsed(false);

				int returnValue = jfc.showOpenDialog(null);
				
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = jfc.getSelectedFile();
					txtUploadFile.setText(selectedFile.getAbsolutePath());
				}
			}
		});
		
		JPanel logoPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
		logoPnl.add(lblUploadFile);
		logoPnl.add(txtUploadFile);
		logoPnl.add(btnSelectFileButton);
		
		JPanel fnctnPnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton btnNextStep = new JButton(rb.getString("nextStep1Button"));
		btnNextStep.setFont(defaultFont);
		btnNextStep.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(!isDataValid()) {
					LOG.warn("The data inputed is invalid");
					JOptionPane.showMessageDialog(null, rb.getString("errorMessageUploadPageMessage"), rb.getString("errorMessageUploadPageTitle"), JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				try {
					List<String> projectList = projectService.getProjectsFromZip(txtUploadFile.getText());
					projectsUI.initialize(txtUploadFile.getText(), projectList);
					dispose();
				} catch (InvalidFileException e1) {
					LOG.error("Could not get data from zip file successfully.", e1);
					JOptionPane.showMessageDialog(null, rb.getString("errorMessageInvalidFileMessage"), rb.getString("errorMessageInvalidFileTitle"), JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		fnctnPnl.add(btnNextStep);

		JPanel borderPnl = new JPanel(new BorderLayout());
		borderPnl.add(logoPnl, BorderLayout.NORTH);
		borderPnl.add(fnctnPnl, BorderLayout.SOUTH);
		
		JPanel container = new JPanel(new FlowLayout(FlowLayout.LEFT));
		container.add(borderPnl);
		
		this.getContentPane().add(container);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
    
    private boolean isDataValid() {
    	return !this.txtUploadFile.getText().isEmpty();
    }

}
