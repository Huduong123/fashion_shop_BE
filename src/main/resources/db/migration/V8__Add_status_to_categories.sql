-- V8: Add status column to categories table
ALTER TABLE categories 
ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE';

-- Add index for better performance when querying by status
CREATE INDEX idx_categories_status ON categories(status);

-- Update existing categories to have default status ACTIVE
UPDATE categories SET status = 'ACTIVE' WHERE status IS NULL; 