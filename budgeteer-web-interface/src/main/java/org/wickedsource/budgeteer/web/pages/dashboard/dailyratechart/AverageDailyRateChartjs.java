package org.wickedsource.budgeteer.web.pages.dashboard.dailyratechart;

import de.adesso.wickedcharts.wicket7.chartjs.Chart;

public class AverageDailyRateChartjs extends Chart {
    private AverageDailyRateChartModel model;

    public AverageDailyRateChartjs(String id, AverageDailyRateChartModel model) {
        super(id, new AverageDailyRateChartConfiguration(model));
        this.model = model;
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        // resetting options to force re-rendering with new parameters
        setChartConfiguration(new AverageDailyRateChartConfiguration(model));
    }

}
