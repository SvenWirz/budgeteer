package org.wickedsource.budgeteer.SheetTemplate;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Test;


public class SheetTemplateWriterTest {
	
	private Workbook wb;
	private Sheet sheet;
	private CellStyle testStyle;
	private SheetTemplate template;
	
	public class TestDTO {
		private String test;
		private double foo;
		private boolean bar;
		private Date date;
		private Calendar calendar;
		
		public Calendar getCalendar() {
			return calendar;
		}
		public void setCalendar(Calendar calendar) {
			this.calendar = calendar;
		}
		public boolean isBar() {
			return bar;
		}
		public void setBar(boolean bar) {
			this.bar = bar;
		}
		public Date getDate() {
			return date;
		}
		public void setDate(Date date) {
			this.date = date;
		}
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
		
		templateRow.createCell(3).setCellValue("{foo} - {bar}");
		templateRow.createCell(10).setCellValue(1235.123456);
		templateRow.createCell(11).setCellFormula("TODAY()");
		templateRow.createCell(12).setCellValue(false);
		templateRow.createCell(13).setCellValue(new GregorianCalendar());
		templateRow.createCell(14).setCellValue(new Date());
		templateRow.createCell(15).setCellValue(new Date());
		templateRow.createCell(8).setCellType(CellType.FORMULA);
		templateRow.getCell(8).setCellFormula("CONCATENATE(\"{foo}\",\"{bar}\")");
		
		
		templateRow.createCell(100).setCellValue("hallo: {foo}");
		
		template = new SheetTemplate(TestDTO.class, sheet);
	}
	
	@SuppressWarnings("unused")
	@Test
	public void init() {
		SheetTemplateWriter<TestDTO> stw = new SheetTemplateWriter<SheetTemplateWriterTest.TestDTO>(template);
	}

	@Test
	public void testIsComplexCell() {
		SheetTemplateWriter<TestDTO> stw = new SheetTemplateWriter<SheetTemplateWriterTest.TestDTO>(template);
		assertTrue(stw.isComplexCell(sheet.getRow(4).getCell(3)));
		assertTrue(stw.isComplexCell(sheet.getRow(4).getCell(100)));
		assertFalse(stw.isComplexCell(sheet.getRow(4).getCell(5)));
		assertTrue(stw.isComplexCell(sheet.getRow(4).getCell(2)));
		assertTrue(stw.isComplexCell(sheet.getRow(4).getCell(1)));
		assertTrue(stw.isComplexCell(sheet.getRow(4).getCell(8)));
	}
	
	@Test
	public void testCopyRowAndIsLastRow() {
		SheetTemplateWriter<TestDTO> stw = new SheetTemplateWriter<SheetTemplateWriterTest.TestDTO>(template);
		stw.copyRow(sheet, 4);

		Row copiedRow = sheet.getRow(5);
		assertNotNull(copiedRow);
		
		for(Cell cell : sheet.getRow(4)) {
			assertEquals(cell.getCellTypeEnum(),cell.getCellTypeEnum());
			switch(cell.getCellTypeEnum()) {
			case NUMERIC:
				assertEquals(cell.getDateCellValue(), copiedRow.getCell(cell.getColumnIndex()).getDateCellValue());
				assertEquals(cell.getNumericCellValue(), copiedRow.getCell(cell.getColumnIndex()).getNumericCellValue(),10e-8);
				break;
			case STRING:
				assertEquals(cell.getStringCellValue(),copiedRow.getCell(cell.getColumnIndex()).getStringCellValue());
				break;
			case BOOLEAN:
				assertEquals(cell.getBooleanCellValue(), copiedRow.getCell(cell.getColumnIndex()).getBooleanCellValue());
				break;
			case FORMULA:
				assertEquals(cell.getCellFormula(), copiedRow.getCell(cell.getColumnIndex()).getCellFormula());
				break;
			default:
				fail("Not supported type // unknown type");
				break;
			}
		}
	}
	
	@Test
	public void testCopyRowAndIsNotLastRow() {
		sheet.createRow(8).createCell(9).setCellValue("Test 123");
		SheetTemplateWriter<TestDTO> stw = new SheetTemplateWriter<SheetTemplateWriterTest.TestDTO>(new SheetTemplate(TestDTO.class, sheet));
		stw.copyRow(sheet, 4);

		Row copiedRow = sheet.getRow(5);
		assertNotNull(copiedRow);
		
		for(Cell cell : sheet.getRow(4)) {
			assertEquals(cell.getCellTypeEnum(),cell.getCellTypeEnum());
			switch(cell.getCellTypeEnum()) {
			case NUMERIC:
				assertEquals(cell.getDateCellValue(), copiedRow.getCell(cell.getColumnIndex()).getDateCellValue());
				assertEquals(cell.getNumericCellValue(), copiedRow.getCell(cell.getColumnIndex()).getNumericCellValue(),10e-8);
				break;
			case STRING:
				assertEquals(cell.getStringCellValue(),copiedRow.getCell(cell.getColumnIndex()).getStringCellValue());
				break;
			case BOOLEAN:
				assertEquals(cell.getBooleanCellValue(), copiedRow.getCell(cell.getColumnIndex()).getBooleanCellValue());
				break;
			case FORMULA:
				assertEquals(cell.getCellFormula(), copiedRow.getCell(cell.getColumnIndex()).getCellFormula());
				break;
			default:
				fail("Not supported type // unknown type");
				break;
			}
		}
	}
	
