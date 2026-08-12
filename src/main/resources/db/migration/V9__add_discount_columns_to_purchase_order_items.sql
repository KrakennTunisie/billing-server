ALTER TABLE purchase_order_items
    ADD COLUMN IF NOT EXISTS discount_type VARCHAR(255);

ALTER TABLE purchase_order_items
    ADD COLUMN IF NOT EXISTS discount_value DOUBLE PRECISION;