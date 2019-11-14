package com.celfocus.omnichannel.digital.table.editor;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import com.celfocus.omnichannel.digital.table.renderer.ToggleRenderer;

public class ToggleEditor extends AbstractCellEditor implements TableCellEditor, ItemListener {

	private ToggleRenderer tr = new ToggleRenderer();

	public ToggleEditor() {
		tr.addItemListener(this);
	}

	@Override
	public Object getCellEditorValue() {
		return tr.isSelected();
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col) {
		Boolean v = (Boolean) value;
		tr.setSelected(v);
		tr.setText("OK");
		return tr;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		this.fireEditingStopped();
	}
}