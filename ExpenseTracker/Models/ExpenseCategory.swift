import Foundation

enum ExpenseCategory: String, CaseIterable, Identifiable, Codable {
    case housing
    case utilities
    case groceries
    case dining
    case transportation
    case entertainment
    case healthcare
    case savings
    case other

    var id: String { rawValue }

    var name: String {
        rawValue.capitalized
    }

    var systemImage: String {
        switch self {
        case .housing: return "house.fill"
        case .utilities: return "bolt.fill"
        case .groceries: return "cart.fill"
        case .dining: return "fork.knife"
        case .transportation: return "car.fill"
        case .entertainment: return "gamecontroller.fill"
        case .healthcare: return "cross.case.fill"
        case .savings: return "banknote"
        case .other: return "questionmark.circle"
        }
    }
}
