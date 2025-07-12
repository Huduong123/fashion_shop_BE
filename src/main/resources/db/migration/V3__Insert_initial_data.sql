-- V3__Insert_initial_data.sql (MySQL) - PHIÊN BẢN ĐÃ CẬP NHẬT

-- Dữ liệu người dùng và danh mục vẫn giữ nguyên
INSERT INTO users (id, username, password, email, full_name, phone, gender, birth_date) VALUES
(1, 'admin', '$2a$12$H7YNqN.rFijke07DqC.eTuu0n70mZN22wjGWS5NAuVOEpZUMor0Va', 'admin@example.com', 'Admin User', '0901234567', 'MALE', '1980-01-01'),
(2, 'john_doe', '$2a$12$H7YNqN.rFijke07DqC.eTuu0n70mZN22wjGWS5NAuVOEpZUMor0Va', 'john@example.com', 'John Doe', '0902345678', 'MALE', '1990-03-10'),
(3, 'system', '$2a$12$H7YNqN.rFijke07DqC.eTuu0n70mZN22wjGWS5NAuVOEpZUMor0Va', 'system@example.com', 'System', '0903456789', 'MALE', '1995-07-15');

INSERT INTO authorities (user_id, authority) VALUES
(1, 'ROLE_ADMIN'),
(2, 'ROLE_USER'),
(3, 'ROLE_SYSTEM');

INSERT INTO categories (id, name, description) VALUES
(1, 'Áo', 'Tất cả các loại áo thời trang'),
(2, 'Quần', 'Quần jeans, quần short, quần dài...'),
(3, 'Phụ kiện', 'Nón, dây nịt, túi xách...');

-- Chèn dữ liệu cho colors và sizes
INSERT INTO colors (id, name) VALUES (1, 'Đen'), (2, 'Trắng'), (3, 'Xanh Navy');
INSERT INTO sizes (id, name) VALUES (1, 'S'), (2, 'M'), (3, 'L');

-- Chèn dữ liệu cho products (thông tin chung)
INSERT INTO products (id, name, description, category_id) VALUES
(1, 'Áo Thun Nam Basic', 'Chất liệu cotton 100%, form rộng.', 1),
(2, 'Quần Jean Skinny', 'Co giãn nhẹ, phù hợp đi làm.', 2),
(3, 'Nón Bucket', 'Phong cách street style.', 3);

-- Chèn dữ liệu cho product_variants (chỉ chứa product_id, color_id, image_url)
INSERT INTO product_variants (id, product_id, color_id, image_url) VALUES
-- Các biến thể của Áo Thun (id=1)
(1, 1, 1, '/images/ao-thun-den.jpg'),    -- Áo Thun, Đen
(2, 1, 2, '/images/ao-thun-trang.jpg'),  -- Áo Thun, Trắng
-- Biến thể của Quần Jean (id=2)
(3, 2, 3, '/images/quan-jean-xanh.jpg'), -- Quần Jean, Xanh Navy
-- Biến thể của Nón Bucket (id=3)
(4, 3, 1, '/images/non-bucket-den.jpg'); -- Nón Bucket, Đen

-- Chèn dữ liệu cho product_variant_sizes (size cụ thể với giá và số lượng)
INSERT INTO product_variant_sizes (product_variant_id, size_id, price, quantity) VALUES
-- Áo Thun Đen (variant_id=1) có size M và L
(1, 2, 199000.00, 50), -- Size M
(1, 3, 199000.00, 40), -- Size L
-- Áo Thun Trắng (variant_id=2) có size M và L
(2, 2, 210000.00, 60), -- Size M
(2, 3, 210000.00, 45), -- Size L
-- Quần Jean Xanh Navy (variant_id=3) có size L
(3, 3, 399000.00, 30), -- Size L
-- Nón Bucket Đen (variant_id=4) có size S (one size)
(4, 1, 120000.00, 20); -- Size S (one size fits all)

-- Chèn dữ liệu cho cart_items (trỏ tới product_variant_id và size_id)
-- Giỏ hàng của user 'john_doe' (id=2)
INSERT INTO cart_items (user_id, product_variant_id, size_id, quantity) VALUES
(2, 1, 2, 2), -- 2 cái Áo Thun Đen, Size M
(2, 3, 3, 1); -- 1 cái Quần Jean Xanh Navy, Size L

-- Chèn dữ liệu cho orders và order_items (trỏ tới product_variant_id và size_id)
-- Tạo một đơn hàng mẫu
INSERT INTO orders (id, user_id, total_price, status) VALUES
    (1, 2, 330000.00, 'PAID'); -- (210000*1) + (120000*1)

-- Chi tiết đơn hàng đó
INSERT INTO order_items (order_id, product_variant_id, size_id, quantity, price) VALUES
(1, 2, 2, 1, 210000.00), -- 1 cái Áo Thun Trắng Size M (lưu giá tại thời điểm mua)
(1, 4, 1, 1, 120000.00); -- 1 cái Nón Bucket Size S

-- Chèn dữ liệu cho reviews (vẫn trỏ tới product_id chung)
INSERT INTO reviews (user_id, product_id, rating, comment) VALUES
(2, 1, 5, 'Áo chất lượng, rất đẹp!'),
(2, 3, 4, 'Nón đẹp nhưng giao hơi lâu');