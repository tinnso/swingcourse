package com.diaryclient.usermgr;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class UserTableModel extends AbstractTableModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8737382124129221077L;
	
	private String[] columnNames = {"选择", "用户名","姓名","用户种类","记录日期","更新日期", "有效"};
	public List<Boolean> checks;
	public List<String> accounts;
	public List<String> names;
	public List<Integer> types;
	public List<Date> updatedates;
	public List<Date> insertdates;
	public List<Integer> deleteds;
	
	public List<Integer> userids;
	
	
	SimpleDateFormat datetimeformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public int getRowCount() {
		return accounts.size();
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
				return accounts.get(rowIndex);
				
			case 2:
				return names.get(rowIndex);
				
			case 3:
				return types.get(rowIndex) == 1 ? "普通用户":"管理员";

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
				
			case 6:
				return deleteds.get(rowIndex) == 0 ? "有效":"无效";
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
		case 6:
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