	@Test
	public void testFieldValueToCellValue() throws IllegalAccessException, NoSuchFieldException, SecurityException {
		SheetTemplateWriter<TestDTO> stw = new SheetTemplateWriter<SheetTemplateWriterTest.TestDTO>(template);
		TestDTO dto = new TestDTO();
		dto.setBar(true);
		Date now = new Date();
		dto.setDate(now);
		dto.setFoo(123.4567899);
		dto.setTest("Foo");
		Calendar cal = new GregorianCalendar();
		dto.setCalendar(cal);
		
		Field[] fields = dto.getClass().getDeclaredFields();
		Arrays.stream(fields).forEach(field -> field.setAccessible(true));

		stw.mapFieldValueToCellValue(dto, fields[3], sheet.getRow(4).getCell(14));
		assertEquals(CellType.NUMERIC,sheet.getRow(4).getCell(14).getCellTypeEnum());
		assertEquals(now,sheet.getRow(4).getCell(14).getDateCellValue());
		
		stw.mapFieldValueToCellValue(dto, fields[1], sheet.getRow(4).getCell(10));
		assertEquals(CellType.NUMERIC, sheet.getRow(4).getCell(10).getCellTypeEnum());
		assertEquals(123.4567899,sheet.getRow(4).getCell(10).getNumericCellValue(),10e-8);
		
		stw.mapFieldValueToCellValue(dto, fields[2], sheet.getRow(4).getCell(12));
		assertEquals(CellType.BOOLEAN, sheet.getRow(4).getCell(12).getCellTypeEnum());
		assertEquals(true,sheet.getRow(4).getCell(12).getBooleanCellValue());
		
		stw.mapFieldValueToCellValue(dto, fields[0], sheet.getRow(4).getCell(15));
		assertEquals(CellType.STRING, sheet.getRow(4).getCell(15).getCellTypeEnum());
		assertEquals("Foo",sheet.getRow(4).getCell(15).getStringCellValue());
	}
	
	@Test
	public void testReplaceTemplateTags() {
		SheetTemplateWriter<TestDTO> stw = new SheetTemplateWriter<SheetTemplateWriterTest.TestDTO>(template);
		TestDTO dto = new TestDTO();
		dto.setBar(true);
		Date now = new Date();
		dto.setDate(now);
		dto.setFoo(123.4567899);
		dto.setTest("Foo");
		Calendar cal = new GregorianCalendar();
		dto.setCalendar(cal);
		
		Row row = sheet.getRow(4);
		
		stw.replaceTemplateTags(template.getFieldMapping(), dto, row);

		assertEquals("Foo",row.getCell(5).getStringCellValue());
		assertEquals("123.4567899 - true",row.getCell(3).getStringCellValue());
		assertEquals("hallo: 123.4567899",row.getCell(100).getStringCellValue());
	}
	
	@Test
	public void testWriteDataIntoSheet() {
		SheetTemplateWriter<TestDTO> stw = new SheetTemplateWriter<SheetTemplateWriterTest.TestDTO>(template);
		TestDTO dto1 = new TestDTO();
		dto1.setBar(true);
		dto1.setFoo(123.4567899);
		dto1.setTest("Foo");
		
		TestDTO dto2 = new TestDTO();
		dto2.setBar(false);
		dto2.setFoo(987.654321);
		dto2.setTest("Bar");
		
		stw.writeDataIntoSheet(Arrays.asList(dto1,dto2), sheet);
		
		Row row = sheet.getRow(4);

		assertEquals("Foo",row.getCell(5).getStringCellValue());
		assertEquals("123.4567899 - true",row.getCell(3).getStringCellValue());
		assertEquals("hallo: 123.4567899",row.getCell(100).getStringCellValue());
		
		row = sheet.getRow(5);
		assertEquals("Bar",row.getCell(5).getStringCellValue());
		assertEquals("987.654321 - false",row.getCell(3).getStringCellValue());
		assertEquals("hallo: 987.654321",row.getCell(100).getStringCellValue());
	}
	
	@Test
	public void testWriteDataIntoSheetAndListIsEmpty() {
		SheetTemplateWriter<TestDTO> stw = new SheetTemplateWriter<SheetTemplateWriterTest.TestDTO>(template);
		stw.writeDataIntoSheet(new ArrayList<TestDTO>(), sheet);
		assertTrue(rowIsEmpty(sheet.getRow(4)));
	}
	
	private boolean rowIsEmpty(Row row) {
	    if (row == null) {
	        return true;
	    }
	    if (row.getLastCellNum() <= 0) {
	        return true;
	    }
	    for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
	        Cell cell = row.getCell(cellNum);
	        if (cell != null && cell.getCellTypeEnum() != CellType.BLANK && !cell.toString().equals("")) {
	            return false;
	        }
	    }
	    return true;
	}
	
}
