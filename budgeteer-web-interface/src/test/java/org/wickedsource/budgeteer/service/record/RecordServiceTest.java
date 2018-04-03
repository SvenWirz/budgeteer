package org.wickedsource.budgeteer.service.record;

import com.querydsl.core.types.Predicate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.boot.BudgeteerBooter;
import org.wickedsource.budgeteer.persistence.budget.BudgetEntity;
import org.wickedsource.budgeteer.persistence.imports.ImportEntity;
import org.wickedsource.budgeteer.persistence.person.PersonEntity;
import org.wickedsource.budgeteer.persistence.record.MonthlyAggregatedRecordBean;
import org.wickedsource.budgeteer.persistence.record.WeeklyAggregatedRecordBean;
import org.wickedsource.budgeteer.persistence.record.WorkRecordEntity;
import org.wickedsource.budgeteer.persistence.record.WorkRecordRepository;
import org.wickedsource.budgeteer.service.budget.BudgetTagFilter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {BudgeteerBooter.class})
public class RecordServiceTest {

    private static final List<String> EMPTY_STRING_LIST = new ArrayList<>(0);

    @MockBean
    private RecordJoiner recordJoiner;

    @MockBean
    private WorkRecordRepository workRecordRepository;

    @Autowired
    private RecordService service;

    @Test
    public void testGetWeeklyAggregationForPerson() {
        List<AggregatedRecord> recordList = createAggregatedRecordList();
        when(recordJoiner.joinWeekly(anyListOf(WeeklyAggregatedRecordBean.class), anyListOf(WeeklyAggregatedRecordBean.class))).thenReturn(recordList);
        List<AggregatedRecord> resultList = service.getWeeklyAggregationForPerson(1L);
        Assert.assertEquals(recordList, resultList);
    }

    @Test
    public void testGetMonthlyAggregationForPerson() {
        List<AggregatedRecord> recordList = createAggregatedRecordList();
        when(recordJoiner.joinMonthly(anyListOf(MonthlyAggregatedRecordBean.class), anyListOf(MonthlyAggregatedRecordBean.class))).thenReturn(recordList);
        List<AggregatedRecord> resultList = service.getMonthlyAggregationForPerson(1L);
        Assert.assertEquals(recordList, resultList);
    }

    @Test
    public void testGetWeeklyAggregationForBudget() {
        List<AggregatedRecord> recordList = createAggregatedRecordList();
        when(recordJoiner.joinWeekly(anyListOf(WeeklyAggregatedRecordBean.class), anyListOf(WeeklyAggregatedRecordBean.class))).thenReturn(recordList);
        List<AggregatedRecord> resultList = service.getWeeklyAggregationForBudget(1L);
        Assert.assertEquals(recordList, resultList);
    }

    @Test
    public void testGetMonthlyAggregationForBudget() {
        List<AggregatedRecord> recordList = createAggregatedRecordList();
        when(recordJoiner.joinWeekly(anyListOf(WeeklyAggregatedRecordBean.class), anyListOf(WeeklyAggregatedRecordBean.class))).thenReturn(recordList);
        List<AggregatedRecord> resultList = service.getWeeklyAggregationForBudget(1L);
        Assert.assertEquals(recordList, resultList);
    }

    @Test
    public void testGetWeeklyAggregationForBudgets() {
        List<AggregatedRecord> recordList = createAggregatedRecordList();
        when(recordJoiner.joinWeekly(anyListOf(WeeklyAggregatedRecordBean.class), anyListOf(WeeklyAggregatedRecordBean.class))).thenReturn(recordList);
        List<AggregatedRecord> resultList = service.getWeeklyAggregationForBudgets(new BudgetTagFilter(EMPTY_STRING_LIST, 1L));
        Assert.assertEquals(recordList, resultList);
    }

    @Test
    public void testGetMonthlyAggregationForBudgets() {
        List<AggregatedRecord> recordList = createAggregatedRecordList();
        when(recordJoiner.joinMonthly(anyListOf(MonthlyAggregatedRecordBean.class), anyListOf(MonthlyAggregatedRecordBean.class))).thenReturn(recordList);
        List<AggregatedRecord> resultList = service.getMonthlyAggregationForBudgets(new BudgetTagFilter(EMPTY_STRING_LIST, 1L));
        Assert.assertEquals(recordList, resultList);
    }

    @Test
    public void testGetFilteredRecords() {
        List<WorkRecordEntity> recordList = createRecordList();
        when(workRecordRepository.findAll(any(Predicate.class))).thenReturn(recordList);
        List<WorkRecord> filteredRecords = service.getFilteredRecords(new WorkRecordFilter(1L));
        Assert.assertEquals(recordList.size(), filteredRecords.size());
        Assert.assertEquals(WorkRecord.class, filteredRecords.get(0).getClass());
    }

    private List<WorkRecordEntity> createRecordList() {
        List<WorkRecordEntity> list = new ArrayList<>();
        WorkRecordEntity record = new WorkRecordEntity();
        record.setDailyRate(MoneyUtil.createMoney(100d));
        record.setDate(new Date());
        record.setId(1L);
        record.setBudget(new BudgetEntity());
        record.setImportRecord(new ImportEntity());
        record.setMinutes(480);
        record.setPerson(new PersonEntity());
        list.add(record);
        return list;
    }


    private List<AggregatedRecord> createAggregatedRecordList() {
        List<AggregatedRecord> list = new ArrayList<>();
        list.add(new AggregatedRecord());
        list.add(new AggregatedRecord());
        list.add(new AggregatedRecord());
        return list;
    }
}
