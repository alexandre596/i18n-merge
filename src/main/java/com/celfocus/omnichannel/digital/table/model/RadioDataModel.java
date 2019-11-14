package com.celfocus.omnichannel.digital.table.model;

import javax.swing.table.AbstractTableModel;

import com.celfocus.omnichannel.digital.table.data.FileLine;

public class RadioDataModel extends AbstractTableModel {

    private static final String[] names = {"Chave", "Valor Local", "Valor Prod"};
    private Object[][] values;

    public RadioDataModel(Object[][] values) {
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