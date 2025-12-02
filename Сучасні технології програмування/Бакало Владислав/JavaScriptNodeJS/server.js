const express = require('express');
const fs = require('fs').promises;
const path = require('path');
const multer = require('multer');

const app = express();
const PORT = 3000;

// Налаштування Multer для завантаження зображень
const storage = multer.diskStorage({
    destination: function (req, file, cb) {
        cb(null, 'images/products/')
    },
    filename: function (req, file, cb) {
        const uniqueName = Date.now() + '-' + file.originalname;
        cb(null, uniqueName);
    }
});

const upload = multer({ 
    storage: storage,
    limits: { fileSize: 5 * 1024 * 1024 }, // 5MB
    fileFilter: (req, file, cb) => {
        const allowedTypes = /jpeg|jpg|png|gif|webp/;
        const extname = allowedTypes.test(path.extname(file.originalname).toLowerCase());
        const mimetype = allowedTypes.test(file.mimetype);
        
        if (mimetype && extname) {
            return cb(null, true);
        } else {
            cb(new Error('Дозволені лише зображення!'));
        }
    }
});

// Middleware
app.use(express.json());
app.use(express.static('public'));
app.use('/images', express.static('images'));
app.use(express.urlencoded({ extended: true }));

// Дані про товари з фото
const products = [
    { 
        id: 1, 
        name: 'iPhone 14 Pro', 
        price: 999, 
        category: 'phones',
        image: '/images/products/iphone14.jpg',
        description: 'Новий iPhone 14 Pro з Dynamic Island',
        rating: 4.8,
        inStock: true,
        discount: 10
    },
    { 
        id: 2, 
        name: 'Samsung Galaxy S23 Ultra', 
        price: 1199, 
        category: 'phones',
        image: '/images/products/samsung.jpg',
        description: '200MP камера, S Pen, потужний процесор',
        rating: 4.7,
        inStock: true,
        discount: 0
    },
    { 
        id: 3, 
        name: 'MacBook Pro 16" M2', 
        price: 2499, 
        category: 'laptops',
        image: '/images/products/macbook.jpg',
        description: 'Потужний ноутбук для професіоналів',
        rating: 4.9,
        inStock: true,
        discount: 5
    },
    { 
        id: 4, 
        name: 'Dell XPS 13 Plus', 
        price: 1599, 
        category: 'laptops',
        image: '/images/products/dell.jpg',
        description: 'Безрамковий дисплей, потужна продуктивність',
        rating: 4.6,
        inStock: false,
        discount: 15
    },
    { 
        id: 5, 
        name: 'AirPods Pro 2', 
        price: 249, 
        category: 'accessories',
        image: '/images/products/airpods.jpg',
        description: 'Активне шумозаглушення нового покоління',
        rating: 4.5,
        inStock: true,
        discount: 0
    },
    { 
        id: 6, 
        name: 'Sony WH-1000XM5', 
        price: 399, 
        category: 'accessories',
        image: '/images/products/sony.jpg',
        description: 'Найкраще шумозаглушення на ринку',
        rating: 4.8,
        inStock: true,
        discount: 20
    },
    { 
        id: 7, 
        name: 'iPad Pro M2', 
        price: 1099, 
        category: 'tablets',
        image: '/images/products/ipad.jpg',
        description: 'Планшет з потужністю ноутбука',
        rating: 4.7,
        inStock: true,
        discount: 0
    },
    { 
        id: 8, 
        name: 'Apple Watch Ultra', 
        price: 799, 
        category: 'watches',
        image: '/images/products/watch.jpg',
        description: 'Розроблений для екстремальних умов',
        rating: 4.6,
        inStock: true,
        discount: 12
    }
];

// Головна сторінка
app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, 'public', 'index.html'));
});

