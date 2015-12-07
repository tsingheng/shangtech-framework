package net.shangtech.framework.excel;

import java.beans.PropertyDescriptor;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.util.CollectionUtils;

public class WorkbookReader<T> {
	
	private static final Logger logger = LoggerFactory.getLogger(WorkbookReader.class);

	private int startSheet;
	
	private int endSheet;
	
	private Map<String, List<T>> result;
	
	private List<T> allResult;
	
	private List<ExcelError> errors;
	
	private Workbook book;
	
	private Class<T> modelClass;
	
	private Map<Integer, PropertyDescriptor> mappedFileds = new HashMap<Integer, PropertyDescriptor>();
	
	private static final CustomDateEditor DATE_EDITOR = new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true);
	
	public WorkbookReader(Class<T> modelClass){
		this.modelClass = modelClass;
		initialize();
	}
	
	public static Workbook createWorkBook(InputStream is){
		Workbook book = null;
		try{
			if(!is.markSupported()){
				is = new PushbackInputStream(is, 512);
			}
			if(POIFSFileSystem.hasPOIFSHeader(is)){
				book = new HSSFWorkbook(is);
			}else if(POIXMLDocument.hasOOXMLHeader(is)){
				book = new XSSFWorkbook(is);
			}else{
				throw new RuntimeException("不支持的excel文件格式");
			}
		}catch(Exception e){
			
		}
		return book;
	}
	
	public void read(InputStream is){
		try{
			if(!is.markSupported()){
				is = new PushbackInputStream(is, 512);
			}
			if(POIFSFileSystem.hasPOIFSHeader(is)){
				book = new HSSFWorkbook(is);
			}else if(POIXMLDocument.hasOOXMLHeader(is)){
				book = new XSSFWorkbook(is);
			}else{
				throw new RuntimeException("不支持的excel文件格式");
			}
		}catch(Exception e){
			
		}
		
		int sheetNum = book.getNumberOfSheets();
		if(sheetNum == 0){
			return;
		}
		if(endSheet > 0){
			sheetNum = endSheet;
		}
		
		result = new HashMap<String, List<T>>();
		WorkSheet workSheet = modelClass.getAnnotation(WorkSheet.class);
		int start = 0;
		if(workSheet != null){
			start = workSheet.start();
		}
		for(int i = startSheet; i < sheetNum; i++){
			List<T> resultOfSheet = new LinkedList<T>();
			Sheet sheet = book.getSheetAt(i);
			result.put(sheet.getSheetName(), resultOfSheet);
			int rowNum = sheet.getPhysicalNumberOfRows();
			for(int j = start; j < rowNum; j++){
				Row row = sheet.getRow(j);
				T object = row2Object(row);
				resultOfSheet.add(object);
			}
		}
	}
	
	private T row2Object(Row row){
		T object = BeanUtils.instantiateClass(modelClass);
		BeanWrapperImpl wrapper = new BeanWrapperImpl(object);
		wrapper.registerCustomEditor(Date.class, DATE_EDITOR);
		for(Entry<Integer, PropertyDescriptor> entry : mappedFileds.entrySet()){
			int cel = entry.getKey();
			wrapper.setPropertyValue(entry.getValue().getName(), getCellValue(row.getCell(cel)));
		}
		return object;
	}
	
	private Object getCellValue(Cell cell){
		if(cell == null){
			return null;
		}
		switch (cell.getCellType()){
		case Cell.CELL_TYPE_BLANK:
		case Cell.CELL_TYPE_ERROR:
			return null;
		case Cell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue();
		case Cell.CELL_TYPE_NUMERIC:
			return cell.getNumericCellValue();
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue();
		default:
			return null;
		}
	}

	public Map<String, List<T>> getResult() {
		return result;
	}

	public void setResult(Map<String, List<T>> result) {
		this.result = result;
	}
	
	public List<T> getAllResult(){
		if(CollectionUtils.isEmpty(result)){
			return null;
		}
		if(allResult == null){
			allResult = new LinkedList<>();
			for(List<T> list : result.values()){
				allResult.addAll(list);
			}
		}
		return allResult;
	}

	public List<ExcelError> getErrors() {
		return errors;
	}

	public void setErrors(List<ExcelError> errors) {
		this.errors = errors;
	}

	public Class<T> getModelClass() {
		return modelClass;
	}

	public void setModelClass(Class<T> modelClass) {
		this.modelClass = modelClass;
	}
	
	private void initialize() {
		PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(modelClass);
		for(PropertyDescriptor pd : pds){
			if(pd.getWriteMethod() != null){
				Field field = FieldUtils.getDeclaredField(modelClass, pd.getName(), true);
				logger.info(pd.getName());
				Column column = field.getAnnotation(Column.class);
				if(column != null){
					int col = CellReference.convertColStringToIndex(column.col());
					logger.info("col is {}", col);
					mappedFileds.put(col, pd);
				}
			}
		}
	}

	public int getStartSheet() {
		return startSheet;
	}

	public void setStartSheet(int startSheet) {
		this.startSheet = startSheet;
	}

	public int getEndSheet() {
		return endSheet;
	}

	public void setEndSheet(int endSheet) {
		this.endSheet = endSheet;
	}
	
}
