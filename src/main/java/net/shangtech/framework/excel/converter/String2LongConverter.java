package net.shangtech.framework.excel.converter;

import org.apache.poi.ss.usermodel.Cell;

public class String2LongConverter extends ColumnConverter<Long> {

	@Override
	public Long convert(Cell cell) {
		String value = cell.getRichStringCellValue().toString();
		if(value == null){
			return null;
		}
		return Long.parseLong(value);
	}

}
