package com.diaryclient.remindermgr;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class ReminderTableModel extends AbstractTableModel {
	
	//private String[] columnNames = {"选择", "提醒时间","内容","有效","记录日期","更新日期"};
	private String[] columnNames = {"选择", "提醒时间","内容","有效"};
	private SimpleDateFormat datetimeformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	public List<Boolean> checks;
	public List<Date> remindertimes;
	public List<String> remindertexts;
	public List<Integer> enables;
	public List<Date> insertdates;
	public List<Date> updatedates;
	
		
	public List<Integer> reminderids;
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public int getRowCount() {
		return remindertimes.size();
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}
	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Date tmpDate = null;
		
		switch (columnIndex) {
			case 0:
				return checks.get(rowIndex);
		
			case 1:
				tmpDate = remindertimes.get(rowIndex);
				if (null != tmpDate) {
					return datetimeformat.format(tmpDate);
				} else {
					return "";
				}
				
			case 2:
				return remindertexts.get(rowIndex);
				
			case 3:
				return enables.get(rowIndex) == 1 ? "有效":"无效";

			case 4:
				tmpDate = insertdates.get(rowIndex);
				if (null != tmpDate) {
					return datetimeformat.format(tmpDate);
				} else {
					return "";
				}
				
			case 5:
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
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return Boolean.class;
		case 1:
			return String.class;
		case 2:
			return String.class;
			
		case 3:
			return String.class;
		case 5:
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
