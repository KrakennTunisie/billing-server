ALTER TABLE payment_entity
    ADD COLUMN IF NOT EXISTS payment_status VARCHAR(255);