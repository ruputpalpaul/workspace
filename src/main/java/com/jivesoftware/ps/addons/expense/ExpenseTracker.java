package com.jivesoftware.ps.addons.expense;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A simple in-memory personal expense tracker that supports recording spending,
 * defining budgets and generating summary statistics per month.
 */
public class ExpenseTracker {

    private final AtomicLong idGenerator = new AtomicLong(1);
    private final Map<Long, Expense> expenses = new LinkedHashMap<Long, Expense>();
    private final Map<YearMonth, Map<ExpenseCategory, BigDecimal>> budgets =
            new HashMap<YearMonth, Map<ExpenseCategory, BigDecimal>>();

    /**
     * Records an expense and assigns it a unique identifier.
     *
     * @param amount      the amount spent, must be greater than zero
     * @param date        when the expense occurred
     * @param category    the category of the expense
     * @param description optional note about the expense
     * @param tags        optional labels that help group expenses
     * @return the saved {@link Expense}
     */
    public Expense recordExpense(BigDecimal amount,
                                 LocalDate date,
                                 ExpenseCategory category,
                                 String description,
                                 Collection<String> tags) {
        Objects.requireNonNull(amount, "amount");
        Objects.requireNonNull(date, "date");
        Objects.requireNonNull(category, "category");

        if (amount.signum() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        BigDecimal normalizedAmount = amount.setScale(2, RoundingMode.HALF_UP);
        Set<String> safeTags = tags == null ? Collections.<String>emptySet() : new HashSet<String>(tags);
        long id = idGenerator.getAndIncrement();
        Expense expense = new Expense(id, normalizedAmount, date, category, description, safeTags);
        expenses.put(id, expense);
        return expense;
    }

    /**
     * Removes an expense by its identifier.
     *
     * @return {@code true} if the expense existed and was removed
     */
    public boolean removeExpense(long expenseId) {
        return expenses.remove(expenseId) != null;
    }

    /**
     * Retrieves an expense by id.
     */
    public Optional<Expense> getExpense(long expenseId) {
        return Optional.ofNullable(expenses.get(expenseId));
    }

    /**
     * Returns all recorded expenses ordered by insertion time.
     */
    public List<Expense> getExpenses() {
        return Collections.unmodifiableList(new ArrayList<Expense>(expenses.values()));
    }

    /**
     * Returns the expenses for the provided month.
     */
    public List<Expense> getExpenses(YearMonth month) {
        List<Expense> monthlyExpenses = new ArrayList<Expense>();
        for (Expense expense : expenses.values()) {
            if (expense.occurredIn(month)) {
                monthlyExpenses.add(expense);
            }
        }
        return Collections.unmodifiableList(monthlyExpenses);
    }

    /**
     * Returns the expenses for the provided month filtered by category.
     */
    public List<Expense> getExpenses(YearMonth month, ExpenseCategory category) {
        List<Expense> monthlyExpenses = new ArrayList<Expense>();
        for (Expense expense : expenses.values()) {
            if (expense.getCategory() == category && expense.occurredIn(month)) {
                monthlyExpenses.add(expense);
            }
        }
        return Collections.unmodifiableList(monthlyExpenses);
    }

    /**
     * Computes the total spent during a given month across all categories.
     */
    public BigDecimal getTotalSpent(YearMonth month) {
        BigDecimal total = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        for (Expense expense : expenses.values()) {
            if (expense.occurredIn(month)) {
                total = total.add(expense.getAmount());
            }
        }
        return total;
    }

    /**
     * Computes the totals spent per category for the given month.
     */
    public Map<ExpenseCategory, BigDecimal> getCategoryTotals(YearMonth month) {
        Map<ExpenseCategory, BigDecimal> totals = new EnumMap<ExpenseCategory, BigDecimal>(ExpenseCategory.class);
        for (Expense expense : expenses.values()) {
            if (expense.occurredIn(month)) {
                BigDecimal previous = totals.containsKey(expense.getCategory())
                        ? totals.get(expense.getCategory())
                        : BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
                totals.put(expense.getCategory(), previous.add(expense.getAmount()));
            }
        }
        return Collections.unmodifiableMap(totals);
    }

    /**
     * Defines a monthly budget for a given category.
     */
    public void setBudget(YearMonth month, ExpenseCategory category, BigDecimal amount) {
        Objects.requireNonNull(month, "month");
        Objects.requireNonNull(category, "category");
        Objects.requireNonNull(amount, "amount");

        if (amount.signum() < 0) {
            throw new IllegalArgumentException("Budget amount cannot be negative");
        }

        Map<ExpenseCategory, BigDecimal> monthlyBudgets = budgets.get(month);
        if (monthlyBudgets == null) {
            monthlyBudgets = new EnumMap<ExpenseCategory, BigDecimal>(ExpenseCategory.class);
            budgets.put(month, monthlyBudgets);
        }
        monthlyBudgets.put(category, amount.setScale(2, RoundingMode.HALF_UP));
    }

    /**
     * Retrieves the configured budget for a month and category, if available.
     */
    public Optional<BigDecimal> getBudget(YearMonth month, ExpenseCategory category) {
        Map<ExpenseCategory, BigDecimal> monthlyBudgets = budgets.get(month);
        if (monthlyBudgets == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(monthlyBudgets.get(category));
    }

    /**
     * Calculates the variance between the budget and actual spending.
     */
    public Optional<BigDecimal> getBudgetVariance(YearMonth month, ExpenseCategory category) {
        Optional<BigDecimal> budget = getBudget(month, category);
        if (!budget.isPresent()) {
            return Optional.empty();
        }
        BigDecimal spent = getCategoryTotals(month).containsKey(category)
                ? getCategoryTotals(month).get(category)
                : BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        return Optional.of(budget.get().subtract(spent));
    }

    /**
     * Provides a detailed view of budget vs. spending for every configured category in a month.
     */
    public List<BudgetStatus> evaluateBudgets(YearMonth month) {
        Map<ExpenseCategory, BigDecimal> monthlyBudgets = budgets.get(month);
        if (monthlyBudgets == null || monthlyBudgets.isEmpty()) {
            return Collections.emptyList();
        }

        Map<ExpenseCategory, BigDecimal> categoryTotals = getCategoryTotals(month);
        List<BudgetStatus> statuses = new ArrayList<BudgetStatus>();
        for (Map.Entry<ExpenseCategory, BigDecimal> entry : monthlyBudgets.entrySet()) {
            ExpenseCategory category = entry.getKey();
            BigDecimal budgeted = entry.getValue();
            BigDecimal spent = categoryTotals.containsKey(category)
                    ? categoryTotals.get(category)
                    : BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
            BigDecimal variance = budgeted.subtract(spent);
            statuses.add(new BudgetStatus(month, category, budgeted, spent, variance));
        }
        return Collections.unmodifiableList(statuses);
    }
}
