package org.wickedsource.budgeteer.web.pages.dashboard.burnedbudgetchart;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.web.BudgeteerSession;
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
import de.adesso.wickedcharts.chartjs.chartoptions.Ticks;
import de.adesso.wickedcharts.chartjs.chartoptions.label.TextLabel;
import de.adesso.wickedcharts.chartjs.chartoptions.valueType.DoubleValue;

public class BurnedBudgetChartConfiguration extends ChartConfiguration implements Serializable {
	

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BurnedBudgetChartConfiguration(BurnedBudgetChartModel model) {
    	setType(ChartType.BAR);
    	
    	List<String> labelList = ChartUtils.getWeekLabels(model.getNumberOfWeeks(), PropertyLoader.getProperty(BurnedBudgetChart.class, "chart.weekLabelFormat"));

    	
    	Dataset dataset = new Dataset()
    			.setBackgroundColor(ChartStyling.getColors().get(0))
    			.setData(DoubleValue.of(MoneyUtil.toDouble(model.getObject(), BudgeteerSession.get().getSelectedBudgetUnit())))
    			.setLabel(PropertyLoader.getProperty(BurnedBudgetChart.class, "chart.seriesName"));
    			
    	    	
    	setData(new Data()
    			.setLabels(TextLabel.of(labelList))
    			.setDatasets(Arrays.asList(dataset))
    			);
    	
		Options options = new Options()
				.setMaintainAspectRatio(false)
				.setResponsive(true)
				.setLegend(new Legend()
						.setDisplay(false))
				.setScales(new Scales()
						.setYAxes(new AxesScale()
								.setDisplay(true)
								.setTicks(new Ticks()
										.setFontFamily(ChartStyling.getFontFamily())
										.setFontSize(ChartStyling.getFontSize())
										.setBeginAtZero(true)
										.setSuggestedMin(0)
										.setMaxTicksLimit(5)))
						.setXAxes(new AxesScale()
								.setTicks(new Ticks()
										.setFontFamily(ChartStyling.getFontFamily())
										.setFontSize(ChartStyling.getFontSize()))
								.setGridLines(new GridLines()
										.setDisplay(false))));
		
		setOptions(options);
    	
    	
    }
}
