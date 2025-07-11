-- Create product_variant_images table
CREATE TABLE product_variant_images (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_variant_id BIGINT NOT NULL,
    image_url VARCHAR(500) NOT NULL,
    alt_text VARCHAR(255),
    is_primary BOOLEAN NOT NULL DEFAULT FALSE,
    display_order INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_product_variant_images_variant_id 
        FOREIGN KEY (product_variant_id) REFERENCES product_variants(id) 
        ON DELETE CASCADE
);

-- Create index for better performance
CREATE INDEX idx_product_variant_images_variant_id ON product_variant_images(product_variant_id);
CREATE INDEX idx_product_variant_images_primary ON product_variant_images(product_variant_id, is_primary);
CREATE INDEX idx_product_variant_images_order ON product_variant_images(product_variant_id, display_order);

-- Add constraint to ensure only one primary image per variant
-- Note: This will be enforced in application logic as well
-- MySQL doesn't support WHERE clause in CREATE UNIQUE INDEX, so we'll enforce this in application logic 