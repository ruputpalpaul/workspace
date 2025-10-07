import SwiftUI

@main
struct ExpenseTrackerApp: App {
    @StateObject private var viewModel = ExpenseTrackerViewModel()

    var body: some Scene {
        WindowGroup {
            ContentView()
                .environmentObject(viewModel)
        }
    }
}
