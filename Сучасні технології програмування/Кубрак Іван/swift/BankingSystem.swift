import Foundation

// ---------------------------
// MARK: - Transaction Model
// ---------------------------
class Transaction {
    let type: String
    let amount: Double
    let date: Date
    
    init(type: String, amount: Double) {
        self.type = type
        self.amount = amount
        self.date = Date()
    }
    
    func formatted() -> String {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
        return "[\(formatter.string(from: date))] \(type): \(amount) грн"
    }
}

// ---------------------------
// MARK: - Bank Account
// ---------------------------
class BankAccount {
    let owner: String
    private(set) var balance: Double = 0.0
    private var history: [Transaction] = []
    
    init(owner: String, initialBalance: Double) {
        self.owner = owner
        self.balance = initialBalance
        history.append(Transaction(type: "Initial Balance", amount: initialBalance))
    }
    
    func deposit(_ amount: Double) {
        guard amount > 0 else {
            print("❌ Неможливо внести суму ≤ 0")
            return
        }
        balance += amount
        history.append(Transaction(type: "Deposit", amount: amount))
        print("✅ Успішне поповнення: \(amount) грн")
    }
    
    func withdraw(_ amount: Double) {
        guard amount > 0 else {
            print("❌ Неможливо зняти суму ≤ 0")
            return
        }
        guard amount <= balance else {
            print("❌ Недостатньо коштів!")
            return
        }
        balance -= amount
        history.append(Transaction(type: "Withdraw", amount: amount))
        print("💸 Знято: \(amount) грн")
    }
    
    func showHistory() {
        print("\n----- ІСТОРІЯ ТРАНЗАКЦІЙ -----")
        if history.isEmpty {
            print("Немає транзакцій.")
        } else {
            for t in history {
                print(t.formatted())
            }
        }
        print("--------------------------------\n")
    }
    
    func showInfo() {
        print("\n===== Інформація про рахунок =====")
        print("Власник: \(owner)")
        print("Баланс: \(balance) грн")
        print("==================================\n")
    }
}

// ---------------------------
// MARK: - Bank System
// ---------------------------
class BankSystem {
    private var accounts: [BankAccount] = []
    
    func createAccount() {
        print("Введіть ім'я власника:")
        let name = readLine() ?? "Клієнт"
        
        print("Початковий баланс:")
        let balStr = readLine() ?? "0"
        let balance = Double(balStr) ?? 0
        
        let acc = BankAccount(owner: name, initialBalance: balance)
        accounts.append(acc)
        
        print("🎉 Рахунок створено!\n")
    }
    
    func listAccounts() {
        print("\n🔎 Список рахунків:")
        if accounts.isEmpty {
            print("Немає створених рахунків.\n")
            return
        }
        
        for (i, acc) in accounts.enumerated() {
            print("[\(i)] \(acc.owner) — баланс: \(acc.balance) грн")
        }
        print("")
    }
    
    func manageAccount() {
        listAccounts()
        
        print("Виберіть номер рахунку:")
        guard let idxStr = readLine(),
              let idx = Int(idxStr),
              idx >= 0, idx < accounts.count else {
            print("❌ Невірний вибір!")
            return
        }
        
        let acc = accounts[idx]
        
        while true {
            print("\n--- Меню рахунку \(acc.owner) ---")
            print("1. Поповнити")
            print("2. Зняти")
            print("3. Показати інформацію")
            print("4. Історія транзакцій")
            print("5. Назад")
            print("Ваш вибір:")
            
            let choice = readLine() ?? ""
            
            switch choice {
            case "1":
                print("Сума поповнення:")
                let s = Double(readLine() ?? "") ?? 0
                acc.deposit(s)
            case "2":
                print("Сума зняття:")
                let s = Double(readLine() ?? "") ?? 0
                acc.withdraw(s)
            case "3":
                acc.showInfo()
            case "4":
                acc.showHistory()
            case "5":
                return
            default:
                print("❌ Невірна команда")
            }
        }
    }
    
    func start() {
        while true {
            print("=========== BANK SYSTEM ===========")
            print("1. Створити рахунок")
            print("2. Переглянути рахунки")
            print("3. Керувати рахунком")
            print("4. Вихід")
            print("Ваш вибір:")
            
            let input = readLine() ?? "0"
            
            switch input {
            case "1": createAccount()
            case "2": listAccounts()
            case "3": manageAccount()
            case "4":
                print("👋 До побачення!")
                return
            default:
                print("❌ Невірний вибір")
            }
        }
    }
}

// ---------------------------
// MARK: - Start Program
// ---------------------------
let system = BankSystem()
system.start()
