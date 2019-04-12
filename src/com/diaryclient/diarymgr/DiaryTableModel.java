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
	public List<Date> dates;
	public List<Date> updatedates;
	public List<Date> insertdates;
	public List<Integer> diaryids;
	
	SimpleDateFormat datetimeformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
	
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
		
		Date tmpDate = null;
		
		switch (columnIndex) {
			case 0:
				return checks.get(rowIndex);
		
			case 1:
				tmpDate = dates.get(rowIndex);
				if (null != tmpDate) {
					return dateformat.format(tmpDate);
				} else {
					return "";
				}
	

			case 2:
				tmpDate = insertdates.get(rowIndex);
				if (null != tmpDate) {
					return datetimeformat.format(tmpDate);
				} else {
					return "";
				}
				
				
				
			case 3:
				tmpDate = updatedates.get(rowIndex);
				if (null != tmpDate) {
					return datetimeformat.format(tmpDate);
				} else {
					return "";
				}
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
