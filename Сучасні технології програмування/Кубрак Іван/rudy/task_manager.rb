#!/usr/bin/env ruby

# --------------------------
# Міні-Банк з меню та історією
# --------------------------

class BankAccount
  attr_accessor :balance, :history, :name

  def initialize(name, initial_balance = 0)
    @name = name
    @balance = initial_balance
    @history = []
  end

  def deposit(amount)
    @balance += amount
    @history << "Поповнення: +#{amount} грн"
    puts "Ви поклали #{amount} грн. Новий баланс: #{@balance} грн."
  end

  def withdraw(amount)
    if amount > @balance
      puts "Недостатньо коштів! Баланс: #{@balance} грн."
      @history << "Спроба зняття: -#{amount} грн (неуспішно)"
    else
      @balance -= amount
      @history << "Зняття: -#{amount} грн"
      puts "Ви зняли #{amount} грн. Новий баланс: #{@balance} грн."
    end
  end

  def display_balance
    puts "#{@name} - поточний баланс: #{@balance} грн"
  end

  def show_history
    puts "Історія операцій для #{@name}:"
    if @history.empty?
      puts "  Немає операцій."
    else
      @history.each_with_index do |op, index|
        puts "  #{index + 1}. #{op}"
      end
    end
  end
end

# --------------------------
# Створюємо кілька рахунків
# --------------------------

accounts = []
accounts << BankAccount.new("Картка №1", 1000)
accounts << BankAccount.new("Картка №2", 500)

# --------------------------
# Просте меню
# --------------------------

puts "=== Міні-Банк ==="

accounts.each_with_index do |acc, idx|
  puts "#{idx + 1}. #{acc.name} (Баланс: #{acc.balance} грн)"
end

accounts.each do |acc|
  puts "\n--- Операції для #{acc.name} ---"
  acc.display_balance
  acc.deposit(200)
  acc.withdraw(150)
  acc.withdraw(2000)  # спроба перевищення
  acc.show_history
end

puts "\n=== Кінець демонстрації ==="
