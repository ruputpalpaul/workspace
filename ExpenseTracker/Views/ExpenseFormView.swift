import SwiftUI

struct ExpenseFormView: View {
    @Environment(\.dismiss) private var dismiss

    @State private var title: String = ""
    @State private var amount: Double = 0
    @State private var category: ExpenseCategory = .groceries
    @State private var date: Date = Date()
    @State private var notes: String = ""

    let onSave: (String, Decimal, ExpenseCategory, Date, String?) -> Void

    var body: some View {
        Form {
            Section("Details") {
                TextField("Title", text: $title)
                TextField(
                    "Amount",
                    value: $amount,
                    format: .currency(code: Locale.current.currency?.identifier ?? "USD")
                )
                    .keyboardType(.decimalPad)
                Picker("Category", selection: $category) {
                    ForEach(ExpenseCategory.allCases) { category in
                        Label(category.name, systemImage: category.systemImage)
                            .tag(category)
                    }
                }
                DatePicker("Date", selection: $date, displayedComponents: [.date, .hourAndMinute])
            }

            Section("Notes") {
                TextField("Optional notes", text: $notes, axis: .vertical)
            }
        }
        .navigationTitle("New Expense")
        .toolbar {
            ToolbarItem(placement: .cancellationAction) {
                Button("Cancel", role: .cancel) { dismiss() }
            }
            ToolbarItem(placement: .confirmationAction) {
                Button("Save") {
                    onSave(title, Decimal(amount), category, date, notes.isEmpty ? nil : notes)
                    dismiss()
                }
                .disabled(!isValid)
            }
        }
    }

    private var isValid: Bool {
        !title.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty && amount >= 0
    }
}

#Preview {
    NavigationStack {
        ExpenseFormView { _, _, _, _, _ in }
    }
}
