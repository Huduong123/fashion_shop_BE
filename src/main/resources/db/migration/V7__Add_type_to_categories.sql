-- V7: Add type column to categories table
ALTER TABLE categories 
ADD COLUMN type VARCHAR(20) NOT NULL DEFAULT 'DROPDOWN';

-- Add index for better performance when querying by type
CREATE INDEX idx_categories_type ON categories(type);

-- Update existing categories to have default type DROPDOWN
UPDATE categories SET type = 'DROPDOWN' WHERE type IS NULL; 