<?php
// Параметри підключення до бази даних
$host = 'localhost';
$dbname = 'postgres';
$username = 'postgres';
$password = '1234';

// Отримання даних для реєстрації через командний рядок
echo "Введіть ім'я користувача: ";
$username = trim(fgets(STDIN));

echo "Введіть електронну пошту: ";
$email = trim(fgets(STDIN));

echo "Введіть пароль: ";
$password = trim(fgets(STDIN));

// Хешування паролю
$hashed_password = $password;

try {
  // Підключення до бази даних з використанням PDO
  $conn = new PDO("pgsql:host=$host;dbname=$dbname", $username, $password);
  $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

  // Підготовка запиту для реєстрації нового користувача
  $query = "INSERT INTO users (username, email, password) VALUES (:username, :email, :password)";
  $stmt = $conn->prepare($query);
  $stmt->bindParam(':username', $username);
  $stmt->bindParam(':email', $email);
  $stmt->bindParam(':password', $hashed_password);
  $stmt->execute();

  echo "Користувач зареєстрований успішно!";
} catch (PDOException $e) {
  echo "Помилка: " . $e->getMessage();
}
?>
