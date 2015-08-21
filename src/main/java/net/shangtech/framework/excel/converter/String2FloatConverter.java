package net.shangtech.framework.excel.converter;

import org.apache.poi.ss.usermodel.Cell;

public class String2FloatConverter extends ColumnConverter<Float> {

	@Override
	public Float convert(Cell cell) {
		String value = cell.getRichStringCellValue().toString();
		if(value == null){
			return null;
		}
		return Float.parseFloat(value);
	}

}