// API для отримання товарів
app.get('/api/products', (req, res) => {
    const category = req.query.category;
    const search = req.query.search?.toLowerCase();
    
    let filteredProducts = products;
    
    if (category && category !== 'all') {
        filteredProducts = filteredProducts.filter(p => p.category === category);
    }
    
    if (search) {
        filteredProducts = filteredProducts.filter(p => 
            p.name.toLowerCase().includes(search) || 
            p.description.toLowerCase().includes(search)
        );
    }
    
    res.json(filteredProducts);
});

// Обробка замовлення
app.post('/api/order', async (req, res) => {
    try {
        const { name, email, phone, address, payment, cart } = req.body;
        
        // Валідація
        if (!name || !email || !phone || !cart || cart.length === 0) {
            return res.status(400).json({ error: 'Будь ласка, заповніть всі обов\'язкові поля' });
        }

        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(email)) {
            return res.status(400).json({ error: 'Невірний формат email' });
        }

        // Створення замовлення
        const order = {
            id: Date.now(),
            date: new Date().toLocaleString('uk-UA'),
            customer: { name, email, phone, address: address || 'Не вказано', payment: payment || 'cash' },
            items: cart,
            total: cart.reduce((sum, item) => sum + (item.price * item.quantity * (100 - item.discount) / 100), 0)
        };

        // Форматування для файлу
        const orderText = `

        ЗАМОВЛЕННЯ #${order.id}               
         ${order.date}                  

Клієнт:
   • Ім'я: ${order.customer.name}
   • Email: ${order.customer.email}
   • Телефон: ${order.customer.phone}
   • Адреса: ${order.customer.address}
   • Оплата: ${order.customer.payment === 'card' ? 'Карта' : 'Готівка'}

Товари:
${order.items.map(item => {
    const originalPrice = item.price * item.quantity;
    const discountPrice = originalPrice * (100 - item.discount) / 100;
    return `   • ${item.name} x${item.quantity}
      ${item.discount > 0 ? `⤵️  -${item.discount}%` : ''}
      ${item.discount > 0 ? `Ціна: $${originalPrice} → $${discountPrice.toFixed(2)}` : `Ціна: $${originalPrice}`}`;
}).join('\n')}

Підсумок:
   • Кількість товарів: ${order.items.reduce((sum, item) => sum + item.quantity, 0)}
   • Знижка: $${(order.items.reduce((sum, item) => sum + (item.price * item.quantity), 0) - order.total).toFixed(2)}
   • До сплати: $${order.total.toFixed(2)}

Статус: Очікує обробки
`;

        // Запис у файл
        await fs.appendFile('orders.txt', orderText);
        
        // Логування в консоль
        console.log(`Нове замовлення #${order.id} від ${name} на суму $${order.total.toFixed(2)}`);
        
        res.json({ 
            success: true, 
            orderId: order.id,
            total: order.total.toFixed(2)
        });
        
    } catch (error) {
        console.error('Помилка при обробці замовлення:', error);
        res.status(500).json({ error: 'Помилка сервера' });
    }
});

// Адмін панель для перегляду замовлень
app.get('/admin', (req, res) => {
    res.sendFile(path.join(__dirname, 'views', 'admin.html'));
});

// API для отримання замовлень
app.get('/api/orders', async (req, res) => {
    try {
        const data = await fs.readFile('orders.txt', 'utf8');
        res.json({ orders: data });
    } catch (error) {
        res.json({ orders: '' });
    }
});

// Завантаження зображень (для адміна)
app.post('/api/upload', upload.single('image'), (req, res) => {
    if (!req.file) {
        return res.status(400).json({ error: 'Файл не завантажено' });
    }
    res.json({ 
        success: true, 
        filename: req.file.filename,
        path: `/images/products/${req.file.filename}`
    });
});

// Сторінка успіху
app.get('/success', (req, res) => {
    res.sendFile(path.join(__dirname, 'views', 'success.html'));
});

// Запуск сервера
app.listen(PORT, () => {
    console.log(`Сервер запущено на http://localhost:${PORT}`);
    console.log(`Магазин: http://localhost:${PORT}/`);
    console.log(`Адмін-панель: http://localhost:${PORT}/admin`);
});