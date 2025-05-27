-- V2__Add_foreign_keys.sql
-- Thêm các ràng buộc khóa ngoại (Foreign Keys) sau khi tất cả các bảng đã được tạo

-- Foreign key for authorities
ALTER TABLE authorities
    ADD CONSTRAINT fk_authorities_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

-- Foreign keys for cart_items
ALTER TABLE cart_items
    ADD CONSTRAINT fk_cart_items_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    ADD CONSTRAINT fk_cart_items_product FOREIGN KEY (product_id) REFERENCES products (id);

-- Foreign key for orders
ALTER TABLE orders
    ADD CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES users (id);

-- Foreign keys for order_items
ALTER TABLE order_items
    ADD CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE,
    ADD CONSTRAINT fk_order_items_product FOREIGN KEY (product_id) REFERENCES products (id);

-- Foreign key for products
ALTER TABLE products
    ADD CONSTRAINT fk_products_category FOREIGN KEY (category_id) REFERENCES categories (id);

-- Foreign keys for reviews
ALTER TABLE reviews
    ADD CONSTRAINT fk_reviews_user FOREIGN KEY (user_id) REFERENCES users (id),
    ADD CONSTRAINT fk_reviews_product FOREIGN KEY (product_id) REFERENCES products (id);

-- Foreign key for user_addresses (Mới)
ALTER TABLE user_addresses
    ADD CONSTRAINT fk_user_addresses_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;