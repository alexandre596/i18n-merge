package com.celfocus.omnichannel.digital.ui;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import com.celfocus.omnichannel.digital.table.data.FileLine;
import com.celfocus.omnichannel.digital.table.editor.RadioEditor;
import com.celfocus.omnichannel.digital.table.editor.ToggleEditor;
import com.celfocus.omnichannel.digital.table.model.RadioDataModel;
import com.celfocus.omnichannel.digital.table.model.ToggleDataModel;
import com.celfocus.omnichannel.digital.table.renderer.RadioRenderer;
import com.celfocus.omnichannel.digital.table.renderer.ToggleRenderer;

@org.springframework.stereotype.Component
public class MergeUI extends JFrame {

	private Font defaultFont;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					MergeUI window = new MergeUI();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MergeUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// defaultFont = new Font(fontFamily, Font.PLAIN, fontSize);
		defaultFont = new Font("Tahoma", Font.PLAIN, 16);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Create the button panel
		JPanel buttonPanel = this.buildButtonPanel();

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		JPanel tab1 = new JPanel();
		tab1.setLayout(new GridBagLayout());

		JPanel newLinesPanel = this.buildNewLinesPanel();
		JPanel updatedLinesPanel = this.buildUpdatedLinesPanel();
		JPanel removedLinesPanel = this.buildRemovedLinesPanel();

		tab1.add(newLinesPanel, gbc);
		tab1.add(updatedLinesPanel, gbc);
		tab1.add(removedLinesPanel, gbc);
		tabbedPane.addTab("Projeto 1", tab1);
		getContentPane().add(tabbedPane, gbc);
		getContentPane().add(buttonPanel, gbc);

		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	private JPanel buildNewLinesPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		Object[][] data = { { "Teste1", "valor1", true }, { "Teste2fdsfdsfds", "dfasdfdasg gffdsg sdfg dsfgd", true },
				{ "Teste3fdfs", "gsdfgfds fdsg dsfg dsfg fgfd", true },
				{ "Teste1", "sdfgfd fdsg fdsg dsf gdsf gdsf", true },
				{ "Teste9dfds", "fgdsgfdgs gsf gfs gfdsg df gdf gdf", true }, };
		
		JTable table = buildSimpleTable(data);
		panel.add(table);

		// Add a border around the panel.
		panel.setBorder(BorderFactory.createTitledBorder("Labels adicionadas"));

		return panel;
	}
	
	private JPanel buildUpdatedLinesPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		Object[][] data = { { "aa", new FileLine("Teste1", false), new FileLine("valor1", true) }, 
				{ "fdasfsdsdf", new FileLine("Teste2fdsfdsfds", false), new FileLine("dfasdfdasg gffdsg sdfg dsfgd", true) },
				{ "sdfdsfsfdsd", new FileLine("Teste3fdfs", false), new FileLine("gsdfgfds fdsg dsfg dsfg fgfd", true) },
				{ "fdsffdasfdsafsdsfd", new FileLine("Teste1", false), new FileLine("sdfgfd fdsg fdsg dsf gdsf gdsf", true) },
				{ "gsfdgfsgfsdgfdsgfdgdgfs", new FileLine("Teste9dfds", false), new FileLine("fgdsgfdgs gsf gfs gfdsg df gdf gdf", true) }, };
		
		JTable table = buildUpdatedValuesTable(data);
		panel.add(table);

		// Add a border around the panel.
		panel.setBorder(BorderFactory.createTitledBorder("Labels adicionadas"));

		return panel;
	}

	private JPanel buildRemovedLinesPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		Object[][] data = { { "Teste1", "valor1", true }, { "Teste2fdsfdsfds", "dfasdfdasg gffdsg sdfg dsfgd", true },
				{ "Teste3fdfs", "gsdfgfds fdsg dsfg dsfg fgfd", true },
				{ "Teste1", "sdfgfd fdsg fdsg dsf gdsf gdsf", true },
				{ "Teste9dfds", "fgdsgfdgs gsf gfs gfdsg df gdf gdf", true }, };
		
		JTable table = buildSimpleTable(data);
		panel.add(table);

		// Add a border around the panel.
		panel.setBorder(BorderFactory.createTitledBorder("Labels removidas"));

		return panel;
	}
	
	private JTable buildSimpleTable(Object[][] data) {
		ToggleDataModel model = new ToggleDataModel(data);
		JTable table = new JTable(model);
		
        table.setDefaultRenderer(Boolean.class, new ToggleRenderer());
        table.setDefaultEditor(Boolean.class, new ToggleEditor());
        
        return buildTable(table);
	}
	
	private JTable buildUpdatedValuesTable(Object[][] data) {
		RadioDataModel model = new RadioDataModel(data);
		JTable table = new JTable(model);
		
        table.setDefaultRenderer(Boolean.class, new RadioRenderer());
        table.setDefaultEditor(Boolean.class, new RadioEditor());
        
        return buildTable(table);
	}
	
	private JTable buildTable(JTable table) {
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
		JButton btnNextStep = new JButton("Avançar");
		btnNextStep.setFont(defaultFont);
		panel.add(btnNextStep);
		return panel;
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

