package org.wickedsource.budgeteer.RowTemplate;
import static org.junit.Assert.*;

import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Test;

public class RowTemplateTest {
	
	private Workbook wb;
	private Sheet sheet;
	private CellStyle testStyle;
	
	public class TestDTO {
		private String test;
		private double foo;
		private double bar;
		public String getTest() {
			return test;
		}
		public void setTest(String test) {
			this.test = test;
		}
		public double getFoo() {
			return foo;
		}
		public void setFoo(double foo) {
			this.foo = foo;
		}
		public double getBar() {
			return bar;
		}
		public void setBar(double bar) {
			this.bar = bar;
		}

		
	}
	
	@Before
	public void setUp() {
		wb = new XSSFWorkbook();
		sheet = wb.createSheet();
		Row templateRow = sheet.createRow(4);
		templateRow.createCell(2).setCellValue("test");
		
		templateRow.createCell(5).setCellValue("{test}");
		testStyle = templateRow.getCell(5).getCellStyle();
		testStyle.setFillBackgroundColor(IndexedColors.BLACK.getIndex());
		
		templateRow.createCell(3).setCellValue("{bar}");
		
		
		templateRow.createCell(100).setCellValue("{foo}");
	}
	
	@Test
	public void init() {
		new RowTemplate(TestDTO.class, sheet);
	}
	
	@Test
	public void testGetColumnMapping() {
		RowTemplate st = new RowTemplate(TestDTO.class, sheet);
		Map<String,Integer> mapping = st.getColumnMapping();
		assertNotNull(mapping);
		
		assertTrue(mapping.containsKey("test"));
		assertEquals(mapping.get("test"),Integer.valueOf(5));
		assertTrue(mapping.containsKey("bar"));
		assertEquals(mapping.get("bar"),Integer.valueOf(3));
		assertTrue(mapping.containsKey("foo"));
		assertEquals(mapping.get("foo"),Integer.valueOf(100));
	}
	
	@Test
	public void testGetStyleMapping() {
		RowTemplate st = new RowTemplate(TestDTO.class, sheet);
		Map<Integer,CellStyle> cellStyleMapping = st.getCellStyleMapping();
		assertNotNull(cellStyleMapping);
		assertEquals(testStyle,cellStyleMapping.get(5));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCellContainsTemplateTagAndExceptException() {
		RowTemplate st = new RowTemplate(TestDTO.class, sheet);
		Cell cell = sheet.getRow(4).getCell(2);
		st.mapCellValueToFieldName(cell);
	}
	
	@Test
	public void testCellContainsTemplateTagAndExceptValue() {
		RowTemplate st = new RowTemplate(TestDTO.class, sheet);
		Cell cell = sheet.getRow(4).getCell(5);
		String value = st.mapCellValueToFieldName(cell);
		assertEquals("test", value);
	}
	
	@Test
	public void testCellContainsTemplateTagAndContains() {
		RowTemplate st = new RowTemplate(TestDTO.class, sheet);
		Cell testCell = sheet.getRow(4).getCell(5);
		boolean contains = st.cellContainsTemplateTag(testCell);
		assertTrue(contains);
	}
	
	@Test
	public void testCellContainsTemplateTagAndNotContains() {
		RowTemplate st = new RowTemplate(TestDTO.class, sheet);
		Cell testCell = sheet.getRow(4).getCell(2);
		boolean contains = st.cellContainsTemplateTag(testCell);
		assertFalse(contains);
	}

}
