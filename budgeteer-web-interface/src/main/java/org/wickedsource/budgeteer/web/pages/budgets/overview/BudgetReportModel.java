package org.wickedsource.budgeteer.web.pages.budgets.overview;

import java.io.File;
import java.util.List;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.budget.BudgetDetailData;
import org.wickedsource.budgeteer.service.budget.BudgetService;
import org.wickedsource.budgeteer.service.budget.BudgetTagFilter;
import org.wickedsource.budgeteer.service.budget.ReportService;

public class BudgetReportModel extends LoadableDetachableModel<File> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SpringBean
    private BudgetService service;
    
    @SpringBean
    private ReportService reportService;

    private long projectId;

    private IModel<BudgetTagFilter> filterModel;

    public BudgetReportModel(long projectId, IModel<BudgetTagFilter> filterModel) {
        Injector.get().inject(this);
        this.filterModel = filterModel;
        this.projectId = projectId;
    }

    @Override
    protected File load() {
        List<BudgetDetailData> budgetList = service.loadBudgetsDetailData(projectId, filterModel.getObject());
    	return reportService.createReportFile(budgetList); 
    }

    public void setFilter(IModel<BudgetTagFilter> filterModel) {
        this.filterModel = filterModel;
    }

}
