package org.wickedsource.budgeteer.web.pages.budgets.overview;

import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.persistence.budget.BudgetEntity;
import org.wickedsource.budgeteer.persistence.budget.BudgetRepository;
import org.wickedsource.budgeteer.persistence.budget.BudgetTagEntity;
import org.wickedsource.budgeteer.persistence.record.PlanRecordRepository;
import org.wickedsource.budgeteer.persistence.record.WorkRecordRepository;
import org.wickedsource.budgeteer.service.ServiceTestTemplate;
import org.wickedsource.budgeteer.service.budget.BudgetDetailData;
import org.wickedsource.budgeteer.service.budget.BudgetService;
import org.wickedsource.budgeteer.service.budget.BudgetTagFilter;
import org.wickedsource.budgeteer.service.budget.ReportService;

public class ReportServiceTest extends ServiceTestTemplate {
	
	@Autowired
	private ReportService reportService;
	
    @Autowired
    private BudgetRepository budgetRepository;
    
    @Autowired
    private BudgetService budgetService;
    
    @Autowired
    private PlanRecordRepository planRecordRepository;


    @Autowired
    private WorkRecordRepository workRecordRepository;
    
	@Test
	public void testCreateReportFile() {
        Date date = new Date();
        when(budgetRepository.findByAtLeastOneTag(1l, Arrays.asList("1", "2", "3"))).thenReturn(Arrays.asList(createBudgetEntity()));
        when(workRecordRepository.getLatestWordRecordDate(1l)).thenReturn(date);
        when(workRecordRepository.getSpentBudget(1l)).thenReturn(100000.0);
        when(planRecordRepository.getPlannedBudget(1l)).thenReturn(200000.0);
        when(workRecordRepository.getAverageDailyRate(1l)).thenReturn(50000.0);
        List<BudgetDetailData> data = budgetService.loadBudgetsDetailData(1l, new BudgetTagFilter(Arrays.asList("1", "2", "3"), 1l));
        Assert.assertEquals(1, data.size());
        Assert.assertEquals(100000.0d, data.get(0).getSpent().getAmountMinor().doubleValue(), 1d);
        Assert.assertEquals(-100000.0d, data.get(0).getUnplanned().getAmountMinor().doubleValue(), 1d);
        Assert.assertEquals(50000.0d, data.get(0).getAvgDailyRate().getAmountMinor().doubleValue(), 1d);
        
		File createdFile = reportService.createReportFile(data);
	}
	

    private BudgetEntity createBudgetEntity() {
        BudgetEntity budget = new BudgetEntity();
        budget.setId(1l);
        budget.setTotal(MoneyUtil.createMoneyFromCents(100000));
        budget.setName("Budget 123");
        budget.getTags().add(new BudgetTagEntity("Tag1"));
        budget.getTags().add(new BudgetTagEntity("Tag2"));
        budget.getTags().add(new BudgetTagEntity("Tag3"));
//        budget.setDescription("Test budget");
        budget.setImportKey("budget123");
        return budget;
    }
}
