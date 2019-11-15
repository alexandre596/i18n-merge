package com.celfocus.omnichannel.digital.table.editor;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import com.celfocus.omnichannel.digital.table.data.FileLine;
import com.celfocus.omnichannel.digital.table.renderer.DoubleToggleRenderer;

public class DoubleToggleEditor extends AbstractCellEditor implements TableCellEditor, ItemListener {

	private DoubleToggleRenderer dtr = new DoubleToggleRenderer();

	public DoubleToggleEditor() {
		dtr.addItemListener(this);
	}

	@Override
	public Object getCellEditorValue() {
		return dtr.isSelected();
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col) {
		FileLine v = (FileLine) value;
		dtr.setSelected(v.getSelected());
		dtr.setText(v.getValue());
		return dtr;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		this.fireEditingStopped();
	}
}