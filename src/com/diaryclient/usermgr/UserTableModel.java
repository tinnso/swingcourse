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
	
	private String[] columnNames = {"ѡ��", "�û���","����","�û�����","��¼����","��������", "��Ч"};
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
				return types.get(rowIndex) == 0 ? "��ͨ�û�":"����Ա";

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
				return deleteds.get(rowIndex) == 0 ? "��Ч":"��Ч";
			default:
					return 0;
			
		}
	}

}
