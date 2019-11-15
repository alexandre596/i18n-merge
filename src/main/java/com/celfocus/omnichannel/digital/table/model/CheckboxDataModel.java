package com.celfocus.omnichannel.digital.table.model;

import javax.swing.table.AbstractTableModel;

public class CheckboxDataModel extends AbstractTableModel {

    private String[] names;
    private Object[][] values;

    public CheckboxDataModel(String[] names, Object[][] values) {
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
        if (col == 2) {
        	this.values[row][col] = aValue;
            this.fireTableCellUpdated(row, col);
        }
    }

    @Override
    public Class<?> getColumnClass(int col) {
        if (col < 2) {
            return String.class;
        } else if (col == 2) {
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
        return col == 2;
    }
}