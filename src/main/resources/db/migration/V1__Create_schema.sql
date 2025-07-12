-- V1__Create_schema.sql (MySQL) - PHIÊN BẢN ĐÃ CẬP NHẬT

-- Table: users
CREATE TABLE users (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       full_name VARCHAR(100),
                       phone VARCHAR(20),
                       gender VARCHAR(10),
                       birth_date DATE,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       enabled TINYINT(1) DEFAULT 1
);

-- Table: user_addresses
CREATE TABLE user_addresses (
                                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                user_id BIGINT NOT NULL,
                                recipient_name VARCHAR(100) NOT NULL,
                                phone_number VARCHAR(20) NOT NULL,
                                address_detail TEXT NOT NULL,
                                is_default TINYINT(1) DEFAULT 0,
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Table: authorities
CREATE TABLE authorities (
                             id BIGINT PRIMARY KEY AUTO_INCREMENT,
                             user_id BIGINT NOT NULL,
                             authority VARCHAR(50) NOT NULL,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Table: categories (with hierarchy support)
CREATE TABLE categories (
                            id BIGINT PRIMARY KEY AUTO_INCREMENT,
                            name VARCHAR(100) NOT NULL UNIQUE,
                            description TEXT,
                            parent_id BIGINT,
                            type VARCHAR(20) NOT NULL DEFAULT 'DROPDOWN',
                            status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Bảng colors và sizes
CREATE TABLE colors (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        name VARCHAR(50) NOT NULL UNIQUE,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE sizes (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       name VARCHAR(20) NOT NULL UNIQUE,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Bảng products chỉ chứa thông tin chung
CREATE TABLE products (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          name VARCHAR(255) NOT NULL,
                          description TEXT,
                          category_id BIGINT,
                          enabled TINYINT(1) NOT NULL DEFAULT 1,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Bảng product_variants chứa các biến thể (màu sắc, hình ảnh) - không chứa size, price, quantity
CREATE TABLE product_variants (
                                  id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                  product_id BIGINT NOT NULL,
                                  color_id BIGINT NOT NULL,
                                  image_url VARCHAR(255),
                                  status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
                                  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                  CONSTRAINT uq_product_variant UNIQUE (product_id, color_id)
);

-- Bảng product_variant_sizes chứa thông tin size, giá và số lượng cho từng biến thể
CREATE TABLE product_variant_sizes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_variant_id BIGINT NOT NULL,
    size_id BIGINT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    quantity INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uq_product_variant_size UNIQUE (product_variant_id, size_id)
);

-- Bảng product_variant_images chứa nhiều hình ảnh cho mỗi biến thể
CREATE TABLE product_variant_images (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_variant_id BIGINT NOT NULL,
    image_url VARCHAR(500) NOT NULL,
    alt_text VARCHAR(255),
    is_primary BOOLEAN NOT NULL DEFAULT FALSE,
    display_order INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Bảng cart_items tham chiếu tới product_variant_id và size_id
CREATE TABLE cart_items (
                            id BIGINT PRIMARY KEY AUTO_INCREMENT,
                            user_id BIGINT NOT NULL,
                            product_variant_id BIGINT NOT NULL,
                            size_id BIGINT NOT NULL,
                            quantity INT NOT NULL,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Table: orders
CREATE TABLE orders (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        user_id BIGINT NOT NULL,
                        total_price DECIMAL(10,2) NOT NULL,
                        status ENUM('PENDING','PAID','CANCELLED','SHIPPED') DEFAULT 'PENDING',
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Bảng order_items tham chiếu tới product_variant_id và size_id
CREATE TABLE order_items (
                             id BIGINT PRIMARY KEY AUTO_INCREMENT,
                             order_id BIGINT NOT NULL,
                             product_variant_id BIGINT NOT NULL,
                             size_id BIGINT NOT NULL,
                             quantity INT NOT NULL,
                             price DECIMAL(10,2) NOT NULL,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Table: reviews
CREATE TABLE reviews (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         user_id BIGINT NOT NULL,
                         product_id BIGINT NOT NULL,
                         rating INT CHECK (rating BETWEEN 1 AND 5),
                         comment TEXT,
                         enabled TINYINT(1) NOT NULL DEFAULT 0,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Add Auxiliary Indexes
CREATE INDEX idx_authorities_user_id ON authorities (user_id);
CREATE INDEX idx_cart_items_user_id ON cart_items (user_id);
CREATE INDEX idx_cart_items_variant_id ON cart_items (product_variant_id);
CREATE INDEX idx_cart_items_size_id ON cart_items (size_id);
CREATE INDEX idx_orders_user_id ON orders (user_id);
CREATE INDEX idx_order_items_order_id ON order_items (order_id);
CREATE INDEX idx_order_items_variant_id ON order_items (product_variant_id);
CREATE INDEX idx_order_items_size_id ON order_items (size_id);
CREATE INDEX idx_products_category_id ON products (category_id);
CREATE INDEX idx_reviews_user_id ON reviews (user_id);
CREATE INDEX idx_reviews_product_id ON reviews (product_id);
CREATE INDEX idx_user_addresses_user_id ON user_addresses (user_id);
CREATE INDEX idx_variant_product_id ON product_variants (product_id);
CREATE INDEX idx_variant_color_id ON product_variants (color_id);
CREATE INDEX idx_categories_parent_id ON categories(parent_id);
CREATE INDEX idx_categories_type ON categories(type);
CREATE INDEX idx_categories_status ON categories(status);
CREATE INDEX idx_product_variants_status ON product_variants(status);
CREATE INDEX idx_product_variant_images_variant_id ON product_variant_images(product_variant_id);
CREATE INDEX idx_product_variant_images_primary ON product_variant_images(product_variant_id, is_primary);
CREATE INDEX idx_product_variant_images_order ON product_variant_images(product_variant_id, display_order);

-- Add Check Constraints
ALTER TABLE product_variants
ADD CONSTRAINT chk_product_variant_status 
CHECK (status IN ('ACTIVE', 'INACTIVE', 'DISCONTINUED', 'OUT_OF_STOCK'));