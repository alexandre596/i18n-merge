package com.celfocus.omnichannel.digital.table.editor;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import com.celfocus.omnichannel.digital.table.renderer.CheckboxRenderer;

public class CheckboxEditor extends AbstractCellEditor implements TableCellEditor, ItemListener {

	private CheckboxRenderer jr = new CheckboxRenderer();

	public CheckboxEditor() {
		jr.addItemListener(this);
	}

	@Override
	public Object getCellEditorValue() {
		return jr.isSelected();
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col) {
		Boolean v = (Boolean) value;
		jr.setSelected(v);
		return jr;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		this.fireEditingStopped();
	}
}