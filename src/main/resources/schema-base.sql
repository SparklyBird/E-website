CREATE TABLE IF NOT EXISTS category (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL UNIQUE
);

INSERT INTO category(name) VALUES ('Books');
INSERT INTO category(name) VALUES ('Coffee Mugs');
INSERT INTO category(name) VALUES ('Mouse Pads');
INSERT INTO category(name) VALUES ('Luggage Tags');

CREATE TABLE IF NOT EXISTS product (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    sku TEXT UNIQUE NOT NULL,
    name TEXT NOT NULL,
    description TEXT,
    image_url TEXT,
    active BOOLEAN NOT NULL DEFAULT 1,
    units_in_stock INTEGER NOT NULL DEFAULT 0,
    unit_price REAL NOT NULL CHECK(unit_price >= 0),
    category_id INTEGER,
    FOREIGN KEY (category_id) REFERENCES category(id)
);


-- -----------------------------------------------------
-- Books
-- -----------------------------------------------------
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, unit_price, category_id) VALUES ('BOOK-TECH-1000', 'Crash Course in Python', 'Learn Python at your own pace. The author explains how the technology works in easy-to-understand language. This book includes working examples that you can apply to your own projects. Purchase the book and get started today!', 'images/products/books/book-zigmunds-1000.png', 1, 100, 14.99, 1);
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, unit_price, category_id) VALUES ('BOOK-TECH-1001', 'Become a Guru in JavaScript', 'Learn JavaScript at your own pace. The author explains how the technology works in easy-to-understand language. This book includes working examples that you can apply to your own projects. Purchase the book and get started today!', 'images/products/books/book-zigmunds-1001.png', 1, 100, 20.99, 1);
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, unit_price, category_id) VALUES ('BOOK-TECH-1002', 'Exploring Vue.js', 'Learn Vue.js at your own pace. The author explains how the technology works in easy-to-understand language. This book includes working examples that you can apply to your own projects. Purchase the book and get started today!', 'images/products/books/book-zigmunds-1002.png', 1, 100, 14.99, 1);
INSERT INTO product (sku, name, description, image_url, active, units_in_stock, unit_price, category_id) VALUES ('BOOK-TECH-1003', 'Advanced Techniques in Big Data', 'Learn Big Data at your own pace. The author explains how the technology works in easy-to-understand language. This book includes working examples that you can apply to your own projects. Purchase the book and get started today!', 'images/products/books/book-zigmunds-1003.png', 1, 100, 13.99, 1);
