package main

const htmlContent = `
<!DOCTYPE html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Weather</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            margin: 0;
            color: #333;
            transition: background 1s ease;
        }
        .container {
            width: 100%;
            max-width: 450px;
            padding: 20px;
        }
        .card {
            background: rgba(255, 255, 255, 0.95);
            padding: 2rem;
            border-radius: 30px;
            box-shadow: 0 15px 35px rgba(0,0,0,0.2);
            text-align: center;
            backdrop-filter: blur(10px);
        }
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
            font-size: 0.9rem;
            color: #666;
        }
        .input-group {
            display: flex;
            gap: 10px;
            margin-bottom: 30px;
            background: #f0f2f5;
            padding: 5px;
            border-radius: 50px;
        }
        input {
            flex: 1;
            padding: 12px 20px;
            border: none;
            background: transparent;
            outline: none;
            font-size: 16px;
        }
        button {
            padding: 12px 25px;
            background: #333;
            color: white;
            border: none;
            border-radius: 50px;
            cursor: pointer;
            transition: transform 0.2s;
            font-weight: bold;
        }
        button:hover { transform: scale(1.05); }
        
        .main-weather { margin-bottom: 30px; }
        .city-title { font-size: 2rem; margin: 0; color: #2d3436; }
        .weather-icon { font-size: 6rem; margin: 10px 0; display: inline-block; animation: float 3s ease-in-out infinite; }
        .temp-large { font-size: 4.5rem; font-weight: 800; color: #2d3436; line-height: 1; }
        .condition-text { font-size: 1.5rem; color: #636e72; margin-top: 5px; }

        .grid-details {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 15px;
            margin-bottom: 30px;
        }
        .detail-box {
            background: #f8f9fa;
            padding: 15px;
            border-radius: 15px;
            text-align: left;
        }
        .detail-label { font-size: 0.8rem; color: #b2bec3; display: block; margin-bottom: 5px; }
        .detail-val { font-size: 1.1rem; font-weight: bold; color: #2d3436; }

        .forecast {
            border-top: 1px solid #dfe6e9;
            padding-top: 20px;
            display: flex;
            justify-content: space-between;
        }
        .forecast-day {
            text-align: center;
            font-size: 0.9rem;
        }
        .f-day { color: #636e72; margin-bottom: 5px; font-weight: bold; }
        .f-icon { font-size: 1.5rem; margin: 5px 0; }
        .f-temp { font-weight: bold; }

        @keyframes float {
            0% { transform: translateY(0px); }
            50% { transform: translateY(-10px); }
            100% { transform: translateY(0px); }
        }
    </style>
</head>
<body>

<div class="container">
    <div class="card">
        <div class="header">
            <span id="localTime">--:--</span>
            <span>REAL WEATHER</span>
        </div>

        <div class="input-group">
            <input type="text" id="cityInput" placeholder="Введіть місто (English)..." value="Kyiv">
            <button onclick="getWeather()">Знайти</button>
        </div>

        <div class="main-weather">
            <h1 class="city-title" id="cityName">Завантаження...</h1>
            <div class="weather-icon" id="weatherIcon">🌤️</div>
            <div class="temp-large"><span id="tempVal">--</span>°</div>
            <div class="condition-text" id="conditionText">--</div>
        </div>

        <div class="grid-details">
            <div class="detail-box">
                <span class="detail-label">Відчувається</span>
                <span class="detail-val" id="feelsLike">--°</span>
            </div>
            <div class="detail-box">
                <span class="detail-label">Вологість</span>
                <span class="detail-val" id="humidity">--%</span>
            </div>
            <div class="detail-box">
                <span class="detail-label">Вітер</span>
                <span class="detail-val" id="windSpeed">-- км/г</span>
            </div>
            <div class="detail-box">
                <span class="detail-label">UV Індекс</span>
                <span class="detail-val" id="uvIndex">--</span>
            </div>
            <div class="detail-box">
                <span class="detail-label">Схід сонця</span>
                <span class="detail-val" id="sunrise">--:--</span>
            </div>
            <div class="detail-box">
                <span class="detail-label">Захід сонця</span>
                <span class="detail-val" id="sunset">--:--</span>
            </div>
        </div>

        <div class="forecast" id="forecastContainer">
            </div>
    </div>
</div>

<script>
    async function getWeather() {
        const city = document.getElementById('cityInput').value;
        if (!city) return;

        // Показуємо стан завантаження
        document.getElementById('cityName').innerText = "Шукаю...";

        try {
            const response = await fetch('/weather?city=' + city);
            const data = await response.json();

            if (data.error) {
                alert("Помилка: " + data.error);
                document.getElementById('cityName').innerText = "Помилка";
                return;
            }

            updateUI(data);
            updateBackground(data.condition);
        } catch (e) {
            console.error(e);
            alert("Помилка з'єднання");
        }
    }

    function updateUI(data) {
        document.getElementById('cityName').innerText = data.city;
        document.getElementById('weatherIcon').innerText = data.icon_emoji;
        document.getElementById('tempVal').innerText = data.temperature_celsius;
        document.getElementById('conditionText').innerText = data.condition;
        document.getElementById('localTime').innerText = "Місцевий час: " + data.local_time;
        
        document.getElementById('feelsLike').innerText = data.feels_like + '°';
        document.getElementById('windSpeed').innerText = data.wind_speed + ' км/г';
        document.getElementById('humidity').innerText = data.humidity + '%';
        document.getElementById('uvIndex').innerText = data.uv_index;
        document.getElementById('sunrise').innerText = data.sunrise;
        document.getElementById('sunset').innerText = data.sunset;

        const forecastDiv = document.getElementById('forecastContainer');
        forecastDiv.innerHTML = '';
        data.forecast.forEach(day => {
            forecastDiv.innerHTML += 
                '<div class="forecast-day">' +
                    '<div class="f-day">' + day.day + '</div>' +
                    '<div class="f-icon">' + day.icon + '</div>' +
                    '<div class="f-temp">' + day.temperature + '°</div>' +
                '</div>';
        });
    }

    function updateBackground(condition) {
        const body = document.body;
        condition = condition.toLowerCase();
        
        if (condition.includes("дощ") || condition.includes("rain")) {
            body.style.background = "linear-gradient(135deg, #2c3e50 0%, #3498db 100%)";
        } else if (condition.includes("ясно") || condition.includes("clear")) {
            body.style.background = "linear-gradient(135deg, #f6d365 0%, #fda085 100%)";
        } else if (condition.includes("сніг") || condition.includes("snow")) {
            body.style.background = "linear-gradient(135deg, #e6dada 0%, #274046 100%)";
        } else if (condition.includes("гроза") || condition.includes("thunder")) {
            body.style.background = "linear-gradient(135deg, #141E30 0%, #243B55 100%)";
        } else {
            // Хмарно
            body.style.background = "linear-gradient(135deg, #606c88 0%, #3f4c6b 100%)";
        }
    }

    getWeather();
    
    document.getElementById("cityInput").addEventListener("keypress", function(event) {
        if (event.key === "Enter") getWeather();
    });
</script>

</body>
</html>
`
