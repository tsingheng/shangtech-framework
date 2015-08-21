package net.shangtech.framework.excel.converter;

import org.apache.poi.ss.usermodel.Cell;

public class String2DoubleConverter extends ColumnConverter<Double> {

	@Override
	public Double convert(Cell cell) {
		String value = cell.getRichStringCellValue().toString();
		if(value == null){
			return null;
		}
		return Double.parseDouble(value);
	}

}
