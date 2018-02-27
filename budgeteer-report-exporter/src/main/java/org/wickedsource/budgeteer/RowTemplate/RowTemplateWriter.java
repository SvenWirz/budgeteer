package org.wickedsource.budgeteer.RowTemplate;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;


public class RowTemplateWriter<T> {
	
	private RowTemplate template;

	public RowTemplateWriter(RowTemplate sheetTemplate) {
		this.template = sheetTemplate;
	}
	
	public void writeDataIntoSheet(List<T> entries, Sheet sheet) {
		if(null != entries && !entries.isEmpty() && !template.getDtoClass().isInstance(entries.get(0))) {
			throw new IllegalArgumentException();
		}
		
		// Get Mappings
		Map<String,Integer> mapping = template.getColumnMapping();
		Map<Integer,CellStyle> styleMapping = template.getCellStyleMapping(); 
		
		// Insert new Rows
		sheet.shiftRows(template.getStartRowIndex()+1, sheet.getLastRowNum(), entries.size());
		
		// Iterate over new rows
		int rowIndex = template.getStartRowIndex();
		int columnIndex = -1;
		for (T dto : entries) {
			Row currentRow = sheet.createRow(rowIndex++);
			for (Field field : template.getDtoClass().getDeclaredFields()) {
				field.setAccessible(true);
				if (mapping.containsKey(field.getName())) {
					columnIndex = mapping.get(field.getName());
					Cell cell = currentRow.createCell(columnIndex);
					cell.setCellStyle(styleMapping.get(columnIndex));
					try {
						mapCellValue(dto, field, cell);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	void mapCellValue(T dto, Field field, Cell cell) throws IllegalAccessException {
		Object fieldValue = field.get(dto);
		if (Double.class.isInstance(fieldValue)) {
			cell.setCellValue(Double.class.cast(fieldValue));
		} else if (String.class.isInstance(fieldValue)) {
			cell.setCellValue(String.class.cast(fieldValue));
		} else if (Boolean.class.isInstance(fieldValue)) {
			cell.setCellValue(Boolean.class.cast(fieldValue));
		} else if (Date.class.isInstance(fieldValue)) {
			cell.setCellValue(Date.class.cast(fieldValue));
		} else if (RichTextString.class.isInstance(fieldValue)) {
			cell.setCellValue(RichTextString.class.cast(fieldValue));
		} else if (Calendar.class.isInstance(fieldValue)) {
			cell.setCellValue(Calendar.class.cast(fieldValue));
		} else {
			throw new IllegalArgumentException();
		}
	}

}
