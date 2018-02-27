package org.wickedsource.budgeteer.service.budget;

import lombok.Data;

@Data
public class BudgetSummary {
	public BudgetSummary(String description) {
		this.name = description;
	}

	private String name;
}
