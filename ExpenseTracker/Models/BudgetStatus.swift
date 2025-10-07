import Foundation

struct BudgetStatus: Codable {
    var monthlyLimit: Decimal
    var spent: Decimal

    var remaining: Decimal {
        monthlyLimit - spent
    }

    var progress: Double {
        guard monthlyLimit > 0 else { return 0 }
        let percentage = (spent as NSDecimalNumber).doubleValue / (monthlyLimit as NSDecimalNumber).doubleValue
        return min(max(percentage, 0), 1)
    }

    var statusText: String {
        if spent > monthlyLimit {
            return "Over budget by \(format(amount: spent - monthlyLimit))"
        }
        return "\(format(amount: spent)) of \(format(amount: monthlyLimit)) spent"
    }

    private func format(amount: Decimal) -> String {
        let formatter = NumberFormatter()
        formatter.numberStyle = .currency
        formatter.locale = .current
        return formatter.string(from: amount as NSDecimalNumber) ?? "$0.00"
    }
}
