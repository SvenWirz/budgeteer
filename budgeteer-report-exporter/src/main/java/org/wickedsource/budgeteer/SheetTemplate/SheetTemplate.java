package org.wickedsource.budgeteer.SheetTemplate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class SheetTemplate {

	private static final Pattern PATTERN = Pattern.compile("\\{([a-zA-Z0-9\\._]+)\\}");
	
	private Multimap<String,Integer> fieldMapping;

	private Sheet sheet;
	private int templateRowIndex;
	private List<String> fieldList;
	private Class<?> dtoClass;
	private FlagTemplate flagTemplate;
	
	public SheetTemplate(Class<?> dtoClass, Sheet sheet) {
		this.sheet = sheet;
		this.dtoClass = dtoClass;
		this.fieldMapping = ArrayListMultimap.create();
		createFieldList();
		processSheet();
	}

	private void createFieldList() {
		fieldList = Arrays.stream(dtoClass.getDeclaredFields())
				.map(field -> field.getName())
				.collect(Collectors.toList());
	}
	
	private void processSheet() {
		findTemplateRow();
		createFieldMapping();
		checkForFlagTemplate();
	}

	private void checkForFlagTemplate() {
		Sheet flagSheet =sheet.getWorkbook().getSheet("Flags");
		if(flagSheet != null) {
			flagTemplate = new FlagTemplate(flagSheet);
		} else {
			flagTemplate = null;
		}
	}

	private void createFieldMapping() {
		for(Cell cell : sheet.getRow(templateRowIndex)) { 
			List<String> fields = mapCellValueToFieldNames(cell);
			if(null != fields) {
				fields.stream().forEach(name -> fieldMapping.put(name, cell.getColumnIndex()));
			}
		}
	}

	private void findTemplateRow() {
		for(Row currentRow : sheet) {
			if(rowContainsTemplate(currentRow)) {
				templateRowIndex = currentRow.getRowNum();
				return;
			}
		}
	}
	
	private boolean rowContainsTemplate(Row currentRow) {
		for(Cell cell : currentRow) {
			if(cellContainsTemplateTag(cell)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param cell
	 * @return
	 * @throws IllegalArgumentException
	 */
	List<String> mapCellValueToFieldNames(Cell cell) {
	String cellValue;
	if(cell.getCellTypeEnum().equals(CellType.FORMULA)) {
		cellValue = cell.getCellFormula();
	} else if(cell.getCellTypeEnum().equals(CellType.STRING)){
		cellValue = cell.getStringCellValue();
	} else {
		return null;
	}
	Matcher matcher = PATTERN.matcher(cellValue);
	List<String> fields = new ArrayList<String>(matcher.groupCount());
	while(matcher.find()) {
		if(dtoHasField(matcher.group(1))) {
			fields.add(matcher.group(1));
		}
	}
	return fields;
	}

	boolean dtoHasField(String group) {
		if(group.charAt(0) == '.') {
			return false;
		}
		String[] tokens = group.split("\\.");
		return fieldList.contains(tokens[0]);
	}

	/**
	 * 
	 * @param cell
	 * @return
	 */
	boolean cellContainsTemplateTag(Cell cell) {
		List<String> fields = mapCellValueToFieldNames(cell);
		return (null != fields && fields.size() > 0);
	}

	public Multimap<String, Integer> getFieldMapping() {
		return fieldMapping;
	}
	
	public int getTemplateRowIndex() {
		return templateRowIndex;
	}

	public Class<?> getDtoClass() {
		return dtoClass;
	}

	public Sheet getSheet() {
		return sheet;
	}

	public List<String> getFieldList() {
		return fieldList;
	}
	
	FlagTemplate getFlagTemplate() {
		return flagTemplate;
	}
	
	
}
