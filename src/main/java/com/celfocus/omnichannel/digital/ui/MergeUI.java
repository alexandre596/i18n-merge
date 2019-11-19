package com.celfocus.omnichannel.digital.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.apache.commons.lang3.SerializationUtils;
import org.springframework.beans.factory.annotation.Value;

import com.celfocus.omnichannel.digital.dto.MergeStatus;
import com.celfocus.omnichannel.digital.dto.Project;
import com.celfocus.omnichannel.digital.dto.ResolvedMerge;
import com.celfocus.omnichannel.digital.dto.ValueDifference;
import com.celfocus.omnichannel.digital.table.data.FileLine;
import com.celfocus.omnichannel.digital.table.editor.CheckboxEditor;
import com.celfocus.omnichannel.digital.table.editor.DoubleToggleEditor;
import com.celfocus.omnichannel.digital.table.model.CheckboxDataModel;
import com.celfocus.omnichannel.digital.table.model.DoubleToggleDataModel;
import com.celfocus.omnichannel.digital.table.renderer.CheckboxRenderer;
import com.celfocus.omnichannel.digital.table.renderer.DoubleToggleRenderer;
import com.celfocus.omnichannel.digital.table.renderer.LabelRenderer;

@org.springframework.stereotype.Component
public class MergeUI extends JFrame {

	private Font defaultFont;
	private Locale locale;
	private ResourceBundle rb;

	private JTabbedPane tabbedPane;

	private JButton btnPreviousStep;
	private JButton btnNextStep;
	private JButton btnFinishStep;

	private Map<Project, MergeStatus> mergeStatus;

	@Value("${font.family}")
	private String fontFamily;

