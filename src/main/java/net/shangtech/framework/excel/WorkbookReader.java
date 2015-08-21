package net.shangtech.framework.excel;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.shangtech.framework.excel.converter.ColumnConverter;
import net.shangtech.framework.excel.converter.Object2StringConverter;
import net.shangtech.framework.excel.converter.String2ByteConverter;
import net.shangtech.framework.excel.converter.String2DoubleConverter;
import net.shangtech.framework.excel.converter.String2FloatConverter;
import net.shangtech.framework.excel.converter.String2IntegerConverter;
import net.shangtech.framework.excel.converter.String2LongConverter;
import net.shangtech.framework.excel.support.Column;
import net.shangtech.framework.excel.support.ModelMappingException;
import net.shangtech.framework.excel.support.ModelMethod;
import net.shangtech.framework.excel.support.WorkbookReadException;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.CollectionUtils;

public class WorkbookReader<T> {
	
	private boolean recordSuccess = true;
	
	private boolean recordFailed = true;
	
	private List<WorkPage> pages;
	
	private List<LinkedList<T>> successList;
	
	private List<T> allSuccessList;
	
	private List<LinkedList<T>> failedList;
	
	private List<T> allFailedList;
	
	private Workbook book;
	
	List<ModelMethod> modelMethods;
	
	private Class<T> modelClass;
	
	public WorkbookReader(Class<T> modelClass){
		this.modelClass = modelClass;
	}	
	
	public void read(InputStream is, WorkbookReadHandler<T> handler) {
		doRead(is, handler);
	}
	
	public void read(InputStream is){
		doRead(is, null);
	}
	
	public void read(InputStream is, WorkbookReadHandler<T> handler, boolean recordSuccess, boolean recordFailed) {
		this.recordFailed = recordFailed;
		this.recordSuccess = recordSuccess;
		
		doRead(is, handler);
	}
	
