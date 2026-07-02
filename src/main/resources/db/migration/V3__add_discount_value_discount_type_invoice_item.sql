ALTER TABLE invoice_items
    ADD discount_type VARCHAR(255);

ALTER TABLE invoice_items
    ADD discount_value DOUBLE PRECISION;
