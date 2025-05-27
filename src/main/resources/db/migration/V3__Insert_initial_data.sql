-- V3__Insert_initial_data.sql (MySQL)
-- Chèn dữ liệu ban đầu vào các bảng

-- Initial data for users
-- Note: For AUTO_INCREMENT columns in MySQL, if you insert explicit IDs,
-- the AUTO_INCREMENT counter will be updated to MAX(id) + 1.
INSERT INTO users (id, username, password, email, full_name, phone, gender, birth_date, created_at, updated_at, enabled) VALUES
                                                                                                                             (1, 'admin', '$2a$12$H7YNqN.rFijke07DqC.eTuu0n70mZN22wjGWS5NAuVOEpZUMor0Va', 'admin@example.com', 'Admin User', '0901234567', 'MALE', '1980-01-01', '2025-05-15 15:47:15', '2025-05-17 07:41:46', 1), -- BOOLEAN TRUE is 1 in MySQL
                                                                                                                             (2, 'john_doe', '$2a$12$H7YNqN.rFijke07DqC.eTuu0n70mZN22wjGWS5NAuVOEpZUMor0Va', 'john@example.com', 'John Doe', '0902345678', 'MALE', '1990-03-10', '2025-05-15 15:47:15', '2025-05-17 07:41:49', 1),
                                                                                                                             (3, 'jane_smith', '$2a$12$H7YNqN.rFijke07DqC.eTuu0n70mZN22wjGWS5NAuVOEpZUMor0Va', 'jane@example.com', 'Jane Smith', '0903456789', 'FEMALE', '1992-07-22', '2025-05-15 15:47:15', '2025-05-17 07:41:51', 1),
                                                                                                                             (4, 'user123', '$2a$10$4/j0U/CwHVg19GzF4qoy3eIXOEaHT/fBNmD2TIyOyinviND1NhNzi', 'user@gmail.com', 'User Test', '0909123456', 'MALE', '1995-05-15', '2025-05-17 13:33:19', '2025-05-17 13:33:19', 1),
                                                                                                                             (5, 'user1', '$2a$10$LrGi5SJt.YQmfNN9uwfQO.I3xecYQeZigSqR4BKhuJtNcFarPiaDq', 'user1@gmail.com', 'User Test1', '0909123256', 'FEMALE', '1998-09-01', '2025-05-20 05:28:33', '2025-05-20 05:28:33', 1),
                                                                                                                             (6, 'user12', '$2a$10$BQ1ZYDDQTUhQolIdOyPwr.tVKnj1CCLsja0ms7EQmdsm4xY7LzyIG', 'user12@gmail.com', 'User Test2', '0909123223', 'MALE', '1987-11-20', '2025-05-20 05:47:15', '2025-05-20 05:47:15', 1),
                                                                                                                             (8, 'user3', '$2a$10$RJTaS1S6eDx.w6hk7/2.xO36IRbV.V9Qz5zXNEVpWZSQ.zhykp7W.', 'user3@gmail.com', 'User Test3', '0909127623', 'FEMALE', '1993-02-28', '2025-05-20 07:05:07', '2025-05-20 07:05:07', 1),
                                                                                                                             (9, 'user4', '$2a$10$DcGFj1RMPBeF1/KxxK4SWuSo4J6J0cGPWNTw0HRC3J07fve57bEcK', 'user4@gmail.com', 'User Test4', '0909127613', 'MALE', '2000-04-05', '2025-05-20 07:22:38', '2025-05-20 07:22:38', 1),
                                                                                                                             (10, 'user6', '$2a$10$YI6/gmbn4Ug984v6mqGCEOHBvOjhWUt0znwD2Pxob9Xkn9YFK7he6', 'user66@gmail.com', 'User Test6', '0666666666', 'FEMALE', '1989-06-12', '2025-05-23 15:09:01', '2025-05-24 16:58:39', 1),
                                                                                                                             (11, 'user7', '$2a$10$QWxXDcly.W7bSr/VWkTXoeBUNTVdbp9wrvCR./klh9W9Onz/v0.SG', 'user7@gmail.com', 'User Test7', '0909666667', 'MALE', '1997-08-30', '2025-05-23 15:50:20', '2025-05-23 15:50:20', 1);

-- Initial data for user_addresses
INSERT INTO user_addresses (id, user_id, recipient_name, phone_number, address_detail, is_default, created_at, updated_at) VALUES
                                                                                                                               (1, 2, 'Lương Thị Hương Quỳnh', '0763554775', '330 Phan Đình Phùng, Phường 01, Quận Phú Nhuận, Hồ Chí Minh', 1, '2025-05-25 10:00:00', '2025-05-25 10:00:00'),
                                                                                                                               (2, 1, 'Admin User', '0901234567', '789 Admin Road, Cyber City, HCMC', 1, '2025-05-25 10:01:00', '2025-05-25 10:01:00'),
                                                                                                                               (3, 2, 'John Doe', '0902345679', '101 Other Street, District 1, HCMC', 0, '2025-05-25 10:02:00', '2025-05-25 10:02:00');

