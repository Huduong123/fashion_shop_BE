-- V3__Insert_initial_data.sql (MySQL) - PHIÊN BẢN ĐÃ GỘP ĐẦY ĐỦ V4 & V5

-- 1. Chèn dữ liệu người dùng và quyền hạn
INSERT INTO users (id, username, password, email, full_name, phone, gender, birth_date) VALUES
(1, 'admin', '$2a$12$H7YNqN.rFijke07DqC.eTuu0n70mZN22wjGWS5NAuVOEpZUMor0Va', 'admin@example.com', 'Admin User', '0901234567', 'MALE', '1980-01-01'),
(2, 'john_doe', '$2a$12$H7YNqN.rFijke07DqC.eTuu0n70mZN22wjGWS5NAuVOEpZUMor0Va', 'john@example.com', 'John Doe', '0902345678', 'MALE', '1990-03-10'),
(3, 'system', '$2a$12$H7YNqN.rFijke07DqC.eTuu0n70mZN22wjGWS5NAuVOEpZUMor0Va', 'system@example.com', 'System', '0903456789', 'MALE', '1995-07-15');

INSERT INTO authorities (user_id, authority) VALUES
(1, 'ROLE_ADMIN'),
(2, 'ROLE_USER'),
(3, 'ROLE_SYSTEM');

-- 2. Chèn dữ liệu cho Categories (bao gồm cả dữ liệu từ V4 và slug từ V5)
-- 2.1. Chèn các category gốc
INSERT INTO categories (id, name, description, slug) VALUES
(1, 'Áo', 'Tất cả các loại áo thời trang', 'ao'),
(2, 'Quần', 'Quần jeans, quần short, quần dài...', 'quan'),
(3, 'Phụ kiện', 'Nón, dây nịt, túi xách...', 'phu-kien');

-- 2.2. Bổ sung category cha còn thiếu để làm cơ sở cho dữ liệu V4
INSERT INTO categories (id, name, description, parent_id, slug) VALUES
(4, 'Áo Nam', 'Các loại áo dành cho nam giới', 1, 'ao-nam'),
(5, 'Quần Nam', 'Các loại quần dành cho nam giới', 2, 'quan-nam');

-- 2.3. Chèn các category con từ V4 (đã sửa parent_id và thêm slug)
-- Lưu ý: ID được chèn tự động, bắt đầu từ 6
INSERT INTO categories (name, description, parent_id, type, status, slug) VALUES
('Áo polo nam', 'Áo polo dành cho nam giới', 4, 'LINK', 'ACTIVE', 'ao-polo-nam'),       -- id=6
('Áo thun nam', 'Áo thun tay ngắn, tay dài dành cho nam', 4, 'LINK', 'ACTIVE', 'ao-thun-nam'),   -- id=7
('Áo sơ mi nam', 'Áo sơ mi công sở và đi chơi dành cho nam', 4, 'LINK', 'ACTIVE', 'ao-so-mi-nam'), -- id=8
('Áo khoác nam', 'Áo khoác, jacket dành cho nam', 4, 'LINK', 'ACTIVE', 'ao-khoac-nam'),   -- id=9
('Quần jean nam', 'Quần jean dành cho nam giới', 5, 'LINK', 'ACTIVE', 'quan-jean-nam'), -- id=10
('Quần tây nam', 'Quần tây công sở dành cho nam', 5, 'LINK', 'ACTIVE', 'quan-tay-nam'),   -- id=11
('Quần short nam', 'Quần short mùa hè dành cho nam', 5, 'LINK', 'ACTIVE', 'quan-short-nam'); -- id=12

-- 3. Chèn dữ liệu Colors và Sizes
INSERT INTO colors (id, name) VALUES (1, 'Đen'), (2, 'Trắng'), (3, 'Xanh Navy');
INSERT INTO sizes (id, name) VALUES (1, 'S'), (2, 'M'), (3, 'L');

-- 4. Chèn dữ liệu Products (cập nhật category_id để trỏ đến category con chi tiết hơn)
INSERT INTO products (id, name, description, category_id) VALUES
(1, 'Áo Thun Nam Basic', 'Chất liệu cotton 100%, form rộng.', 7), -- Trỏ đến 'Áo thun nam' (id=7)
(2, 'Quần Jean Skinny', 'Co giãn nhẹ, phù hợp đi làm.', 10),       -- Trỏ đến 'Quần jean nam' (id=10)
(3, 'Nón Bucket', 'Phong cách street style.', 3);                   -- Trỏ đến 'Phụ kiện' (id=3)

-- 5. Chèn dữ liệu cho các biến thể sản phẩm (Product Variants)
INSERT INTO product_variants (id, product_id, color_id, image_url) VALUES
-- Các biến thể của Áo Thun (id=1)
(1, 1, 1, '/images/ao-thun-den.jpg'),    -- Áo Thun, Đen
(2, 1, 2, '/images/ao-thun-trang.jpg'),  -- Áo Thun, Trắng
-- Biến thể của Quần Jean (id=2)
(3, 2, 3, '/images/quan-jean-xanh.jpg'), -- Quần Jean, Xanh Navy
-- Biến thể của Nón Bucket (id=3)
(4, 3, 1, '/images/non-bucket-den.jpg'); -- Nón Bucket, Đen

-- 6. Chèn dữ liệu chi tiết về size, giá, số lượng cho từng biến thể (Product Variant Sizes)
INSERT INTO product_variant_sizes (product_variant_id, size_id, price, quantity) VALUES
-- Áo Thun Đen (variant_id=1)
(1, 2, 199000.00, 50), -- Size M
(1, 3, 199000.00, 40), -- Size L
-- Áo Thun Trắng (variant_id=2)
(2, 2, 210000.00, 60), -- Size M
(2, 3, 210000.00, 45), -- Size L
-- Quần Jean Xanh Navy (variant_id=3)
(3, 3, 399000.00, 30), -- Size L
-- Nón Bucket Đen (variant_id=4)
(4, 1, 120000.00, 20); -- Size S (one size)

-- 7. Chèn dữ liệu giỏ hàng (Cart Items)
-- Giỏ hàng của user 'john_doe' (id=2)
INSERT INTO cart_items (user_id, product_variant_id, size_id, quantity) VALUES
(2, 1, 2, 2), -- 2 cái Áo Thun Đen, Size M
(2, 3, 3, 1); -- 1 cái Quần Jean Xanh Navy, Size L

-- 8. Chèn dữ liệu đơn hàng (Orders và Order Items)
-- Tạo một đơn hàng mẫu
INSERT INTO orders (id, user_id, total_price, status) VALUES
    (1, 2, 330000.00, 'PAID'); -- (210000*1) + (120000*1)

-- Chi tiết đơn hàng đó
INSERT INTO order_items (order_id, product_variant_id, size_id, quantity, price) VALUES
(1, 2, 2, 1, 210000.00), -- 1 cái Áo Thun Trắng Size M (lưu giá tại thời điểm mua)
(1, 4, 1, 1, 120000.00); -- 1 cái Nón Bucket Size S

-- 9. Chèn dữ liệu đánh giá (Reviews)
INSERT INTO reviews (user_id, product_id, rating, comment) VALUES
(2, 1, 5, 'Áo chất lượng, rất đẹp!'),
(2, 3, 4, 'Nón đẹp nhưng giao hơi lâu');