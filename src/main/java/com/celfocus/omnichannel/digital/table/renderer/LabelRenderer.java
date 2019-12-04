package com.celfocus.omnichannel.digital.table.renderer;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class LabelRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 4344552403179659654L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		setToolTipText((String) value);
		setOpaque(false);
		return this;
	}
}
