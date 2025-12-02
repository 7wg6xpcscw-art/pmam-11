using System;
using System.Collections.Generic;
using System.IO;

class Book
{
    public string Title;
    public string Author;
    public int Year;

    public Book(string t, string a, int y)
    {
        Title = t;
        Author = a;
        Year = y;
    }

    public override string ToString()
    {
        return $"{Title} — {Author} ({Year})";
    }
}

class Library
{
    private List<Book> books = new List<Book>();
    private string filename = "library.txt";

    public Library()
    {
        Load();
    }

    public void AddBook()
    {
        Console.Write("Назва: ");
        string title = Console.ReadLine();

        Console.Write("Автор: ");
        string author = Console.ReadLine();

        Console.Write("Рік: ");
        int year = ReadInt();

        books.Add(new Book(title, author, year));
        Console.WriteLine("Додано!\n");
    }

    public void ListBooks()
    {
        Console.WriteLine("\n--- Список книг ---");
        if (books.Count == 0)
        {
            Console.WriteLine("Пусто.\n");
            return;
        }

        int i = 1;
        foreach (var b in books)
            Console.WriteLine($"{i++}. {b}");

        Console.WriteLine();
    }

    public void SearchBook()
    {
        Console.Write("Введіть слово для пошуку: ");
        string q = Console.ReadLine().ToLower();

        Console.WriteLine("\n--- Результати ---");

        foreach (var b in books)
        {
            if (b.Title.ToLower().Contains(q) ||
                b.Author.ToLower().Contains(q))
            {
                Console.WriteLine(b);
            }
        }

        Console.WriteLine();
    }

    public void DeleteBook()
    {
        ListBooks();
        if (books.Count == 0) return;

        Console.Write("Номер книги: ");
        int n = ReadInt();

        if (n < 1 || n > books.Count)
        {
            Console.WriteLine("Помилка!\n");
            return;
        }

        books.RemoveAt(n - 1);
        Console.WriteLine("Видалено!\n");
    }

    public void Save()
    {
        using (StreamWriter sw = new StreamWriter(filename))
        {
            foreach (var b in books)
                sw.WriteLine($"{b.Title}|{b.Author}|{b.Year}");
        }

        Console.WriteLine("Збережено!\n");
    }

    public void Load()
    {
        if (!File.Exists(filename)) return;

        var lines = File.ReadAllLines(filename);
        foreach (var line in lines)
        {
            var parts = line.Split('|');
            books.Add(new Book(parts[0], parts[1], int.Parse(parts[2])));
        }
    }

    private int ReadInt()
    {
        while (true)
        {
            string s = Console.ReadLine();
            if (int.TryParse(s, out int val))
                return val;

            Console.Write("Введіть число: ");
        }
    }
}

class Program
{
    static void Main()
    {
        Library lib = new Library();

        while (true)
        {
            Console.WriteLine("--- C# LIBRARY ---");
            Console.WriteLine("1. Додати книгу");
            Console.WriteLine("2. Показати книги");
            Console.WriteLine("3. Пошук");
            Console.WriteLine("4. Видалити книгу");
            Console.WriteLine("5. Зберегти");
            Console.WriteLine("6. Вихід");
            Console.Write("Виберіть: ");

            string ch = Console.ReadLine();

            switch (ch)
            {
                case "1": lib.AddBook(); break;
                case "2": lib.ListBooks(); break;
                case "3": lib.SearchBook(); break;
                case "4": lib.DeleteBook(); break;
                case "5": lib.Save(); break;
                case "6": return;
                default: Console.WriteLine("Невірно!\n"); break;
            }
        }
    }
}
