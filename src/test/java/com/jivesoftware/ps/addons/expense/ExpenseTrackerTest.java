package com.jivesoftware.ps.addons.expense;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ExpenseTrackerTest {

    private ExpenseTracker tracker;

    @BeforeEach
    public void setUp() {
        tracker = new ExpenseTracker();
    }

    @Test
    public void recordExpenseAssignsIdentifiersAndStoresData() {
        Set<String> tags = new HashSet<String>(Arrays.asList("lunch", "team"));
        Expense expense = tracker.recordExpense(new BigDecimal("12.75"),
                LocalDate.of(2024, Month.JANUARY, 15),
                ExpenseCategory.FOOD,
                "Team lunch",
                tags);

        assertEquals(1L, expense.getId());
        assertEquals(new BigDecimal("12.75"), expense.getAmount());
        assertEquals(LocalDate.of(2024, Month.JANUARY, 15), expense.getDate());
        assertEquals(ExpenseCategory.FOOD, expense.getCategory());
        assertEquals("Team lunch", expense.getDescription());
        assertEquals(tags, expense.getTags());
        assertEquals(1, tracker.getExpenses().size());
    }

    @Test
    public void totalSpentIsCalculatedPerMonth() {
        YearMonth march = YearMonth.of(2024, Month.MARCH);
        tracker.recordExpense(new BigDecimal("45.10"), LocalDate.of(2024, Month.MARCH, 1), ExpenseCategory.FOOD,
                "Groceries", null);
        tracker.recordExpense(new BigDecimal("25.40"), LocalDate.of(2024, Month.MARCH, 10), ExpenseCategory.TRANSPORTATION,
                "Fuel", null);
        tracker.recordExpense(new BigDecimal("19.99"), LocalDate.of(2024, Month.APRIL, 5), ExpenseCategory.ENTERTAINMENT,
                "Movies", null);

        BigDecimal total = tracker.getTotalSpent(march);
        assertEquals(new BigDecimal("70.50"), total);
    }

    @Test
    public void categoryTotalsAndFilteringWorkTogether() {
        YearMonth january = YearMonth.of(2024, Month.JANUARY);
        tracker.recordExpense(new BigDecimal("30.00"), LocalDate.of(2024, Month.JANUARY, 3), ExpenseCategory.FOOD,
                "Groceries", null);
        tracker.recordExpense(new BigDecimal("45.00"), LocalDate.of(2024, Month.JANUARY, 8), ExpenseCategory.UTILITIES,
                "Electricity", null);
        tracker.recordExpense(new BigDecimal("20.00"), LocalDate.of(2024, Month.JANUARY, 20), ExpenseCategory.FOOD,
                "Dining", null);

        List<Expense> foodExpenses = tracker.getExpenses(january, ExpenseCategory.FOOD);
        assertEquals(2, foodExpenses.size());

        assertEquals(new BigDecimal("50.00"), tracker.getCategoryTotals(january).get(ExpenseCategory.FOOD));
        assertEquals(new BigDecimal("45.00"), tracker.getCategoryTotals(january).get(ExpenseCategory.UTILITIES));
    }

    @Test
    public void budgetsProvideVarianceAndStatus() {
        YearMonth february = YearMonth.of(2024, Month.FEBRUARY);
        tracker.recordExpense(new BigDecimal("150.00"), LocalDate.of(2024, Month.FEBRUARY, 2), ExpenseCategory.HOUSING,
                "Rent", null);
        tracker.recordExpense(new BigDecimal("65.00"), LocalDate.of(2024, Month.FEBRUARY, 11), ExpenseCategory.ENTERTAINMENT,
                "Concert", null);
        tracker.recordExpense(new BigDecimal("80.00"), LocalDate.of(2024, Month.FEBRUARY, 14), ExpenseCategory.ENTERTAINMENT,
                "Theatre", null);

        tracker.setBudget(february, ExpenseCategory.HOUSING, new BigDecimal("200.00"));
        tracker.setBudget(february, ExpenseCategory.ENTERTAINMENT, new BigDecimal("120.00"));

        Optional<BigDecimal> housingVariance = tracker.getBudgetVariance(february, ExpenseCategory.HOUSING);
        assertTrue(housingVariance.isPresent());
        assertEquals(new BigDecimal("50.00"), housingVariance.get());

        Optional<BigDecimal> entertainmentVariance = tracker.getBudgetVariance(february, ExpenseCategory.ENTERTAINMENT);
        assertTrue(entertainmentVariance.isPresent());
        assertEquals(new BigDecimal("-25.00"), entertainmentVariance.get());

        List<BudgetStatus> statuses = tracker.evaluateBudgets(february);
        assertEquals(2, statuses.size());

        BudgetStatus entertainmentStatus = null;
        for (BudgetStatus status : statuses) {
            if (status.getCategory() == ExpenseCategory.ENTERTAINMENT) {
                entertainmentStatus = status;
            }
        }

        assertNotNull(entertainmentStatus);
        assertTrue(entertainmentStatus.isOverspent());
        assertEquals(new BigDecimal("145.00"), entertainmentStatus.getSpentAmount());
    }

    @Test
    public void removeExpenseUpdatesSummaries() {
        YearMonth april = YearMonth.of(2024, Month.APRIL);
        Expense first = tracker.recordExpense(new BigDecimal("60.00"), LocalDate.of(2024, Month.APRIL, 4),
                ExpenseCategory.UTILITIES, "Water", null);
        tracker.recordExpense(new BigDecimal("40.00"), LocalDate.of(2024, Month.APRIL, 9),
                ExpenseCategory.UTILITIES, "Gas", null);

        assertTrue(tracker.removeExpense(first.getId()));
        assertEquals(new BigDecimal("40.00"), tracker.getTotalSpent(april));
        assertEquals(1, tracker.getExpenses(april).size());
    }

    @Test
    public void invalidAmountsAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> tracker.recordExpense(new BigDecimal("0"),
                LocalDate.of(2024, Month.MAY, 1), ExpenseCategory.MISCELLANEOUS, "Invalid", null));
    }
}
