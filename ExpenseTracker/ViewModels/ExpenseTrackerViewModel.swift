import Foundation

final class ExpenseTrackerViewModel: ObservableObject {
    @Published private(set) var expenses: [Expense] = []
    @Published var monthlyBudget: Decimal = 2000
    @Published var selectedMonth: Date = Date()

    private let calendar = Calendar.current

    init() {
        loadSampleData()
    }

    func addExpense(title: String, amount: Decimal, category: ExpenseCategory, date: Date, notes: String?) {
        let sanitizedTitle = title.trimmingCharacters(in: .whitespacesAndNewlines)
        guard !sanitizedTitle.isEmpty else { return }
        guard amount >= 0 else { return }

        let expense = Expense(title: sanitizedTitle, amount: amount, category: category, date: date, notes: notes)
        expenses.append(expense)
        sortExpenses()
    }

    func totalForCurrentMonth() -> Decimal {
        return expenses
            .filter { calendar.isDate($0.date, equalTo: selectedMonth, toGranularity: .month) }
            .reduce(0) { $0 + $1.amount }
    }

    func groupedExpenses() -> [ExpenseSection]? {
        let filtered = expenses.filter { calendar.isDate($0.date, equalTo: selectedMonth, toGranularity: .month) }
        guard !filtered.isEmpty else { return nil }

        let groups = Dictionary(grouping: filtered) { expense -> Date in
            calendar.startOfDay(for: expense.date)
        }

        let formatter = DateFormatter()
        formatter.dateFormat = "EEEE, MMM d"

        return groups
            .map { date, expenses in
                ExpenseSection(
                    id: date,
                    title: formatter.string(from: date),
                    expenses: expenses.sorted { $0.date > $1.date }
                )
            }
            .sorted { $0.id > $1.id }
    }

    func budgetStatus() -> BudgetStatus {
        BudgetStatus(monthlyLimit: monthlyBudget, spent: totalForCurrentMonth())
    }

    private func sortExpenses() {
        expenses.sort { $0.date > $1.date }
    }

    private func loadSampleData() {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy/MM/dd"

        expenses = [
            Expense(title: "Groceries", amount: 120.45, category: .groceries, date: formatter.date(from: "2024/03/02") ?? Date()),
            Expense(title: "Rent", amount: 950, category: .housing, date: formatter.date(from: "2024/03/01") ?? Date()),
            Expense(title: "Streaming", amount: 19.99, category: .entertainment, date: formatter.date(from: "2024/03/05") ?? Date()),
            Expense(title: "Coffee", amount: 4.50, category: .dining, date: formatter.date(from: "2024/03/06") ?? Date())
        ]
        sortExpenses()
    }
}

struct ExpenseSection: Identifiable {
    let id: Date
    let title: String
    let expenses: [Expense]
}
