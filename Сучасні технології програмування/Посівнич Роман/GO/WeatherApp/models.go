package main

type GeoResponse struct {
	Results []struct {
		Latitude  float64 `json:"latitude"`
		Longitude float64 `json:"longitude"`
		Name      string  `json:"name"`
		Country   string  `json:"country"`
		Timezone  string  `json:"timezone"`
	} `json:"results"`
}

type OpenMeteoWeather struct {
	Current struct {
		Temp      float64 `json:"temperature_2m"`
		Humidity  int     `json:"relative_humidity_2m"`
		Apparent  float64 `json:"apparent_temperature"`
		IsDay     int     `json:"is_day"`
		Code      int     `json:"weather_code"`
		Pressure  float64 `json:"surface_pressure"`
		WindSpeed float64 `json:"wind_speed_10m"`
	} `json:"current"`
	Daily struct {
		Time       []string  `json:"time"`
		Code       []int     `json:"weather_code"`
		TempMax    []float64 `json:"temperature_2m_max"`
		Sunrise    []string  `json:"sunrise"`
		Sunset     []string  `json:"sunset"`
		UvIndexMax []float64 `json:"uv_index_max"`
	} `json:"daily"`
}

type DailyForecast struct {
	Day         string `json:"day"`
	Temperature int    `json:"temperature"`
	Icon        string `json:"icon"`
}

type WeatherData struct {
	City        string          `json:"city"`
	Temperature int             `json:"temperature_celsius"`
	FeelsLike   int             `json:"feels_like"`
	Condition   string          `json:"condition"`
	WindSpeed   float64         `json:"wind_speed"`
	Humidity    int             `json:"humidity"`
	Pressure    int             `json:"pressure"`
	UVIndex     int             `json:"uv_index"`
	Sunrise     string          `json:"sunrise"`
	Sunset      string          `json:"sunset"`
	LocalTime   string          `json:"local_time"`
	Icon        string          `json:"icon_emoji"`
	Forecast    []DailyForecast `json:"forecast"`
}
