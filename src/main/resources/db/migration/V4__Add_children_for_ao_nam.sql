-- V4__Add_children_for_ao_nam.sql

-- Thêm children categories cho "Áo nam" (id=5) để test multi-level dropdown
INSERT INTO categories (name, description, parent_id, type, status) VALUES
('Áo polo nam', 'Áo polo dành cho nam giới', 5, 'LINK', 'ACTIVE'),
('Áo thun nam', 'Áo thun tay ngắn, tay dài dành cho nam', 5, 'LINK', 'ACTIVE'),
('Áo sơ mi nam', 'Áo sơ mi công sở và đi chơi dành cho nam', 5, 'LINK', 'ACTIVE'),
('Áo khoác nam', 'Áo khoác, jacket dành cho nam', 5, 'LINK', 'ACTIVE');

-- Thêm children categories cho "Quần nam" (id=6) để test thêm
INSERT INTO categories (name, description, parent_id, type, status) VALUES
('Quần jean nam', 'Quần jean dành cho nam giới', 6, 'LINK', 'ACTIVE'),
('Quần tây nam', 'Quần tây công sở dành cho nam', 6, 'LINK', 'ACTIVE'),
('Quần short nam', 'Quần short mùa hè dành cho nam', 6, 'LINK', 'ACTIVE'); 