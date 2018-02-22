package org.wickedsource.budgeteer.web.pages.contract.details.contractDetailChart;


import java.util.Arrays;

import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.web.PropertyLoader;
import org.wickedsource.budgeteer.web.charts.ChartStyling;
import org.wickedsource.budgeteer.web.charts.ChartUtils;

import de.adesso.wickedcharts.chartjs.ChartConfiguration;
import de.adesso.wickedcharts.chartjs.chartoptions.AxesScale;
import de.adesso.wickedcharts.chartjs.chartoptions.ChartType;
import de.adesso.wickedcharts.chartjs.chartoptions.Data;
import de.adesso.wickedcharts.chartjs.chartoptions.Dataset;
import de.adesso.wickedcharts.chartjs.chartoptions.GridLines;
import de.adesso.wickedcharts.chartjs.chartoptions.Legend;
import de.adesso.wickedcharts.chartjs.chartoptions.Options;
import de.adesso.wickedcharts.chartjs.chartoptions.Scales;
import de.adesso.wickedcharts.chartjs.chartoptions.TooltipMode;
import de.adesso.wickedcharts.chartjs.chartoptions.Tooltips;
import de.adesso.wickedcharts.chartjs.chartoptions.label.TextLabel;
import de.adesso.wickedcharts.chartjs.chartoptions.valueType.DoubleValue;

public class ContractDetailChartConfiguration extends ChartConfiguration {

	// TODO: Data Labels
	
    public ContractDetailChartConfiguration(ContractDetailChartModel model) {
    	setType(ChartType.BAR);
    	
    	setOptions(new Options()
    			.setScales(new Scales()));

    	Dataset remainingTotalBudget = new Dataset()
    			.setLabel(PropertyLoader.getProperty(ContractDetailChart.class, "chart.seriesName.remainingBudget"))
    			.setData(DoubleValue.of(MoneyUtil.toDouble(model.getObject().getRemainingTotalBudget())))
    			.setBackgroundColor(ChartStyling.getColors().get(0));

    	Dataset burnedMoneyAllBudget = new Dataset()
    			.setLabel(PropertyLoader.getProperty(ContractDetailChart.class, "chart.seriesName.burnedBudget"))
    			.setData(DoubleValue.of(MoneyUtil.toDouble(model.getObject().getBurnedMoneyAllBudgets())))
    			.setBackgroundColor(ChartStyling.getColors().get(1));

    	Dataset burnedMoneyInvoice = new Dataset()
    			.setLabel(PropertyLoader.getProperty(ContractDetailChart.class, "chart.seriesName.invoice"))
    			.setData(DoubleValue.of(MoneyUtil.toDouble(model.getObject().getBurnedMoneyInvoice())))
    			.setBackgroundColor(ChartStyling.getColors().get(2));
    	
    	setData(new Data()
    			.setLabels(TextLabel.of(ChartUtils.getMonthLabels(model.getNumberOfMonths())))
    			.setDatasets(Arrays.asList(remainingTotalBudget,burnedMoneyAllBudget,burnedMoneyInvoice)));
    	
		setOptions(new Options()
//				.setMaintainAspectRatio(false)
				.setResponsive(true)
				.setTooltips(new Tooltips()
						.setIntersect(true)
						.setMode(TooltipMode.INDEX))
				.setLegend(new Legend()
						.setDisplay(false))
				.setScales(new Scales()
						.setXAxes(new AxesScale()
								.setGridLines(new GridLines()
										.setDisplay(false)
										.setDrawBorder(false)))
						.setYAxes(new AxesScale()
								.setGridLines(new GridLines()
										.setDrawBorder(false)))));
    }
}
