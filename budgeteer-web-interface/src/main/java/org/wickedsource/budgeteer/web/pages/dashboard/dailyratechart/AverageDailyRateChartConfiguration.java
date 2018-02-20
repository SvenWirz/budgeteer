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
import de.adesso.wickedcharts.chartjs.chartoptions.colors.RgbColor;
import de.adesso.wickedcharts.chartjs.chartoptions.label.Label;
import de.adesso.wickedcharts.chartjs.chartoptions.label.TextLabel;
import de.adesso.wickedcharts.chartjs.chartoptions.valueType.DoubleValue;

import org.apache.wicket.injection.Injector;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.PropertyLoader;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AverageDailyRateChartConfiguration extends ChartConfiguration {

    public AverageDailyRateChartConfiguration(AverageDailyRateChartModel model) {
        Injector.get().inject(this);
        
        String dateFormat = "dd.MMM";
        
        
        setType(ChartType.LINE);
        
		Options options = new Options()
				.setResponsive(true)
				.setLegend(new Legend()
						.setDisplay(false))
				.setScales(new Scales()
						.setYAxes(new AxesScale()
								.setDisplay(true)
								.setTicks(new Ticks()
										.setBeginAtZero(true)
										.setSuggestedMin(0)))
						.setXAxes(new AxesScale()
								.setTicks(new Ticks()
										.setCallback(new CallbackFunction("function(dataLabel, index) {return index % 2 === 0 ? dataLabel : '';}"))
										)
								.setGridLines(new GridLines()
										.setDisplay(false))));
		setOptions(options);
		
		Dataset dataset1 = new Dataset()
				.setFill(false)
				.setBackgroundColor(new RgbColor(0, 192, 239))
				.setBorderColor(new RgbColor(0, 192, 239))
				.setData(DoubleValue.of(MoneyUtil.toDouble(model.getObject(), BudgeteerSession.get().getSelectedBudgetUnit())))
				.setLabel(PropertyLoader.getProperty(AverageDailyRateChart.class, "chart.seriesName"))
				;
		
		setData(new Data()
				.setDatasets(Arrays.asList(dataset1))
				.setLabels(getLabels(model.getNumberOfDays(),dateFormat)));

    }
    
    private List<Label> getLabels(int numberOfDays, String dateFormat) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
    	ArrayList<Label> list = new ArrayList<Label>(numberOfDays);
    	LocalDateTime nowDate = LocalDateTime.now();
    	for(int i = 0; i < numberOfDays; i++) {
    		list.add(new TextLabel(nowDate.plus(i, ChronoUnit.DAYS).format(formatter)));
    	}
    	return list;
    }

}
