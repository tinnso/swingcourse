package com.diaryclient.diarymgr;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.table.AbstractTableModel;

public class DiaryTableModel extends AbstractTableModel {

	private String[] columnNames = {"选择", "日记日期","记录日期","更新日期"};
	public List<Boolean> checks;
	public List<String> dates;
	public List<Date> updatedates;
	public List<Date> insertdates;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return dates.size();
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
			case 0:
				return checks.get(rowIndex);
		
			case 1:
				return dates.get(rowIndex);

			case 2:
				return sdf.format(insertdates.get(rowIndex));
				
			case 3:
				return sdf.format(updatedates.get(rowIndex));

			default:
					return 0;
			
		}
		
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}
	
	@Override
	public Class getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return Boolean.class;
	
		case 1:
			return String.class;

		case 2:
			return String.class;
			
		case 3:
			return String.class;

		default:
				return String.class;
		
		}
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) { 
		if(columnIndex > 0) return false; 
		return true;
	}
	
	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		System.out.println("[row: "+ rowIndex + ", column: " + columnIndex + ", value: " + value + " ]");
		
		if (columnIndex == 0)
		{
			checks.set(rowIndex, (Boolean)value);
		}
		 
	}
}
