package org.wickedsource.budgeteer.web.pages.dashboard.dailyratechart;

import de.adesso.wickedcharts.chartjs.ChartConfiguration;
import de.adesso.wickedcharts.chartjs.chartoptions.AxesScale;
import de.adesso.wickedcharts.chartjs.chartoptions.CallbackFunction;
import de.adesso.wickedcharts.chartjs.chartoptions.ChartType;
import de.adesso.wickedcharts.chartjs.chartoptions.Data;
import de.adesso.wickedcharts.chartjs.chartoptions.Dataset;
import de.adesso.wickedcharts.chartjs.chartoptions.GridLines;
import de.adesso.wickedcharts.chartjs.chartoptions.Legend;
import de.adesso.wickedcharts.chartjs.chartoptions.Options;
import de.adesso.wickedcharts.chartjs.chartoptions.Scales;
import de.adesso.wickedcharts.chartjs.chartoptions.Ticks;
import de.adesso.wickedcharts.chartjs.chartoptions.label.Label;
import de.adesso.wickedcharts.chartjs.chartoptions.label.TextLabel;
import de.adesso.wickedcharts.chartjs.chartoptions.valueType.DoubleValue;

import org.apache.wicket.injection.Injector;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.PropertyLoader;
import org.wickedsource.budgeteer.web.charts.ChartStyling;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class AverageDailyRateChartConfiguration extends ChartConfiguration {

    public AverageDailyRateChartConfiguration(AverageDailyRateChartModel model) {
        Injector.get().inject(this);
        
        String dateFormat = "dd.MMM";
        
        // TODO: Date Labels are wrong. now minus days reverse
        
        setType(ChartType.LINE);
        
		Options options = new Options()
				.setMaintainAspectRatio(false)
				.setResponsive(true)
				.setLegend(new Legend()
						.setDisplay(false))
				.setScales(new Scales()
						.setYAxes(new AxesScale()
								.setDisplay(true)
								.setTicks(new Ticks()
										.setBeginAtZero(true)
										.setSuggestedMin(0)
										.setFontFamily(ChartStyling.getFontFamily())
										.setFontSize(ChartStyling.getFontSize())
										.setMaxTicksLimit(5)
										))
						.setXAxes(new AxesScale()
								.setTicks(new Ticks()
										.setCallback(new CallbackFunction("function(dataLabel, index) {return index % 2 === 0 ? dataLabel : '';}"))
										.setFontFamily(ChartStyling.getFontFamily())
										.setFontSize(ChartStyling.getFontSize())
										)
								.setGridLines(new GridLines()
										.setDisplay(false))));
		setOptions(options);
		
		Dataset dataset1 = new Dataset()
				.setFill(false)
				.setBackgroundColor(ChartStyling.getColors().get(0))
				.setBorderColor(ChartStyling.getColors().get(0))
				.setData(DoubleValue.of(MoneyUtil.toDouble(model.getObject(), BudgeteerSession.get().getSelectedBudgetUnit())))
				.setLabel(PropertyLoader.getProperty(AverageDailyRateChart.class, "chart.seriesName"));
		
		setData(new Data()
				.setDatasets(Arrays.asList(dataset1))
				.setLabels(getLabels(model.getNumberOfDays(),dateFormat)));

    }
    
    private List<Label> getLabels(int numberOfDays, String dateFormat) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat,Locale.ENGLISH);
    	ArrayList<Label> list = new ArrayList<Label>(numberOfDays);
    	LocalDateTime nowDate = LocalDateTime.now();
    	for(int i = 0; i < numberOfDays; i++) {
    		list.add(new TextLabel(nowDate.plus(i, ChronoUnit.DAYS).format(formatter)));
    	}
    	return list;
    }

}