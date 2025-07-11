-- V6: Add parent_id to categories table for hierarchy support
ALTER TABLE categories 
ADD COLUMN parent_id BIGINT;

-- Add foreign key constraint for parent_id referencing the same table
ALTER TABLE categories 
ADD CONSTRAINT fk_categories_parent_id 
FOREIGN KEY (parent_id) REFERENCES categories(id) ON DELETE SET NULL;

-- Add index for better performance when querying parent-child relationships
CREATE INDEX idx_categories_parent_id ON categories(parent_id); 