package main

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"
)

func weatherHandler(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	city := r.URL.Query().Get("city")
	if city == "" {
		http.Error(w, `{"error": "Місто не вказано"}`, http.StatusBadRequest)
		return
	}

	data, err := GetWeatherForCity(city)
	if err != nil {
		http.Error(w, fmt.Sprintf(`{"error": "%s"}`, err.Error()), http.StatusNotFound)
		return
	}

	json.NewEncoder(w).Encode(data)
}

func homeHandler(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "text/html; charset=utf-8")
	fmt.Fprint(w, htmlContent)
}

func main() {
	http.HandleFunc("/weather", weatherHandler)
	http.HandleFunc("/", homeHandler)

	port := ":8080"
	fmt.Printf("Сервер запущено: http://localhost%s\n", port)

	log.Fatal(http.ListenAndServe(port, nil))
}
