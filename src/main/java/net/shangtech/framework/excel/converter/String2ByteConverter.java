package net.shangtech.framework.excel.converter;

import org.apache.poi.ss.usermodel.Cell;

public class String2ByteConverter extends ColumnConverter<Byte> {

	@Override
	public Byte convert(Cell cell) {
		String value = cell.getRichStringCellValue().toString();
		if(value == null){
			return null;
		}
		return Byte.parseByte(value);
	}
	
}
