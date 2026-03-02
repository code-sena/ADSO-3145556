-- Coffee Shop E-commerce Database Model
-- Following instructor's security module pattern
-- Normalized to 3NF, with audit trail and inventory management

DROP DATABASE IF EXISTS coffee_shop;

CREATE DATABASE coffee_shop;

USE coffee_shop;

-- ============================================
-- SECURITY MODULE (Role -> Module -> View)
-- ============================================

CREATE TABLE person (
    person_id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20),
    address VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE role (
    role_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `user` (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    person_id INT NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    last_login TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (person_id) REFERENCES person(person_id) ON DELETE CASCADE
);

CREATE TABLE module (
    module_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    route_path VARCHAR(100),
    icon VARCHAR(50),
    parent_module_id INT,
    sort_order INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (parent_module_id) REFERENCES module(module_id) ON DELETE SET NULL
);

CREATE TABLE view (
    view_id INT PRIMARY KEY AUTO_INCREMENT,
    module_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    route_path VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    sort_order INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (module_id) REFERENCES module(module_id) ON DELETE CASCADE,
    UNIQUE KEY unique_module_view (module_id, route_path)
);

CREATE TABLE action (
    action_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_role (
    user_role_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    assigned_by INT,
    FOREIGN KEY (user_id) REFERENCES `user`(user_id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES role(role_id) ON DELETE CASCADE,
    FOREIGN KEY (assigned_by) REFERENCES `user`(user_id) ON DELETE SET NULL,
    UNIQUE KEY unique_user_role (user_id, role_id)
);

CREATE TABLE role_module (
    role_module_id INT PRIMARY KEY AUTO_INCREMENT,
    role_id INT NOT NULL,
    module_id INT NOT NULL,
    can_read BOOLEAN DEFAULT FALSE,
    can_create BOOLEAN DEFAULT FALSE,
    can_update BOOLEAN DEFAULT FALSE,
    can_delete BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES role(role_id) ON DELETE CASCADE,
    FOREIGN KEY (module_id) REFERENCES module(module_id) ON DELETE CASCADE,
    UNIQUE KEY unique_role_module (role_id, module_id)
);

CREATE TABLE view_module (
    view_module_id INT PRIMARY KEY AUTO_INCREMENT,
    view_id INT NOT NULL,
    module_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (view_id) REFERENCES view(view_id) ON DELETE CASCADE,
    FOREIGN KEY (module_id) REFERENCES module(module_id) ON DELETE CASCADE,
    UNIQUE KEY unique_view_module (view_id, module_id)
);

CREATE TABLE view_action (
    view_action_id INT PRIMARY KEY AUTO_INCREMENT,
    view_id INT NOT NULL,
    action_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (view_id) REFERENCES view(view_id) ON DELETE CASCADE,
    FOREIGN KEY (action_id) REFERENCES action(action_id) ON DELETE CASCADE,
    UNIQUE KEY unique_view_action (view_id, action_id)
);

-- ============================================
-- BUSINESS MODULE
-- ============================================

CREATE TABLE category (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    image_url VARCHAR(500),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE product (
    product_id INT PRIMARY KEY AUTO_INCREMENT,
    category_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    image_url VARCHAR(500),
    is_available BOOLEAN DEFAULT TRUE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES category(category_id) ON DELETE RESTRICT
);

CREATE TABLE inventory (
    inventory_id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT NOT NULL UNIQUE,
    current_stock INT NOT NULL DEFAULT 0,
    minimum_stock INT NOT NULL DEFAULT 10,
    maximum_stock INT NOT NULL DEFAULT 100,
    reorder_point INT NOT NULL DEFAULT 20,
    last_restocked_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES product(product_id) ON DELETE CASCADE
);

CREATE TABLE stock_movement (
    movement_id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT NOT NULL,
    movement_type ENUM('IN', 'OUT', 'ADJUSTMENT') NOT NULL,
    quantity INT NOT NULL,
    reason VARCHAR(255),
    reference_id INT,
    performed_by INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES product(product_id) ON DELETE CASCADE,
    FOREIGN KEY (performed_by) REFERENCES `user`(user_id) ON DELETE RESTRICT
);

CREATE TABLE cart (
    cart_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    status ENUM('ACTIVE', 'CHECKED_OUT', 'ABANDONED') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES `user`(user_id) ON DELETE CASCADE
);

CREATE TABLE cart_item (
    cart_item_id INT PRIMARY KEY AUTO_INCREMENT,
    cart_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    unit_price DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cart_id) REFERENCES cart(cart_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(product_id) ON DELETE CASCADE,
    UNIQUE KEY unique_cart_product (cart_id, product_id)
);

CREATE TABLE bill (
    bill_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    bill_number VARCHAR(50) NOT NULL UNIQUE,
    status_id INT NOT NULL,
    payment_method_id INT NOT NULL,
    payment_status_id INT NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    tax_amount DECIMAL(10, 2) NOT NULL DEFAULT 0,
    discount_amount DECIMAL(10, 2) NOT NULL DEFAULT 0,
    total_amount DECIMAL(10, 2) NOT NULL,
    shipping_address TEXT,
    notes TEXT,
    billed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES `user`(user_id) ON DELETE RESTRICT,
    FOREIGN KEY (payment_method_id) REFERENCES payment_method(payment_method_id),
    FOREIGN KEY (payment_status_id) REFERENCES payment_status(status_id)
);

CREATE TABLE bill_detail (
    bill_detail_id INT PRIMARY KEY AUTO_INCREMENT,
    bill_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (bill_id) REFERENCES bill(bill_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(product_id) ON DELETE RESTRICT
);

-- ============================================
-- LOOKUP TABLES
-- ============================================

CREATE TABLE payment_method (
    payment_method_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE payment_status (
    status_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE bill_status (
    status_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- AUDIT LOG
-- ============================================

CREATE TABLE audit_log (
    audit_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    table_name VARCHAR(100) NOT NULL,
    record_id INT NOT NULL,
    action ENUM('INSERT', 'UPDATE', 'DELETE') NOT NULL,
    old_data JSON,
    new_data JSON,
    changed_by INT,
    ip_address VARCHAR(45),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (changed_by) REFERENCES `user`(user_id) ON DELETE SET NULL
);

-- ============================================
-- SEED DATA
-- ============================================

-- Roles
INSERT INTO role (name, description) VALUES 
('ADMIN', 'Administrator with full access'),
('MANAGER', 'Manager for inventory and orders'),
('CUSTOMER', 'Regular customer');

-- Actions
INSERT INTO action (name, description) VALUES 
('INDEX', 'View list of records'),
('SHOW', 'View single record'),
('CREATE', 'Create new record'),
('UPDATE', 'Update existing record'),
('DELETE', 'Delete record');

-- Modules
INSERT INTO module (name, description, route_path, sort_order) VALUES 
('Dashboard', 'Main dashboard', '/dashboard', 1),
('Users', 'User management', '/users', 2),
('Roles', 'Role management', '/roles', 3),
('Products', 'Product catalog', '/products', 4),
('Categories', 'Category management', '/categories', 5),
('Inventory', 'Inventory management', '/inventory', 6),
('Orders', 'Order management', '/orders', 7),
('Reports', 'Sales reports', '/reports', 8);

-- Views per module
INSERT INTO view (module_id, name, route_path, sort_order) VALUES 
(1, 'Dashboard', '/dashboard', 1),
(2, 'User List', '/users', 1),
(2, 'User Create', '/users/create', 2),
(2, 'User Edit', '/users/edit', 3),
(3, 'Role List', '/roles', 1),
(3, 'Role Create', '/roles/create', 2),
(3, 'Role Edit', '/roles/edit', 3),
(4, 'Product List', '/products', 1),
(4, 'Product Create', '/products/create', 2),
(4, 'Product Edit', '/products/edit', 3),
(5, 'Category List', '/categories', 1),
(5, 'Category Create', '/categories/create', 2),
(5, 'Category Edit', '/categories/edit', 3),
(6, 'Inventory List', '/inventory', 1),
(6, 'Stock Adjustment', '/inventory/adjust', 2),
(7, 'Order List', '/orders', 1),
(7, 'Order Detail', '/orders/detail', 2),
(8, 'Sales Report', '/reports/sales', 1);

-- View-Module relationships
INSERT INTO view_module (view_id, module_id) VALUES 
(1,1), (2,2), (3,2), (4,2), (5,3), (6,3), (7,3), 
(8,4), (9,4), (10,4), (11,5), (12,5), (13,5), 
(14,6), (15,6), (16,7), (17,7), (18,8);

-- View-Action relationships
INSERT INTO view_action (view_id, action_id) VALUES 
(1,1), (2,1), (3,3), (4,2), (4,4), (5,1), (6,3), (7,2), (7,4),
(8,1), (9,3), (10,2), (10,4), (11,1), (12,3), (13,2), (13,4),
(14,1), (15,2), (15,4), (16,1), (17,1), (18,1);

-- Payment Methods
INSERT INTO payment_method (name) VALUES 
('CASH'), ('CREDIT_CARD'), ('DEBIT_CARD'), ('DIGITAL_WALLET');

-- Payment Status
INSERT INTO payment_status (name, description) VALUES 
('PENDING', 'Payment not yet processed'),
('COMPLETED', 'Payment successfully processed'),
('FAILED', 'Payment failed'),
('REFUNDED', 'Payment was refunded');

-- Bill Status
INSERT INTO bill_status (name, description) VALUES 
('PENDING', 'Bill received, awaiting preparation'),
('PREPARING', 'Order is being prepared'),
('READY', 'Order ready for pickup/delivery'),
('DELIVERED', 'Order delivered to customer'),
('CANCELLED', 'Order cancelled');

-- Persons
INSERT INTO person (first_name, last_name, email, phone) VALUES 
('John', 'Admin', 'admin@coffee.shop', '1234567890'),
('Jane', 'Manager', 'manager@coffee.shop', '1234567891'),
('Bob', 'Customer', 'customer@coffee.shop', '1234567892');

-- Users (password: password123)
INSERT INTO user (person_id, email, password_hash) VALUES 
(1, 'admin@coffee.shop', '$2b$10$abcdefghijklmnopqrstuv'),
(2, 'manager@coffee.shop', '$2b$10$abcdefghijklmnopqrstuv'),
(3, 'customer@coffee.shop', '$2b$10$abcdefghijklmnopqrstuv');

-- User Roles
INSERT INTO user_role (user_id, role_id) VALUES 
(1, 1), (2, 2), (3, 3);

-- Role Modules (Admin gets all permissions)
INSERT INTO role_module (role_id, module_id, can_read, can_create, can_update, can_delete) VALUES 
(1, 1, TRUE, TRUE, TRUE, TRUE),
(1, 2, TRUE, TRUE, TRUE, TRUE),
(1, 3, TRUE, TRUE, TRUE, TRUE),
(1, 4, TRUE, TRUE, TRUE, TRUE),
(1, 5, TRUE, TRUE, TRUE, TRUE),
(1, 6, TRUE, TRUE, TRUE, TRUE),
(1, 7, TRUE, TRUE, TRUE, TRUE),
(1, 8, TRUE, TRUE, TRUE, TRUE),
-- Manager
(2, 1, TRUE, FALSE, FALSE, FALSE),
(2, 4, TRUE, TRUE, TRUE, FALSE),
(2, 5, TRUE, TRUE, TRUE, FALSE),
(2, 6, TRUE, TRUE, TRUE, FALSE),
(2, 7, TRUE, TRUE, TRUE, FALSE),
(2, 8, TRUE, FALSE, FALSE, FALSE),
-- Customer
(3, 1, TRUE, FALSE, FALSE, FALSE),
(3, 4, TRUE, FALSE, FALSE, FALSE),
(3, 7, TRUE, TRUE, FALSE, FALSE);

-- Categories
INSERT INTO category (name, description) VALUES 
('Coffee', 'Various coffee beverages'),
('Tea', 'Tea selections'),
('Pastries', 'Fresh baked goods'),
('Sandwiches', 'Savory sandwiches and wraps'),
('Desserts', 'Sweet treats');

-- Products
INSERT INTO product (category_id, name, description, price) VALUES 
(1, 'Espresso', 'Strong and bold single shot', 2.50),
(1, 'Americano', 'Espresso with hot water', 3.00),
(1, 'Cappuccino', 'Espresso with steamed milk foam', 3.50),
(1, 'Latte', 'Espresso with steamed milk', 4.00),
(1, 'Mocha', 'Espresso with chocolate and milk', 4.50),
(2, 'Green Tea', 'Refreshing green tea', 2.00),
(2, 'Black Tea', 'Classic black tea', 2.00),
(3, 'Croissant', 'Buttery French pastry', 3.00),
(3, 'Blueberry Muffin', 'Fresh blueberry muffin', 3.50),
(4, 'Club Sandwich', 'Triple deck sandwich', 6.50),
(5, 'Chocolate Cake', 'Rich chocolate layer cake', 5.00),
(5, 'Cheesecake', 'Creamy New York cheesecake', 5.50);

-- Inventory
INSERT INTO inventory (product_id, current_stock, minimum_stock, maximum_stock, reorder_point) VALUES 
(1, 100, 20, 200, 30),
(2, 100, 20, 200, 30),
(3, 80, 15, 150, 25),
(4, 80, 15, 150, 25),
(5, 60, 10, 100, 20),
(6, 50, 10, 100, 15),
(7, 50, 10, 100, 15),
(8, 30, 5, 60, 10),
(9, 30, 5, 60, 10),
(10, 20, 5, 40, 10),
(11, 15, 3, 30, 5),
(12, 15, 3, 30, 5);

-- ============================================
-- INDEXES
-- ============================================

CREATE INDEX idx_person_email ON person(email);
CREATE INDEX idx_user_email ON `user`(email);
CREATE INDEX idx_user_person ON `user`(person_id);
CREATE INDEX idx_module_parent ON module(parent_module_id);
CREATE INDEX idx_view_module ON view(module_id);
CREATE INDEX idx_role_module_role ON role_module(role_id);
CREATE INDEX idx_product_category ON product(category_id);
CREATE INDEX idx_inventory_product ON inventory(product_id);
CREATE INDEX idx_stock_movement_product ON stock_movement(product_id);
CREATE INDEX idx_cart_user ON cart(user_id, status);
CREATE INDEX idx_bill_user ON bill(user_id, billed_at);
CREATE INDEX idx_bill_detail_bill ON bill_detail(bill_id);
CREATE INDEX idx_audit_table ON audit_log(table_name, record_id, created_at);
