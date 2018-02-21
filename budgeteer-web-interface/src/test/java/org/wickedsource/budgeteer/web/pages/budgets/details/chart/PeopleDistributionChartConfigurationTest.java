package org.wickedsource.budgeteer.web.pages.budgets.details.chart;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.service.statistics.Share;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

import de.adesso.wickedcharts.chartjs.jackson.JsonRenderer;

@SuppressWarnings("unused")
public class PeopleDistributionChartConfigurationTest extends AbstractWebTestTemplate  {
   
	// Example Javascript rendering
//	
//    @Test
//    public void test() {
//        WicketTester tester = getTester();
//        PeopleDistributionChartModel model = new PeopleDistributionChartModel(1l);
//        
//        int numOfUsers = 25;
//        ArrayList<Share> userList = new ArrayList<Share>(numOfUsers);
//        for(int i = 1; i <= numOfUsers; i++) {
//        	userList.add(new Share(MoneyUtil.createMoney(i), "User"+i));
//        }
//        
//        
//        model.setObject(userList);
//        
//    	PeopleDistributionChartConfiguration config = new PeopleDistributionChartConfiguration(model);
//    	JsonRenderer json = new JsonRenderer();
//    	System.out.println(json.toJson(config));
//    }

}