	@Value("${font.size}")
	private int fontSize;

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize(Map<Project, MergeStatus> mergeStatus) {
		this.defaultFont = new Font(fontFamily, Font.PLAIN, fontSize);
		this.locale = Locale.getDefault();
		this.rb = ResourceBundle.getBundle("Translation", locale);

		this.mergeStatus = mergeStatus;

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle(rb.getString("mergeUiTitle"));
		getContentPane().setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Create the button panel
		JPanel buttonPanel = this.buildButtonPanel();

		this.tabbedPane = new JTabbedPane(JTabbedPane.TOP);

		for (Entry<Project, MergeStatus> merge : mergeStatus.entrySet()) {
			JPanel tab = new JPanel();
			tab.setLayout(new GridBagLayout());

			JPanel newLinesPanel = this.buildNewLinesPanel(merge.getValue());
			JPanel updatedLinesPanel = this.buildUpdatedLinesPanel(merge.getValue());
			JPanel removedLinesPanel = this.buildRemovedLinesPanel(merge.getValue());

			if (isAllPanelsHidden(newLinesPanel, updatedLinesPanel, removedLinesPanel)) {
				JLabel noItems = new JLabel(rb.getString("noChangesLabel"));
				noItems.setFont(defaultFont);
				tab.add(noItems);
			}

			tab.add(newLinesPanel, gbc);
			tab.add(updatedLinesPanel, gbc);
			tab.add(removedLinesPanel, gbc);
			this.tabbedPane.addTab(merge.getKey().getProjectName(), tab);
			getContentPane().add(this.tabbedPane, gbc);
			getContentPane().add(buttonPanel, gbc);
		}

		this.tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JTabbedPane sourceTabbedPane = (JTabbedPane) e.getSource();

				btnPreviousStep.setVisible(true);
				btnNextStep.setVisible(true);
				btnFinishStep.setVisible(false);

				if (sourceTabbedPane.getSelectedIndex() == 0) {
					btnPreviousStep.setVisible(false);
				} else if (sourceTabbedPane.getSelectedIndex() == tabbedPane.getComponents().length - 1) {
					btnNextStep.setVisible(false);
					btnFinishStep.setVisible(true);
				}
			}
		});

		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	private boolean isAllPanelsHidden(JPanel newLinesPanel, JPanel updatedLinesPanel, JPanel removedLinesPanel) {
		return !newLinesPanel.isVisible() && !updatedLinesPanel.isVisible() && !removedLinesPanel.isVisible();
	}

	private JPanel buildNewLinesPanel(MergeStatus mergeStatus) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		if (mergeStatus.getNewLines().isEmpty()) {
			panel.setVisible(false);
			return panel;
		}

		Object[][] data = new Object[mergeStatus.getNewLines().size()][3];

		int i = 0;
		for (Entry<String, String> newLine : mergeStatus.getNewLines().entrySet()) {
			data[i][0] = newLine.getKey();
			data[i][1] = newLine.getValue();
			data[i][2] = true;
			i = i + 1;
		}

		JTable table = buildSimpleTable(data);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());

		panel.add(scrollPane);

		// Add a border around the panel.
		panel.setBorder(BorderFactory.createTitledBorder(rb.getString("newLinesLabel")));

		return panel;
	}

	private JPanel buildUpdatedLinesPanel(MergeStatus mergeStatus) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		if (mergeStatus.getDifferences().isEmpty()) {
			panel.setVisible(false);
			return panel;
		}

		Object[][] data = new Object[mergeStatus.getDifferences().size()][3];

		int i = 0;
		for (Entry<String, ValueDifference<String>> newLine : mergeStatus.getDifferences().entrySet()) {
			data[i][0] = newLine.getKey();
			data[i][1] = new FileLine(newLine.getValue().getOldValue(), false);
			data[i][2] = new FileLine(newLine.getValue().getNewValue(), true);
			i = i + 1;
		}

		JTable table = buildUpdatedValuesTable(data);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());

		panel.add(scrollPane);

		// Add a border around the panel.
		panel.setBorder(BorderFactory.createTitledBorder(rb.getString("updatedLinesLabel")));

		return panel;
	}

	private JPanel buildRemovedLinesPanel(MergeStatus mergeStatus) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		if (mergeStatus.getRemovedLines().isEmpty()) {
			panel.setVisible(false);
			return panel;
		}

		Object[][] data = new Object[mergeStatus.getRemovedLines().size()][3];

		int i = 0;
		for (Entry<String, String> newLine : mergeStatus.getRemovedLines().entrySet()) {
			data[i][0] = newLine.getKey();
			data[i][1] = newLine.getValue();
			data[i][2] = true;
			i = i + 1;
		}

		JTable table = buildSimpleTable(data);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());

		panel.add(scrollPane);

		// Add a border around the panel.
		panel.setBorder(BorderFactory.createTitledBorder(rb.getString("removedLinesLabel")));

		return panel;
	}

	private JTable buildSimpleTable(Object[][] data) {
		String[] names = new String[3];
		names[0] = rb.getString("columnKey");
		names[1] = rb.getString("columnValue");
		names[2] = rb.getString("columnCheck");

		CheckboxDataModel model = new CheckboxDataModel(names, data);
		JTable table = new JTable(model);

		table.setDefaultRenderer(Boolean.class, new CheckboxRenderer());
		table.setDefaultEditor(Boolean.class, new CheckboxEditor());

		buildTable(table);
		table.setPreferredScrollableViewportSize(
				new Dimension(table.getPreferredSize().width, table.getRowHeight() * data.length));

		return table;
	}

	private JTable buildUpdatedValuesTable(Object[][] data) {
		String[] names = new String[3];
		names[0] = rb.getString("columnKey");
		names[1] = rb.getString("columnOldValue");
		names[2] = rb.getString("columnNewValue");

		DoubleToggleDataModel model = new DoubleToggleDataModel(names, data);
		JTable table = new JTable(model);

		table.setDefaultRenderer(Boolean.class, new DoubleToggleRenderer());
		table.setDefaultEditor(Boolean.class, new DoubleToggleEditor());

		buildTable(table);
		table.setPreferredScrollableViewportSize(
				new Dimension(table.getPreferredSize().width, table.getRowHeight() * data.length));

		return table;
	}

	private JTable buildTable(JTable table) {
		table.setDefaultRenderer(String.class, new LabelRenderer());

		table.setAutoCreateRowSorter(true);
		table.setOpaque(false);
		((DefaultTableCellRenderer) table.getDefaultRenderer(Object.class)).setOpaque(false);
		table.setShowGrid(false);
		table.setFont(defaultFont);
		table.setRowHeight(25);
		table.setRowSelectionAllowed(false);

		this.resizeColumnWidth(table);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		return table;
	}

	private JPanel buildButtonPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		this.btnPreviousStep = new JButton(rb.getString("previousTabButton"));
		this.btnPreviousStep.setFont(defaultFont);
		this.btnPreviousStep.setVisible(false);

		this.btnPreviousStep.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				tabbedPane.setSelectedIndex(tabbedPane.getSelectedIndex() - 1);
			}
		});

		this.btnNextStep = new JButton(rb.getString("nextTabButton"));
		this.btnNextStep.setFont(defaultFont);

		this.btnNextStep.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				tabbedPane.setSelectedIndex(tabbedPane.getSelectedIndex() + 1);
			}
		});

		this.btnFinishStep = new JButton(rb.getString("nextStep3Button"));
		this.btnFinishStep.setFont(defaultFont);
		this.btnFinishStep.setVisible(false);

		this.btnFinishStep.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Map<Project, ResolvedMerge> resolvedMerge = new LinkedHashMap<>(); 
				for (int i = 0; i < tabbedPane.getTabCount(); i++) {
					if (tabbedPane.getComponent(i) instanceof JPanel) {
						Project project = getProjectFromTitle(tabbedPane.getTitleAt(i));
						resolvedMerge.put(project, getTableDataFromJPanel((JPanel) tabbedPane.getComponent(i), project));
						resolvedMerge.get(project);
					}

				}
			}
		});

		panel.add(this.btnPreviousStep);
		panel.add(this.btnNextStep);
		panel.add(this.btnFinishStep);
		return panel;
	}

	private Project getProjectFromTitle(String title) {
		for (Project p : mergeStatus.keySet()) {
			if (title.equals(p.getProjectName())) {
				return p;
			}
		}

		return new Project();
	}

	private ResolvedMerge getTableDataFromJPanel(JPanel panel, Project project) {
		ResolvedMerge resolvedMerge = new ResolvedMerge();

		// Loop through all the tabs
		for (Component c : panel.getComponents()) {
			if (c instanceof JPanel) {
				JPanel subPanel = (JPanel) c;
				// Check if it's a scroll pane, so I can get the table
				if (this.isComponentJScrollPane(subPanel)) {
					JScrollPane scrollPane = (JScrollPane) subPanel.getComponent(0);
					// Get the table and check if there's any data to it, to avoid ArrayOutOfBoundException
					JTable table = this.getTableFromJScrollPane(scrollPane);
					if (table.getRowCount() > 0) {
						// If it's a "simple" table, aka New and Removed lines table, we add all the data that was checked
						if (this.isSimpleTable(table)) {
							ResolvedMerge simpleTableResult = this.addSelectedLines(project, resolvedMerge, mergeStatus.get(project), table);
							resolvedMerge.getNewLines().putAll(simpleTableResult.getNewLines());
							resolvedMerge.getRemovedLines().putAll(simpleTableResult.getRemovedLines());
						} else if (this.isUpdateTable(table)) {
							// If it's an "update" table, we must check which value was selected before proceeding
							ResolvedMerge updatedItems = this.addUpdatedLines(project, resolvedMerge, mergeStatus.get(project), table);
							resolvedMerge.getUpdatedLines().putAll(updatedItems.getUpdatedLines());
						}
					}
				}
			}
		}
		
		return resolvedMerge;
	}

	private boolean isComponentJScrollPane(JPanel subPanel) {
		return subPanel.getComponentCount() > 0 && subPanel.getComponent(0) != null
				&& subPanel.getComponent(0) instanceof JScrollPane;
	}

	private JTable getTableFromJScrollPane(JScrollPane scrollPane) {
		JViewport viewport = scrollPane.getViewport();
		if (viewport.getComponent(0) != null && viewport.getComponent(0) instanceof JTable) {
			return (JTable) viewport.getComponent(0);
		}

		return new JTable();
	}

	private boolean isSimpleTable(JTable table) {
		return table.getValueAt(0, 1) instanceof String;
	}

	private boolean isUpdateTable(JTable table) {
		return table.getValueAt(0, 1) instanceof FileLine;
	}

	private boolean isNewLine(Project project, String key) {
		return mergeStatus.get(project).getNewLines().containsKey(key);
	}

	private boolean isRemovedLine(Project project, String key) {
		return mergeStatus.get(project).getRemovedLines().containsKey(key);
	}
	
	private ResolvedMerge addSelectedLines(final Project project, final ResolvedMerge resolvedMerge, final MergeStatus mergeStatus, final JTable table) {
		for (int row = 0; row < table.getRowCount(); row++) {
			String key = this.getKeyFromTable(table, row);
			if (this.isNewLine(project, key) && this.isSimpleItemSelected(table, row)) {
				resolvedMerge.getNewLines().put(key, mergeStatus.getNewLines().get(key));
			} else if (this.isRemovedLine(project, key) && this.isSimpleItemSelected(table, row)) {
				resolvedMerge.getRemovedLines().put(key, mergeStatus.getRemovedLines().get(key));
			}
		}
		
		return resolvedMerge;
	}
	
	private ResolvedMerge addUpdatedLines(final Project project, final ResolvedMerge resolvedMerge, final MergeStatus mergeStatus, final JTable table) {
		for (int row = 0; row < table.getRowCount(); row++) {
			String key = this.getKeyFromTable(table, row);
			FileLine selectedLine = this.getSelectedLineFromTable(table, row);
			resolvedMerge.getUpdatedLines().put(key, selectedLine.getValue());
		}
		
		return resolvedMerge;
	}

	private String getKeyFromTable(JTable table, int row) {
		return (String) table.getValueAt(row, 0);
	}

	private Boolean isSimpleItemSelected(JTable table, int row) {
		return (Boolean) table.getValueAt(row, 2);
	}
	
	private FileLine getSelectedLineFromTable(JTable table, int row) {
		FileLine oldValue = (FileLine) table.getValueAt(row, 1);
		FileLine newValue = (FileLine) table.getValueAt(row, 2);
		
		if(newValue.getSelected()) {
			return newValue;
		} else if(oldValue.getSelected()) {
			return oldValue;
		} else {
			throw new RuntimeException(); //TODO
		}
	}

	private void resizeColumnWidth(JTable table) {
		final TableColumnModel columnModel = table.getColumnModel();
		for (int column = 0; column < table.getColumnCount(); column++) {
			int width = 15; // Min width
			for (int row = 0; row < table.getRowCount(); row++) {
				TableCellRenderer renderer = table.getCellRenderer(row, column);
				Component comp = table.prepareRenderer(renderer, row, column);
				width = Math.max(comp.getPreferredSize().width + 10, width);
			}
			if (width > 300)
				width = 300;
			columnModel.getColumn(column).setPreferredWidth(width);
		}
	}
}
