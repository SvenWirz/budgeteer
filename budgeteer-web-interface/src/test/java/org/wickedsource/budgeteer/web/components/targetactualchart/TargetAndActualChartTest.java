package org.wickedsource.budgeteer.web.components.targetactualchart;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

import java.util.Arrays;

import org.apache.wicket.model.IModel;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.wickedsource.budgeteer.service.budget.BudgetTagFilter;
import org.wickedsource.budgeteer.service.statistics.TargetAndActual;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.pages.budgets.components.targetactualchart.BudgetsMonthlyAggregationModel;

public class TargetAndActualChartTest extends AbstractWebTestTemplate {

	@Test
	public void testRender() {
		WicketTester tester = getTester();
		IModel<TargetAndActual> model = new BudgetsMonthlyAggregationModel(model(from(new BudgetTagFilter(Arrays.asList("tag1"), 1L))));
        tester.startComponentInPage(new TargetAndActualChart("chart", model, TargetAndActualChartConfiguration.Mode.MONTHLY));
	}


}
