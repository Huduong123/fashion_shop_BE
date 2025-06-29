-- V2__Add_foreign_keys.sql - PHIÊN BẢN ĐÃ CẬP NHẬT

-- Foreign key for authorities
ALTER TABLE authorities ADD CONSTRAINT fk_authorities_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

-- Foreign key for products
ALTER TABLE products ADD CONSTRAINT fk_products_category FOREIGN KEY (category_id) REFERENCES categories (id);

-- -- MỚI: Foreign keys for product_variants
ALTER TABLE product_variants ADD CONSTRAINT fk_variant_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE;
ALTER TABLE product_variants ADD CONSTRAINT fk_variant_color FOREIGN KEY (color_id) REFERENCES colors(id);
ALTER TABLE product_variants ADD CONSTRAINT fk_variant_size FOREIGN KEY (size_id) REFERENCES sizes(id);

-- -- SỬA ĐỔI: Foreign keys for cart_items
ALTER TABLE cart_items ADD CONSTRAINT fk_cart_items_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;
ALTER TABLE cart_items ADD CONSTRAINT fk_cart_items_variant FOREIGN KEY (product_variant_id) REFERENCES product_variants(id) ON DELETE CASCADE; -- Đã thay đổi

-- Foreign key for orders
ALTER TABLE orders ADD CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES users (id);

-- -- SỬA ĐỔI: Foreign keys for order_items
ALTER TABLE order_items ADD CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE;
ALTER TABLE order_items ADD CONSTRAINT fk_order_items_variant FOREIGN KEY (product_variant_id) REFERENCES product_variants(id); -- Đã thay đổi

-- Foreign keys for reviews
ALTER TABLE reviews ADD CONSTRAINT fk_reviews_user FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE reviews ADD CONSTRAINT fk_reviews_product FOREIGN KEY (product_id) REFERENCES products (id);

-- Foreign key for user_addresses
ALTER TABLE user_addresses ADD CONSTRAINT fk_user_addresses_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;