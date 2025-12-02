import json
import os
from datetime import datetime

FILENAME = "notes.json"


class Note:
    def __init__(self, title, body, created=None):
        self.title = title
        self.body = body
        self.created = created if created else datetime.now().strftime("%Y-%m-%d %H:%M:%S")

    def to_dict(self):
        return {
            "title": self.title,
            "body": self.body,
            "created": self.created
        }

    @staticmethod
    def from_dict(d):
        return Note(d["title"], d["body"], d["created"])

    def __str__(self):
        return f"{self.title} ({self.created})\n{self.body}\n"


class NoteManager:
    def __init__(self):
        self.notes = []
        self.load()

    def load(self):
        if os.path.exists(FILENAME):
            with open(FILENAME, "r", encoding="utf-8") as f:
                data = json.load(f)
                self.notes = [Note.from_dict(n) for n in data]

    def save(self):
        with open(FILENAME, "w", encoding="utf-8") as f:
            json.dump([n.to_dict() for n in self.notes], f, ensure_ascii=False, indent=2)

    def create_note(self):
        print("\n--- Створення нотатки ---")
        title = input("Назва: ")
        body = input("Текст: ")

        self.notes.append(Note(title, body))
        print("Нотатку додано!\n")

    def list_notes(self):
        print("\n--- Всі нотатки ---")
        if not self.notes:
            print("Пусто!\n")
            return

        for i, note in enumerate(self.notes, start=1):
            print(f"{i}. {note.title} | {note.created}")
        print()

    def view_note(self):
        self.list_notes()
        if not self.notes:
            return

        n = self.read_int("Виберіть номер нотатки: ", 1, len(self.notes))
        print("\n--- Перегляд ---")
        print(self.notes[n - 1])

    def edit_note(self):
        self.list_notes()
        if not self.notes:
            return

        n = self.read_int("Номер для редагування: ", 1, len(self.notes))
        note = self.notes[n - 1]

        print(f"Стара назва: {note.title}")
        new_title = input("Нова назва (Enter — не змінювати): ")
        if new_title.strip():
            note.title = new_title

        print(f"Старий текст: {note.body}")
        new_body = input("Новий текст (Enter — не змінювати): ")
        if new_body.strip():
            note.body = new_body

        print("Змінено!\n")

    def delete_note(self):
        self.list_notes()
        if not self.notes:
            return

        n = self.read_int("Виберіть номер для видалення: ", 1, len(self.notes))
        self.notes.pop(n - 1)
        print("Видалено!\n")

    def search(self):
        q = input("Введіть слово для пошуку: ").lower()
        print("\n--- Результати пошуку ---")

        found = False
        for note in self.notes:
            if q in note.title.lower() or q in note.body.lower():
                print(note)
                found = True

        if not found:
            print("Нічого не знайдено.\n")

    def sort_notes(self):
        print("\n1. За назвою")
        print("2. За датою")
        ch = input("Виберіть: ")

        if ch == "1":
            self.notes.sort(key=lambda x: x.title.lower())
        elif ch == "2":
            self.notes.sort(key=lambda x: x.created)
        else:
            print("Невірний вибір!")

        print("Відсортовано!\n")

    @staticmethod
    def read_int(prompt, mn, mx):
        while True:
            try:
                n = int(input(prompt))
                if mn <= n <= mx:
                    return n
                else:
                    print(f"Введіть число від {mn} до {mx}.")
            except ValueError:
                print("Потрібно число!")


def menu():
    print("""
=== PYTHON NOTE MANAGER ===
1. Створити нотатку
2. Показати всі
3. Переглянути
4. Редагувати
5. Видалити
6. Пошук
7. Сортувати
8. Зберегти
9. Вихід
""")


manager = NoteManager()

while True:
    menu()
    choice = input("Виберіть: ")

    match choice:
        case "1": manager.create_note()
        case "2": manager.list_notes()
        case "3": manager.view_note()
        case "4": manager.edit_note()
        case "5": manager.delete_note()
        case "6": manager.search()
        case "7": manager.sort_notes()
        case "8": manager.save()
        case "9":
            manager.save()
            print("Вихід...")
            break
        case _:
            print("Помилка!\n")
