-- V4__Add_enabled_columns.sql (MySQL)

-- Thêm cột 'enabled' vào bảng products
ALTER TABLE products
    ADD COLUMN enabled TINYINT(1) NOT NULL DEFAULT 1; -- MySQL equivalent for BOOLEAN TRUE is 1

-- Thêm cột 'enabled' vào bảng reviews
ALTER TABLE reviews
    ADD COLUMN enabled TINYINT(1) NOT NULL DEFAULT 0; -- MySQL equivalent for BOOLEAN FALSE is 0

-- Nếu bạn muốn các đánh giá cũ (đã chèn bởi V3) được hiển thị, bạn có thể chạy thêm lệnh này:
-- UPDATE reviews SET enabled = 1; -- Update BOOLEAN TRUE is 1
-- Nếu không, các đánh giá cũ sẽ có enabled=0 và cần được phê duyệt thủ công.