package com.celfocus.omnichannel.digital.table.model;

import javax.swing.table.AbstractTableModel;

import com.celfocus.omnichannel.digital.table.data.FileLine;

public class DoubleToggleDataModel extends AbstractTableModel {

	private static final long serialVersionUID = 5812541613267126654L;
	private String[] names;
    private Object[][] values;

    public DoubleToggleDataModel(String[] names, Object[][] values) {
        this.names = names;
    	this.values = values;
    }

    @Override
    public int getRowCount() {
        return values.length;
    }

    @Override
    public int getColumnCount() {
        return names.length;
    }

    @Override
    public Object getValueAt(int row, int col) {
        return this.values[row][col];
    }

    @Override
    public void setValueAt(Object aValue, int row, int col) {
        if (this.values[row][col] instanceof FileLine) {
        	FileLine v = (FileLine) this.values[row][col];
        	v.setSelected((Boolean) aValue);
        	this.fireTableCellUpdated(row, col);
        	
        	// Se selecionar de um lado, tem que desselecionar do outro
        	if((Boolean) aValue) {
        		int counterCol = col == 1 ? 2 : 1;
        		FileLine counterV = (FileLine) this.values[row][counterCol];
        		counterV.setSelected(false);
        		this.fireTableCellUpdated(row, counterCol);
        	}
        }
    }

    @Override
    public Class<?> getColumnClass(int col) {
        if (col == 0) {
            return String.class;
        } else if (col == 1 | col == 2) {
            return Boolean.class;
        } else {
            return null;
        }
    }

    @Override
    public String getColumnName(int col) {
        return names[col];
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return col == 1 | col == 2;
    }
}