	private void doRead(InputStream is, WorkbookReadHandler<T> handler) {
		try {
			if(!is.markSupported()){
				is = new PushbackInputStream(is, 512);
			}
			if(POIFSFileSystem.hasPOIFSHeader(is)){
				book = new HSSFWorkbook(is);
			}else if(POIXMLDocument.hasOOXMLHeader(is)){
				book = new XSSFWorkbook(is);
			}else{
				throw new WorkbookReadException("不支持的excel文件格式");
			}
		} catch (IOException e) {
			throw new WorkbookReadException(e);
		}
		
		int sheetNum = book.getNumberOfSheets();
		if(sheetNum == 0){
			return;
		}
		if(recordFailed){
			failedList = new ArrayList<LinkedList<T>>(sheetNum);
		}
		if(recordSuccess){
			successList = new ArrayList<LinkedList<T>>(sheetNum);
		}
		pages = new ArrayList<WorkPage>(sheetNum);
		for(int i = 0; i < sheetNum; i++){
			WorkPage page = buildWorkPage(i);
			if(page != null){
				pages.add(page);
			}
		}
		
		try {
			buildModelMethods();
			for(WorkPage page : pages){
				Sheet sheet = book.getSheetAt(page.getIndex());
				LinkedList<T> successList = new LinkedList<T>();
				LinkedList<T> failedList = new LinkedList<T>();
				int rowNum = sheet.getPhysicalNumberOfRows();
				for(int i = 1; i < rowNum; i++){
					Row row = sheet.getRow(i);
					T object = row2Object(page, row);
					boolean success = true;
					if(handler != null){
						success = handler.process(page, object);
					}
					if(success && recordSuccess){
						successList.add(object);
					}
					if(!success && recordFailed){
						failedList.add(object);
					}
				}
				if(recordSuccess){
					this.successList.add(successList);
				}
				if(recordFailed){
					this.failedList.add(failedList);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ModelMappingException(e);
		}
	}
	
	private void buildModelMethods() throws InstantiationException, IllegalAccessException{
		Class<T> clazz = getModelClass();
		Method[] methods = clazz.getDeclaredMethods();
		modelMethods = new ArrayList<ModelMethod>();
		for(Method method : methods){
			Column column = method.getAnnotation(Column.class);
			if(column == null){
				continue;
			}
			ModelMethod modelMethod = new ModelMethod();
			if(method.getParameterTypes() == null || method.getParameterTypes().length != 1){
				throw new WorkbookReadException("方法" + method.getName() + "参数格式不正确");
			}
			Class<?> paramClass = method.getParameterTypes()[0];
			ColumnConverter<?> converter = null;
			if(paramClass.equals(Long.class) || paramClass.equals(long.class)){
				converter = new String2LongConverter();
			}else if(paramClass.equals(Integer.class) || paramClass.equals(int.class)){
				converter = new String2IntegerConverter();
			}else if(paramClass.equals(Double.class) || paramClass.equals(double.class)){
				converter = new String2DoubleConverter();
			}else if(paramClass.equals(Float.class) || paramClass.equals(float.class)){
				converter = new String2FloatConverter();
			}else if(paramClass.equals(Byte.class) || paramClass.equals(byte.class)){
				converter = new String2ByteConverter();
			}else if(paramClass.equals(String.class)){
				converter = new Object2StringConverter();
			}
			if(converter == null || !column.converter().equals(Object2StringConverter.class)){
				converter = column.converter().newInstance();
			}
			converter.setPattern(column.pattern());
			modelMethod.setConverter(converter);
			modelMethod.setColumnName(column.name());
			modelMethod.setMethod(method);
			modelMethods.add(modelMethod);
		}
	}
	
	private T row2Object(WorkPage page, Row row) throws Exception{
		Class<T> clazz = getModelClass();
		T object = clazz.newInstance();
		
		for(ModelMethod method : modelMethods){
			Integer index = page.getMapping().get(method.getColumnName());
			if(index != null){
				method.invoke(object, row.getCell(index));
			}
		}
		
		return object;
	}
	
	private WorkPage buildWorkPage(int i){
		WorkPage page = new WorkPage();
		page.setIndex(i);
		Sheet sheet = book.getSheetAt(i);
		page.setName(sheet.getSheetName());
		Row row = sheet.getRow(0);
		if(row == null){
			return null;
//			throw new WorkbookReadException("表格第一行不能为空");
		}
		Map<String, Integer> mapping = new HashMap<String, Integer>();
		Integer cellNum = row.getPhysicalNumberOfCells();
		for(int j = 0; j < cellNum; j++){
			mapping.put(row.getCell(j).getRichStringCellValue().toString(), j);
		}
		page.setMapping(mapping);
		return page;
	}

	public List<LinkedList<T>> getSuccessList() {
		return successList;
	}

	public void setSuccessList(List<LinkedList<T>> successList) {
		this.successList = successList;
	}

	public List<T> getAllSuccessList() {
		if(allSuccessList == null && !CollectionUtils.isEmpty(successList)){
			allSuccessList = new LinkedList<T>();
			for(LinkedList<T> list : successList){
				allSuccessList.addAll(list);
			}
		}
		return allSuccessList;
	}

	public void setAllSuccessList(List<T> allSuccessList) {
		this.allSuccessList = allSuccessList;
	}

	public List<LinkedList<T>> getFailedList() {
		return failedList;
	}

	public void setFailedList(List<LinkedList<T>> failedList) {
		this.failedList = failedList;
	}

	public List<T> getAllFailedList() {
		if(allFailedList == null && !CollectionUtils.isEmpty(failedList)){
			allFailedList = new LinkedList<T>();
			for(LinkedList<T> list : failedList){
				allFailedList.addAll(list);
			}
		}
		return allFailedList;
	}

	public void setAllFailedList(List<T> allFailedList) {
		this.allFailedList = allFailedList;
	}

	public Class<T> getModelClass() {
		return modelClass;
	}

	public void setModelClass(Class<T> modelClass) {
		this.modelClass = modelClass;
	}
	
}
