package main

import (
	"encoding/json"
	"fmt"
	"io"
	"net/http"
	"net/url"
	"strings"
	"time"
)

func GetWeatherForCity(cityName string) (WeatherData, error) {
	lat, lon, name, timezone, err := getCoordinates(cityName)
	if err != nil {
		return WeatherData{}, err
	}

	apiData, err := getRealWeather(lat, lon)
	if err != nil {
		return WeatherData{}, err
	}

	desc, icon := mapWeatherCode(apiData.Current.Code, apiData.Current.IsDay == 1)

	sunrise := parseTime(apiData.Daily.Sunrise[0])
	sunset := parseTime(apiData.Daily.Sunset[0])
	localTime := getLocalTime(timezone)

	forecast := []DailyForecast{}
	for i := 1; i <= 3; i++ {
		if i < len(apiData.Daily.Time) {
			dayDate, _ := time.Parse("2006-01-02", apiData.Daily.Time[i])
			_, fIcon := mapWeatherCode(apiData.Daily.Code[i], true)

			forecast = append(forecast, DailyForecast{
				Day:         dayDate.Format("02.01"),
				Temperature: int(apiData.Daily.TempMax[i]),
				Icon:        fIcon,
			})
		}
	}

	return WeatherData{
		City:        name,
		Temperature: int(apiData.Current.Temp),
		FeelsLike:   int(apiData.Current.Apparent),
		Condition:   desc,
		WindSpeed:   apiData.Current.WindSpeed,
		Humidity:    apiData.Current.Humidity,
		Pressure:    int(apiData.Current.Pressure),
		UVIndex:     int(apiData.Daily.UvIndexMax[0]),
		Sunrise:     sunrise,
		Sunset:      sunset,
		LocalTime:   localTime,
		Icon:        icon,
		Forecast:    forecast,
	}, nil
}

func getCoordinates(city string) (float64, float64, string, string, error) {
	safeCity := url.QueryEscape(city)
	url := fmt.Sprintf("https://geocoding-api.open-meteo.com/v1/search?name=%s&count=1&language=en&format=json", safeCity)

	resp, err := http.Get(url)
	if err != nil {
		return 0, 0, "", "", err
	}
	defer resp.Body.Close()

	var result GeoResponse
	if err := json.NewDecoder(resp.Body).Decode(&result); err != nil {
		return 0, 0, "", "", err
	}

	if len(result.Results) == 0 {
		return 0, 0, "", "", fmt.Errorf("місто не знайдено")
	}

	return result.Results[0].Latitude, result.Results[0].Longitude, result.Results[0].Name, result.Results[0].Timezone, nil
}

func getRealWeather(lat, lon float64) (*OpenMeteoWeather, error) {
	url := fmt.Sprintf("https://api.open-meteo.com/v1/forecast?latitude=%f&longitude=%f&current=temperature_2m,relative_humidity_2m,apparent_temperature,is_day,weather_code,surface_pressure,wind_speed_10m&daily=weather_code,temperature_2m_max,sunrise,sunset,uv_index_max&timezone=auto", lat, lon)

	resp, err := http.Get(url)
	if err != nil {
		return nil, err
	}
	defer resp.Body.Close()

	body, _ := io.ReadAll(resp.Body)
	var result OpenMeteoWeather
	if err := json.Unmarshal(body, &result); err != nil {
		return nil, err
	}

	return &result, nil
}

func mapWeatherCode(code int, isDay bool) (string, string) {
	switch code {
	case 0:
		if isDay {
			return "Ясно", "☀️"
		}
		return "Ясно", "🌙"
	case 1, 2, 3:
		if isDay {
			return "Хмарно", "⛅"
		}
		return "Хмарно", "☁️"
	case 45, 48:
		return "Туман", "🌫️"
	case 51, 53, 55, 61, 63, 65:
		return "Дощ", "🌧️"
	case 71, 73, 75, 77:
		return "Сніг", "❄️"
	case 95, 96, 99:
		return "Гроза", "⚡"
	default:
		return "Хмарно", "☁️"
	}
}

func parseTime(t string) string {
	parts := strings.Split(t, "T")
	if len(parts) > 1 {
		return parts[1]
	}
	return t
}

func getLocalTime(timezone string) string {
	loc, err := time.LoadLocation(timezone)
	if err != nil {
		return "--:--"
	}
	return time.Now().In(loc).Format("15:04")
}
