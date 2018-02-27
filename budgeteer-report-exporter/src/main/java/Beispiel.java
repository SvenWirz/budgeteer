import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.poi.xssf.usermodel.*;
import org.wickedsource.budgeteer.SheetTemplate.SheetTemplate;
import org.wickedsource.budgeteer.SheetTemplate.SheetTemplateWriter;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;


public class Beispiel {
	public static void main(String[] args) throws EncryptedDocumentException, InvalidFormatException, IOException {
		String template = "report-mapping.xlsx";
		String output = "report.xlsx";
		
		InputStream in = new FileInputStream(template);
		XSSFWorkbook wb = (XSSFWorkbook) WorkbookFactory.create(in);

		Sheet overallSheet = wb.getSheetAt(0);
		
		ReportService rs = new ReportService();
		ReportDTO report = rs.getDummyData();
		report.setSummaries(Arrays.asList(new SummaryDTO("Test"), new SummaryDTO("Test2")));

		SheetTemplate rowTemplate = new SheetTemplate(RowDTO.class,overallSheet);
		SheetTemplateWriter<RowDTO> rtw = new SheetTemplateWriter<RowDTO>(rowTemplate);
		rtw.writeDataIntoSheet(report.getEntries(),overallSheet);
		
		SheetTemplate summaryTemplate = new SheetTemplate(SummaryDTO.class,overallSheet);
		SheetTemplateWriter<SummaryDTO> stw = new SheetTemplateWriter<SummaryDTO>(summaryTemplate);
		stw.writeDataIntoSheet(report.getSummaries(),overallSheet);

		XSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
		
		File outputFile = new File(output);
		FileOutputStream out = new FileOutputStream(outputFile);
		wb.write(out);
		wb.close();

	}
}
