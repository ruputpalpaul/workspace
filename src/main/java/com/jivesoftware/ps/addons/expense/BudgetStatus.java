package com.jivesoftware.ps.addons.expense;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Objects;

/**
 * Provides insight into how spending compares to a defined budget.
 */
public final class BudgetStatus {

    private final YearMonth month;
    private final ExpenseCategory category;
    private final BigDecimal budgetedAmount;
    private final BigDecimal spentAmount;
    private final BigDecimal variance;

    BudgetStatus(YearMonth month,
                 ExpenseCategory category,
                 BigDecimal budgetedAmount,
                 BigDecimal spentAmount,
                 BigDecimal variance) {
        this.month = Objects.requireNonNull(month, "month");
        this.category = Objects.requireNonNull(category, "category");
        this.budgetedAmount = Objects.requireNonNull(budgetedAmount, "budgetedAmount");
        this.spentAmount = Objects.requireNonNull(spentAmount, "spentAmount");
        this.variance = Objects.requireNonNull(variance, "variance");
    }

    public YearMonth getMonth() {
        return month;
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    public BigDecimal getBudgetedAmount() {
        return budgetedAmount;
    }

    public BigDecimal getSpentAmount() {
        return spentAmount;
    }

    /**
     * The difference between the budgeted amount and what was actually spent.
     * A negative value indicates overspending.
     */
    public BigDecimal getVariance() {
        return variance;
    }

    public boolean isOverspent() {
        return variance.signum() < 0;
    }
}
