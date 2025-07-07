-- Add status column to product_variants table
ALTER TABLE product_variants 
ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE';

-- Add check constraint for valid status values
ALTER TABLE product_variants
ADD CONSTRAINT chk_product_variant_status 
CHECK (status IN ('ACTIVE', 'INACTIVE', 'DISCONTINUED', 'OUT_OF_STOCK'));

-- Create index on status column for better query performance
CREATE INDEX idx_product_variants_status ON product_variants(status); 