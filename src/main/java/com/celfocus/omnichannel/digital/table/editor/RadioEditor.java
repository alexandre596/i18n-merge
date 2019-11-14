package com.celfocus.omnichannel.digital.table.editor;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import com.celfocus.omnichannel.digital.table.data.FileLine;
import com.celfocus.omnichannel.digital.table.renderer.RadioRenderer;

public class RadioEditor extends AbstractCellEditor implements TableCellEditor, ItemListener {

	private RadioRenderer rr = new RadioRenderer();

	public RadioEditor() {
		rr.addItemListener(this);
	}

	@Override
	public Object getCellEditorValue() {
		return rr.isSelected();
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col) {
		FileLine v = (FileLine) value;
		rr.setSelected(v.getSelected());
		rr.setText(v.getValue());
		return rr;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		this.fireEditingStopped();
	}
}