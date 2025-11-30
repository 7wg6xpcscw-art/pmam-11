# app.py
from flask import Flask, render_template, request
from koshi import solve_all, solve_sir, error_by_x

app = Flask(__name__)


@app.route('/', methods=['GET', 'POST'])
def index():
    # Значення за замовчуванням (те саме, що було у вас в коді)
    default_params = {
        'beta': 0.5,  # Коефіцієнт зараження
        'mu': 0.005,  # Коефіцієнт одужання/смертності
        'r': 0.02,  # Інтенсивність лікування
        'a': 0.01,  # Поріг насичення лікарень
        'Y': 0.005,  # Вакцинація/імунітет
        'population': 750000,
        'T': 50  # Період часу (дні)
    }

    # Якщо користувач натиснув кнопку "Розрахувати"
    if request.method == 'POST':
        try:
            # Зчитуємо дані з форми і оновлюємо параметри
            current_params = {
                'beta': float(request.form.get('beta')),
                'mu': float(request.form.get('mu')),
                'r': float(request.form.get('r')),
                'a': float(request.form.get('a')),
                'Y': float(request.form.get('Y')),
                'population': int(request.form.get('population')),
                'T': int(request.form.get('T'))
            }
        except ValueError:
            # Якщо ввели щось не те, повертаємо дефолт
            current_params = default_params
    else:
        # Якщо це перший вхід на сторінку
        current_params = default_params

    # Викликаємо функції розрахунку
    results_koshi = solve_all()

    # Передаємо параметри у функцію (** розпаковує словник як аргументи)
    results_sir = solve_sir(**current_params)

    errors_y1, max_err_y1 = error_by_x(results_koshi, 'y1')
    errors_y2, max_err_y2 = error_by_x(results_koshi, 'y2')
    errors_y3, max_err_y3 = error_by_x(results_koshi, 'y3')

    deviations = {
        'y1': {'errors': errors_y1, 'max': max_err_y1},
        'y2': {'errors': errors_y2, 'max': max_err_y2},
        'y3': {'errors': errors_y3, 'max': max_err_y3},
    }

    # Передаємо current_params у шаблон, щоб заповнити поля вводу поточними значеннями
    return render_template('index0.html',
                           results=results_koshi,
                           sir_results=results_sir,
                           deviations=deviations,
                           params=current_params)


if __name__ == '__main__':
    app.run(debug=True)