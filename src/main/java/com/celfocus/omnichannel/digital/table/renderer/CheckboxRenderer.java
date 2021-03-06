package com.celfocus.omnichannel.digital.table.renderer;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class CheckboxRenderer extends JCheckBox implements TableCellRenderer {

	private static final long serialVersionUID = 5920373002414705323L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int col) {
		Boolean v = (Boolean) value;
		this.setSelected(v);
		return this;
	}
}