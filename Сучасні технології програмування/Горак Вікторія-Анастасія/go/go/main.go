package main

import (
	"database/sql"
	"fmt"
	"log"
	"net/http"

	_ "github.com/lib/pq"
)

const (
	host     = "localhost"
	port     = 5432
	user     = "postgres"
	password = "1234"
	dbname   = "postgres"
)

func main() {
	dbinfo := fmt.Sprintf("host=%s port=%d user=%s password=%s dbname=%s sslmode=disable",
		host, port, user, password, dbname)

	db, err := sql.Open("postgres", dbinfo)
	if err != nil {
		log.Fatal(err)
	}
	defer db.Close()

	err = db.Ping()
	if err != nil {
		log.Fatal(err)
	}

	http.HandleFunc("/login", func(w http.ResponseWriter, r *http.Request) {
		if r.Method != http.MethodPost {
			http.ServeFile(w, r, "login.html")
			return
		}

		username := r.FormValue("username")
		password := r.FormValue("password")

		// Виконання логіки перевірки логіну у базі даних
		// Можна використовувати db.QueryRow для перевірки логіна та пароля у базі даних

		// Приклад перевірки, де users - назва таблиці користувачів
		row := db.QueryRow("SELECT id FROM users WHERE username = $1 AND password = $2", username, password)
		var userID int
		err := row.Scan(&userID)
		if err != nil {
			fmt.Fprintf(w, "Неправильний логін або пароль")
			return
		}

		fmt.Fprintf(w, "Ви увійшли!")
	})

	http.HandleFunc("/register", func(w http.ResponseWriter, r *http.Request) {
    if r.Method != http.MethodPost {
        http.ServeFile(w, r, "register.html")
        return
    }

    username := r.FormValue("username")
    password := r.FormValue("password")
    email := r.FormValue("email") // Додайте цей рядок для отримання значення email з форми

    // Логіка реєстрації користувача у базі даних
    // Використовуйте db.Exec для вставки нового користувача у таблицю

    _, err := db.Exec("INSERT INTO users(username, password, email) VALUES($1, $2, $3)", username, password, email)
    if err != nil {
        fmt.Fprintf(w, "Не вдалося зареєструвати користувача")
        return
    }

    fmt.Fprintf(w, "Користувач зареєстрований успішно!")
	})

	log.Fatal(http.ListenAndServe(":8080", nil))
}
