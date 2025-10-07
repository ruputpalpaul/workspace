import SwiftUI

struct ExpenseListView: View {
    let groupedExpenses: [ExpenseSection]

    var body: some View {
        List {
            ForEach(groupedExpenses) { section in
                Section(section.title) {
                    ForEach(section.expenses) { expense in
                        HStack {
                            VStack(alignment: .leading, spacing: 4) {
                                Text(expense.title)
                                    .font(.headline)
                                Text(expense.category.name)
                                    .font(.subheadline)
                                    .foregroundStyle(.secondary)
                                if let notes = expense.notes, !notes.isEmpty {
                                    Text(notes)
                                        .font(.footnote)
                                        .foregroundStyle(.secondary)
                                }
                            }
                            Spacer()
                            VStack(alignment: .trailing) {
                                Text(expense.displayAmount)
                                    .font(.headline)
                                Text(expense.date, style: .time)
                                    .font(.caption)
                                    .foregroundStyle(.secondary)
                            }
                            .accessibilityElement(children: .combine)
                            .accessibilityLabel("Amount \(expense.displayAmount) at \(expense.date.formatted(date: .omitted, time: .shortened))")
                        }
                        .padding(.vertical, 8)
                    }
                }
            }
        }
        .listStyle(.insetGrouped)
    }
}

#Preview {
    ExpenseListView(groupedExpenses: ExpenseTrackerViewModel().groupedExpenses() ?? [])
}
