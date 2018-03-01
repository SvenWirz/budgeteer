package org.wickedsource.budgeteer.SheetTemplate;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.collections4.map.MultiKeyMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.google.common.collect.Multimap;

public class TemplateWriter<T> {
	
	
	private SheetTemplate template;
	private Sheet sheet;
	private List<T> entries;
	private MultiKeyMap<Object,String> flagMapping;
	
	private int currentRow;

	public TemplateWriter(SheetTemplate sheetTemplate) {
		this.template = sheetTemplate;
		this.sheet = sheetTemplate.getSheet();
		flagMapping = new MultiKeyMap<Object,String>();
	}
	
	public TemplateWriter(SheetTemplate sheetTemplate, List<T> entries) {
		this.template = sheetTemplate;
		this.sheet = sheetTemplate.getSheet();
		this.entries = entries;
		flagMapping = new MultiKeyMap<Object,String>();
	}
	
	public void setEntries(List<T> entries) {
		this.entries = entries;
	}
	
	public void write() {
		insertRows(); // insert Rows with template tags
		if(null != entries && !entries.isEmpty()) {
			currentRow = template.getTemplateRowIndex();
			entries.stream().forEach(dto -> insert(dto));
		}
	}

	void insert(T dto) {
		replaceTemplateTags(dto);
		setFlags(dto);
		currentRow++;
	}

	private void setFlags(T dto) {
		// TODO Auto-generated method stub
		
	}

	private void replaceTemplateTags(T dto) {
		// TODO Auto-generated method stub
		
	}

	void insertRows() {
		// determine number of rows to be inserted
		int numberOfRows;
		if(null != entries) {
			numberOfRows = entries.size();
		} else {
			numberOfRows = 0;
		}
		
		boolean templateRowIsLastRow = (template.getTemplateRowIndex() == sheet.getLastRowNum());
		
		if(!templateRowIsLastRow && numberOfRows == 0) { // if we do not have data, we have to remove the template row
			sheet.removeRow(sheet.getRow(template.getTemplateRowIndex()));
			sheet.shiftRows(template.getTemplateRowIndex(), sheet.getLastRowNum(), -1);
		} 
		
		// copy template row numberOfRows-1 times
		for(int i = 0; i < numberOfRows-1; i++) {
			copyRow(sheet,template.getTemplateRowIndex()+i);
		}
	}
	
	void copyRow(Sheet sheet, int from) {
		if(from < sheet.getLastRowNum()) {
			sheet.shiftRows(from+1, sheet.getLastRowNum(), 1);
		}
		Row copyRow = sheet.getRow(from);
		Row insertRow = sheet.createRow(from+1);
		for(Cell copyCell : copyRow) {
			Cell insertCell = insertRow.createCell(copyCell.getColumnIndex());
			copyCellValues(copyCell,insertCell);
			insertCell.setCellStyle(copyCell.getCellStyle());
		}
	}
	
	void copyCellValues(Cell copyCell, Cell insertCell) {
		switch (copyCell.getCellTypeEnum()) {
		case STRING:
			insertCell.setCellValue(copyCell.getStringCellValue());
			break;
		case BOOLEAN:
			insertCell.setCellValue(copyCell.getBooleanCellValue());
			break;
		case BLANK:
			insertCell.setCellType(CellType.BLANK);
			break;
		case FORMULA:
			insertCell.setCellFormula(copyCell.getCellFormula());
			break;
		case NUMERIC:
			insertCell.setCellValue(copyCell.getNumericCellValue());
			break;
		default:
			throw new IllegalArgumentException("Unknown Type"); // should not occure
		}
	}

	public void setFlag(T dto, Field field, Cell cell) {
		
	}
	
	public void removeFlagSheet() {
		Sheet flagSheet = sheet.getWorkbook().getSheet("Flags");
		if(null != flagSheet) {
			int sheetIndex = sheet.getWorkbook().getSheetIndex(flagSheet);
			sheet.getWorkbook().removeSheetAt(sheetIndex);
		}
	}
}
