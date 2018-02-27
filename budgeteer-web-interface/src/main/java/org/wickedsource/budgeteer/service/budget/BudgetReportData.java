package org.wickedsource.budgeteer.service.budget;

import java.util.Date;

import lombok.Data;

@Data
public class BudgetReportData {
	private long id;
	private Date from;
	private Date until;
	private String name;
	private double spent_net;
	private double spent_gross;
	private double hoursAggregated;
	private double budgetRemaining_net;
	private double budgetRemaining_gross;
	private double progress;
	private String recipient;
}
