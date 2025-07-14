-- V5__Add_slug_to_categories.sql
-- Thêm cột slug vào bảng categories

-- Thêm cột slug (cho phép NULL để update từ frontend sau)
ALTER TABLE categories 
ADD COLUMN slug VARCHAR(120) NULL;

-- Thêm unique constraint cho slug
ALTER TABLE categories 
ADD CONSTRAINT uk_categories_slug UNIQUE (slug);

-- Tạo index cho slug để tăng tốc tìm kiếm
CREATE INDEX idx_categories_slug ON categories(slug); 