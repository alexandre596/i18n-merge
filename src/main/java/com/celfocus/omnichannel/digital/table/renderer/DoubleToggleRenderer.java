package com.celfocus.omnichannel.digital.table.renderer;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.table.TableCellRenderer;

import com.celfocus.omnichannel.digital.table.data.FileLine;

public class DoubleToggleRenderer extends JToggleButton implements TableCellRenderer {
	
	private static final long serialVersionUID = -8183373633851547654L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int col) {
		FileLine v = (FileLine) value;
        this.setSelected(v.getSelected());
        this.setText(v.getValue());
        this.setToolTipText(v.getValue());
		return this;
	}
}