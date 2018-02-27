package org.wickedsource.budgeteer.service.budget;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.stereotype.Service;
import org.wickedsource.budgeteer.SheetTemplate.SheetTemplate;
import org.wickedsource.budgeteer.SheetTemplate.SheetTemplateWriter;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

@Service
public class ReportService {

	public File createReportFile(List<BudgetDetailData> budgetList) {
		ClassLoader classLoader = getClass().getClassLoader();
		InputStream in = classLoader.getResourceAsStream("report-mapping.xlsx");
		Workbook wb = null;
		try {
			wb = WorkbookFactory.create(in);
		} catch (EncryptedDocumentException | IOException | InvalidFormatException e) {
			e.printStackTrace();
		}
		Sheet sheet = wb.getSheetAt(0);
		SheetTemplate template = new SheetTemplate(BudgetDetailData.class, sheet);
		
		SheetTemplateWriter<BudgetDetailData> tw = new SheetTemplateWriter<BudgetDetailData>(template);
		tw.writeDataIntoSheet(budgetList, wb.getSheetAt(0));
		
		File outputFile = new File("report.xlsx");
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(outputFile);
			wb.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outputFile;
	}
	
	

}
