package cn.itcast.core.action;

import com.opensymphony.xwork2.ActionSupport;

public class BaseAction extends ActionSupport {
	protected String[] selectedRow;
	
	
	
	public String[] getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(String[] selectedRow) {
		this.selectedRow = selectedRow;
	}
}
