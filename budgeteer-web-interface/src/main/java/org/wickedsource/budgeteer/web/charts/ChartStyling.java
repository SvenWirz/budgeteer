package org.wickedsource.budgeteer.web.charts;

import java.util.Arrays;
import java.util.List;

import de.adesso.wickedcharts.chartjs.chartoptions.colors.RgbColor;

public class ChartStyling {
	public static List<RgbColor> getColors() {
		return Arrays.asList(
				new RgbColor(0, 192, 239),
                new RgbColor(243, 156, 18),
                new RgbColor(0, 115, 183),
                new RgbColor(0, 166, 90),
                new RgbColor(0, 31, 63),
                new RgbColor(57, 204, 204),
                new RgbColor(61, 153, 112),
                new RgbColor(61, 153, 112),
                new RgbColor(245, 105, 84)
                );
	}
}
