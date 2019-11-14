package com.celfocus.omnichannel.digital.table.renderer;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.table.TableCellRenderer;

import com.celfocus.omnichannel.digital.table.data.FileLine;

public class RadioRenderer extends JToggleButton implements TableCellRenderer {
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int col) {
		FileLine v = (FileLine) value;
        this.setSelected(v.getSelected());
        this.setText(v.getValue());
		return this;
	}
}