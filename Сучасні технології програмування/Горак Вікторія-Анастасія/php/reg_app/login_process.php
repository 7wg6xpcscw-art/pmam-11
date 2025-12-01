<?php
// Підключення до бази даних PostgreSQL
$host = 'localhost';
$dbname = 'postgres';
$username = 'postgres';
$password = '1234';

echo "Введіть електронну пошту: ";
$login_email = trim(fgets(STDIN));

echo "Введіть пароль: ";
$login_password = trim(fgets(STDIN));

try {
  // Підключення до бази даних з використанням PDO
  $conn = new PDO("pgsql:host=$host;dbname=$dbname", $username, $password);
  $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

  // Підготовка запиту
  $query = "SELECT * FROM users WHERE email = :email AND password = :password";
  $stmt = $conn->prepare($query);
  $stmt->bindParam(':email', $login_email);
  $stmt->bindParam(':password', $login_password);
  $stmt->execute();

  // Перевірка результату запиту
  $user = $stmt->fetch(PDO::FETCH_ASSOC);

  if($user) {
    // Якщо користувач знайдений
    echo "Успішно ввійшли в систему! Вітаємо, {$user['username']}!";
    // Тут можна виконати дії для авторизованого користувача
  } else {
    // Якщо користувач не знайдений
    echo "Невірний email або пароль. Будь ласка, спробуйте ще раз.";
  }
} catch (PDOException $e) {
  echo "Помилка: " . $e->getMessage();
}
