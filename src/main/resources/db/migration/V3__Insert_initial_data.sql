-- V3__Insert_initial_data.sql (MySQL) - ĐÃ SỬA LỖI

-- 1. Chèn dữ liệu người dùng và quyền hạn
INSERT INTO users (id, username, password, email, full_name, phone, gender, birth_date) VALUES
(1, 'admin', '$2a$12$H7YNqN.rFijke07DqC.eTuu0n70mZN22wjGWS5NAuVOEpZUMor0Va', 'admin@example.com', 'Admin User', '0901234567', 'MALE', '1980-01-01'),
(2, 'john_doe', '$2a$12$H7YNqN.rFijke07DqC.eTuu0n70mZN22wjGWS5NAuVOEpZUMor0Va', 'john@example.com', 'John Doe', '0902345678', 'MALE', '1990-03-10'),
(3, 'system', '$2a$12$H7YNqN.rFijke07DqC.eTuu0n70mZN22wjGWS5NAuVOEpZUMor0Va', 'system@example.com', 'System', '0903456789', 'MALE', '1995-07-15');

INSERT INTO authorities (user_id, authority) VALUES
(1, 'ROLE_ADMIN'),
(2, 'ROLE_USER'),
(3, 'ROLE_SYSTEM');

-- 2. Chèn dữ liệu cho Categories
-- 2.1. Chèn các category gốc
INSERT INTO categories (id, name, description, slug) VALUES
(1, 'Áo', 'Tất cả các loại áo thời trang', 'ao'),
(2, 'Quần', 'Quần jeans, quần short, quần dài...', 'quan'),
(3, 'Phụ kiện', 'Nón, dây nịt, túi xách...', 'phu-kien');

-- 2.2. Bổ sung category cha
INSERT INTO categories (id, name, description, parent_id, slug) VALUES
(4, 'Áo Nam', 'Các loại áo dành cho nam giới', 1, 'ao-nam'),
(5, 'Quần Nam', 'Các loại quần dành cho nam giới', 2, 'quan-nam');

-- 2.3. Chèn các category con
INSERT INTO categories (name, description, parent_id, type, status, slug) VALUES
('Áo polo nam', 'Áo polo dành cho nam giới', 4, 'LINK', 'ACTIVE', 'ao-polo-nam'),
('Áo thun nam', 'Áo thun tay ngắn, tay dài dành cho nam', 4, 'LINK', 'ACTIVE', 'ao-thun-nam'),
('Áo sơ mi nam', 'Áo sơ mi công sở và đi chơi dành cho nam', 4, 'LINK', 'ACTIVE', 'ao-so-mi-nam'),
('Áo khoác nam', 'Áo khoác, jacket dành cho nam', 4, 'LINK', 'ACTIVE', 'ao-khoac-nam'),
('Quần jean nam', 'Quần jean dành cho nam giới', 5, 'LINK', 'ACTIVE', 'quan-jean-nam'),
('Quần tây nam', 'Quần tây công sở dành cho nam', 5, 'LINK', 'ACTIVE', 'quan-tay-nam'),
('Quần short nam', 'Quần short mùa hè dành cho nam', 5, 'LINK', 'ACTIVE', 'quan-short-nam');

-- 3. Chèn dữ liệu Colors và Sizes
INSERT INTO colors (id, name) VALUES (1, 'Đen'), (2, 'Trắng'), (3, 'Xanh Navy');
INSERT INTO sizes (id, name) VALUES (1, 'S'), (2, 'M'), (3, 'L');

-- 4. Thêm dữ liệu cho payment_methods
INSERT INTO payment_methods(code, name, is_enabled) VALUES
('COD', 'Thanh toán khi nhận hàng', 1),
('VNPAY', 'Thanh toán qua VNPAY', 1),
('MOMO', 'Thanh toán qua Ví MoMo', 1);