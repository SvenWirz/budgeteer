package org.wickedsource.budgeteer.service.budget;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.SheetTemplate.SheetTemplate;
import org.wickedsource.budgeteer.SheetTemplate.SheetTemplateWriter;
import org.wickedsource.budgeteer.persistence.budget.BudgetEntity;
import org.wickedsource.budgeteer.persistence.budget.BudgetRepository;
import org.wickedsource.budgeteer.persistence.record.WorkRecordRepository;
import org.wickedsource.budgeteer.service.contract.ContractBaseData;
import org.wickedsource.budgeteer.service.contract.ContractService;
import org.wickedsource.budgeteer.service.person.PersonService;
import org.wickedsource.budgeteer.service.record.RecordService;
import org.wickedsource.budgeteer.service.record.WorkRecord;
import org.wickedsource.budgeteer.service.record.WorkRecordFilter;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.money.Money;

@Service
public class ReportService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private WorkRecordRepository workRecordRepository;
    
    @Autowired
    private RecordService recordService;
    
    @Autowired
    private PersonService personService;
    
    @Autowired
    private ContractService contractService;


	public File createReportFile(List<BudgetReportData> budgetList) {
		ClassLoader classLoader = getClass().getClassLoader();
		InputStream in = classLoader.getResourceAsStream("report-template.xlsx");
		XSSFWorkbook wb = null;
		try {
			wb = (XSSFWorkbook) WorkbookFactory.create(in);
		} catch (EncryptedDocumentException | IOException | InvalidFormatException e) {
			e.printStackTrace();
		}
		
		Set<String> recipients = new HashSet<String>();
		budgetList.stream().forEach(budget -> recipients.add(budget.getRecipient()));
		
		List<BudgetSummary> summary = recipients.stream().map(description -> new BudgetSummary(description))
				.collect(Collectors.toList());

		SheetTemplate template = new SheetTemplate(BudgetReportData.class, wb.getSheetAt(0));
		SheetTemplateWriter<BudgetReportData> tw = new SheetTemplateWriter<BudgetReportData>(template);
		tw.setEntries(budgetList);
		budgetList.stream().forEach(budgetData -> {
			if(budgetData.getProgress() >= 0.6 && budgetData.getProgress() < 0.8) {
				tw.addFlag(budgetData, "progress", "warning1");
			} else if(budgetData.getProgress() >= 0.8 && budgetData.getProgress() < 1 ) {
				tw.addFlag(budgetData, "progress", "warning2");
			} else if(budgetData.getProgress() >= 1 ) {
				tw.addFlag(budgetData, "progress", "warninig3");
			}
		});
		tw.write();
		
		writeDTOIntoWorkbook(wb.getSheetAt(0),summary,BudgetSummary.class);
		XSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
		tw.removeFlagSheet();
		return createOutputFile(wb);
	}
	
	private File createOutputFile(XSSFWorkbook wb) {
		File outputFile = null;
		FileOutputStream out = null;
		try {
			outputFile = File.createTempFile("report-", ".xlsx");
			outputFile.deleteOnExit();
			out = new FileOutputStream(outputFile);
			wb.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outputFile;
	}

	private <T> void writeDTOIntoWorkbook(Sheet sheet, List<T> list, Class<?> dtoClass) {
		SheetTemplate template = new SheetTemplate(dtoClass, sheet);
		SheetTemplateWriter<T> tw = new SheetTemplateWriter<T>(template);
		tw.setEntries(list);
		tw.write();
	}
	
	public List<BudgetReportData> loadBudgetReportData(long projectId, BudgetTagFilter filter) {
		List<BudgetEntity> budgets;
		if (filter.getSelectedTags().isEmpty()) {
			budgets = budgetRepository.findByProjectIdOrderByNameAsc(projectId);
		} else {
			budgets = budgetRepository.findByAtLeastOneTag(projectId, filter.getSelectedTags());
		}
		List<BudgetReportData> data = budgets.stream().map(budget -> mapBudgetToBudgetReportData(budget))
				.collect(Collectors.toList());
		return data;
	}
	
	private BudgetReportData mapBudgetToBudgetReportData(BudgetEntity entity) {
        Double spentBudgetInCents = workRecordRepository.getSpentBudget(entity.getId());
        WorkRecordFilter filter = new WorkRecordFilter(BudgeteerSession.get().getProjectId());
        filter.getBudgetList().add(new BudgetBaseData(entity.getId(), ""));
        filter.getPossiblePersons().addAll(personService.loadPeopleBaseDataByBudget(entity.getId()));
        List<WorkRecord> records = recordService.getFilteredRecords(filter);
        ContractBaseData contract = contractService.getContractById(entity.getContract().getId());
        
        Double hours = records.stream().mapToDouble(record -> record.getHours()).sum();
        String recipient = contract.getContractAttributes() != null ? contract.getContractAttributes().get(0).getValue() : "";
        
		BudgetReportData data = new BudgetReportData();
		
		data.setName(entity.getName());
		data.setFrom(contract.getStartDate());
		data.setUntil(new Date()); // TODO: ausgewählter Monat
		data.setRecipient(recipient);
		data.setSpent_net(MoneyUtil.toDouble(toMoneyNullsafe(spentBudgetInCents)));
		data.setSpent_gross(data.getSpent_net()*1.19); // TODO: Steuersatz
		data.setBudgetRemaining_net(MoneyUtil.toDouble(entity.getTotal().minus(toMoneyNullsafe(spentBudgetInCents))));
		data.setBudgetRemaining_gross(data.getBudgetRemaining_net()*1.19);
		data.setHoursAggregated(hours);
		data.setProgress(MoneyUtil.toDouble(toMoneyNullsafe(spentBudgetInCents))/MoneyUtil.toDouble(entity.getTotal()));
		return data;
	}

    private Money toMoneyNullsafe(Double cents) {
        if (cents == null) {
            return MoneyUtil.createMoneyFromCents(0l);
        } else {
            return MoneyUtil.createMoneyFromCents(Math.round(cents));
        }
    }
	

}
