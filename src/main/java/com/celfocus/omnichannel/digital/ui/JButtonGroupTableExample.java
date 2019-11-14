package com.celfocus.omnichannel.digital.ui;

//File: JButtonGroupTableExample.java
/* (swing1.1beta3) */

/**
 * @version 1.0 12/03/98
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;

public class JButtonGroupTableExample extends JFrame {

	public JButtonGroupTableExample() {
		super("JButtonGroupTable Example ");
		UIDefaults ui = UIManager.getLookAndFeel().getDefaults();
		UIManager.put("RadioButton.focus", ui.getColor("control"));

		DefaultTableModel dm = new DefaultTableModel();
		dm.setDataVector(
				new Object[][] { { "Do you like dogs?", "5" }, { "Do you like cats?", "4" },
						{ "Do you like Windows?", "1" }, { "Do you like Java?", "5" }, { "Do you like Swing?", "4" } },
				new Object[] { "Questions", "Answers" });

		JTable table = new JTable(dm) {
			public void tableChanged(TableModelEvent e) {
				super.tableChanged(e);
				repaint();
			}
		};

		table.getColumn("Answers").setCellRenderer(new RadioButtonRenderer());
		table.setRowHeight(30);
		table.getColumn("Answers").setCellEditor(new RadioButtonEditor(new JCheckBox()));
		JScrollPane scroll = new JScrollPane(table);
		getContentPane().add(scroll);
		setSize(1000, 200);
		setVisible(true);
	}

	public static void main(String[] args) {
		JButtonGroupTableExample frame = new JButtonGroupTableExample();
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
}

class RadioButtonRenderer implements TableCellRenderer {
	public JPanel pnl = new JPanel();
	public ButtonGroup group1 = new ButtonGroup();
	public JRadioButton btnFive = new JRadioButton("Very Very Much");
	public JRadioButton btnFour = new JRadioButton("Very Much");
	public JRadioButton btnThree = new JRadioButton("Okay");
	public JRadioButton btnTwo = new JRadioButton("Not Much");
	public JRadioButton btnOne = new JRadioButton("Hate it");

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		if (value == null)
			return null;

		group1.add(btnFive);
		group1.add(btnFour);
		group1.add(btnThree);
		group1.add(btnTwo);
		group1.add(btnOne);

		pnl.add(btnFive);
		pnl.add(btnFour);
		pnl.add(btnThree);
		pnl.add(btnTwo);
		pnl.add(btnOne);

		btnOne.setSelected(false);
		btnTwo.setSelected(false);
		btnThree.setSelected(false);
		btnFour.setSelected(false);
		btnFive.setSelected(false);
		switch (Integer.parseInt((String) value)) {

		case 1:
			btnOne.setSelected(true);
			break;
		case 2:
			btnTwo.setSelected(true);
			break;
		case 3:
			btnThree.setSelected(true);
			break;
		case 4:
			btnFour.setSelected(true);
			break;
		case 5:
			btnFive.setSelected(true);
			break;
		}

		return pnl;
	}
}

class RadioButtonEditor extends DefaultCellEditor implements ItemListener {
	public JPanel pnl = new JPanel();
	public ButtonGroup group1 = new ButtonGroup();
	public JRadioButton btnFive = new JRadioButton("Very Very Much");
	public JRadioButton btnFour = new JRadioButton("Very Much");
	public JRadioButton btnThree = new JRadioButton("Okay");
	public JRadioButton btnTwo = new JRadioButton("Not Much");
	public JRadioButton btnOne = new JRadioButton("Hate it");

	public RadioButtonEditor(JCheckBox checkBox) {
		super(checkBox);
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		if (value == null)
			return null;

		group1.add(btnFive);
		group1.add(btnFour);
		group1.add(btnThree);
		group1.add(btnTwo);
		group1.add(btnOne);

		pnl.add(btnFive);
		pnl.add(btnFour);
		pnl.add(btnThree);
		pnl.add(btnTwo);
		pnl.add(btnOne);

		btnOne.setSelected(false);
		btnTwo.setSelected(false);
		btnThree.setSelected(false);
		btnFour.setSelected(false);
		btnFive.setSelected(false);

		switch (Integer.parseInt((String) value)) {

		case 1:
			btnOne.setSelected(true);
			break;
		case 2:
			btnTwo.setSelected(true);
			break;
		case 3:
			btnThree.setSelected(true);
			break;
		case 4:
			btnFour.setSelected(true);
			break;
		case 5:
			btnFive.setSelected(true);
			break;
		}

		return pnl;
	}

	public Object getCellEditorValue() {
		if (btnFive.isSelected() == true)
			return "5";
		if (btnFour.isSelected() == true)
			return "4";

		if (btnThree.isSelected() == true)
			return "3";

		if (btnTwo.isSelected() == true)
			return "2";

		if (btnOne.isSelected() == true)
			return "1";
		return "";

	}

	public void itemStateChanged(ItemEvent e) {
		super.fireEditingStopped();
	}
}