import SwiftUI

struct BudgetSettingsView: View {
    @Environment(\.dismiss) private var dismiss

    @State private var amount: Double
    let onSave: (Decimal) -> Void

    init(initialBudget: Decimal, onSave: @escaping (Decimal) -> Void) {
        self._amount = State(initialValue: (initialBudget as NSDecimalNumber).doubleValue)
        self.onSave = onSave
    }

    var body: some View {
        Form {
            Section("Monthly Budget") {
                TextField(
                    "Amount",
                    value: $amount,
                    format: .currency(code: Locale.current.currency?.identifier ?? "USD")
                )
                .keyboardType(.decimalPad)
            }
        }
        .navigationTitle("Edit Budget")
        .toolbar {
            ToolbarItem(placement: .cancellationAction) {
                Button("Cancel", role: .cancel) { dismiss() }
            }
            ToolbarItem(placement: .confirmationAction) {
                Button("Save") {
                    onSave(Decimal(amount))
                    dismiss()
                }
                .disabled(amount < 0)
            }
        }
    }
}

#Preview {
    NavigationStack {
        BudgetSettingsView(initialBudget: 2000) { _ in }
    }
}
