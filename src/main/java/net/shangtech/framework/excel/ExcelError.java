package net.shangtech.framework.excel;

import java.io.Serializable;

public class ExcelError implements Serializable {
	
	private static final long serialVersionUID = 7527236468930492024L;
	
	private String sheet;

	private Integer row;
	
	private Integer col;
	
	private String message;
	
	public Integer getRow() {
		return row;
	}
	public void setRow(Integer row) {
		this.row = row;
	}
	public Integer getCol() {
		return col;
	}
	public void setCol(Integer col) {
		this.col = col;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getSheet() {
		return sheet;
	}
	public void setSheet(String sheet) {
		this.sheet = sheet;
	}
	
}
