import math
import numpy as np
import random

def runge_kutta_step(x, y, h, f):
    k1 = np.array(f(x, y))
    k2 = np.array(f(x + h / 2, y + h * k1 / 2))
    k3 = np.array(f(x + h / 2, y + h * k2 / 2))
    k4 = np.array(f(x + h, y + h * k3))
    return y + (h / 6) * (k1 + 2 * k2 + 2 * k3 + k4)

def system(x, y):
    y1, y2, y3 = y
    dy1 = y2 + x
    dy2 = y3 - y1
    dy3 = np.sin(x) - y2
    return [dy1, dy2, dy3]

def normal_random(rnd, mean=0, std_dev=1):
    u1 = rnd.random()
    u2 = rnd.random()
    z = math.sqrt(-2 * math.log(u1)) * math.cos(2 * math.pi * u2)
    return mean + std_dev * z

def generate_initial_conditions(n=10, ax=0, bx=1, ayi=-2, byi=2):
    conditions = []
    rnd = random.Random()
    mean = (ayi + byi) / 2
    std_dev = (byi - ayi) / 4

    for _ in range(n):
        x0 = rnd.uniform(ax, bx)
        y0 = [np.clip(normal_random(rnd, mean, std_dev), ayi, byi) for _ in range(3)]
        conditions.append((x0, y0))
    return conditions

def solve_all():
    ax, bx = 0, 1
    ayi, byi = -2, 2
    x_end = 10
    h = 0.1

    initial_conditions = generate_initial_conditions(10, ax, bx, ayi, byi)
    results = {'y1': [], 'y2': [], 'y3': []}

    for x0, y0 in initial_conditions:
        x_vals = [x0]
        y_vals = [np.array(y0)]
        x = x0
        while x <= x_end:
            y_next = runge_kutta_step(x, y_vals[-1], h, system)
            x += h
            x_vals.append(x)
            y_vals.append(y_next)

        y_vals = np.array(y_vals)
        label = f"#{len(results['y1']) + 1}: x0={x0:.2f}, y0={np.round(y0, 2)}"

        results['y1'].append({'x': x_vals, 'y': y_vals[:, 0].tolist(), 'label': label})
        results['y2'].append({'x': x_vals, 'y': y_vals[:, 1].tolist(), 'label': label})
        results['y3'].append({'x': x_vals, 'y': y_vals[:, 2].tolist(), 'label': label})

    return results


# koshi.py (оновлена частина)

def solve_sir(beta, mu, r, a, Y, population, T):
    h = 0.5
    steps = int(T / h)

    # Видаляємо жорстко задані змінні тут, бо вони тепер приходять як аргументи

    results = []
    rnd = random.Random()

    for i in range(4):
        # ... (код генерації I0 залишається тим самим) ...
        if i % 2 == 0:
            I0 = rnd.uniform(35251, 59639)
        else:
            mean_I = 50000
            std_dev_I = 8000
            I0 = np.clip(normal_random(rnd, mean_I, std_dev_I), 35251, 59639)

        S0 = population - I0
        S0_norm = S0 / population
        I0_norm = I0 / population

        S_vals = [S0_norm]
        I_vals = [I0_norm]
        t_vals = [0]
        S, I = S0_norm, I0_norm

        for step in range(steps):
            # Використовуємо передані beta, Y, mu, r, a
            dS = -beta * S * I - Y * S
            dI = beta * S * I - mu * I - (r * I) / (a + I)

            S += h * dS
            I += h * dI

            t = (step + 1) * h
            t_vals.append(t)
            S_vals.append(S)
            I_vals.append(I)

        label = f"#{len(results) + 1}: S₀={S0:.0f}, I₀={I0:.0f}"
        results.append({'x': t_vals, 'y_S': S_vals, 'y_I': I_vals, 'label': label})

    return results

def error_by_x(results_dict, var_name, num_points=200, eps=1e-6):

    solutions = results_dict[var_name]

    if not solutions or len(solutions) < 2:
        return [], {'x': None, 'deviation': None, 'lines': None}

    all_xs = [x for sol in solutions for x in sol['x']]
    global_min, global_max = min(all_xs), max(all_xs)

    common_x = np.linspace(global_min, global_max, num_points)

    interpolated_values = []
    x_ranges = []

    for sol in solutions:
        x_vals = np.array(sol['x'])
        y_vals = np.array(sol['y'])
        interp_func = lambda x: np.interp(x, x_vals, y_vals) if x_vals[0] <= x <= x_vals[-1] else np.nan
        y_interp = np.array([interp_func(x) for x in common_x])
        interpolated_values.append(y_interp)
        x_ranges.append((x_vals[0], x_vals[-1]))

    errors = []
    for i, x in enumerate(common_x):
        values_at_x = []
        line_indices = []

        for idx, y_interp in enumerate(interpolated_values):
            if not np.isnan(y_interp[i]):
                values_at_x.append(y_interp[i])
                line_indices.append(idx + 1)

        if len(values_at_x) < 2:
            continue

        max_dev = 0
        max_pair = (None, None)
        n = len(values_at_x)
        for a in range(n):
            for b in range(a + 1, n):
                dev = abs(values_at_x[a] - values_at_x[b])
                if dev > max_dev:
                    max_dev = dev
                    max_pair = (line_indices[a], line_indices[b])

        if max_dev > eps:
            errors.append({'x': x, 'deviation': max_dev, 'lines': max_pair})

    max_error = max(errors, key=lambda d: d['deviation']) if errors else {'x': None, 'deviation': None, 'lines': None}

    return errors, max_error
