ALTER TABLE purchase_order_items
    ADD discount_type VARCHAR(255);

ALTER TABLE purchase_order_items
    ADD discount_value DOUBLE PRECISION;