package org.wickedsource.budgeteer.persistence.budget;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.IntegrationTestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BudgetRepositoryTest extends IntegrationTestTemplate {

    @Autowired
    private BudgetRepository budgetRepository;

    @Test
    @DatabaseSetup("getAllTagsInProject.xml")
    @DatabaseTearDown(value = "getAllTagsInProject.xml", type = DatabaseOperation.DELETE_ALL)
    public void testGetAllTagsInProject() {
        List<String> tags = budgetRepository.getAllTagsInProject(1l);
        Assert.assertEquals(4, tags.size());
        Assert.assertTrue(tags.contains("Tag 1"));
        Assert.assertTrue(tags.contains("Tag 2"));
        Assert.assertTrue(tags.contains("Tag 3"));
        Assert.assertTrue(tags.contains("Tag 4"));
    }

    @Test
    @DatabaseSetup("findByProjectId.xml")
    @DatabaseTearDown(value = "findByProjectId.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindByProjectId() {
        List<BudgetEntity> budgets = budgetRepository.findByProjectIdOrderByNameAsc(1l);
        Assert.assertEquals(1, budgets.size());
        Assert.assertEquals("Budget 1", budgets.get(0).getName());
    }

    @Test
    @DatabaseSetup("findByAtLeastOneTag.xml")
    @DatabaseTearDown(value = "findByAtLeastOneTag.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindByAtLeastOneTag() {
        List<BudgetEntity> budgets = budgetRepository.findByAtLeastOneTag(1l, Arrays.asList("Tag 1", "Tag 3"));
        Assert.assertEquals(2, budgets.size());
        List<BudgetEntity> budgets2 = budgetRepository.findByAtLeastOneTag(1l, Arrays.asList("Tag 3"));
        Assert.assertEquals(1, budgets2.size());
    }

    @Test
    @DatabaseSetup("getMissingBudgetTotals.xml")
    @DatabaseTearDown(value = "getMissingBudgetTotals.xml", type = DatabaseOperation.DELETE_ALL)
    public void testGetMissingBudgetTotalsForProject() {
        List<MissingBudgetTotalBean> missingTotals = budgetRepository.getMissingBudgetTotalsForProject(1l);
        Assert.assertEquals(1, missingTotals.size());
        Assert.assertEquals(1l, missingTotals.get(0).getBudgetId());
        Assert.assertEquals("Budget 1", missingTotals.get(0).getBudgetName());
    }

    @Test
    @DatabaseSetup("getMissingBudgetTotals.xml")
    @DatabaseTearDown(value = "getMissingBudgetTotals.xml", type = DatabaseOperation.DELETE_ALL)
    public void testGetMissingBudgetTotalForBudget() {
        MissingBudgetTotalBean missingTotal = budgetRepository.getMissingBudgetTotalForBudget(1l);
        Assert.assertEquals(1l, missingTotal.getBudgetId());
        Assert.assertEquals("Budget 1", missingTotal.getBudgetName());
        Assert.assertNull(budgetRepository.getMissingBudgetTotalForBudget(2l));
    }

    @Test
    @DatabaseSetup("getTaxCoefficient.xml")
    @DatabaseTearDown(value = "getTaxCoefficient.xml", type = DatabaseOperation.DELETE_ALL)
    public void testGetTaxCoefficientForBudget() {
        Double taxCoefficient1 = budgetRepository.getTaxCoefficientByBudget(1L);
        Double taxCoefficient2 = budgetRepository.getTaxCoefficientByBudget(2L);
        Double taxCoefficient3 = budgetRepository.getTaxCoefficientByBudget(3L);
        Double taxCoefficient4 = budgetRepository.getTaxCoefficientByBudget(4L);
        assertEquals(2.0, taxCoefficient1, 10e-8);
        assertEquals(1.0, taxCoefficient2, 10e-8);
        assertEquals(1.19, taxCoefficient3, 10e-8);
        assertEquals(1.0, taxCoefficient4, 10e-8);
    }

}
