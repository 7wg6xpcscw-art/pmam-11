<?php
// === ФАЙЛ З ДАНИМИ ===
$filename = "tasks.json";

// === ЗАВАНТАЖЕННЯ ===
$tasks = [];
if (file_exists($filename)) {
    $data = file_get_contents($filename);
    $tasks = json_decode($data, true);
    if (!is_array($tasks)) $tasks = [];
}

// === ДОДАТИ ЗАВДАННЯ ===
if (isset($_POST["add"])) {
    $title = trim($_POST["title"]);
    if ($title !== "") {
        $tasks[] = [
            "title" => $title,
            "done" => false,
            "created" => date("Y-m-d H:i:s")
        ];
        save($tasks, $filename);
    }
}

// === ПОМІТИТИ ВИКОНАНО ===
if (isset($_GET["done"])) {
    $i = intval($_GET["done"]);
    if (isset($tasks[$i])) {
        $tasks[$i]["done"] = !$tasks[$i]["done"];
        save($tasks, $filename);
    }
}

// === ВИДАЛИТИ ===
if (isset($_GET["del"])) {
    $i = intval($_GET["del"]);
    if (isset($tasks[$i])) {
        array_splice($tasks, $i, 1);
        save($tasks, $filename);
    }
}

// === ФУНКЦІЯ ЗБЕРЕЖЕННЯ ===
function save($tasks, $filename)
{
    file_put_contents($filename, json_encode($tasks, JSON_PRETTY_PRINT | JSON_UNESCAPED_UNICODE));
    header("Location: index.php");
    exit;
}

// === ФІЛЬТР ===
$filter = $_GET["filter"] ?? "all";

function filtered_tasks($tasks, $filter)
{
    if ($filter === "done")
        return array_filter($tasks, fn($t) => $t["done"]);

    if ($filter === "notdone")
        return array_filter($tasks, fn($t) => !$t["done"]);

    return $tasks;
}

$visible = filtered_tasks($tasks, $filter);
?>
<!DOCTYPE html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <title>PHP To-Do Manager</title>
    <style>
        body {
            font-family: Arial;
            max-width: 600px;
            margin: 20px auto;
        }

        .done {
            text-decoration: line-through;
            color: gray;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
        }

        th, td {
            padding: 8px;
            border-bottom: 1px solid #ddd;
        }

        .btn {
            padding: 5px 10px;
            background: #29a;
            color: white;
            text-decoration: none;
            border-radius: 4px;
        }

        .btn-del {
            background: #c33;
        }

        .filters a {
            margin-right: 10px;
        }
    </style>
</head>
<body>

<h2>PHP To-Do Manager</h2>

<!-- ФОРМА -->
<form method="post">
    <input type="text" name="title" placeholder="Нове завдання" style="width:70%; padding:5px">
    <button name="add" class="btn">Додати</button>
</form>

<!-- ФІЛЬТРИ -->
<div class="filters">
    <strong>Фільтр:</strong>
    <a href="?filter=all">Всі</a>
    <a href="?filter=done">Виконані</a>
    <a href="?filter=notdone">Невиконані</a>
</div>

<!-- ТАБЛИЦЯ -->
<table>
    <tr>
        <th>Назва</th>
        <th>Створено</th>
        <th>Статус</th>
        <th>Дії</th>
    </tr>

    <?php foreach ($visible as $i => $task): ?>
        <tr>
            <td class="<?= $task["done"] ? 'done' : '' ?>">
                <?= htmlspecialchars($task["title"]) ?>
            </td>
            <td><?= $task["created"] ?></td>
            <td><?= $task["done"] ? "✔" : "✘" ?></td>
            <td>
                <a class="btn" href="?done=<?= $i ?>">
                    <?= $task["done"] ? "Відмінити" : "Готово" ?>
                </a>
                <a class="btn btn-del" href="?del=<?= $i ?>">X</a>
            </td>
        </tr>
    <?php endforeach; ?>
</table>

</body>
</html>
