import Foundation

struct Expense: Identifiable, Codable {
    let id: UUID
    var title: String
    var amount: Decimal
    var category: ExpenseCategory
    var date: Date
    var notes: String?

    init(id: UUID = UUID(), title: String, amount: Decimal, category: ExpenseCategory, date: Date = Date(), notes: String? = nil) {
        self.id = id
        self.title = title
        self.amount = amount
        self.category = category
        self.date = date
        self.notes = notes
    }

    var displayAmount: String {
        let formatter = NumberFormatter()
        formatter.numberStyle = .currency
        formatter.locale = .current
        return formatter.string(from: amount as NSDecimalNumber) ?? "$0.00"
    }

    var monthIdentifier: String {
        let formatter = DateFormatter()
        formatter.dateFormat = "LLLL yyyy"
        return formatter.string(from: date)
    }
}
