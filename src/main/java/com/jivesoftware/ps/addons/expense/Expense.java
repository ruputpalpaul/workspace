package com.jivesoftware.ps.addons.expense;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Immutable representation of a single expense entry.
 */
public final class Expense {

    private final long id;
    private final BigDecimal amount;
    private final LocalDate date;
    private final ExpenseCategory category;
    private final String description;
    private final Set<String> tags;

    Expense(long id,
            BigDecimal amount,
            LocalDate date,
            ExpenseCategory category,
            String description,
            Set<String> tags) {
        this.id = id;
        this.amount = Objects.requireNonNull(amount, "amount");
        this.date = Objects.requireNonNull(date, "date");
        this.category = Objects.requireNonNull(category, "category");
        this.description = description == null ? "" : description.trim();
        this.tags = Collections.unmodifiableSet(tags == null
                ? Collections.<String>emptySet()
                : new LinkedHashSet<String>(tags));
    }

    public long getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public Set<String> getTags() {
        return tags;
    }

    /**
     * Indicates if the expense occurred in the provided month.
     */
    public boolean occurredIn(YearMonth month) {
        return YearMonth.from(date).equals(month);
    }

    /**
     * Creates a copy of this expense with a different identifier.
     */
    Expense withId(long newId) {
        return new Expense(newId, amount, date, category, description, tags);
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", amount=" + amount +
                ", date=" + date +
                ", category=" + category +
                ", description='" + description + '\'' +
                ", tags=" + tags +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Expense expense = (Expense) o;
        return id == expense.id;
    }

    @Override
    public int hashCode() {
        return Long.valueOf(id).hashCode();
    }
}
