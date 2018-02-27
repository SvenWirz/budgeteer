package org.wickedsource.budgeteer.RowTemplate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class RowTemplate {

	private Class<?> dtoClass;
	private Sheet sheet;
	private Map<String,Integer> columnMapping;
	private Map<Integer,CellStyle> cellStyleMapping;
	private List<String> fields;
	
	private int startRowNumber;
	
	private static final Pattern pattern = Pattern.compile("\\{(\\w*)\\}");
	
	public RowTemplate(Class<?> dtoClass, Sheet sheet) {
		this.dtoClass = dtoClass;
		this.sheet = sheet;
		this.columnMapping = new HashMap<String,Integer>();
		this.cellStyleMapping = new HashMap<Integer,CellStyle>();
		
		createFieldList();
		findStartRow();
		createMappings();
	}
	
	private void findStartRow() {
		for(Row row : sheet) {
			for(Cell cell: row) {
				if(cellContainsTemplateTag(cell)) {
					startRowNumber = cell.getRowIndex();
					return;
				}
			}
		}
	}

	private void createFieldList() {
		fields = Arrays.stream(dtoClass.getDeclaredFields())
				.map(field -> field.getName())
				.collect(Collectors.toList());
	}

	private void createMappings() {
		Row startRow = sheet.getRow(startRowNumber);
		for(Cell cell : startRow) {
			if(cellContainsTemplateTag(cell)) {
				try {
					columnMapping.put(mapCellValueToFieldName(cell), cell.getColumnIndex());
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}
		}
		
		for(Entry<String,Integer> entry: columnMapping.entrySet()) {
			cellStyleMapping.put(entry.getValue(), startRow.getCell(entry.getValue()).getCellStyle());
		}
	}

	String mapCellValueToFieldName(Cell cell) throws IllegalArgumentException {
	Matcher matcher = pattern.matcher(cell.getStringCellValue());
	if(!matcher.matches()) {
		throw new IllegalArgumentException(); 
	}
	return matcher.group(1);
	}

	boolean cellContainsTemplateTag(Cell cell) {
		String fieldName = null;
		try {
			fieldName = mapCellValueToFieldName(cell);
		} catch(IllegalArgumentException e) {
			return false;
		}
		return fields.contains(fieldName);
	}

	/**
	 * 
	 * Getter 
	 * 
	 */
	
	public Map<String,Integer> getColumnMapping() {
		return columnMapping;
	}
	
	public Map<Integer,CellStyle> getCellStyleMapping() {
		
		return cellStyleMapping;
	}
	
	public Class<?> getDtoClass() {
		return dtoClass;
	}
	
	public int getStartRowIndex() {
		return startRowNumber;
	}
}
