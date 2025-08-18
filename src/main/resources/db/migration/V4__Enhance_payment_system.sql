-- V4__Enhance_payment_system.sql

-- 1. Thêm cột 'type' vào bảng payment_methods để phân loại phương thức thanh toán
--    - OFFLINE: Thanh toán trực tiếp, không qua cổng thanh toán (ví dụ: COD).
--    - ONLINE_REDIRECT: Thanh toán qua cổng thanh toán yêu cầu chuyển hướng người dùng (ví dụ: MoMo, VNPay).
ALTER TABLE payment_methods
ADD COLUMN type ENUM('OFFLINE', 'ONLINE_REDIRECT') NOT NULL DEFAULT 'OFFLINE' COMMENT 'Loại phương thức thanh toán' AFTER is_enabled;

-- 2. Cập nhật loại cho các phương thức thanh toán đã tồn tại được chèn ở V3
--    - COD là OFFLINE.
--    - VNPAY và MOMO là ONLINE_REDIRECT.
UPDATE payment_methods SET type = 'OFFLINE' WHERE code = 'COD';
UPDATE payment_methods SET type = 'ONLINE_REDIRECT' WHERE code IN ('VNPAY', 'MOMO');


-- 3. Tạo bảng payment_transactions để lưu trữ lịch sử giao dịch thanh toán.
--    Bảng này rất quan trọng để theo dõi trạng thái của từng lần thanh toán, đặc biệt với các cổng online.
CREATE TABLE payment_transactions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL COMMENT 'Liên kết tới đơn hàng',
    transaction_code VARCHAR(50) NOT NULL UNIQUE COMMENT 'Mã giao dịch duy nhất do hệ thống của mình tạo ra',
    gateway_transaction_id VARCHAR(100) COMMENT 'Mã giao dịch từ cổng thanh toán (MoMo, VNPay,...)',
    amount DECIMAL(10,2) NOT NULL COMMENT 'Số tiền của giao dịch',
    status ENUM('PENDING', 'SUCCESS', 'FAILED', 'CANCELLED') NOT NULL DEFAULT 'PENDING' COMMENT 'Trạng thái giao dịch',
    payment_method_code VARCHAR(50) NOT NULL COMMENT 'Mã của phương thức thanh toán được sử dụng (Vd: MOMO)',
    note TEXT COMMENT 'Ghi chú thêm hoặc thông báo lỗi từ cổng thanh toán',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 4. Thêm khóa ngoại và chỉ mục cho bảng payment_transactions để tối ưu truy vấn và đảm bảo toàn vẹn dữ liệu.
-- Khóa ngoại liên kết tới bảng orders
ALTER TABLE payment_transactions
ADD CONSTRAINT fk_transactions_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE;

-- Chỉ mục (index) để tăng tốc độ tìm kiếm
CREATE INDEX idx_transactions_order_id ON payment_transactions(order_id);
CREATE INDEX idx_transactions_status ON payment_transactions(status);
CREATE INDEX idx_transactions_gateway_id ON payment_transactions(gateway_transaction_id);