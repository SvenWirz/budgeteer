package org.wickedsource.budgeteer.service.record;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.boot.BudgeteerBooter;
import org.wickedsource.budgeteer.persistence.record.MonthlyAggregatedRecordBean;
import org.wickedsource.budgeteer.persistence.record.WeeklyAggregatedRecordBean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {BudgeteerBooter.class})
public class RecordJoinerTest {

    private DateFormat format = new SimpleDateFormat("dd.MM.yyyy");

    @Autowired
    private RecordJoiner joiner;

    @Test
    public void testJoinWeekly() throws Exception {
        List<AggregatedRecord> records = joiner.joinWeekly(createWeeklyWorkRecords(), createWeeklyPlanRecords());
        Assert.assertEquals(3, records.size());

        Assert.assertEquals(format.parse("03.03.2014"), records.get(0).getAggregationPeriodStart());
        Assert.assertEquals(format.parse("09.03.2014"), records.get(0).getAggregationPeriodEnd());
        Assert.assertEquals("Week #10", records.get(0).getAggregationPeriodTitle());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(50000), records.get(0).getBudgetBurned());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(12300), records.get(0).getBudgetPlanned());
        Assert.assertEquals(5d, records.get(0).getHours(), 0.1d);

        Assert.assertEquals(format.parse("06.04.2015"), records.get(1).getAggregationPeriodStart());
        Assert.assertEquals(format.parse("12.04.2015"), records.get(1).getAggregationPeriodEnd());
        Assert.assertEquals("Week #15", records.get(1).getAggregationPeriodTitle());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(50000), records.get(1).getBudgetBurned());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(12300), records.get(1).getBudgetPlanned());
        Assert.assertEquals(5d, records.get(1).getHours(), 0.1d);

        Assert.assertEquals(format.parse("13.04.2015"), records.get(2).getAggregationPeriodStart());
        Assert.assertEquals(format.parse("19.04.2015"), records.get(2).getAggregationPeriodEnd());
        Assert.assertEquals("Week #16", records.get(2).getAggregationPeriodTitle());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(60000), records.get(2).getBudgetBurned());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(32100), records.get(2).getBudgetPlanned());
        Assert.assertEquals(6d, records.get(2).getHours(), 0.1d);

    }

    @Test
    public void testJoinMonthly() throws Exception {
        List<AggregatedRecord> records = joiner.joinMonthly(createMonthlyWorkRecords(), createMonthlyPlanRecords());
        Assert.assertEquals(3, records.size());

        Assert.assertEquals(format.parse("01.01.2014"), records.get(0).getAggregationPeriodStart());
        Assert.assertEquals(format.parse("31.01.2014"), records.get(0).getAggregationPeriodEnd());
        Assert.assertEquals("2014/01", records.get(0).getAggregationPeriodTitle());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(50000), records.get(0).getBudgetBurned());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(12300), records.get(0).getBudgetPlanned());
        Assert.assertEquals(5d, records.get(0).getHours(), 0.1d);

        Assert.assertEquals(format.parse("01.06.2015"), records.get(1).getAggregationPeriodStart());
        Assert.assertEquals(format.parse("30.06.2015"), records.get(1).getAggregationPeriodEnd());
        Assert.assertEquals("2015/06", records.get(1).getAggregationPeriodTitle());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(50000), records.get(1).getBudgetBurned());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(12300), records.get(1).getBudgetPlanned());
        Assert.assertEquals(5d, records.get(1).getHours(), 0.1d);

        Assert.assertEquals(format.parse("01.07.2015"), records.get(2).getAggregationPeriodStart());
        Assert.assertEquals(format.parse("31.07.2015"), records.get(2).getAggregationPeriodEnd());
        Assert.assertEquals("2015/07", records.get(2).getAggregationPeriodTitle());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(60000), records.get(2).getBudgetBurned());
        Assert.assertEquals(MoneyUtil.createMoneyFromCents(32100), records.get(2).getBudgetPlanned());
        Assert.assertEquals(6d, records.get(2).getHours(), 0.1d);

    }

    private List<WeeklyAggregatedRecordBean> createWeeklyWorkRecords() {
        List<WeeklyAggregatedRecordBean> beans = new ArrayList<WeeklyAggregatedRecordBean>();
        beans.add(new WeeklyAggregatedRecordBean(2015, 15, 5d, 50000));
        beans.add(new WeeklyAggregatedRecordBean(2015, 16, 6d, 60000));
        beans.add(new WeeklyAggregatedRecordBean(2014, 10, 5d, 50000));
        return beans;
    }

    private List<WeeklyAggregatedRecordBean> createWeeklyPlanRecords(){
        List<WeeklyAggregatedRecordBean> beans = new ArrayList<WeeklyAggregatedRecordBean>();
        beans.add(new WeeklyAggregatedRecordBean(2015, 15, 5d, 12300));
        beans.add(new WeeklyAggregatedRecordBean(2014, 10, 5d, 12300));
        beans.add(new WeeklyAggregatedRecordBean(2015, 16, 6d, 32100));
        return beans;
    }

    private List<MonthlyAggregatedRecordBean> createMonthlyWorkRecords() {
        List<MonthlyAggregatedRecordBean> beans = new ArrayList<MonthlyAggregatedRecordBean>();
        beans.add(new MonthlyAggregatedRecordBean(2015, 5, 5d, 50000));
        beans.add(new MonthlyAggregatedRecordBean(2015, 6, 6d, 60000));
        beans.add(new MonthlyAggregatedRecordBean(2014, 0, 5d, 50000));
        return beans;
    }

    private List<MonthlyAggregatedRecordBean> createMonthlyPlanRecords(){
        List<MonthlyAggregatedRecordBean> beans = new ArrayList<MonthlyAggregatedRecordBean>();
        beans.add(new MonthlyAggregatedRecordBean(2015, 5, 5d, 12300));
        beans.add(new MonthlyAggregatedRecordBean(2014, 0, 5d, 12300));
        beans.add(new MonthlyAggregatedRecordBean(2015, 6, 6d, 32100));
        return beans;
    }


}
