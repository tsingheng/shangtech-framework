package net.shangtech.framework.excel.converter;

import org.apache.poi.ss.usermodel.Cell;

public class String2IntegerConverter extends ColumnConverter<Integer> {

	@Override
	public Integer convert(Cell cell) {
		String value = cell.getRichStringCellValue().toString();
		if(value == null){
			return null;
		}
		return Integer.parseInt(value);
	}

}