-- Initial data for authorities
INSERT INTO authorities (id, user_id, authority, created_at, updated_at) VALUES
                                                                             (1, 1, 'ROLE_ADMIN', '2025-05-16 15:27:46', '2025-05-16 15:27:46'),
                                                                             (2, 2, 'ROLE_USER', '2025-05-16 15:27:46', '2025-05-16 15:27:46'),
                                                                             (3, 3, 'ROLE_USER', '2025-05-16 15:27:46', '2025-05-16 15:27:46'),
                                                                             (4, 4, 'ROLE_ADMIN', '2025-05-18 07:16:52', '2025-05-18 07:16:52'),
                                                                             (5, 6, 'ROLE_ADMIN', '2025-05-20 05:47:15', '2025-05-20 15:28:34'),
                                                                             (6, 8, 'ROLE_ADMIN', '2025-05-20 07:05:07', '2025-05-23 15:29:47'),
                                                                             (7, 9, 'ROLE_USER', '2025-05-20 07:22:38', '2025-05-20 07:22:38'),
                                                                             (11, 10, 'ROLE_USER', '2025-05-23 15:09:01', '2025-05-23 15:09:01'),
                                                                             (12, 11, 'ROLE_USER', '2025-05-23 15:50:20', '2025-05-23 15:50:20');

-- Initial data for categories
INSERT INTO categories (id, name, description, created_at, updated_at) VALUES
                                                                           (1, 'Áo', 'Tất cả các loại áo thời trang', '2025-05-15 15:47:25', '2025-05-15 15:47:25'),
                                                                           (2, 'Quần', 'Quần jeans, quần short, quần dài...', '2025-05-15 15:47:25', '2025-05-15 15:47:25'),
                                                                           (3, 'Phụ kiện', 'Nón, dây nịt, túi xách...', '2025-05-15 15:47:25', '2025-05-15 15:47:25');

-- Initial data for products
INSERT INTO products (id, name, description, price, quantity, image_url, category_id, created_at, updated_at) VALUES
                                                                                                                  (1, 'Áo Thun Nam Basic', 'Chất liệu cotton 100%, form rộng.', 199000.00, 50, '/images/ao1.jpg', 1, '2025-05-15 15:47:30', '2025-05-15 15:47:30'),
                                                                                                                  (2, 'Quần Jean Skinny', 'Co giãn nhẹ, phù hợp đi làm.', 399000.00, 30, '/images/quan1.jpg', 2, '2025-05-15 15:47:30', '2025-05-15 15:47:30'),
                                                                                                                  (3, 'Nón Bucket Đen', 'Phong cách street style.', 120000.00, 20, '/images/phukien1.jpg', 3, '2025-05-15 15:47:30', '2025-05-15 15:47:30');

-- Initial data for cart_items
INSERT INTO cart_items (id, user_id, product_id, quantity, created_at, updated_at) VALUES
                                                                                       (1, 2, 1, 2, '2025-05-15 15:47:35', '2025-05-15 15:47:35'),
                                                                                       (2, 2, 2, 1, '2025-05-15 15:47:35', '2025-05-15 15:47:35'),
                                                                                       (3, 3, 3, 1, '2025-05-15 15:47:35', '2025-05-15 15:47:35');

-- Initial data for orders
INSERT INTO orders (id, user_id, total_price, status, created_at, updated_at) VALUES
                                                                                  (1, 2, 797000.00, 'PAID', '2025-05-15 15:47:39', '2025-05-15 15:47:39'),
                                                                                  (2, 3, 120000.00, 'PENDING', '2025-05-15 15:47:39', '2025-05-15 15:47:39');

-- Initial data for order_items
INSERT INTO order_items (id, order_id, product_id, quantity, price, created_at, updated_at) VALUES
                                                                                                (1, 1, 1, 2, 199000.00, '2025-05-15 15:47:46', '2025-05-15 15:47:46'),
                                                                                                (2, 1, 2, 1, 399000.00, '2025-05-15 15:47:46', '2025-05-15 15:47:46'),
                                                                                                (3, 2, 3, 1, 120000.00, '2025-05-15 15:47:46', '2025-05-15 15:47:46');

-- Initial data for reviews
INSERT INTO reviews (id, user_id, product_id, rating, comment, created_at, updated_at) VALUES
                                                                                           (1, 2, 1, 5, 'Áo chất lượng, rất đẹp!', '2025-05-15 15:47:52', '2025-05-15 15:47:52'),
                                                                                           (2, 3, 3, 4, 'Nón đẹp nhưng giao hơi lâu', '2025-05-15 15:47:52', '2025-05-15 15:47:52');

-- MySQL AUTO_INCREMENT handles sequence values automatically.
-- No need for SELECT setval statements like in PostgreSQL.
-- If you need to explicitly set the AUTO_INCREMENT counter, you can use:
-- ALTER TABLE users AUTO_INCREMENT = 12; -- Example to set auto-increment counter to start from 12
-- (Repeat for other tables if necessary)