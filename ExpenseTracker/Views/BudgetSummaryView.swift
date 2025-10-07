import SwiftUI

struct BudgetSummaryView: View {
    let status: BudgetStatus

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            HStack {
                VStack(alignment: .leading, spacing: 4) {
                    Text("Monthly Budget")
                        .font(.headline)
                    Text(status.statusText)
                        .font(.subheadline)
                        .foregroundStyle(.secondary)
                }
                Spacer()
                Text(status.remainingText)
                    .font(.title3)
                    .fontWeight(.semibold)
                    .foregroundStyle(status.remaining >= 0 ? .green : .red)
            }

            ProgressView(value: status.progress)
                .tint(status.progress < 0.75 ? .green : status.progress < 1 ? .orange : .red)
        }
        .padding()
        .background(.thinMaterial, in: RoundedRectangle(cornerRadius: 16, style: .continuous))
        .accessibilityElement(children: .combine)
        .accessibilityLabel("Monthly budget status")
        .accessibilityValue(status.statusText)
    }
}

private extension BudgetStatus {
    var remainingText: String {
        let formatter = NumberFormatter()
        formatter.numberStyle = .currency
        formatter.locale = .current
        let value = remaining >= 0 ? remaining : -remaining
        let formatted = formatter.string(from: value as NSDecimalNumber) ?? "$0.00"
        return remaining >= 0 ? "Remaining: \(formatted)" : "Over: \(formatted)"
    }
}

#Preview {
    BudgetSummaryView(status: BudgetStatus(monthlyLimit: 2000, spent: 1250))
        .padding()
}
