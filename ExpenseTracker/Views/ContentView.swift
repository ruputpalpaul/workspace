import SwiftUI

struct ContentView: View {
    @EnvironmentObject private var viewModel: ExpenseTrackerViewModel
    @State private var presentingNewExpense = false
    @State private var showingBudgetEditor = false

    var body: some View {
        NavigationStack {
            VStack(spacing: 16) {
                BudgetSummaryView(status: viewModel.budgetStatus())
                if let grouped = viewModel.groupedExpenses() {
                    ExpenseListView(groupedExpenses: grouped)
                } else {
                    ContentUnavailableView(
                        "No expenses",
                        systemImage: "tray",
                        description: Text("Add a new expense to begin tracking your budget.")
                    )
                }
            }
            .padding()
            .navigationTitle(monthTitle)
            .toolbar {
                ToolbarItemGroup(placement: .topBarTrailing) {
                    Button {
                        presentingNewExpense = true
                    } label: {
                        Image(systemName: "plus")
                    }
                    .accessibilityLabel("Add expense")

                    Button {
                        showingBudgetEditor = true
                    } label: {
                        Image(systemName: "slider.horizontal.3")
                    }
                    .accessibilityLabel("Edit budget")
                }
                ToolbarItem(placement: .topBarLeading) {
                    DatePicker(
                        "Month",
                        selection: $viewModel.selectedMonth,
                        displayedComponents: [.date]
                    )
                    .datePickerStyle(.compact)
                    .labelsHidden()
                }
            }
            .sheet(isPresented: $presentingNewExpense) {
                NavigationStack {
                    ExpenseFormView { title, amount, category, date, notes in
                        viewModel.addExpense(title: title, amount: amount, category: category, date: date, notes: notes)
                    }
                }
            }
            .sheet(isPresented: $showingBudgetEditor) {
                NavigationStack {
                    BudgetSettingsView(initialBudget: viewModel.monthlyBudget) { newBudget in
                        viewModel.monthlyBudget = newBudget
                    }
                }
            }
        }
    }

    private var monthTitle: String {
        let formatter = DateFormatter()
        formatter.dateFormat = "LLLL yyyy"
        return formatter.string(from: viewModel.selectedMonth)
    }
}

#Preview {
    ContentView()
        .environmentObject(ExpenseTrackerViewModel())
}
